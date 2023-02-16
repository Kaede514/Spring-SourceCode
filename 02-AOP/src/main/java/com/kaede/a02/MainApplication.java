package com.kaede.a02;

import com.kaede.a02.service.MyService;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

// VM Options:-javaagent:D:/Maven/MavenRepo/org/aspectj/aspectjweaver/1.9.7/aspectjweaver-1.9.7.jar
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
