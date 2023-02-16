package org.springframework.aop.framework.autoproxy;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.aop.Advisor;
import org.springframework.aop.aspectj.AspectJExpressionPointcut;
import org.springframework.aop.aspectj.annotation.AnnotationAwareAspectJAutoProxyCreator;
import org.springframework.aop.support.DefaultPointcutAdvisor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ConfigurationClassPostProcessor;
import org.springframework.context.support.GenericApplicationContext;
import org.springframework.core.annotation.Order;

import java.util.List;

/**
 * @author kaede
 * @create 2023-02-13
 */

public class a08 {

    static class Target1 {
        public void foo() {
            System.out.println("target1 foo");
        }
    }

    static class Target2 {
        public void bar() {
            System.out.println("target2 bar");
        }
    }

    // 高级切面类
    @Aspect
    @Order(1)
    static class Aspect1 {
        @Before("execution(* foo())")
        public void before() {
            System.out.println("Aspect1 before...");
        }
        @After("execution(* foo())")
        public void after() {
            System.out.println("Aspect1 after...");
        }
    }

    @Configuration
    static class Config {
        // 低级切面类
        @Bean
        public Advisor advisor3(MethodInterceptor advice3) {
            AspectJExpressionPointcut pt = new AspectJExpressionPointcut();
            pt.setExpression("execution(* foo())");
            // 传入切点和通知
            DefaultPointcutAdvisor advisor = new DefaultPointcutAdvisor(pt, advice3);
            advisor.setOrder(2);
            return advisor;
        }
        @Bean
        public MethodInterceptor advice3() {
            return new MethodInterceptor() {
                @Override
                public Object invoke(MethodInvocation methodInvocation) throws Throwable {
                    System.out.println("advice3 before...");
                    Object result = methodInvocation.proceed();
                    System.out.println("advice3 after...");
                    return result;
                }
            };
        }
    }

    public static void main(String[] args) {
        GenericApplicationContext context = new GenericApplicationContext();
        context.registerBean("aspect1" ,Aspect1.class);
        context.registerBean("config", Config.class);
        context.registerBean(ConfigurationClassPostProcessor.class);

        // 实现了BeanPostProcessor，创建 -> (*) 依赖注入 -> 初始化 (*)
        context.registerBean(AnnotationAwareAspectJAutoProxyCreator.class);
        // 因为findEligibleAdvisors和wrapIfNecessary都是protected
        // 故将该类的包设为org.springframework.aop.framework.autoproxy方便调用

        context.refresh();
        for (String name : context.getBeanDefinitionNames()) {
            System.out.println(name);
        }

        AnnotationAwareAspectJAutoProxyCreator creator = context.getBean(AnnotationAwareAspectJAutoProxyCreator.class);
        List<Advisor> advisors = creator.findEligibleAdvisors(Target1.class, "target1");
        System.out.println(">>>>>>>>>>>>>>>>>");
        for (Advisor advisor : advisors) {
            System.out.println(advisor);
        }
        List<Advisor> advisors2 = creator.findEligibleAdvisors(Target2.class, "target2");
        System.out.println(">>>>>>>>>>>>>>>>>");
        for (Advisor advisor : advisors2) {
            System.out.println(advisor);
        }

        // 打印结果如下：
        // Spring给将来所有代理都要加的切面：org.springframework.aop.interceptor.ExposeInvocationInterceptor.ADVISOR
        // 低级切面
        // 高级切面转换后的低级切面，一个通知对应一个低级切面

        System.out.println("<<<<<<<<<<<<<<<<<");
        Object o1 = creator.wrapIfNecessary(new Target1(), "target1", "target1");
        System.out.println(o1.getClass());
        Object o2 = creator.wrapIfNecessary(new Target2(), "target2", "target2");
        System.out.println(o2.getClass());

        ((Target1) o1).foo();
    }

}
