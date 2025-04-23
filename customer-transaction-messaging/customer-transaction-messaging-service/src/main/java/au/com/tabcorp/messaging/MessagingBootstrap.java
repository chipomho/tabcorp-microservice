package au.com.tabcorp.messaging;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration()
@ComponentScan(basePackages = {"au.com.tabcorp.messaging.properties"},
        includeFilters = {@ComponentScan.Filter(type = org.springframework.context.annotation.FilterType.ANNOTATION, value = {org.springframework.boot.context.properties.ConfigurationProperties.class})})
@ComponentScan(basePackages = {"au.com.tabcorp.messaging.configuration"},
        includeFilters = {@ComponentScan.Filter(type = org.springframework.context.annotation.FilterType.ANNOTATION, value = {org.springframework.context.annotation.Configuration.class})})
@ComponentScan(basePackages = {"au.com.tabcorp.messaging.service"},
        includeFilters = {@ComponentScan.Filter(type = org.springframework.context.annotation.FilterType.ANNOTATION, value = {org.springframework.stereotype.Service.class})})
public class MessagingBootstrap {
}
