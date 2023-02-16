package com.kaede.a04;

import lombok.Data;
import org.springframework.boot.convert.ApplicationConversionService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.format.support.DefaultFormattingConversionService;
import org.springframework.format.support.FormattingConversionService;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.web.bind.ServletRequestDataBinder;
import org.springframework.web.bind.ServletRequestParameterPropertyValues;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.support.ConfigurableWebBindingInitializer;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.method.support.InvocableHandlerMethod;
import org.springframework.web.servlet.mvc.method.annotation.ServletRequestDataBinderFactory;

import java.util.Arrays;
import java.util.Date;

public class TestServletDataBinderFactory {
    public static void main(String[] args) throws Exception {
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.setParameter("birthday", "1999|01|02");
        request.setParameter("address.name", "西安");

        User target = new User();
        // 1.用工厂创建DataBinder，无转换功能
        /* ServletRequestDataBinderFactory factory = new ServletRequestDataBinderFactory(null, null);
        WebDataBinder dataBinder = factory.createBinder(new ServletWebRequest(request), target, "user"); */

        // 2.用@InitBinder转换 PropertyEditorRegistry PropertyEditor
        /* InvocableHandlerMethod method = new InvocableHandlerMethod(new MyController(), MyController.class.getMethod("aaa", WebDataBinder.class));
        ServletRequestDataBinderFactory factory = new ServletRequestDataBinderFactory(Arrays.asList(method), null);
        WebDataBinder dataBinder = factory.createBinder(new ServletWebRequest(request), target, "user"); */

        // 3.用ConversionService转换 ConversionService Formatter
        /* FormattingConversionService service = new FormattingConversionService();
        service.addFormatter(new MyDateFormatter("用ConversionService方式扩展转换功能"));
        ConfigurableWebBindingInitializer initializer = new ConfigurableWebBindingInitializer();
        initializer.setConversionService(service);
        ServletRequestDataBinderFactory factory = new ServletRequestDataBinderFactory(null, initializer); */

        // 4.同时加了@InitBinder和ConversionService
        // InvocableHandlerMethod method = new InvocableHandlerMethod(new MyController(), MyController.class.getMethod("aaa", WebDataBinder.class));
        // FormattingConversionService service = new FormattingConversionService();
        // service.addFormatter(new MyDateFormatter("用ConversionService方式扩展转换功能"));
        // ConfigurableWebBindingInitializer initializer = new ConfigurableWebBindingInitializer();
        // initializer.setConversionService(service);
        // ServletRequestDataBinderFactory factory = new ServletRequestDataBinderFactory(Arrays.asList(method), initializer);

        // 5.使用默认ConversionService转换，需要配合注解使用
        // SpringBoot中的ApplicationConversionService功能更强大
        DefaultFormattingConversionService service = new DefaultFormattingConversionService();
        ConfigurableWebBindingInitializer initializer = new ConfigurableWebBindingInitializer();
        initializer.setConversionService(service);
        ServletRequestDataBinderFactory factory = new ServletRequestDataBinderFactory(null, initializer);

        WebDataBinder dataBinder = factory.createBinder(new ServletWebRequest(request), target, "user");
        dataBinder.bind(new ServletRequestParameterPropertyValues(request));
        System.out.println(target);
    }

    static class MyController {
        @InitBinder
        public void aaa(WebDataBinder dataBinder) {
            // 扩展dataBinder的转换器
            dataBinder.addCustomFormatter(new MyDateFormatter("用@InitBinder方式扩展的"));
        }
    }

    @Data
    public static class User {
        @DateTimeFormat(pattern = "yyyy|MM|dd")
        private Date birthday;
        private Address address;
    }

    @Data
    public static class Address {
        private String name;
    }
}
