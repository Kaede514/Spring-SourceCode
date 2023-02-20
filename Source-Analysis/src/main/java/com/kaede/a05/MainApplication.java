package com.kaede.a05;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.support.GenericApplicationContext;

/**
 * @author kaede
 * @create 2023-02-18
 */

@SpringBootApplication
public class MainApplication {

    public static void main(String[] args) throws Exception {
        ConfigurableApplicationContext context = SpringApplication.run(MainApplication.class, args);
        /* UserService service = context.getBean(UserService.class);
        System.out.println(service);
        Object user = context.getBean("user");
        System.out.println(user);
        User user1 = context.getBean(User.class);
        System.out.println(user1);
        System.out.println(user == user1);
        Object factoryBean = context.getBean("&user");
        System.out.println(factoryBean);
        UserFactoryBean factoryBean1 = context.getBean(UserFactoryBean.class);
        System.out.println(factoryBean1);
        System.out.println(factoryBean == factoryBean1); */
    }

}
