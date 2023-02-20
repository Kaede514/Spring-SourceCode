package com.kaede.a02;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.GenericApplicationContext;
import org.springframework.core.env.*;

import java.io.IOException;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

/**
 * @author kaede
 * @create 2023-02-17
 */

@Slf4j
public class EnvirommentTest {

    @Test
    public void testMoreEnvProperties() throws IOException {
        ApplicationContext ctx = new GenericApplicationContext();
        Environment env = ctx.getEnvironment();
        boolean containsMyProperty = env.containsProperty("JAVA_HOME");
        System.out.println("containsMyProperty = " + containsMyProperty);
        System.out.println(env.getProperty("JAVA_HOME"));
    }

    @Test
    public void testSystemProperties() {
        Properties properties = System.getProperties();
        Set<Map.Entry<Object, Object>> entries = properties.entrySet();
        for (Map.Entry<Object,Object> entry:entries){
            System.out.println(entry);
        }
    }

    @Test
    public void testSystemEnv() {
        Map<String, String> env = System.getenv();
        Set<Map.Entry<String, String>> entries = env.entrySet();
        for (Map.Entry<String,String> entry:entries){
            System.out.println(entry);
        }
    }

    public class MyPropertySource extends PropertySource<String> {
        public MyPropertySource(String name) {
            super(name);
        }
        @Override
        public String getProperty(String name) {
            // 这里可以是源自任何逻辑的键值对来源，如从properties文件中检索、数据库检索等
            return "hello";
        }
    }
    @Test
    public void testStandardEnvironment() {
        // 定义一个标准环境
        StandardEnvironment env = new StandardEnvironment();
        MutablePropertySources sources = env.getPropertySources();

        // 添加PropertySource
        sources.addFirst(new MyPropertySource("my-source"));
        System.out.println(env.getProperty("xxx-yyy"));

        Properties props = new Properties();
        props.put("age", "12");
        PropertiesPropertySource source = new PropertiesPropertySource("my-property" ,props);
        sources.addFirst(source);
        System.out.println(env.getProperty("age"));
    }

}
