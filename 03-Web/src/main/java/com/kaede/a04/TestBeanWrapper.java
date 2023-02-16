package com.kaede.a04;

import lombok.Data;
import org.springframework.beans.BeanWrapperImpl;
import org.springframework.beans.SimpleTypeConverter;
import org.springframework.core.GenericTypeResolver;
import org.springframework.format.Formatter;
import org.springframework.format.support.FormatterPropertyEditorAdapter;
import org.springframework.format.support.FormattingConversionService;

import java.util.Date;

public class TestBeanWrapper {
    public static void main(String[] args) {
        // 利用反射原理，为bean的属性赋值
        MyBean target = new MyBean();
        BeanWrapperImpl wrapper = new BeanWrapperImpl(target);
        wrapper.setPropertyValue("a", "10");
        wrapper.setPropertyValue("b", "hello");
        wrapper.setPropertyValue("c", "1999/03/04");
        System.out.println(target);
    }

    @Data
    static class MyBean {
        private int a;
        private String b;
        private Date c;
    }
}
