package com.kaede.a05;

import org.springframework.beans.factory.FactoryBean;
import org.springframework.stereotype.Component;

/**
 * @author kaede
 * @create 2023-02-18
 */

// @Component("user")
public class UserFactoryBean implements FactoryBean<User> {

    @Override
    public User getObject() throws Exception {
        // 过程可能很复杂，往往用来完成复杂对象的创建
        return new User("kaede", 22);
    }

    @Override
    public Class<?> getObjectType() {
        return User.class;
    }

}
