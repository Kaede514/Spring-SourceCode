package com.kaede.a05;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.web.method.ControllerAdviceBean;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerAdapter;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
public class MainApplication {

    public static void main(String[] args) throws Exception {
        AnnotationConfigApplicationContext context =
                new AnnotationConfigApplicationContext(WebConfig.class);
        // 手动创建HandlerAdapter
        RequestMappingHandlerAdapter handlerAdapter = new RequestMappingHandlerAdapter();
        handlerAdapter.setApplicationContext(context);
        // 其中进行初始化全局InitBinder
        handlerAdapter.afterPropertiesSet();

        log.info("1. 刚开始...");
        showBindMethods(handlerAdapter);

        Method getDataBinderFactory = RequestMappingHandlerAdapter.class.getDeclaredMethod("getDataBinderFactory", HandlerMethod.class);
        getDataBinderFactory.setAccessible(true);

        log.info("2. 模拟调用 Controller1 的 foo 方法时 ...");
        getDataBinderFactory.invoke(handlerAdapter, new HandlerMethod(new WebConfig.Controller1(), WebConfig.Controller1.class.getMethod("foo")));
        showBindMethods(handlerAdapter);

        log.info("3. 模拟调用 Controller2 的 bar 方法时 ...");
        getDataBinderFactory.invoke(handlerAdapter, new HandlerMethod(new WebConfig.Controller2(), WebConfig.Controller2.class.getMethod("bar")));
        showBindMethods(handlerAdapter);

        context.close();
    }

    // 通过反射查看RequestMappingHandlerAdapter中initBinderCache和initBinderAdviceCache的内容
    @SuppressWarnings("all")
    private static void showBindMethods(RequestMappingHandlerAdapter handlerAdapter) throws NoSuchFieldException, IllegalAccessException {
        Field initBinderAdviceCache = RequestMappingHandlerAdapter.class.getDeclaredField("initBinderAdviceCache");
        initBinderAdviceCache.setAccessible(true);
        Map<ControllerAdviceBean, Set<Method>> globalMap = (Map<ControllerAdviceBean, Set<Method>>) initBinderAdviceCache.get(handlerAdapter);
        log.info("全局的 @InitBinder 方法 {}",
                globalMap.values().stream()
                        .flatMap(ms -> ms.stream().map(m -> m.getName()))
                        .collect(Collectors.toList())
        );

        Field initBinderCache = RequestMappingHandlerAdapter.class.getDeclaredField("initBinderCache");
        initBinderCache.setAccessible(true);
        Map<Class<?>, Set<Method>> controllerMap = (Map<Class<?>, Set<Method>>) initBinderCache.get(handlerAdapter);
        log.info("控制器的 @InitBinder 方法 {}",
                controllerMap.entrySet().stream()
                        .flatMap(e -> e.getValue().stream().map(v -> e.getKey().getSimpleName() + "." + v.getName()))
                        .collect(Collectors.toList())
        );
    }

}
