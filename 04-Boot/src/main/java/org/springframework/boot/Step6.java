package org.springframework.boot;

import lombok.Data;
import org.springframework.boot.context.properties.bind.Bindable;
import org.springframework.boot.context.properties.bind.Binder;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.StandardEnvironment;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.support.ResourcePropertySource;

import java.io.IOException;
import java.lang.reflect.Field;

public class Step6 {

    public static void main(String[] args) throws IOException, IllegalAccessException, NoSuchFieldException {
        SpringApplication application = new SpringApplication();
        ConfigurableEnvironment env = new StandardEnvironment();
        env.getPropertySources().addLast(
            new ResourcePropertySource("step4", new ClassPathResource("step4.properties")));

        // 指定绑定键值的前缀、绑定的数据类型
        User user = Binder.get(env).bind("user", User.class).get();
        System.out.println(user);

        // 指定绑定键值的前缀、绑定的对象
        User user1 = new User();
        Binder.get(env).bind("user", Bindable.ofInstance(user1));
        System.out.println(user1);

        // 绑定spring.main前缀的key-value至SpringApplication
        env.getPropertySources().addLast(
            new ResourcePropertySource("step6", new ClassPathResource("step6.properties")));
        Field field = application.getClass().getDeclaredField("lazyInitialization");
        field.setAccessible(true);
        System.out.println(field.get(application));

        Binder.get(env).bind("spring.main", Bindable.ofInstance(application));
        Field field1 = application.getClass().getDeclaredField("lazyInitialization");
        field1.setAccessible(true);
        System.out.println(field1.get(application));
    }

    @Data
    static class User {
        private String firstName;
        private String middleName;
        private String lastName;
    }

}
