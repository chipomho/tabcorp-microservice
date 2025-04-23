package au.com.tabcorp.messaging.service;

import au.com.tabcorp.messaging.configuration.RabbitMQConfiguration;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.core.env.Environment;

public abstract class AbstractMessageService {

    protected final ObjectMapper objectMapper;

    protected String queueName;
    protected String exchangeName;


    protected AbstractMessageService(final ObjectMapper objectMapper, final Environment environment) {
        this.objectMapper = objectMapper;
        exchangeName = environment.getProperty("spring.rabbitmq.exchange",String.class, RabbitMQConfiguration.EXCHANGE_NAME);
        queueName = environment.getProperty("spring.rabbitmq.queue",String.class, RabbitMQConfiguration.QUEUE_NAME);
    };

}
