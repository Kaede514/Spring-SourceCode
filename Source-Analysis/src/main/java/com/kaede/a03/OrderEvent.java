package com.kaede.a03;

import org.springframework.context.ApplicationEvent;

/**
 * @author kaede
 * @create 2023-02-17
 */

public class OrderEvent extends ApplicationEvent {
    public OrderEvent(Object source) {
        super(source);
    }
}
