package au.com.tabcorp.data;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration()
@EnableConfigurationProperties()
@ComponentScan(basePackages = {"au.com.tabcorp.data.properties"},
        includeFilters = {@ComponentScan.Filter(type = org.springframework.context.annotation.FilterType.ANNOTATION, value = {org.springframework.boot.context.properties.ConfigurationProperties.class})})
@ComponentScan(basePackages = {"au.com.tabcorp.data.configuration"},
        includeFilters = {@ComponentScan.Filter(type = org.springframework.context.annotation.FilterType.ANNOTATION, value = {org.springframework.context.annotation.Configuration.class})})
@ComponentScan(basePackages = {"au.com.tabcorp.data.repository"},
        includeFilters = {@ComponentScan.Filter(type = org.springframework.context.annotation.FilterType.ANNOTATION, value = {org.springframework.stereotype.Repository.class})})
@ComponentScan(basePackages = {"au.com.tabcorp.data.service"},
        includeFilters = {@ComponentScan.Filter(type = org.springframework.context.annotation.FilterType.ANNOTATION, value = {org.springframework.stereotype.Service.class})})
public class DataBootstrap {
}
