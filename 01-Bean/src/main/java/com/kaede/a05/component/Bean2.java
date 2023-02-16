package com.kaede.a05.component;

import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class Bean2 {
    public Bean2() {
        log.info("Bean2被Spring管理了");
    }
}
