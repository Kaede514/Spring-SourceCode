package com.kaede.a04.sub;

import org.springframework.core.GenericTypeResolver;
import org.springframework.core.ResolvableType;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

public class TestGenericType {
    public static void main(String[] args) {
        // 获取泛型参数
        // 1.Java API
        System.out.println(">>>>>>>>>>>>>>>>>>>>>>>");
        Type type = StudentDao.class.getGenericSuperclass();
        System.out.println(type);
        if (type instanceof ParameterizedType)
            System.out.println(((ParameterizedType) type).getActualTypeArguments()[0]);

        // 2.Spring API
        System.out.println(">>>>>>>>>>>>>>>>>>>>>>>");
        Class<?> s1 = GenericTypeResolver.resolveTypeArgument(StudentDao.class, BaseDao.class);
        System.out.println(s1);

        // 3.Spring API
        System.out.println(">>>>>>>>>>>>>>>>>>>>>>>");
        Class<?> s2 = ResolvableType.forClass(StudentDao.class).getSuperType().getGeneric().resolve();
        System.out.println(s2);
    }
}
