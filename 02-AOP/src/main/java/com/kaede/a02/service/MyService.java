package com.kaede.a02.service;

import org.springframework.stereotype.Service;

@Service
public class MyService {

    public void foo() {
        System.out.println("foo()");
        bar();
    }

    public void bar() {
        System.out.println("bar()");
    }

}
