package com.kaede.a01;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.yaml.snakeyaml.Yaml;

@Controller
@Slf4j
public class Controller1 {

    @GetMapping("/test1")
    public ModelAndView test1() throws Exception {
        log.info("test1()");
        return null;
    }

    @PostMapping("/test2")
    public ModelAndView test2(@RequestParam("name") String name) {
        log.info("test2({})", name);
        return null;
    }

    @PutMapping("/test3")
    public ModelAndView test3(@Token String token) {
        log.info("test3({})", token);
        return null;
    }

    @RequestMapping("/test4")
    @Yml
    public User test4() {
        log.info("test4");
        return new User("张三", 18);
    }

    @Data
    @AllArgsConstructor
    public static class User {
        private String name;
        private int age;
    }

}
