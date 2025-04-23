package au.com.tabcorp.data.configuration;

import au.com.tabcorp.common.BeanNames;
import au.com.tabcorp.data.properties.R2DBCConfigurationProperties;
import io.r2dbc.spi.ConnectionFactories;
import io.r2dbc.spi.ConnectionFactory;
import io.r2dbc.spi.ConnectionFactoryOptions;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.r2dbc.core.R2dbcEntityTemplate;
import org.springframework.data.r2dbc.repository.config.EnableR2dbcRepositories;
import org.springframework.r2dbc.connection.R2dbcTransactionManager;
import org.springframework.transaction.ReactiveTransactionManager;
import org.springframework.transaction.reactive.TransactionalOperator;

import static io.r2dbc.spi.ConnectionFactoryOptions.PASSWORD;
import static io.r2dbc.spi.ConnectionFactoryOptions.USER;

@EnableR2dbcRepositories(entityOperationsRef = BeanNames.Data.R2DBC_TEMPLATE, basePackages = "au.com.tabcorp.data.repository")
@Configuration(proxyBeanMethods = true)
public class TabcorpDataConfiguration {

    @Bean(BeanNames.Data.R2DBC_TEMPLATE)
    public R2dbcEntityTemplate r2dbcEntityTemplate(@Autowired()@Qualifier(BeanNames.Data.CONNECTION)final ConnectionFactory connectionFactory) {
        return new R2dbcEntityTemplate(connectionFactory);
    }

    @Bean(BeanNames.Data.CONNECTION)
    public ConnectionFactory connectionFactory(@Autowired() final R2DBCConfigurationProperties properties) {
        final ConnectionFactoryOptions baseOptions = ConnectionFactoryOptions.parse(properties.getUrl());
        ConnectionFactoryOptions.Builder ob = ConnectionFactoryOptions.builder().from(baseOptions);
        if (StringUtils.isNotBlank(properties.getUser())) {
            ob.option(USER, properties.getUser());
        }
        if (StringUtils.isNotBlank(properties.getPassword())) {
            ob.option(PASSWORD, properties.getPassword());
        }
        return ConnectionFactories.get(ob.build());
    }

    @Bean(BeanNames.Data.REACTIVE_TRANSACTION_MANAGER)
    public ReactiveTransactionManager reactiveTransactionManager(@Autowired() @Qualifier(BeanNames.Data.CONNECTION)final ConnectionFactory connectionFactory) {
        return new R2dbcTransactionManager(connectionFactory);
    }

    @Bean(BeanNames.Data.TRANSACTION_OPERATOR)
    public TransactionalOperator transactionalOperator(@Autowired() @Qualifier(BeanNames.Data.REACTIVE_TRANSACTION_MANAGER) final ReactiveTransactionManager reactiveTransactionManager) {
        return TransactionalOperator.create(reactiveTransactionManager);
    }


}
