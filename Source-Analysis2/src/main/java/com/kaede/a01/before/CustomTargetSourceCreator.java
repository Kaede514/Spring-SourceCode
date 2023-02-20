package com.kaede.a01.before;

import org.springframework.aop.TargetSource;
import org.springframework.aop.framework.autoproxy.TargetSourceCreator;

/**
 * @author kaede
 * @create 2023-02-19
 */

public class CustomTargetSourceCreator implements TargetSourceCreator {

    @Override
    public TargetSource getTargetSource(Class<?> beanClass, String beanName) {
        if (beanClass.isAssignableFrom(User.class)) {
            return new MyTargetSource();
        }
        return null;
    }

}
