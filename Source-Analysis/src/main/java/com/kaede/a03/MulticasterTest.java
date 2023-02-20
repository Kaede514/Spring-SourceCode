package com.kaede.a03;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.context.event.SimpleApplicationEventMulticaster;

/**
 * @author kaede
 * @create 2023-02-17
 */

@Slf4j
public class MulticasterTest {

    @Test
    public void testEventMulticaster() {
        SimpleApplicationEventMulticaster caster = new SimpleApplicationEventMulticaster();
        // 添加观察者
        caster.addApplicationListener(new EmailListener());
        caster.addApplicationListener(new EmailListener());
        caster.addApplicationListener(new MessageListener());
        // 发布事件
        caster.multicastEvent(new OrderEvent(this));
    }

}
