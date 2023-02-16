package com.kaede.a02;

import org.apache.catalina.Context;
import org.apache.catalina.LifecycleException;
import org.apache.catalina.connector.Connector;
import org.apache.catalina.startup.Tomcat;
import org.apache.coyote.http11.Http11Nio2Protocol;
import org.springframework.boot.autoconfigure.web.servlet.DispatcherServletRegistrationBean;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;
import org.springframework.web.servlet.DispatcherServlet;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerAdapter;

import javax.servlet.ServletContainerInitializer;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Arrays;
import java.util.Collections;
import java.util.Map;
import java.util.Set;

public class TestTomcat {

    public static void main(String[] args) throws LifecycleException, IOException {
        // 1.创建Tomcat对象
        Tomcat tomcat = new Tomcat();
        // 设置基础目录
        tomcat.setBaseDir("tomcat");

        // 2.创建项目文件夹，即docBase文件夹
        File docBase = Files.createTempDirectory("boot.").toFile();
        // 程序退出时自动删除该临时目录
        docBase.deleteOnExit();

        // 3.创建Tomcat项目，在Tomcat中称为Context，空白字符串代表虚拟目录/
        Context context = tomcat.addContext("", docBase.getAbsolutePath());

        WebApplicationContext springContext = getApplicationContext();

        // 4.编程添加Servlet
        context.addServletContainerInitializer(new ServletContainerInitializer() {
            @Override
            public void onStartup(Set<Class<?>> c, ServletContext ctx) throws ServletException {
                HelloServlet helloServlet = new HelloServlet();
                ctx.addServlet("aaa", helloServlet).addMapping("/hello");

                DispatcherServlet dispatcherServlet = springContext.getBean(DispatcherServlet.class);
                ctx.addServlet("dispatcherServlet", dispatcherServlet).addMapping("/");
                for (ServletRegistrationBean registrationBean : springContext.getBeansOfType(ServletRegistrationBean.class).values()) {
                    registrationBean.onStartup(ctx);
                }
            }
        }, Collections.emptySet());

        // 5.启动Tomcat
        tomcat.start();

        // 6.创建连接器，设置监听端口
        Connector connector = new Connector(new Http11Nio2Protocol());
        connector.setPort(8080);
        tomcat.setConnector(connector);
    }

    public static WebApplicationContext getApplicationContext() {
        // AnnotationConfigServletWebServerApplicationContext已经支持了内嵌tomcat
        // 此处使用不支持tomcat的实现
        AnnotationConfigWebApplicationContext context = new AnnotationConfigWebApplicationContext();
        context.register(Config.class);
        context.refresh();
        return context;
    }

    @Configuration
    static class Config {
        @Bean
        public DispatcherServletRegistrationBean registrationBean(DispatcherServlet dispatcherServlet) {
            return new DispatcherServletRegistrationBean(dispatcherServlet, "/");
        }

        @Bean
        // 这个例子中必须为DispatcherServlet提供AnnotationConfigWebApplicationContext，否则会选择XmlWebApplicationContext实现
        public DispatcherServlet dispatcherServlet(WebApplicationContext applicationContext) {
            return new DispatcherServlet(applicationContext);
        }

        @Bean
        public RequestMappingHandlerAdapter requestMappingHandlerAdapter() {
            RequestMappingHandlerAdapter handlerAdapter = new RequestMappingHandlerAdapter();
            handlerAdapter.setMessageConverters(Arrays.asList(new MappingJackson2HttpMessageConverter()));
            return handlerAdapter;
        }

        @RestController
        static class MyController {
            @GetMapping("hello2")
            public Map<String,Object> hello() {
                return Collections.singletonMap("hello2", "hello2, spring!");
            }
        }
    }

}
