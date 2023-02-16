package com.kaede.a05.component;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;

/**
 * @author kaede
 * @create 2023-02-12
 */

@Controller
@Slf4j
public class Bean3 {
    public Bean3() {
        log.info("Bean3被Spring管理了");
    }
}