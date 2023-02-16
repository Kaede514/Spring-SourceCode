package com.kaede.a03;

import org.springframework.core.LocalVariableTableParameterNameDiscoverer;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.Arrays;

public class MainApplication {

    public static void main(String[] args) throws NoSuchMethodException, ClassNotFoundException {
        // 1.反射获取参数名
        Method foo = Bean1.class.getMethod("foo", String.class, int.class);
        for (Parameter parameter : foo.getParameters()) {
            System.out.println(parameter.getName());
        }
        Method foo2 = Bean2.class.getMethod("foo", String.class, int.class);
        for (Parameter parameter : foo2.getParameters()) {
            System.out.println(parameter.getName());
        }

        // 2.基于LocalVariableTable本地变量表
        LocalVariableTableParameterNameDiscoverer discoverer = new LocalVariableTableParameterNameDiscoverer();
        String[] parameterNames = discoverer.getParameterNames(foo);
        System.out.println(Arrays.toString(parameterNames));
        String[] parameterNames2 = discoverer.getParameterNames(foo2);
        System.out.println(Arrays.toString(parameterNames2));
    }

}
