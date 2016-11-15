package org.eclipse.scout.tasks.scout.platform;

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
    // Print all SQL statements during development time
    //TODO [msm] 'spring.jpa.show-sql' is not working here
    properties.put("spring.jpa.show-sql", "true");
    //TODO [msm] add scout auth filter, "?debug=true" etc.
    PROPERTIES = Collections.unmodifiableMap(properties);
  }

  @Override
  public void postProcessEnvironment(ConfigurableEnvironment environment, SpringApplication application) {
    if (isLocalApplication(environment)) {
      final PropertySource<?> propertySource = new MapPropertySource("refresh", PROPERTIES);
      environment.getPropertySources().addLast(propertySource);
    }
  }

  protected boolean isLocalApplication(ConfigurableEnvironment environment) {
    return environment.getPropertySources().get("remoteUrl") == null;
  }
}
