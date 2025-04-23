package au.com.tabcorp.messaging.service;

import au.com.tabcorp.common.BeanNames;
import au.com.tabcorp.common.model.TransactionModel;
import au.com.tabcorp.messaging.configuration.RabbitMQConfiguration;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.lang3.SerializationUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.rabbitmq.OutboundMessage;
import reactor.rabbitmq.QueueSpecification;
import reactor.rabbitmq.Sender;

import java.nio.charset.StandardCharsets;

import static au.com.tabcorp.messaging.configuration.RabbitMQConfiguration.ROUTING_KEY;
import static org.apache.commons.lang3.StringUtils.trim;

@Service(BeanNames.Messaging.RABBITMQ_MESSAGE_SENDER)
public class MessageSender extends AbstractMessageService{

    protected static final Logger LOG = LoggerFactory.getLogger(MessageSender.class.getName());

    protected final Sender sender;

    protected String routingKey;

    public MessageSender(@Autowired()@Qualifier(BeanNames.Common.OBJECT_MAPPER)final ObjectMapper objectMapper,
                         final Sender sender, final Environment environment) {
        super(objectMapper, environment);
        this.sender = sender;
        this.routingKey = environment.getProperty("spring.rabbitmq.queue-routing-key",String.class, ROUTING_KEY);
    }

  public Mono<Void> sendMessage(final TransactionModel model) {
      try {
          //Serialize object to json
          final String json = trim(objectMapper.writeValueAsString(model));
          //Outbound Message that will be sent by the Sender
          Flux<OutboundMessage> outbound = Flux.just(new OutboundMessage(exchangeName, routingKey, json.getBytes(StandardCharsets.UTF_8)));

          // Declare the queue then send the flux of messages.
          sender.declareQueue(QueueSpecification.queue(queueName).durable(true))
                .thenMany(sender.sendWithPublishConfirms(outbound))
                .doOnError(e -> LOG.error("Transaction Send failed", e))
                .subscribe(m -> {
                    if (LOG.isInfoEnabled()){
                        LOG.info("Message sent : "+ (m.isAck() ? "ack" : "nack") + " "+ (m.isReturned()? "returned" : "not returned") );
                    }
                 });
          return Mono.empty().then();
      }
      catch (Exception ex) {
          //TODO: custom exchange required
          return Mono.error(ex);
      }
  }

}
