package com.kaede.a01;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.embedded.tomcat.TomcatServletWebServerFactory;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.support.GenericApplicationContext;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashSet;

/**
 * @author kaede
 * @create 2023-02-15
 */

@SpringBootApplication
public class MainApplication {

    public static void main(String[] args) throws InvocationTargetException, IllegalAccessException, NoSuchMethodException {
        System.out.println("1.获取 BeanDefinition 源");
        SpringApplication application = new SpringApplication(MainApplication.class);
        HashSet<String> set = new HashSet<>();
        set.add("classpath:b01.xml");
        application.setSources(set);
        System.out.println("2.推断应用类型");
        Method method = WebApplicationType.class.getDeclaredMethod("deduceFromClasspath");
        method.setAccessible(true);
        System.out.println("应用类型为: " + method.invoke(null, null));
        System.out.println("3.添加 ApplicationContext 初始化器");
        application.addInitializers(new ApplicationContextInitializer<ConfigurableApplicationContext>() {
            @Override
            public void initialize(ConfigurableApplicationContext applicationContext) {
                if (applicationContext instanceof GenericApplicationContext) {
                    ((GenericApplicationContext) applicationContext).registerBean("bean3" ,Bean3.class);
                }
            }
        });
        System.out.println("4.添加监听器");
        application.addListeners(new ApplicationListener<ApplicationEvent>() {
            @Override
            public void onApplicationEvent(ApplicationEvent event) {
                System.out.println("事件类型为: " + event.getClass());
            }
        });
        System.out.println("5.推断主启动类");
        Method method1 = SpringApplication.class.getDeclaredMethod("deduceMainApplicationClass");
        method1.setAccessible(true);
        System.out.println("主类是: " + method1.invoke(application, null));
        // 创建初始化好的Spring容器
        ConfigurableApplicationContext context = application.run(args);
        for (String name : context.getBeanDefinitionNames()) {
            System.out.println(name);
        }
        context.close();
    }

    @Bean
    public TomcatServletWebServerFactory servletWebServerFactory() {
        return new TomcatServletWebServerFactory();
    }

    static class Bean1 {
    }

    static class Bean2 {
    }
    @Bean
    public Bean2 bean2() {
        return new Bean2();
    }

    static class Bean3 {

    }

}
