package org.eclipse.scout.springboot.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Import;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

/**
 * Spring Boot start class.
 */
@SpringBootApplication
@EnableWebMvc
@Import(ScoutServletConfig.class)
public class Application {

  public static void main(final String[] args) {
    start(args);
  }

  public static ConfigurableApplicationContext start(final String[] args) {
    return SpringApplication.run(Application.class, args);
  }

}
