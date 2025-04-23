package au.com.tabcorp.toolkit.api;

import au.com.tabcorp.core.CoreBootstrap;
import au.com.tabcorp.core.annotations.Delegate;
import au.com.tabcorp.data.DataBootstrap;
import au.com.tabcorp.messaging.MessagingBootstrap;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.core.env.Environment;
import org.springframework.web.reactive.config.EnableWebFlux;

import java.io.PrintStream;

@EnableConfigurationProperties()
@EnableAutoConfiguration()
@EnableWebFlux()
@ComponentScan(basePackages = {"au.com.tabcorp.toolkit.api.properties"},
        includeFilters = {@ComponentScan.Filter(type = org.springframework.context.annotation.FilterType.ANNOTATION, value = {org.springframework.boot.context.properties.ConfigurationProperties.class})})
@ComponentScan(basePackages = {"au.com.tabcorp.toolkit.api.components","au.com.tabcorp.toolkit.api.validators"},
        includeFilters = {@ComponentScan.Filter(type = org.springframework.context.annotation.FilterType.ANNOTATION, value = {org.springframework.stereotype.Component.class})})
@ComponentScan(basePackages = {"au.com.tabcorp.toolkit.api.controllers"},
        includeFilters = {@ComponentScan.Filter(type = org.springframework.context.annotation.FilterType.ANNOTATION, value = {org.springframework.web.bind.annotation.RestController.class})})
@ComponentScan(basePackages = {"au.com.tabcorp.toolkit.api.delegates"},
        includeFilters = {@ComponentScan.Filter(type = org.springframework.context.annotation.FilterType.ANNOTATION, value = {org.springframework.stereotype.Service.class, Delegate.class})})
@ComponentScan(basePackages = {"au.com.tabcorp.toolkit.api.configuration"},
        includeFilters = {@ComponentScan.Filter(type = org.springframework.context.annotation.FilterType.ANNOTATION, value = {org.springframework.context.annotation.Configuration.class})})
public class TabcorpToolkitAPIStarter {

    public static void main(String[] args) {
        System.setProperty("java.awt.headless","true");

        final SpringApplication application =  new SpringApplication(TabcorpToolkitAPIStarter.class, DatabaseConfiguration.class, MessagingBootstrap.class,  CoreBootstrap.class, DataBootstrap.class);
        application.setLogStartupInfo(false);
        application.setBanner((Environment environment, Class<?> sourceClass, PrintStream out) -> {
            String version = "v1";//environment.getProperty("version");
            out.println();
            out.println("==================================================================================================================");
            out.printf ("    Tabcorp Technical - Test   :: Web API Version %s %n", version );
            out.println("                               :: by Tinashe Chipomho" );
            out.println("==================================================================================================================");
            out.println();
        });
        application.run(args);
    }

}
