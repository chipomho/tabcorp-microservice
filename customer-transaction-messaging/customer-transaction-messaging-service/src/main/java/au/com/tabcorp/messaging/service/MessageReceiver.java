package au.com.tabcorp.messaging.service;

import au.com.tabcorp.common.BeanNames;
import au.com.tabcorp.common.model.TransactionModel;
import au.com.tabcorp.data.entity.CustomerTransaction;
import au.com.tabcorp.data.service.CustomerTransactionService;
import au.com.tabcorp.messaging.configuration.RabbitMQConfiguration;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;
import reactor.core.Disposable;
import reactor.rabbitmq.Receiver;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.nio.charset.StandardCharsets;

import static java.util.Objects.nonNull;

@Service(BeanNames.Messaging.RABBITMQ_MESSAGE_RECEIVER)
public class MessageReceiver extends AbstractMessageService{

    protected static final Logger LOG = LoggerFactory.getLogger(MessageReceiver.class.getName());

    protected final Receiver receiver;

    protected final CustomerTransactionService customerTransactionService;

    private Disposable subscription;

    @Autowired()
    public MessageReceiver(@Autowired()@Qualifier(BeanNames.Common.OBJECT_MAPPER)final ObjectMapper objectMapper,
                         final Receiver receiver, final Environment environment,
                           final CustomerTransactionService customerTransactionService) {
        super(objectMapper, environment);
        this.receiver = receiver;
        exchangeName = environment.getProperty("spring.rabbitmq.exchange",String.class, RabbitMQConfiguration.EXCHANGE_NAME);
        queueName = environment.getProperty("spring.rabbitmq.queue",String.class, RabbitMQConfiguration.QUEUE_NAME);
        this.customerTransactionService = customerTransactionService;
    }

    @PostConstruct
    public void startConsuming() {
        LOG.info("Starting message consumers.....");
        subscription = receiver.consumeAutoAck(queueName)
                .doOnNext(delivery -> {
                    Disposable savedSubscription = null;
                    try {
                        String message = new String(delivery.getBody(), StandardCharsets.UTF_8);
                        final TransactionModel transactionData = objectMapper.readValue(message, TransactionModel.class);
                        savedSubscription = customerTransactionService.save(CustomerTransaction.builder().transactionTime(transactionData.getTransactionTime())
                                .customerId(transactionData.getCustomerId()).productCode(transactionData.getProductCode())
                                .quantity(transactionData.getQuantity()).build())
                                .doOnSuccess(saved -> {
                                    if (LOG.isInfoEnabled()){
                                        LOG.info("Successfully saved transaction: " + saved.getId() );
                                    }
                                })
                                .doOnError(error -> {
                                    if (LOG.isErrorEnabled()){
                                        LOG.error("Error saving transaction: " + error.getMessage() , error);
                                    }
                                })
                                .subscribe();
                        // Process your message reactively here
                        // Maybe call reactive Redis / DB service
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                    finally {
                        if (nonNull(savedSubscription)){
                            savedSubscription.dispose();
                        }
                    }
                })
                .subscribe();
    }

    @PreDestroy()
    void stopConsuming() {
        LOG.info("Stopping message consumers....");
        subscription.dispose();
    }

}
