package com.kaede.a01;

import org.springframework.context.ApplicationEvent;

/**
 * @author kaede
 * @create 2023-02-11
 */

// 事件一般要继承ApplicationEvent
public class UserRegisteredEvent extends ApplicationEvent {

    public UserRegisteredEvent(Object source) {
        super(source);
    }

}
