package com.kaede.a05;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class Bean1 {
    public Bean1() {
        log.info("Bean1被Spring管理了");
    }
}

