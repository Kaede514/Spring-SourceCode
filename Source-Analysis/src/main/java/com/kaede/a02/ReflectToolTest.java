package com.kaede.a02;

import cn.hutool.core.bean.BeanUtil;
import com.kaede.a01.User;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;
import org.springframework.beans.MutablePropertyValues;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.GenericBeanDefinition;
import org.springframework.beans.factory.support.SimpleBeanDefinitionRegistry;
import org.springframework.beans.factory.xml.XmlBeanDefinitionReader;
import org.springframework.core.ResolvableType;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;

/**
 * @author kaede
 * @create 2023-02-17
 */

@Slf4j
public class ReflectToolTest {

    @Test
    public void testIntrospect1() throws IntrospectionException {
        // 添加Object.class表示不继承Object中的属性，即只取自己的
        BeanInfo beanInfo = Introspector.getBeanInfo(User.class, Object.class);
        // 取得属性描述器
        PropertyDescriptor[] propertyDescriptors = beanInfo.getPropertyDescriptors();
        for (PropertyDescriptor propertyDescriptor : propertyDescriptors) {
            log.info("{}", propertyDescriptor.getPropertyType());
            log.info("{}", propertyDescriptor.getReadMethod());
            log.info("{}", propertyDescriptor.getWriteMethod());
        }
    }

    @Test
    public void testIntrospect2() throws Exception {
        User user = new User();
        PropertyDescriptor pd = new PropertyDescriptor("age", User.class);
        // 得到属性的写方法，为属性赋值
        Method method = pd.getWriteMethod();
        method.invoke(user, 24);
        // 获取属性的值
        method = pd.getReadMethod();
        System.out.println("age = " + method.invoke(user, null));
    }

    @Test
    public void testBeanUtil() throws Exception {
        User user = new User();
        // 赋值
        BeanUtil.setProperty(user, "name", "tom");
        BeanUtil.setProperty(user, "age", 10);
        log.info("user -> {}", user);
        // 获取值
        log.info("the user's name is -> {}", (String) BeanUtil.getProperty(user, "name"));
    }

    @Test
    public void testCreate() throws Exception {
        // 1.通过任意形式捕获beanDefinition
        GenericBeanDefinition beanDefinition = new GenericBeanDefinition();
        beanDefinition.setBeanClassName("com.kaede.a01.User");
        MutablePropertyValues propertyValues = new MutablePropertyValues();
        propertyValues.addPropertyValue("name", "lily");
        propertyValues.addPropertyValue("age", 12);
        beanDefinition.setPropertyValues(propertyValues);

        // 2.通过权限定名称获得Class
        Class<?> clazz = Class.forName(beanDefinition.getBeanClassName());

        // 3.使用BeanWrapper包裹实例，使其更方便地使用反射方法
        BeanWrapper beanWrapper = new BeanWrapperImpl(clazz);
        beanWrapper.setPropertyValues(beanDefinition.getPropertyValues());
        Object bean = beanWrapper.getWrappedInstance();
        log.info("The bean is [{}]", bean);
    }

    @Test
    public void testBatchCreate() throws Exception {
        // 1.通过任意形式捕获beanDefinition
        BeanDefinitionRegistry registry = new SimpleBeanDefinitionRegistry();
        XmlBeanDefinitionReader xmlReader = new XmlBeanDefinitionReader(registry);
        xmlReader.loadBeanDefinitions("classpath:spring.xml");

        // 2.通过反射实例化
        String[] definitionNames = registry.getBeanDefinitionNames();
        for (String definitionName : definitionNames) {
            BeanDefinition beanDefinition = registry.getBeanDefinition(definitionName);
            Class<?> clazz = Class.forName(beanDefinition.getBeanClassName());

            // 3.使用BeanWrapper包裹实例，使其更方便使用反射方法
            BeanWrapper beanWrapper = new BeanWrapperImpl(clazz);
            beanWrapper.setPropertyValues(beanDefinition.getPropertyValues());
            Object bean = beanWrapper.getWrappedInstance();
            System.out.println(bean);
        }
    }

    private HashMap<Integer, List<String>> myMap;
    @Test
    public void example() throws NoSuchFieldException {
        ResolvableType t = ResolvableType.forField(getClass().getDeclaredField("myMap"));
        // AbstractMap<Integer, List<String>>
        System.out.println(t.getSuperType());
        // Map<Integer, List<String>>
        System.out.println(t.asMap());
        // 获取泛型
        System.out.println(t.getGeneric(0));
        System.out.println(t.getGeneric(0).resolve());  // Integer
        System.out.println(t.getGeneric(1));        // List<String>
        System.out.println(t.getGeneric(1).resolve());  // List
        // 第二个泛型里面的泛型，即List<String>里的String
        System.out.println(t.resolveGeneric(1, 0));
    }

}
