package au.com.tabcorp.core;

import au.com.tabcorp.core.annotations.Facade;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration()
@ComponentScan(basePackages = {"au.com.tabcorp.core.properties"},
        includeFilters = {@ComponentScan.Filter(type = org.springframework.context.annotation.FilterType.ANNOTATION, value = {org.springframework.boot.context.properties.ConfigurationProperties.class})})
@ComponentScan(basePackages = {"au.com.tabcorp.core.configuration"},
        includeFilters = {@ComponentScan.Filter(type = org.springframework.context.annotation.FilterType.ANNOTATION, value = {org.springframework.context.annotation.Configuration.class})})
@ComponentScan(basePackages = {"au.com.tabcorp.core.facade","au.com.tabcorp.core.service"},
        includeFilters = {@ComponentScan.Filter(type = org.springframework.context.annotation.FilterType.ANNOTATION, value = {org.springframework.stereotype.Service.class, Facade.class})})
public class CoreBootstrap {
}
