package com.kaede.a01.aop;

import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;

// 注意此切面并未被Spring管理
@Aspect
public class MyAspect {

    @Before("execution(* com.kaede.a01.service.MyService.foo())")
    public void before() {
        System.out.println("before()");
    }

}
