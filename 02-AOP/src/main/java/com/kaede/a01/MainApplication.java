package com.kaede.a01;

import com.kaede.a01.service.MyService;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

@SpringBootApplication
public class MainApplication {

    public static void main(String[] args) {
        ConfigurableApplicationContext context = SpringApplication.run(MainApplication.class, args);

        MyService service = context.getBean(MyService.class);
        System.out.println("service class: " + service.getClass());
        service.foo();

        context.close();
    }

}
