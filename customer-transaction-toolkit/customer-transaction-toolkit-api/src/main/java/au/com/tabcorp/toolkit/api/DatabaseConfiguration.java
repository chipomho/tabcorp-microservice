package au.com.tabcorp.toolkit.api;

import au.com.tabcorp.common.BeanNames;
import io.r2dbc.spi.ConnectionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.r2dbc.connection.init.ConnectionFactoryInitializer;
import org.springframework.r2dbc.connection.init.ResourceDatabasePopulator;

@Configuration()
public class DatabaseConfiguration {

    @Bean(BeanNames.Data.CONNECTION_FACTORY_INITIALIZER)
    public ConnectionFactoryInitializer initializer(@Autowired() @Qualifier(BeanNames.Data.CONNECTION)final ConnectionFactory connectionFactory) {
        final ConnectionFactoryInitializer initializer = new ConnectionFactoryInitializer();
        initializer.setConnectionFactory(connectionFactory);

        //and populate the schema and database
        initializer.setDatabasePopulator(new ResourceDatabasePopulator(
                new ClassPathResource("/META-INF/scripts/tabcorp.data.postgresql.customer.schema.sql"),
                new ClassPathResource("/META-INF/scripts/tabcorp.data.postgresql.customer.data.sql")
        ));
        return initializer;
    }

}
