package com.kaede.a03;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationListener;

/**
 * @author kaede
 * @create 2023-02-17
 */

@Slf4j
public class MessageListener implements ApplicationListener<OrderEvent> {

    @Override
    public void onApplicationEvent(OrderEvent event) {
        log.info("MessageListener listen ...");
        log.info("event = {}", event);
    }

}
