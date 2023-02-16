package com.kaede.a01;

import org.springframework.boot.autoconfigure.web.servlet.DispatcherServletRegistrationBean;
import org.springframework.boot.autoconfigure.web.servlet.WebMvcProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.web.embedded.tomcat.TomcatServletWebServerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.web.servlet.DispatcherServlet;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import java.util.Arrays;

@Configuration
// 默认扫描配置所在包及其子包
@ComponentScan
// 读取类路径下的配置文件
@PropertySource("classpath:application.properties")
@EnableConfigurationProperties(WebMvcProperties.class)
public class WebConfig {

    // 内嵌web容器工厂
    @Bean
    public TomcatServletWebServerFactory tomcatServletWebServerFactory() {
        return new TomcatServletWebServerFactory();
    }

    // 创建DispatcherServlet
    @Bean
    public DispatcherServlet dispatcherServlet() {
        return new DispatcherServlet();
    }

    // 注册DispatcherServlet，SpringMVC的入口
    @Bean
    public DispatcherServletRegistrationBean dispatcherServletRegistrationBean(
            DispatcherServlet dispatcherServlet, WebMvcProperties properties) {
        DispatcherServletRegistrationBean registrationBean = new DispatcherServletRegistrationBean(dispatcherServlet, "/");
        // DispatcherServlet默认在第一次被访问时初始化
        // 设置值大于0时会在tomcat启动时进行初始化
        // registrationBean.setLoadOnStartup(1);
        registrationBean.setLoadOnStartup(properties.getServlet().getLoadOnStartup());
        return registrationBean;
    }

    // 如果用DispatcherServlet初始化时默认添加的组件，并不会作为bean，给测试带来困扰
    // 1.加入RequestMappingHandlerMapping
    @Bean
    public RequestMappingHandlerMapping requestMappingHandlerMapping() {
        return new RequestMappingHandlerMapping();
    }
    // 2.继续加入RequestMappingHandlerAdapter，会替换掉DispatcherServlet默认的4个HandlerAdapter
    // 因为其中的invokeHandlerMethod的访问修饰符是protected，给定义子类型
    @Bean
    public MyRequestMappingHandlerAdapter myRequestMappingHandlerAdapter() {
        MyRequestMappingHandlerAdapter handlerAdapter = new MyRequestMappingHandlerAdapter();
        // 将自定义的参数解析器加入到handlerAdapter中
        handlerAdapter.setCustomArgumentResolvers(Arrays.asList(new TokenArgumentResolver()));
        // 将自定义的返回值处理器加入到handlerAdapter中
        handlerAdapter.setCustomReturnValueHandlers(Arrays.asList(new YmlReturnValueHandler()));
        return handlerAdapter;
    }
    // 3.演示RequestMappingHandlerAdapter初始化后，有哪些参数、返回值处理器

    // 3.1创建自定义参数处理器

    // 3.2创建自定义返回值处理器

}
