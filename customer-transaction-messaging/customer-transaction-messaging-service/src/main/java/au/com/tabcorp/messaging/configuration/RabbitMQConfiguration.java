package au.com.tabcorp.messaging.configuration;

import au.com.tabcorp.common.BeanNames;
import com.rabbitmq.client.Address;
import com.rabbitmq.client.ConnectionFactory;
import org.apache.commons.lang3.StringUtils;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.Declarables;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import reactor.core.scheduler.Schedulers;
import reactor.rabbitmq.*;

import java.util.Arrays;

@Configuration()
public class RabbitMQConfiguration {

    public static final String EXCHANGE_NAME = "Tabcorp.TechnicalTask.Exchange";
    public static final String QUEUE_NAME = "Tabcorp.TechnicalTask.Queue";
    public static final String VIRTUAL_HOST = "Tabcorp";
    public static final String ROUTING_KEY = "customer-transaction";

    @Bean(BeanNames.Messaging.RABBITMQ_CONNECTION_FACTORY)
    public ConnectionFactory rabbitConnectionFactory(final Environment environment) {
      final ConnectionFactory factory = new ConnectionFactory();

      factory.setUsername(environment.getRequiredProperty("spring.rabbitmq.username",String.class));
      factory.setPassword(environment.getRequiredProperty("spring.rabbitmq.password",String.class));
      factory.setVirtualHost(environment.getProperty("spring.rabbitmq.virtual-host",String.class,VIRTUAL_HOST));

      // Optional tuning
      factory.setConnectionTimeout(environment.getProperty("spring.rabbitmq.connection-timeout", Integer.class,3000)); // in milliseconds
      factory.useNio();
      return factory;
    }

    @Bean(BeanNames.Messaging.RABBITMQ_CLUSTER_NODE_ADDRESSES)Address[] addresses(final Environment environment){
        // Comma-separated list of RabbitMQ cluster node addresses
        final String addresses = StringUtils.trim(environment.getRequiredProperty("spring.rabbitmq.node-addresses",String.class));
        return Arrays.stream(addresses.split("[,]"))
                .map(value->new Address(Address.parseHost(value), Address.parsePort(value)) ).toArray(Address[]::new);
    }

    @Bean()
    public SenderOptions senderOptions(@Autowired()@Qualifier(BeanNames.Messaging.RABBITMQ_CONNECTION_FACTORY) final ConnectionFactory factory,
                                       @Autowired()@Qualifier(BeanNames.Messaging.RABBITMQ_CLUSTER_NODE_ADDRESSES)final Address[] addresses,
                                       final Environment environment) {
        final String connectionName = environment.getProperty("spring.rabbitmq.sender-connection-name",String.class,"techtask-sender");
        return new SenderOptions()
                .connectionFactory(factory)
                .connectionSupplier(supplier-> supplier.newConnection(addresses,connectionName))
                .resourceManagementScheduler(Schedulers.boundedElastic());
    }

    @Bean
    public Sender sender(SenderOptions options) {
        return RabbitFlux.createSender(options);
    }

    @Bean
    public ReceiverOptions receiverOptions(@Autowired()@Qualifier(BeanNames.Messaging.RABBITMQ_CONNECTION_FACTORY) ConnectionFactory factory,
                                           @Autowired()@Qualifier(BeanNames.Messaging.RABBITMQ_CLUSTER_NODE_ADDRESSES)final Address[] addresses,
                                           final Environment environment) {
        final String connectionName = environment.getProperty("spring.rabbitmq.receiver-connection-name",String.class,"techtask-receiver");
        return new ReceiverOptions().connectionFactory(factory)
                .connectionSupplier(supplier-> supplier.newConnection(addresses,connectionName));
    }

    @Bean
    public Receiver receiver(final ReceiverOptions options) {
        return RabbitFlux.createReceiver(options);
    }

    @Bean
    public Declarables rabbitBindings(final Environment environment) {
        final String exchangeName = environment.getProperty("spring.rabbitmq.exchange",String.class, EXCHANGE_NAME);
        final String queueName = environment.getProperty("spring.rabbitmq.queue",String.class, QUEUE_NAME);
        final String routingKey = environment.getProperty("spring.rabbitmq.queue-routing-key",String.class, ROUTING_KEY);
        return new Declarables(
                new DirectExchange(exchangeName,true, false),
                new Queue(queueName, true),
                new Binding(queueName, Binding.DestinationType.QUEUE, exchangeName, routingKey, null)
        );
    }


}
