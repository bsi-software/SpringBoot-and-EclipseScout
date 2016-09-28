package org.eclipse.scout.tasks.scout.platform;

import org.eclipse.scout.rt.platform.IBean;
import org.eclipse.scout.rt.platform.IBeanInstanceProducer;
import org.springframework.context.ApplicationContext;

/**
 * Acts as delegate to lookup beans in Spring bean manager.
 */
public class ScoutSpringBeanProducer<T> implements IBeanInstanceProducer<T> {

  private final ApplicationContext springApplicationContext;

  public ScoutSpringBeanProducer(ApplicationContext springApplicationContext) {
    this.springApplicationContext = springApplicationContext;
  }

  @Override
  public T produce(IBean<T> bean) {
    // Lookup the bean by its fully qualified name and not its class name. This
    // allows to query that very bean only, and does not return beans which implement that type.
    //
    // BEANS.get(RunContext.class) --> returns a RunContext instance only, but not ClientRunContext
    // BEANS.all(RunContext.class) --> returns RunContext and ClientRunContext instances
    return springApplicationContext.getBean(bean.getBeanClazz().getName(), bean.getBeanClazz());
  }
}
