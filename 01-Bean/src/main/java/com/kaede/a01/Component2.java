package com.kaede.a01;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

/**
 * @author kaede
 * @create 2023-02-11
 */

@Slf4j
@Component
public class Component2 {

    @EventListener
    public void receiveEvent(UserRegisteredEvent event) {
        log.info("{}", event);
        log.info("发送短信");
    }

}
