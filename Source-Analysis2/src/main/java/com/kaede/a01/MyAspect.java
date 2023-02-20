package com.kaede.a01;

import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class MyAspect {
    @Before("execution(* com.kaede.a01.MyService.service())")
    public void before() {
        System.out.println("before service...");
    }
}