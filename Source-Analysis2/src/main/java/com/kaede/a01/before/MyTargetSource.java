package com.kaede.a01.before;

import org.springframework.aop.TargetSource;

/**
 * @author kaede
 * @create 2023-02-19
 */

public class MyTargetSource implements TargetSource {
    @Override
    public Class<?> getTargetClass() {
        return User.class;
    }

    @Override
    public boolean isStatic() {
        return false;
    }

    @Override
    public Object getTarget() throws Exception {
        return new User("kaede");
    }

    @Override
    public void releaseTarget(Object target) throws Exception {

    }
}
