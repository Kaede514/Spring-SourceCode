package com.kaede.a08;

import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import javax.annotation.PreDestroy;

@Scope("application")
@Component
@Slf4j
public class BeanForApplication {

    @PreDestroy
    public void destroy() {
        log.info("BeanForApplication destroy");
    }

}