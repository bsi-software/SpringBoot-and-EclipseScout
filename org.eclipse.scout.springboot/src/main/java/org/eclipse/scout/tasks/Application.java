package org.eclipse.scout.tasks;

import org.eclipse.scout.tasks.spring.ScoutServletConfiguration;
import org.eclipse.scout.tasks.spring.WebMvcConfiguration;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Import;

/**
 * Spring Boot start class
 */
@SpringBootApplication
@Import({ScoutServletConfiguration.class, WebMvcConfiguration.class})
public class Application {

  public static void main(final String[] args) {
    applySystemProperties();
    SpringApplication.run(Application.class, args);
  }

  /**
   * Apply system properties that have to bet set before the boot application starts
   */
  protected static void applySystemProperties() {
    System.setProperty("spring.devtools.restart.enabled", "true");
  }

}
