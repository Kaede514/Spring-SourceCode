package com.kaede.a01;

import org.springframework.boot.web.servlet.context.AnnotationConfigServletWebServerApplicationContext;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.HandlerMethodReturnValueHandler;
import org.springframework.web.servlet.HandlerExecutionChain;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import java.util.List;
import java.util.Map;

public class MainApplication {

    public static void main(String[] args) throws Exception {
        // 支持内嵌tomcat容器的Spring容器的实现
        AnnotationConfigServletWebServerApplicationContext context =
            new AnnotationConfigServletWebServerApplicationContext(WebConfig.class);
        // 解析@RequestMapping及派生注解，生成路径和控制器方法间的映射关系，在初始化时生成
        RequestMappingHandlerMapping handlerMapping = context.getBean(RequestMappingHandlerMapping.class);
        // 获取映射结果
        Map<RequestMappingInfo, HandlerMethod> handlerMethods = handlerMapping.getHandlerMethods();
        handlerMethods.forEach((k, v) -> {
            System.out.println(k + " = " + v);
        });
        // 假设请求来了，获取控制器方法，传入模拟HttpServletRequest进行测试
        // 返回处理器执行链对象，包含控制器方法和拦截器
        HandlerExecutionChain chain = handlerMapping.getHandler(new MockHttpServletRequest("PUT", "/test4"));
        System.out.println(chain);

        MyRequestMappingHandlerAdapter handlerAdapter = context.getBean(MyRequestMappingHandlerAdapter.class);
        MockHttpServletRequest request = new MockHttpServletRequest("PUT", "/test4");
        // request.addHeader("token", "abc123");
        MockHttpServletResponse response = new MockHttpServletResponse();
        handlerAdapter.invokeHandlerMethod(request, response, (HandlerMethod) chain.getHandler());

        // 检查响应
        System.out.println(response.getContentAsString());

        System.out.println(">>>>>>>>>>>>>>>>>>>>> 参数解析器");
        // 获取参数解析器
        for (HandlerMethodArgumentResolver resolver : handlerAdapter.getArgumentResolvers()) {
            System.out.println(resolver);
        }
        // 获取返回值处理器，返回值最终会被转为ModelAndView
        System.out.println(">>>>>>>>>>>>>>>>>>>>> 返回值处理器");
        for (HandlerMethodReturnValueHandler handler : handlerAdapter.getReturnValueHandlers()) {
            System.out.println(handler);
        }
    }

}
