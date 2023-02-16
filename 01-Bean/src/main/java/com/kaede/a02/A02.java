package com.kaede.a02;

import org.springframework.boot.autoconfigure.web.servlet.DispatcherServletRegistrationBean;
import org.springframework.boot.web.embedded.tomcat.TomcatServletWebServerFactory;
import org.springframework.boot.web.servlet.context.AnnotationConfigServletWebServerApplicationContext;
import org.springframework.boot.web.servlet.server.ServletWebServerFactory;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.context.support.FileSystemXmlApplicationContext;
import org.springframework.web.servlet.DispatcherServlet;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.Controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class A02 {

    static class Bean1 {
    }

    static class Bean2 {
        private Bean1 bean1;

        public void setBean1(Bean1 bean1) {
            this.bean1 = bean1;
        }

        public Bean1 getBean1() {
            return bean1;
        }
    }

    public static void main(String[] args) {
        // testClassPathXmlApplicationContext();
        // testFileSystemXmlApplicationContext();
        // 原理
        /* DefaultListableBeanFactory beanFactory = new DefaultListableBeanFactory();
        System.out.println("读取之前...");
        for (String name : beanFactory.getBeanDefinitionNames()) {
            System.out.println(name);
        }
        // 找到xml配置文件，将其中的标签变换为BeanDefination
        XmlBeanDefinitionReader reader = new XmlBeanDefinitionReader(beanFactory);
        reader.loadBeanDefinitions(new ClassPathResource("b01.xml"));
        // reader.loadBeanDefinitions(new FileSystemResource("01-Bean/src/main/resources/b01.xml"));
        System.out.println("读取之后...");
        for (String name : beanFactory.getBeanDefinitionNames()) {
            System.out.println(name);
        } */
        // testAnnotationConfigApplicationContext();
        testAnnotationConfigServletWebServerApplicationContext();
    }

    // 较为经典的容器，基于classpath下xml格式的配置文件来创建SpringApplicationContext容器
    private static void testClassPathXmlApplicationContext() {
        ClassPathXmlApplicationContext context =
                new ClassPathXmlApplicationContext("b01.xml");
        for (String name : context.getBeanDefinitionNames()) {
            System.out.println(name);
        }
        System.out.println(context.getBean(Bean2.class).getBean1());
    }

    // 基于磁盘路径下xml格式的配置文件来创建
    private static void testFileSystemXmlApplicationContext() {
        FileSystemXmlApplicationContext context =
            new FileSystemXmlApplicationContext(
                "01-Bean/src/main/resources/b01.xml");
        for (String name : context.getBeanDefinitionNames()) {
            System.out.println(name);
        }
        System.out.println(context.getBean(Bean2.class).getBean1());
    }

    @Configuration
    static class Config {
        @Bean
        public Bean1 bean1() {
            return new Bean1();
        }
        @Bean
        public Bean2 bean2(Bean1 bean1) {
            Bean2 bean2 = new Bean2();
            bean2.setBean1(bean1);
            return bean2;
        }
    }

    // 较为经典的容器，基于java配置类来创建
    private static void testAnnotationConfigApplicationContext() {
        AnnotationConfigApplicationContext context =
            new AnnotationConfigApplicationContext(Config.class);
        for (String name : context.getBeanDefinitionNames()) {
            System.out.println(name);
        }
        System.out.println(context.getBean(Bean2.class).getBean1());
    }

    @Configuration
    static class WebConfig {
        // 至少要加如下三个Bean才能适用于web环境
        @Bean
        public ServletWebServerFactory servletWebServerFactory(){
            // 提供Web容器
            return new TomcatServletWebServerFactory();
        }
        @Bean
        public DispatcherServlet dispatcherServlet() {
            // 前端控制器，请求的入口点
            return new DispatcherServlet();
        }
        @Bean
        public DispatcherServletRegistrationBean registrationBean(DispatcherServlet dispatcherServlet) {
            // 将DispatcherServlet注册到tomcat服务器中
            return new DispatcherServletRegistrationBean(dispatcherServlet, "/");
        }
        // 给Bean起名时如果以/开头，就可以识别/后的名称作为访问路径
        @Bean("/hello")
        public Controller controller1() {
            return new Controller() {
                @Override
                public ModelAndView handleRequest(HttpServletRequest req, HttpServletResponse resp) throws Exception {
                    resp.getWriter().print("hello");
                    return null;
                }
            };
        }
    }

    // 较为经典的容器，基于java配置类来创建，用于web环境
    private static void testAnnotationConfigServletWebServerApplicationContext() {
        AnnotationConfigServletWebServerApplicationContext context =
            new AnnotationConfigServletWebServerApplicationContext(WebConfig.class);
        for (String name : context.getBeanDefinitionNames()) {
            System.out.println(name);
        }
    }

}
