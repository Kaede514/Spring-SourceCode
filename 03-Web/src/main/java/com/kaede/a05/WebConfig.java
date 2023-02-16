package com.kaede.a05;

import com.kaede.a04.MyDateFormatter;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;

@Configuration
public class WebConfig {

    /**
     * @ControllerAdvice：对控制器提供一些增强功能
     *  1.@ExceptionHandler：处理异常
     *  2.@ModelAttribute：返回值作为模型数据补充至控制器的执行过程中
     *  3.@InitBinder：补充自定义的类型转换器
     */
    @ControllerAdvice
    static class MyControllerAdvice {
        @InitBinder
        public void binder(WebDataBinder webDataBinder) {
            webDataBinder.addCustomFormatter(new MyDateFormatter("binder 转换器"));
        }
    }

    @Controller
    static class Controller1 {
        @InitBinder
        public void binder1(WebDataBinder webDataBinder) {
            webDataBinder.addCustomFormatter(new MyDateFormatter("binder1 转换器"));
        }

        public void foo() {}
    }

    @Controller
    static class Controller2 {
        @InitBinder
        public void binder21(WebDataBinder webDataBinder) {
            webDataBinder.addCustomFormatter(new MyDateFormatter("binder21 转换器"));
        }

        @InitBinder
        public void binder22(WebDataBinder webDataBinder) {
            webDataBinder.addCustomFormatter(new MyDateFormatter("binder22 转换器"));
        }

        public void bar() {}
    }

}
