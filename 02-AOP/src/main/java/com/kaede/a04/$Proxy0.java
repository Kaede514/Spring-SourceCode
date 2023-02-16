package com.kaede.a04;

import com.kaede.a04.MainApplication.Foo;
import com.kaede.a04.MainApplication.Target;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.lang.reflect.UndeclaredThrowableException;

/**
 * @author kaede
 * @create 2023-02-13
 */

public class $Proxy0 extends Proxy implements Foo {

    /* private InvocationHandler h;

    public $Proxy0(InvocationHandler handler) {
        this.h = handler;
    } */

    public $Proxy0(InvocationHandler h) {
        super(h);
    }

    @Override
    public void foo() {
        try {
            // Method foo = Foo.class.getMethod("foo");
            // 传入方法对象和参数
            h.invoke(this, foo, new Object[0]);
        } catch (RuntimeException | Error e) {
            throw e;
        } catch (Throwable e) {
            throw new UndeclaredThrowableException(e);
        }
    }

    @Override
    public int bar() {
        try {
            // Method bar = Foo.class.getMethod("bar");
            // 传入方法对象和参数
            Object result = h.invoke(this, bar, new Object[0]);
            return (int) result;
        } catch (RuntimeException | Error e) {
            throw e;
        } catch (Throwable e) {
            throw new UndeclaredThrowableException(e);
        }
    }

    // 方法对象只需获取一次，所以放在静态代码块中赋值
    static Method foo;
    static Method bar;
    static {
        try {
            foo = Foo.class.getMethod("foo");
            bar = Foo.class.getMethod("bar");
        } catch (NoSuchMethodException e) {
            throw new NoSuchMethodError(e.getMessage());
        }
    }

}
