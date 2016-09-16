package org.eclipse.scout.springboot.platform;

import org.eclipse.scout.rt.platform.BEANS;
import org.eclipse.scout.rt.platform.Order;
import org.eclipse.scout.rt.platform.PlatformStateLatch;
import org.eclipse.scout.rt.platform.Replace;
import org.eclipse.scout.rt.platform.interceptor.IBeanDecorator;
import org.eclipse.scout.rt.platform.internal.BeanFilter;
import org.eclipse.scout.rt.platform.internal.BeanManagerImplementor;
import org.eclipse.scout.rt.platform.internal.PlatformImplementor;
import org.eclipse.scout.rt.platform.inventory.ClassInventory;
import org.eclipse.scout.rt.platform.util.Assertions;
import org.eclipse.scout.rt.server.commons.WebappEventListener;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationListener;
import org.springframework.context.ConfigurableApplicationContext;

/**
 * Scout platform which uses Spring bean manager to manage and lookup beans.
 * <p>
 * This allows Scout beans to be looked up via {@link BEANS#get(Class)} or {@link ApplicationContext#getBean(Class)}.
 * Also, when lookup beans via Scout bean manager, the semantic of {@link Replace}, {@link Order} and
 * {@link IBeanDecorator} is still guaranteed.
 * <p>
 * This class is registered in 'META-INF/services/org.eclipse.scout.rt.platform'.
 */
public class ScoutSpringPlatform extends PlatformImplementor implements ApplicationListener<ApplicationReadyEvent> {

  private ConfigurableApplicationContext springApplicationContext;

  /**
   * This method does nothing. Use {@link #start(ConfigurableApplicationContext)} instead.
   */
  @Override
  public void start(final PlatformStateLatch stateLatch) {
    stateLatch.release();
  }

  /**
   * Starts the Scout platform.
   */
  public void start(final ConfigurableApplicationContext context) {
    Assertions.assertFalse(isPlatformStarted());
    this.springApplicationContext = context;
    this.springApplicationContext.addApplicationListener(this);
    super.start(null);
  }

  @Override
  public void onApplicationEvent(final ApplicationReadyEvent event) {
    super.changeState(State.PlatformStarted, true);
    notifyPlatformStarted();
  }

  @Override
  protected void changeState(final State newState, final boolean throwOnIllegalStateChange) {
    if (State.PlatformStarted.equals(newState)) {
      return; // Scout platform transitions into this state upon Spring platform is ready. See 'onApplicationEvent(ApplicationReadyEvent)'.
    }
    super.changeState(newState, throwOnIllegalStateChange);
  }

  @Override
  protected BeanManagerImplementor createBeanManager() {
    final BeanDefinitionRegistry springBeanRegistry = (BeanDefinitionRegistry) springApplicationContext.getBeanFactory();
    final ScoutSpringBeanManager beanManager = new ScoutSpringBeanManager(springApplicationContext, springBeanRegistry);

    for (final Class<?> bean : new BeanFilter().collect(ClassInventory.get())) {
      if (!ignoreBean(bean)) {
        beanManager.registerClass(bean);
      }
    }

    return beanManager;
  }

  private boolean ignoreBean(final Class<?> bean) {
    return bean == WebappEventListener.ServletContextRegistration.class; // Platform listener which registers ServletContext
  }
}
