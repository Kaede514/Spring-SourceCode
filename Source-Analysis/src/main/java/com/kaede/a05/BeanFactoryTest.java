package com.kaede.a05;

import com.kaede.a01.Dog;
import org.junit.jupiter.api.Test;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;
import org.springframework.beans.MutablePropertyValues;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.beans.factory.support.RootBeanDefinition;
import org.springframework.beans.factory.support.SimpleInstantiationStrategy;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.support.GenericXmlApplicationContext;

/**
 * @author kaede
 * @create 2023-02-17
 */

public class BeanFactoryTest {

    @Test
    public void testHierarchicalBeanFactory() {
        // 创建一个核心的工厂
        DefaultListableBeanFactory parent = new DefaultListableBeanFactory();
        parent.registerSingleton("user", new User("kaede", 20));
        // 创建一个子工厂，独自管理各个层级的内容
        DefaultListableBeanFactory child = new DefaultListableBeanFactory();
        // 设置父子关联关系
        child.setParentBeanFactory(parent);
        // 子工厂可以访问父工厂的bean
        System.out.println(child.containsLocalBean("user"));
        System.out.println(child.containsBean("user"));
        System.out.println(child.getBean("user"));
    }

    @Test
    public void testInstantiation() throws ClassNotFoundException {
        // 编写bean的定义
        RootBeanDefinition beanDefinition = new RootBeanDefinition();
        beanDefinition.setBeanClassName("com.kaede.a01.User");

        MutablePropertyValues propertyValues = new MutablePropertyValues();
        propertyValues.addPropertyValue("name", "kaede");
        propertyValues.addPropertyValue("age", 20);
        beanDefinition.setPropertyValues(propertyValues);
        beanDefinition.resolveBeanClass(Thread.currentThread().getContextClassLoader());

        // 模仿一个beanDefinition如何变成一个bean
        DefaultListableBeanFactory beanFactory = new DefaultListableBeanFactory();
        beanFactory.registerBeanDefinition("user", beanDefinition);
        // 实例化
        SimpleInstantiationStrategy instantiationStrategy = new SimpleInstantiationStrategy();
        Object user = instantiationStrategy.instantiate(beanDefinition, "user", beanFactory);
        // 属性填充
        BeanWrapper wrapper = new BeanWrapperImpl(user);
        wrapper.setPropertyValues(beanDefinition.getPropertyValues());
        System.out.println("The user is [{" + user + "}]");
    }

    @Test
    public void testConfig() throws InterruptedException {
        MyGenericApplicationContext myGenericApplicationContext = new MyGenericApplicationContext();
        myGenericApplicationContext.registerShutdownHook();
        System.out.println("before");
        Thread.sleep(2000);
        System.out.println("after");
    }

    @Test
    public void testXmlConfig(){
        GenericXmlApplicationContext xmlApplicationContext = new GenericXmlApplicationContext("classpath:spring.xml");
        Dog dog = (Dog) xmlApplicationContext.getBean("dog");
        System.out.println(dog);
    }

}
