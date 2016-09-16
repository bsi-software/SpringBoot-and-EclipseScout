package org.eclipse.scout.springboot;

import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toSet;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.List;
import java.util.Set;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

import org.eclipse.scout.rt.platform.ApplicationScoped;
import org.eclipse.scout.rt.platform.BEANS;
import org.eclipse.scout.rt.platform.Bean;
import org.eclipse.scout.rt.platform.CreateImmediately;
import org.eclipse.scout.rt.platform.IBean;
import org.eclipse.scout.rt.platform.Order;
import org.eclipse.scout.rt.platform.Replace;
import org.eclipse.scout.rt.platform.SimpleBeanDecorationFactory;
import org.eclipse.scout.rt.platform.interceptor.IBeanDecorator;
import org.eclipse.scout.rt.platform.interceptor.IBeanInvocationContext;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.springframework.beans.factory.NoUniqueBeanDefinitionException;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.stereotype.Component;

public class ScoutSpringBeanManagerTest {

  private static ConfigurableApplicationContext springContext;

  //
  // Test setup:
  //
  //              +-------+
  //              | ICar  |
  //              +---+---+
  //                  |
  //              +------------+
  //              | AbstractVW |
  //              +-----+------+
  //                    |
  //      +-------------+-------------+
  //      |             |             |
  // +----------+ +------------+ +----------+
  // | Polo (1) | | Touran (0) | | Golf (2) |
  // +----------+ +------------+ +----+-----+
  //                                  |
  //                          +------------------+
  //                          | EGolf (@Replace) |
  //                          +------------------+
  //

  @BeforeClass
  public static void beforeClass() {
    springContext = Application.start(new String[0]);
  }

  @AfterClass
  public static void afterClass() {
    springContext.stop();
  }

  @Test
  public void testLookupByInterface() {
    assertEquals(Touran.class, BEANS.get(ICar.class).getClass());
    try {
      springContext.getBean(ICar.class);
      fail("NoUniqueBeanDefinitionException expected");
    }
    catch (NoUniqueBeanDefinitionException e) {
    }

    assertEquals(asList(Touran.class, Polo.class, EGolf.class), BEANS.all(ICar.class).stream().map(ICar::getClass).collect(toList()));
    assertEquals(asSet(Touran.class, Polo.class, Golf.class, EGolf.class), springContext.getBeansOfType(ICar.class).values().stream().map(ICar::getClass).collect(toSet()));
  }

  @Test
  public void testLookupByAbstractClass() {
    assertEquals(Touran.class, BEANS.get(AbstractVW.class).getClass());
    try {
      springContext.getBean(AbstractVW.class);
      fail("NoUniqueBeanDefinitionException expected");
    }
    catch (NoUniqueBeanDefinitionException e) {
    }

    assertEquals(asList(Touran.class, Polo.class, EGolf.class), BEANS.all(AbstractVW.class).stream().map(ICar::getClass).collect(toList()));
    assertEquals(asSet(Touran.class, Polo.class, Golf.class, EGolf.class), springContext.getBeansOfType(AbstractVW.class).values().stream().map(ICar::getClass).collect(toSet()));
  }

  @Test
  public void testLookupByClass() {
    // Test Lookup for 'Polo'
    assertEquals(Polo.class, BEANS.get(Polo.class).getClass());
    assertEquals(Polo.class, springContext.getBean(Polo.class).getClass());

    assertEquals(asList(Polo.class), BEANS.all(Polo.class).stream().map(ICar::getClass).collect(toList()));
    assertEquals(asList(Polo.class), springContext.getBeansOfType(Polo.class).values().stream().map(ICar::getClass).collect(toList()));

    // Test Lookup for 'Touran'
    assertEquals(Touran.class, BEANS.get(Touran.class).getClass());
    assertEquals(Touran.class, springContext.getBean(Touran.class).getClass());

    assertEquals(asList(Touran.class), BEANS.all(Touran.class).stream().map(ICar::getClass).collect(toList()));
    assertEquals(asList(Touran.class), springContext.getBeansOfType(Touran.class).values().stream().map(ICar::getClass).collect(toList()));

    // Test Lookup for 'Golf'
    assertEquals(EGolf.class, BEANS.get(Golf.class).getClass());
    try {
      springContext.getBean(Golf.class);
      fail("NoUniqueBeanDefinitionException expected");
    }
    catch (NoUniqueBeanDefinitionException e) {
    }

    assertEquals(asList(EGolf.class), BEANS.all(Golf.class).stream().map(ICar::getClass).collect(toList()));
    assertEquals(asSet(Golf.class, EGolf.class), springContext.getBeansOfType(Golf.class).values().stream().map(ICar::getClass).collect(toSet()));

    // Test Lookup for 'EGolf'
    assertEquals(EGolf.class, BEANS.get(EGolf.class).getClass());
    assertEquals(EGolf.class, springContext.getBean(EGolf.class).getClass());

    assertEquals(asList(EGolf.class), BEANS.all(EGolf.class).stream().map(ICar::getClass).collect(toList()));
    assertEquals(asList(EGolf.class), springContext.getBeansOfType(EGolf.class).values().stream().map(ICar::getClass).collect(toList()));
  }

