package org.eclipse.scout.tasks.spring;

import java.io.IOException;

import javax.servlet.http.HttpServletResponse;

import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

@Configuration
@EnableWebMvc
@Controller
@RequestMapping("/")
public class WebMvcConfiguration extends WebMvcConfigurerAdapter {

  public static final String SCOUT_CONTEXT_PATH = "/ui";
  public static final String WEBJARS_CONTEXT_PATH = "/webjars";
  public static final String API_CONTEXT_PATH = "/api";
  public static final String H2_CONTEXT_PATH = "/h2-console";

  @RequestMapping(value = "/", method = RequestMethod.GET)
  public void baseRedirect(HttpServletResponse httpServletResponse) throws IOException {
    redirect(httpServletResponse);
  }

  @RequestMapping(value = "/index.html", method = RequestMethod.GET)
  public void indexRedirect(HttpServletResponse httpServletResponse) throws IOException {
    redirect(httpServletResponse);
  }

  @RequestMapping(value = SCOUT_CONTEXT_PATH, method = RequestMethod.GET)
  public void scoutBaseRedirect(HttpServletResponse httpServletResponse) throws IOException {
    redirect(httpServletResponse);
  }

  protected void redirect(HttpServletResponse httpServletResponse) throws IOException {
    httpServletResponse.sendRedirect(SCOUT_CONTEXT_PATH + "/");
  }

  @Override
  public void addResourceHandlers(ResourceHandlerRegistry registry) {
    if (!registry.hasMappingForPattern(WEBJARS_CONTEXT_PATH + "/**")) {
      registry
          .addResourceHandler(WEBJARS_CONTEXT_PATH + "/**")
          .addResourceLocations("classpath:/META-INF/resources/webjars/");
    }
  }

}
