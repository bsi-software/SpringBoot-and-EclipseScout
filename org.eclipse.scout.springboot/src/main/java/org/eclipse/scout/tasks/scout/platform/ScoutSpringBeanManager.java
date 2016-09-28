package org.eclipse.scout.tasks.scout.platform;

import org.eclipse.scout.rt.platform.BeanMetaData;
import org.eclipse.scout.rt.platform.IBean;
import org.eclipse.scout.rt.platform.internal.BeanImplementor;
import org.eclipse.scout.rt.platform.internal.BeanManagerImplementor;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.GenericBeanDefinition;
import org.springframework.context.ApplicationContext;

/**
 * Bean manager which registers bean meta data in Scout and Spring bean manager. However, the bean instance is managed
 * by Spring bean manager only. For more detail, see {@link ScoutSpringBeanProducer}
 *
 * @see ScoutSpringBeanProducer
 */
public class ScoutSpringBeanManager extends BeanManagerImplementor {

  private final ApplicationContext springApplicationContext;
  private final BeanDefinitionRegistry springBeanRegistry;

  public ScoutSpringBeanManager(final ApplicationContext springApplicationContext, final BeanDefinitionRegistry springBeanRegistry) {
    this.springApplicationContext = springApplicationContext;
    this.springBeanRegistry = springBeanRegistry;
  }

  @Override
  protected <T> BeanImplementor<T> createBeanImplementor(final BeanMetaData beanData) {
    return new BeanImplementor<>(beanData, new ScoutSpringBeanProducer<>(springApplicationContext));
  }

  @Override
  public <T> IBean<T> registerBean(final BeanMetaData beanData) {
    final IBean<T> bean = super.registerBean(beanData);
    registerAsSpringBean(bean);
    return bean;
  }

  @Override
  public void unregisterBean(final IBean<?> bean) {
    unregisterAsSpringBean(bean);
    super.unregisterBean(bean);
  }

  private <T> void registerAsSpringBean(final IBean<T> bean) {
    if (bean.getBeanInstanceProducer() == null) {
      return;
    }

    final GenericBeanDefinition springBean = new GenericBeanDefinition();
    springBean.setBeanClass(bean.getBeanClazz());
    if (BeanManagerImplementor.isApplicationScoped(bean)) {
      springBean.setScope(BeanDefinition.SCOPE_SINGLETON);
      springBean.setLazyInit(!BeanManagerImplementor.isCreateImmediately(bean));
    }
    else {
      springBean.setScope(BeanDefinition.SCOPE_PROTOTYPE);
    }
    springBeanRegistry.registerBeanDefinition(bean.getBeanClazz().getName(), springBean);
  }

  private void unregisterAsSpringBean(final IBean<?> bean) {
    springBeanRegistry.removeBeanDefinition(bean.getBeanClazz().getName());
  }
}
