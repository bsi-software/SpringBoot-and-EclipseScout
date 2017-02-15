package org.eclipse.scout.tasks.scout.platform.dev;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.env.EnvironmentPostProcessor;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.MapPropertySource;
import org.springframework.core.env.PropertySource;

/**
 * {@link EnvironmentPostProcessor} to add properties that make sense when working at development time.
 */
@Order(Ordered.HIGHEST_PRECEDENCE)
public class ScoutSpringDevToolsPropertyDefaultsPostProcessor implements EnvironmentPostProcessor {

  private static final Map<String, Object> PROPERTIES;

  static {
    final Map<String, Object> properties = new HashMap<String, Object>();
    // Scout doesn't support persistent sessions
    properties.put("server.session.persistent", "false");

    // Make session timeout greater
    properties.put("server.session.timeout", "900");

    // Print all SQL statements during development time
    properties.put("spring.jpa.properties.hibernate.show_sql", "false");
    properties.put("spring.jpa.properties.hibernate.format_sql", "false");

    PROPERTIES = Collections.unmodifiableMap(properties);
  }

  @Override
  public void postProcessEnvironment(ConfigurableEnvironment environment, SpringApplication application) {
    if (isSpringDevToolsAvailable()) {
      final PropertySource<?> propertySource = new MapPropertySource("scout-dev", PROPERTIES);
      environment.getPropertySources().addLast(propertySource);

      // Enable Scout Development-Mode
      System.setProperty("scout.dev.mode", "true");
    }
  }

  protected boolean isSpringDevToolsAvailable() {
    Class<?> devToolsPropertyDefaultsPostProcessor;
    try {
      devToolsPropertyDefaultsPostProcessor = Class.forName("org.springframework.boot.devtools.env.DevToolsPropertyDefaultsPostProcessor", true, this.getClass().getClassLoader());
    }
    catch (ClassNotFoundException e) {
      return false;
    }
    return devToolsPropertyDefaultsPostProcessor != null;
  }

}
