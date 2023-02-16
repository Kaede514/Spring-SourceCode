package com.kaede.a02;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.core.DefaultParameterNameDiscoverer;
import org.springframework.core.MethodParameter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockPart;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.support.DefaultDataBinderFactory;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.method.annotation.ExpressionValueMethodArgumentResolver;
import org.springframework.web.method.annotation.RequestHeaderMethodArgumentResolver;
import org.springframework.web.method.annotation.RequestParamMethodArgumentResolver;
import org.springframework.web.method.support.HandlerMethodArgumentResolverComposite;
import org.springframework.web.method.support.ModelAndViewContainer;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.support.StandardServletMultipartResolver;
import org.springframework.web.servlet.HandlerMapping;
import org.springframework.web.servlet.mvc.method.annotation.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class MainApplication {

    static class Controller {
        public void test(
            @RequestParam("name1") String name1,
            String name2,
            @RequestParam("age") int age,
            // 可以在一些注解中使用${}获取非请求中的数据
            @RequestParam(name = "home", defaultValue = "${JAVA_HOME}") String home1,
            @RequestParam("file") MultipartFile file,
            @PathVariable("id") int id,
            @RequestHeader("Content-Type") String header,
            @CookieValue("token") String token,
            @Value("${JAVA_HOME}") String home2,
            HttpServletRequest request,     // request、response、session...
            @ModelAttribute("abc") User user1,
            User user2,
            @RequestBody User user3
        ) {}
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    static class User {
        private String name;
        private int age;
    }

    private static HttpServletRequest mockRequest() {
        // 用来测试的请求对象
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.setParameter("name1", "zhangsan");
        request.setParameter("name2", "lisi");
        request.addPart(new MockPart("file", "abc", "hello".getBytes(StandardCharsets.UTF_8)));
        Map<String, String> map = new AntPathMatcher().extractUriTemplateVariables("/test/{id}", "/test/123");
        request.setAttribute(HandlerMapping.URI_TEMPLATE_VARIABLES_ATTRIBUTE, map);
        request.setContentType("application/json");
        request.setCookies(new Cookie("token", "123456"));
        request.setParameter("name", "张三");
        request.setParameter("age", "18");

        String content = "{" +
            "\"name\":\"李四\"," +
            "\"age\": 20" +
            "}";
        request.setContent(content.getBytes(StandardCharsets.UTF_8));

        return new StandardServletMultipartResolver().resolveMultipart(request);
    }

    public static void main(String[] args) throws Exception {
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(WebConfig.class);
        DefaultListableBeanFactory beanFactory = context.getDefaultListableBeanFactory();
        // 准备测试Request
        HttpServletRequest request = mockRequest();

        // 1.控制器方法被封装为HandlerMethod
        HandlerMethod handlerMethod = new HandlerMethod(new Controller(), Controller.class.getMethod("test", String.class, String.class, int.class, String.class, MultipartFile.class, int.class, String.class, String.class, String.class, HttpServletRequest.class, User.class, User.class, User.class));

        // 2.准备对象绑定与类型转换
        ServletRequestDataBinderFactory factory = new ServletRequestDataBinderFactory(null, null);

        // 3.准备ModelAndViewContainer用来存储中间Model结果
        ModelAndViewContainer container = new ModelAndViewContainer();

        // 4.解析每个参数值
        for (MethodParameter param : handlerMethod.getMethodParameters()) {
            // 多个解析器的组合，组合模式
            HandlerMethodArgumentResolverComposite composite = new HandlerMethodArgumentResolverComposite();
            composite.addResolvers(
                new RequestParamMethodArgumentResolver(beanFactory, false),
                new PathVariableMethodArgumentResolver(),
                new RequestHeaderMethodArgumentResolver(beanFactory),
                new ServletCookieValueMethodArgumentResolver(beanFactory),
                new ExpressionValueMethodArgumentResolver(beanFactory),
                new ServletRequestMethodArgumentResolver(),
                new ServletModelAttributeMethodProcessor(false),
                new RequestResponseBodyMethodProcessor(Arrays.asList(new MappingJackson2HttpMessageConverter())),
                new ServletModelAttributeMethodProcessor(true),
                new RequestParamMethodArgumentResolver(beanFactory, true),
                new RequestParamMethodArgumentResolver(beanFactory, true)
            );
            String annotations = Arrays.stream(param.getParameterAnnotations())
                .map(a -> a.annotationType().getSimpleName())
                .collect(Collectors.joining());
            String str = annotations.length() > 0 ? " @"+annotations : "";
            param.initParameterNameDiscovery(new DefaultParameterNameDiscoverer());

            // 会逐一调用每个解析器的supportsParameter方法，直到找到支持此参数的解析器
            if (composite.supportsParameter(param)) {
                // 解析参数
                Object value = composite.resolveArgument(param, container, new ServletWebRequest(request), factory);
                System.out.println("[" + param.getParameterIndex() + "]" + str + " "
                    + param.getParameterType().getSimpleName() + " " + param.getParameterName()
                    + " = " + value);
            }
        }
    }

}