  @Test
  public void testInstanceScopedBean() {
    InstanceScopedBean bean1 = BEANS.get(InstanceScopedBean.class);
    InstanceScopedBean bean2 = BEANS.get(InstanceScopedBean.class);
    assertNotSame(bean1, bean2);

    InstanceScopedBean springBean1 = springContext.getBean(InstanceScopedBean.class);
    InstanceScopedBean springBean2 = springContext.getBean(InstanceScopedBean.class);
    assertNotSame(springBean1, springBean2);
  }

  @Test
  public void testApplicationScopedBean() {
    ApplicationScopedBean bean1 = BEANS.get(ApplicationScopedBean.class);
    ApplicationScopedBean bean2 = BEANS.get(ApplicationScopedBean.class);

    ApplicationScopedBean springBean1 = springContext.getBean(ApplicationScopedBean.class);
    ApplicationScopedBean springBean2 = springContext.getBean(ApplicationScopedBean.class);

    assertEquals(1, asSet(bean1, bean2, springBean1, springBean2).size());
  }

  @Test
  public void testPostConstruct() {
    assertTrue(BEANS.get(PostConstructBean.class).isInitialized());
    assertTrue(springContext.getBean(PostConstructBean.class).isInitialized());
  }

  @Test
  public void testCreateImmediately() {
    assertTrue(CreateImmediatelyBean.isConstructed());
  }

  // decorator is only installed when looked up via Scout Bean Manager
  @Test
  public void testDecorator() {
    assertEquals("HELLO", BEANS.get(IPingBean.class).ping("hello"));
    assertEquals("hello", springContext.getBean(IPingBean.class).ping("hello"));

    assertEquals("hello", BEANS.get(PingBean.class).ping("hello"));
    assertEquals("hello", springContext.getBean(PingBean.class).ping("hello"));
  }

  @Test
  public void testSpringAutoWiring() {
    AutoWiredBean autoWiredBean = BEANS.get(AutoWiredBean.class);
    assertNotNull(autoWiredBean.getInjectedBean());
    assertSame(springContext.getBean(SomeSpringBean.class), autoWiredBean.getInjectedBean());
    assertSame(springContext.getBean(AutoWiredBean.class).getInjectedBean(), springContext.getBean(SomeSpringBean.class));
  }

  @SafeVarargs
  private static <T> Set<T> asSet(T... values) {
    return Stream.of(values).collect(Collectors.toSet());
  }

  @SafeVarargs
  private static <T> List<T> asList(T... values) {
    return Stream.of(values).collect(Collectors.toList());
  }

  // ==== Definition of some test beans ==== //

  @Bean
  public interface ICar {
  }

  public static abstract class AbstractVW implements ICar {
  }

  @Order(1)
  public static class Polo extends AbstractVW {
  }

  @Order(0)
  public static class Touran extends AbstractVW {
  }

  @Order(2)
  public static class Golf extends AbstractVW {
  }

  @Replace
  public static class EGolf extends Golf {
  }

  @Bean
  public static class InstanceScopedBean {
  }

  @ApplicationScoped
  public static class ApplicationScopedBean {
  }

  @Bean
  public static class PostConstructBean {

    private AtomicBoolean initialized = new AtomicBoolean();

    public boolean isInitialized() {
      return initialized.get();
    }

    @PostConstruct
    protected void postConstruct() {
      assertTrue(initialized.compareAndSet(false, true));
    }
  }

  @ApplicationScoped
  @CreateImmediately
  public static class CreateImmediatelyBean {

    private static final AtomicBoolean CONSTRUCTED = new AtomicBoolean();

    public static boolean isConstructed() {
      return CONSTRUCTED.get();
    }

    public CreateImmediatelyBean() {
      assertTrue(CONSTRUCTED.compareAndSet(false, true));
    }
  }

  @ApplicationScoped
  public interface IPingBean {
    String ping(String ping);
  }

  public static class PingBean implements IPingBean {

    @Override
    public String ping(String ping) {
      return ping;
    }
  }

  @Replace
  public static class PingDecorationFactory extends SimpleBeanDecorationFactory {

    @Override
    public <T> IBeanDecorator<T> decorate(IBean<T> bean, Class<? extends T> queryType) {
      if (PingBean.class.equals(bean.getBeanClazz())) {
        return new IBeanDecorator<T>() {

          @Override
          public Object invoke(IBeanInvocationContext<T> context) {
            Object returnValue = context.proceed();
            if ("ping".equals(context.getTargetMethod().getName())) {
              returnValue = ((String) returnValue).toUpperCase();
            }

            return returnValue;
          }
        };
      }
      return null;
    }
  }

  @ApplicationScoped
  public static class AutoWiredBean {

    @Inject
    public SomeSpringBean injectedBean;

    public SomeSpringBean getInjectedBean() {
      return injectedBean;
    }
  }

  @Component
  public static class SomeSpringBean {
  }
}
