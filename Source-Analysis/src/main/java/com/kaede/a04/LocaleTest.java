package com.kaede.a04;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.context.support.ResourceBundleMessageSource;

import java.util.Locale;

/**
 * @author kaede
 * @create 2023-02-17
 */

@Slf4j
public class LocaleTest {

    @Test
    public void testMessageSource(){
        ResourceBundleMessageSource rbs = new ResourceBundleMessageSource();
        // 指定文件
        rbs.setBasename("i18n/message");
        rbs.setDefaultEncoding("UTF-8");
        rbs.setDefaultLocale(Locale.CHINA);
        System.out.println(rbs.getMessage("hello", null, Locale.CHINA));
        System.out.println(rbs.getMessage("hello", null, Locale.US));
        System.out.println(rbs.getMessage("hello", null, Locale.getDefault()));
    }

}
