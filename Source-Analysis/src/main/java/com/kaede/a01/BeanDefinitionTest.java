package com.kaede.a01;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.BeanMetadataAttribute;
import org.springframework.beans.MutablePropertyValues;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.*;
import org.springframework.beans.factory.xml.XmlBeanDefinitionReader;
import org.springframework.context.annotation.AnnotatedBeanDefinitionReader;
import org.springframework.context.annotation.ClassPathBeanDefinitionScanner;

import java.util.Arrays;

/**
 * @author kaede
 * @create 2023-02-16
 */

@Slf4j
public class BeanDefinitionTest {

    @Test
    public void testGenericBeanDefinition() {
        GenericBeanDefinition beanDefinition = new GenericBeanDefinition();

        beanDefinition.setBeanClassName("com.kaede.a01.User");
        beanDefinition.setScope(BeanDefinition.SCOPE_PROTOTYPE);
        beanDefinition.setInitMethodName("init");
        // 此处类似setter注入的描述
        MutablePropertyValues propertyValues = new MutablePropertyValues();
        propertyValues.addPropertyValue("name", "kaede");
        propertyValues.addPropertyValue("age", 20);
        beanDefinition.setPropertyValues(propertyValues);

        System.out.println(beanDefinition);
    }

    @Test
    public void testRootBeanDefinition() {
        RootBeanDefinition dog = new RootBeanDefinition();
        dog.setBeanClassName("com.kaede.a01.Dog");
        BeanMetadataAttribute color = new BeanMetadataAttribute("color", "white");
        BeanMetadataAttribute age = new BeanMetadataAttribute("age", "3");
        dog.addMetadataAttribute(color);
        dog.addMetadataAttribute(age);

        // 子Definition的创建需要依赖父Definition
        ChildBeanDefinition teddy = new ChildBeanDefinition("dog");
        teddy.setBeanClassName("com.kaede.a01.TeddyDog");
        BeanMetadataAttribute name = new BeanMetadataAttribute("name", "doudou");
        teddy.addMetadataAttribute(name);

        System.out.println(dog);
        System.out.println(teddy);
    }

    @Test
    public void testRegistry() {
        // 定义一个注册器，用来注册和管理BeanDefinition
        BeanDefinitionRegistry registry = new SimpleBeanDefinitionRegistry();

        GenericBeanDefinition beanDefinition = new GenericBeanDefinition();
        beanDefinition.setBeanClassName("com.kaede.a01.User");
        MutablePropertyValues propertyValues = new MutablePropertyValues();
        propertyValues.addPropertyValue("name", "kaede");
        propertyValues.addPropertyValue("age", 20);
        beanDefinition.setPropertyValues(propertyValues);

        // 进行注册
        registry.registerBeanDefinition("user", beanDefinition);
        for (String name : registry.getBeanDefinitionNames()) {
            System.out.println(name);
        }
        System.out.println(registry.getBeanDefinitionCount());
    }

    @Test
    public void testRegistryByXml() {
        // 定义一个注册器，用来注册和管理BeanDefinition
        BeanDefinitionRegistry registry = new SimpleBeanDefinitionRegistry();

        // 通过xml文件加载
        XmlBeanDefinitionReader xmlReader = new XmlBeanDefinitionReader(registry);
        xmlReader.loadBeanDefinitions("classpath:spring.xml");

        System.out.println((Arrays.toString(registry.getBeanDefinitionNames())));
    }

    @Test
    public void testRegistryByAnnotation() {
        // 定义一个注册器，用来注册和管理BeanDefinition
        BeanDefinitionRegistry registry = new SimpleBeanDefinitionRegistry();

        // 注册内置BeanPostProcessor和相关的BeanDefinition
        AnnotatedBeanDefinitionReader annoReader = new AnnotatedBeanDefinitionReader(registry);
        annoReader.register(User.class);

        for (String name : registry.getBeanDefinitionNames()) {
            System.out.println(name);
        }
    }

    @Test
    public void testRegistryByScanner() {
        BeanDefinitionRegistry registry = new SimpleBeanDefinitionRegistry();

        // 通过扫描包的方式
        ClassPathBeanDefinitionScanner scanner = new ClassPathBeanDefinitionScanner(registry);
        scanner.scan("com.kaede.a01");
        for (String name : registry.getBeanDefinitionNames()) {
            System.out.println(name);
        }
    }

    /* @Test
    public void testAsm() throws IOException {
        Resource resource = new ClassPathResource("com/kaede/a01/User.class");
        ClassReader classReader = new ClassReader(resource.getInputStream());
        log.info(classReader.getClassName());

        // 缺少visitor的reader能力有限，此处只做几个简单的实现
        // visitor实现相对复杂，没有必要去学习
        // classReader.accept(xxxVisitor);

        // 返回的对应的常量池中常量的偏移量+1
        // 0-3 cafebaba 4-7 主次版本号 8-9 第一个是10+1
        // 二进制可以使用bined插件查看
        log.info("The first item is {}.", classReader.getItem(1));
        log.info("The first item is {}.", classReader.getItem(2));
        // 00 3A 这是字节码文件看到的
        // 常量池的计数是 1-57 0表示不引用任何一个常量池项目
        log.info("The first item is {}.", classReader.getItemCount());

        // 通过javap -v .\User.class class文件访问标志
        // flags: (0x0021) ACC_PUBLIC, ACC_SUPER 十进制就是33
        // ACC_SUPER 0x00 20 是否允许使用invokespecial字节码指令的新语义．
        // ACC_PUBLIC 0x00 01 是否为Public类型
        log.info("classReader.getAccess() is {}", classReader.getAccess());
    } */

}
