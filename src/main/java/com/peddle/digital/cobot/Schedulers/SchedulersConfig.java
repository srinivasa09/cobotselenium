package com.peddle.digital.cobot.Schedulers;


import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * 
 * @author Srinivasa Reddy Challa, Raj Kumar
 * Configuration to tell to Spring to register all the Schedulers
 * in the current java package
 */
@Configuration
@EnableScheduling
@ComponentScan("com.peddle.digital.cobot.Schedulers")
public class SchedulersConfig {

}

