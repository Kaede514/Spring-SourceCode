package com.kaede.a01;

import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.beans.factory.xml.XmlBeanDefinitionReader;
import org.springframework.boot.*;
import org.springframework.boot.web.embedded.tomcat.TomcatServletWebServerFactory;
import org.springframework.boot.web.reactive.context.AnnotationConfigReactiveWebServerApplicationContext;
import org.springframework.boot.web.servlet.context.AnnotationConfigServletWebServerApplicationContext;
import org.springframework.boot.web.servlet.server.ServletWebServerFactory;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.*;
import org.springframework.context.support.GenericApplicationContext;
import org.springframework.core.io.ClassPathResource;

import java.util.Arrays;

public class MainApplication3 {

    public static void main(String[] args) throws Exception {
        SpringApplication application = new SpringApplication();
        application.addInitializers(new ApplicationContextInitializer<ConfigurableApplicationContext>() {
            @Override
            public void initialize(ConfigurableApplicationContext applicationContext) {
                System.out.println("执行初始化器增强");
            }
        });

        System.out.println(">>>>>>>>>>>>>>>>>>>>>>>> 2.封装启动args");
        DefaultApplicationArguments arguments = new DefaultApplicationArguments(args);

        System.out.println(">>>>>>>>>>>>>>>>>>>>>>>> 8.创建容器");
        GenericApplicationContext context = createApplicationContext(WebApplicationType.SERVLET);

        System.out.println(">>>>>>>>>>>>>>>>>>>>>>>> 9.准备容器");
        // 回调初始化器
        for (ApplicationContextInitializer initializer : application.getInitializers()) {
            initializer.initialize(context);
        }

        System.out.println(">>>>>>>>>>>>>>>>>>>>>>>> 10.加载bean定义");
        DefaultListableBeanFactory beanFactory = context.getDefaultListableBeanFactory();
        AnnotatedBeanDefinitionReader reader = new AnnotatedBeanDefinitionReader(
            beanFactory // 设置将来读取的BeanDefination的存放位置
        );
        reader.register(Config.class);
        XmlBeanDefinitionReader reader2 = new XmlBeanDefinitionReader(beanFactory);
        reader2.loadBeanDefinitions(new ClassPathResource("b03.xml"));
        ClassPathBeanDefinitionScanner scanner = new ClassPathBeanDefinitionScanner(beanFactory);
        scanner.scan("com.kaede.a01.sub");

        System.out.println(">>>>>>>>>>>>>>>>>>>>>>>> 11.refresh容器");
        context.refresh();
        for (String name : context.getBeanDefinitionNames()) {
            System.out.println("name: " + name + "  source: "  +
                beanFactory.getBeanDefinition(name).getResourceDescription());
        }

        System.out.println(">>>>>>>>>>>>>>>>>>>>>>>> 12.执行runner");
        // 此时SpringBoot已启动完毕，可执行一些数据预加载、测试等操作
        for (CommandLineRunner runner : context.getBeansOfType(CommandLineRunner.class).values()) {
            runner.run(args);
        }
        for (ApplicationRunner runner : context.getBeansOfType(ApplicationRunner.class).values()) {
            runner.run(arguments);
        }
    }

    private static GenericApplicationContext createApplicationContext(WebApplicationType type) {
        GenericApplicationContext context = null;
        switch (type) {
            case SERVLET:
                context = new AnnotationConfigServletWebServerApplicationContext();
            case REACTIVE:
                context = new AnnotationConfigReactiveWebServerApplicationContext();
            case NONE:
                context = new AnnotationConfigApplicationContext();
        }
        return context;
    }

    static class Bean4 {
    }
    static class Bean5 {
    }
    static class Bean6 {
    }

    @Configuration
    static class Config {
        @Bean
        public Bean5 bean5() {
            return new Bean5();
        }

        @Bean
        public ServletWebServerFactory servletWebServerFactory() {
            return new TomcatServletWebServerFactory();
        }

        @Bean
        public CommandLineRunner commandLineRunner() {
            return new CommandLineRunner() {
                // args是从main方法中传递过来的参数数组
                @Override
                public void run(String... args) throws Exception {
                    System.out.println("commandLineRunner()... " + Arrays.toString(args));
                }
            };
        }

        @Bean
        public ApplicationRunner applicationRunner() {
            return new ApplicationRunner() {
                // args是main方法传递的经过封装后的参数对象
                @Override
                public void run(ApplicationArguments args) throws Exception {
                    System.out.println("applicationRunner()... " + Arrays.toString(args.getSourceArgs()));
                    System.out.println(args.getOptionNames());
                    System.out.println(args.getOptionValues("server.port"));
                    System.out.println(args.getNonOptionArgs());
                }
            };
        }
    }

}
