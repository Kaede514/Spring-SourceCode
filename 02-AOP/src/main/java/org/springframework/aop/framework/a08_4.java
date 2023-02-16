package org.springframework.aop.framework;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;

import java.lang.reflect.AccessibleObject;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;

public class a08_4 {

    static class Target {
        public void foo() {
            System.out.println("Target.foo()");
        }
    }

    static class Advice1 implements MethodInterceptor {
        @Override
        public Object invoke(MethodInvocation invocation) throws Throwable {
            System.out.println("Advice1.before()");
            // 调用下一个通知或目标
            Object result = invocation.proceed();
            System.out.println("Advice1.after()");
            return result;
        }
    }

    static class Advice2 implements MethodInterceptor {
        @Override
        public Object invoke(MethodInvocation invocation) throws Throwable {
            System.out.println("Advice2.before()");
            // 调用下一个通知或目标
            Object result = invocation.proceed();
            System.out.println("Advice2.after()");
            return result;
        }
    }

    static class MyInvocation implements MethodInvocation {
        private Object target;
        private Method method;
        private Object[] args;
        List<MethodInterceptor> methodInterceptorList;
        private int count = 1;  // 调用次数

        public MyInvocation(Object target, Method method, Object[] args, List<MethodInterceptor> methodInterceptorList) {
            this.target = target;
            this.method = method;
            this.args = args;
            this.methodInterceptorList = methodInterceptorList;
        }

        @Override
        public Method getMethod() {
            return method;
        }

        @Override
        public Object[] getArguments() {
            return args;
        }

        // 调用每一个环绕通知, 调用目标
        @Override
        public Object proceed() throws Throwable {
            if (count > methodInterceptorList.size()) {
                // 调用目标，返回并结束递归
                return method.invoke(target, args);
            }
            // 逐一调用通知，count + 1
            MethodInterceptor methodInterceptor = methodInterceptorList.get(count - 1);
            count++;
            return methodInterceptor.invoke(this);
        }

        @Override
        public Object getThis() {
            return target;
        }

        @Override
        public AccessibleObject getStaticPart() {
            return method;
        }
    }

    public static void main(String[] args) throws Throwable {
        Target target = new Target();
        List<MethodInterceptor> list = Arrays.asList(new Advice1(), new Advice2());
        MyInvocation invocation = new MyInvocation(target, Target.class.getMethod("foo"), new Object[0], list);
        invocation.proceed();
    }

}
