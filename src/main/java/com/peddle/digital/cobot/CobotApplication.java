package com.peddle.digital.cobot;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.support.SpringBootServletInitializer;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

/**
 * @author Srinivasa Reddy Challa, Raj Kumar
 * 
 * Application Startup class
 *
 */

@SpringBootApplication
@EnableJpaAuditing
public class CobotApplication extends SpringBootServletInitializer {
	
	
    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
        return application.sources(CobotApplication.class).properties(
                "execute.mode:servlet-container");
    }

    public static void main(String[] args) throws Exception {
        new SpringApplicationBuilder(CobotApplication.class).properties(
                "execute.mode:standalone").run(args);
    }
	
//	public static void main(String[] args) {
//		SpringApplication.run(CobotApplication.class, args);
//	}
}

