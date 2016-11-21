package org.eclipse.scout.tasks;

import java.io.IOException;

import javax.servlet.http.HttpServletResponse;

import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

@EnableWebMvc
@Configuration
@Controller
@RequestMapping("/")
public class WebMvcConfig {

  public static final String SCOUT_CONTEXT_PATH = "/scout";
  public static final String API_SERVICES_PATH = "/api";

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

  @RequestMapping(value = "/login.html", method = RequestMethod.GET)
  public void loginRedirect(HttpServletResponse httpServletResponse) throws IOException {
    httpServletResponse.sendRedirect(SCOUT_CONTEXT_PATH + "/" + "login.html");
  }

  @RequestMapping(value = "/logout.html", method = RequestMethod.GET)
  public void logoutRedirect(HttpServletResponse httpServletResponse) throws IOException {
    httpServletResponse.sendRedirect(SCOUT_CONTEXT_PATH + "/" + "logout.html");
  }

}
