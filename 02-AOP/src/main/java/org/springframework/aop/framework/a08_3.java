package org.springframework.aop.framework;

import org.aopalliance.intercept.MethodInvocation;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.springframework.aop.Advisor;
import org.springframework.aop.aspectj.*;
import org.springframework.aop.interceptor.ExposeInvocationInterceptor;
import org.springframework.aop.support.DefaultPointcutAdvisor;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

public class a08_3 {

    static class Aspect {
        @Before("execution(* foo())")
        public void before() {
            System.out.println("before");
        }

        @After("execution(* foo())")
        public void after() {
            System.out.println("after");
        }

        @AfterReturning("execution(* foo())")
        public void afterReturning() {
            System.out.println("afterReturning");
        }

        @AfterThrowing("execution(* foo())")
        public void afterThrowing() {
            System.out.println("afterThrowing");
        }

        @Around("execution(* foo())")
        public Object around(ProceedingJoinPoint pjp) throws Throwable {
            try {
                System.out.println("around...before");
                return pjp.proceed();
            } finally {
                System.out.println("around...after");
            }
        }
    }

    static class Target {
        public void foo() {
            System.out.println("target foo");
        }
    }

    public static void main(String[] args) throws Throwable {
        AspectInstanceFactory factory = new SingletonAspectInstanceFactory(new Aspect());
        // 高级切面转低级切面类
        List<Advisor> list = new ArrayList<>();
        for (Method method : Aspect.class.getDeclaredMethods()) {
            if (method.isAnnotationPresent(Before.class)) {
                // 解析切点
                String expression = method.getAnnotation(Before.class).value();
                AspectJExpressionPointcut pointcut = new AspectJExpressionPointcut();
                pointcut.setExpression(expression);
                // 通知类
                AspectJMethodBeforeAdvice advice = new AspectJMethodBeforeAdvice(method, pointcut, factory);
                // 切面
                Advisor advisor = new DefaultPointcutAdvisor(pointcut, advice);
                list.add(advisor);
            } else if (method.isAnnotationPresent(AfterReturning.class)) {
                // 解析切点
                String expression = method.getAnnotation(AfterReturning.class).value();
                AspectJExpressionPointcut pointcut = new AspectJExpressionPointcut();
                pointcut.setExpression(expression);
                // 通知类
                AspectJAfterReturningAdvice advice = new AspectJAfterReturningAdvice(method, pointcut, factory);
                // 切面
                Advisor advisor = new DefaultPointcutAdvisor(pointcut, advice);
                list.add(advisor);
            } else if (method.isAnnotationPresent(Around.class)) {
                // 解析切点
                String expression = method.getAnnotation(Around.class).value();
                AspectJExpressionPointcut pointcut = new AspectJExpressionPointcut();
                pointcut.setExpression(expression);
                // 通知类
                AspectJAroundAdvice advice = new AspectJAroundAdvice(method, pointcut, factory);
                // 切面
                Advisor advisor = new DefaultPointcutAdvisor(pointcut, advice);
                list.add(advisor);
            }
        }
        for (Advisor advisor : list) {
            System.out.println(advisor);
        }

        // 2.通知统一转换为环绕通知MethodInterceptor
        /**
         * 其实无论ProxyFactory基于哪种方式创建代理，最后调用advice的是一个MethodInvocation对象
         *     a. 因为advisor有多个，且一个套一个调用，因此需要一个调用链对象，即MethodInvocation
         *     b. MethodInvocation要知道advice有哪些，还要知道目标，调用次序如下
         *     将 MethodInvocation 放入当前线程
         *         |-> before1 ----------------------------------- 从当前线程获取 MethodInvocation
         *         |                                             |
         *         |   |-> before2 --------------------          | 从当前线程获取 MethodInvocation
         *         |   |                              |          |
         *         |   |   |-> target ------ 目标   advice2    advice1
         *         |   |                              |          |
         *         |   |-> after2 ---------------------          |
         *         |                                             |
         *         |-> after1 ------------------------------------
         *     c. 从上图看出，环绕通知才适合作为advice，因此其他before、afterReturning都会被转换成环绕通知
         *         around、after、afterthrowing实现了MethodInvocation接口，已经是环绕通知了，无需转换
         *     d. 统一转换为环绕通知，体现的是设计模式中的适配器模式
         *         - 对外是为了方便使用要区分before、afterReturning
         *         - 对内统一都是环绕通知，统一用MethodInterceptor表示
         * 此步获取所有执行时需要的advice(静态)
         *     a. 即统一转换为MethodInterceptor环绕通知，这体现在方法名中的Interceptors上
         *     b. 适配如下
         *       - MethodBeforeAdviceAdapter将@Before AspectJMethodBeforeAdvice适配为MethodBeforeAdviceInterceptor
         *       - AfterReturningAdviceAdapter将@AfterReturning AspectJAfterReturningAdvice适配为AfterReturningAdviceInterceptor
         */
        ProxyFactory proxyFactory = new ProxyFactory();
        Target target = new Target();
        proxyFactory.setTarget(target);
        // 准备将MethodInvocation调用链暴露至当前线程
        proxyFactory.addAdvice(ExposeInvocationInterceptor.INSTANCE);
        proxyFactory.addAdvisors(list);

        System.out.println(">>>>>>>>>>>>>>>>>>>>>>");
        // 将before、afterReturning转换为环绕通知
        List<Object> methodInterceptorList = proxyFactory.getInterceptorsAndDynamicInterceptionAdvice(Target.class.getMethod("foo"), Target.class);
        for (Object o : methodInterceptorList) {
            System.out.println(o);
        }

        System.out.println(">>>>>>>>>>>>>>>>>>>>>>");
        // 3. 创建并执行调用链(环绕通知 + 目标)
        MethodInvocation methodInvocation = new ReflectiveMethodInvocation(
            null, target, Target.class.getMethod("foo"), new Object[0], Target.class, methodInterceptorList
        );
        methodInvocation.proceed();
    }

}
