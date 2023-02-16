package com.kaede.a01;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

/**
 * @author kaede
 * @create 2023-02-11
 */

@Slf4j
@Component
public class Component1 {

    @Autowired
    private ApplicationEventPublisher context;

    public void register() {
        log.info("用户注册");
        context.publishEvent(new UserRegisteredEvent(this));
    }

}
