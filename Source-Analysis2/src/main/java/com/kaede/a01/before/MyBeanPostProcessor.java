package com.kaede.a01.before;

import org.springframework.aop.framework.autoproxy.AbstractAutoProxyCreator;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.core.PriorityOrdered;
import org.springframework.stereotype.Component;

/**
 * @author kaede
 * @create 2023-02-19
 */

@Component
public class MyBeanPostProcessor implements BeanPostProcessor, PriorityOrdered {

    @Override
    public int getOrder() {
        return -1;
    }

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        if (bean instanceof AbstractAutoProxyCreator) {
            ((AbstractAutoProxyCreator) bean).setCustomTargetSourceCreators(
                new CustomTargetSourceCreator()
            );
        }
        return bean;
    }

}
