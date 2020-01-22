package com.peddle.digital.cobot.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.core.io.FileSystemResource;

/**
 * @author Raj Kumar
 * 
 * Class to load the various configurations used by application
 *
 */
@Configuration
public class ApplicationContext {

  @Bean
  public PropertySourcesPlaceholderConfigurer propertySourcesPlaceholderConfigurer() {
    PropertySourcesPlaceholderConfigurer properties = new PropertySourcesPlaceholderConfigurer();
    System.setProperty("COBOT_HOME", System.getenv("COBOT_HOME"));
    properties.setLocation(new FileSystemResource(System.getenv("COBOT_HOME")+"/cobot_saviynt.properties"));
    properties.setIgnoreResourceNotFound(false);

    return properties;
  }
}