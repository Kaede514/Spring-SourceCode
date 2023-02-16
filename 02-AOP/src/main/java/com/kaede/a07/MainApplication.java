package com.kaede.a07;

import org.aopalliance.intercept.MethodInterceptor;
import org.springframework.aop.aspectj.AspectJExpressionPointcut;
import org.springframework.aop.framework.ProxyFactory;
import org.springframework.aop.support.DefaultPointcutAdvisor;

public class MainApplication {

    interface I1 {
        void foo();
    }

    static class Target1 implements I1 {
        public void foo() {
            System.out.println("target1 foo");
        }
    }

    public static void main(String[] args) {
        /**
         * 两个切面概念
         * aspect = 包含多个通知和切点
         *   通知1(advice) + 切点1(pointcut)
         *   通知2(advice) + 切点2(pointcut)
         *   通知3(advice) + 切点3(pointcut)
         * advisor = 更细粒度的切面，包含一个通知和切点
         * aspect最终在生效执行之前会被拆解为多个advisor
         */
        // 1.备好切点
        AspectJExpressionPointcut pointcut = new AspectJExpressionPointcut();
        pointcut.setExpression("execution(* foo())");
        // 2.备好通知
        MethodInterceptor advice = invocation -> {
            System.out.println("before...");
            // 调用目标
            Object result = invocation.proceed();
            System.out.println("after...");
            return result;
        };
        // 3.备好切面
        DefaultPointcutAdvisor advisor = new DefaultPointcutAdvisor(pointcut, advice);

        /**
         * 4.创建代理
         *  1. proxyTargetClass = false，目标实现了接口，用jdk实现
         *  2. proxyTargetClass = false，目标没有实现接口，用cglib实现
         *  3. proxyTargetClass = true，总是使用cglib实现
         *  proxyTargetClass默认为false
         */
        Target1 target1 = new Target1();
        ProxyFactory factory = new ProxyFactory();
        factory.setTarget(target1);
        // 添加切面
        factory.addAdvisor(advisor);
        // 创建代理对象
        I1 proxy = (I1) factory.getProxy();
        System.out.println(proxy.getClass());
        proxy.foo();

        // 设置接口类型
        factory.setInterfaces(target1.getClass().getInterfaces());
        I1 proxy1 = (I1) factory.getProxy();
        System.out.println(proxy1.getClass());
        proxy1.foo();

        // 设置proxyTargetClass为true
        factory.setProxyTargetClass(true);
        I1 proxy2 = (I1) factory.getProxy();
        System.out.println(proxy2.getClass());
        proxy2.foo();
    }

}
