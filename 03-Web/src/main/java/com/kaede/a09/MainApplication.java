package com.kaede.a09;

import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.method.annotation.ExceptionHandlerExceptionResolver;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Collections;
import java.util.Map;

public class MainApplication {

    static class Controller1 {
        public void foo() {}
        @ExceptionHandler
        @ResponseBody
        public Map<String, Object> handle(ArithmeticException e) {
            return Collections.singletonMap("error", e.getMessage());
        }
    }

    static class Controller2 {
        public void foo() {}
        @ExceptionHandler
        public ModelAndView handle(ArithmeticException e) {
            return new ModelAndView("test2", Collections.singletonMap("error", e.getMessage()));
        }
    }

    static class Controller3 {
        public void foo() {}
        @ExceptionHandler
        @ResponseBody
        public Map<String, Object> handle(IOException e3) {
            return Collections.singletonMap("error", e3.getMessage());
        }
    }

    static class Controller4 {
        public void foo() {}
        @ExceptionHandler
        @ResponseBody
        public Map<String, Object> handler(Exception e, HttpServletRequest request) {
            System.out.println(request);
            return Collections.singletonMap("error", e.getMessage());
        }
    }

    public static void main(String[] args) throws NoSuchMethodException {
        ExceptionHandlerExceptionResolver resolver = new ExceptionHandlerExceptionResolver();
        resolver.setMessageConverters(Arrays.asList(new MappingJackson2HttpMessageConverter()));
        // 其中添加了一些默认的参数解析器和返回值处理器
        resolver.afterPropertiesSet();

        MockHttpServletRequest request = new MockHttpServletRequest();
        MockHttpServletResponse response = new MockHttpServletResponse();

        // 1.测试json
        HandlerMethod handlerMethod = new HandlerMethod(new Controller1(), Controller1.class.getMethod("foo"));
        Exception e = new ArithmeticException("被零除");
        resolver.resolveException(request, response, handlerMethod, e);
        System.out.println(new String(response.getContentAsByteArray(), StandardCharsets.UTF_8));

        // 2.测试 mav
        HandlerMethod handlerMethod2 = new HandlerMethod(new Controller2(), Controller2.class.getMethod("foo"));
        Exception e2 = new ArithmeticException("被零除");
        ModelAndView mav = resolver.resolveException(request, response, handlerMethod2, e2);
        System.out.println(mav.getViewName() + " " + mav.getModel());

        // 3.测试嵌套异常
        HandlerMethod handlerMethod3 = new HandlerMethod(new Controller3(), Controller3.class.getMethod("foo"));
        Exception e3 = new Exception("e1", new RuntimeException("e2", new IOException("e3")));
        resolver.resolveException(request, response, handlerMethod3, e3);
        System.out.println(new String(response.getContentAsByteArray(), StandardCharsets.UTF_8));

        // 4.测试异常处理方法参数解析
        HandlerMethod handlerMethod4 = new HandlerMethod(new Controller4(), Controller4.class.getMethod("foo"));
        Exception e4 = new Exception("e1");
        resolver.resolveException(request, response, handlerMethod4, e4);
        System.out.println(new String(response.getContentAsByteArray(), StandardCharsets.UTF_8));
    }

}
