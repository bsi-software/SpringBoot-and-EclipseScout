package org.eclipse.scout.springboot.demo;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

@EnableWebMvc
@Configuration
public class WebMvcConfig {

//  @Bean
//  public ServletRegistrationBean dispatcherRegistration(WebApplicationContext webApplicationContext) {
//    ServletRegistrationBean registration = new ServletRegistrationBean(
//        new DispatcherServlet(webApplicationContext));
//    registration.addUrlMappings("/services/*");
//    return registration;
//  }

}
