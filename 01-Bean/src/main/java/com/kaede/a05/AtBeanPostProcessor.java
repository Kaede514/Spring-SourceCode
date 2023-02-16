package com.kaede.a05;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.AbstractBeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.type.MethodMetadata;
import org.springframework.core.type.classreading.CachingMetadataReaderFactory;
import org.springframework.core.type.classreading.MetadataReader;

import java.io.IOException;
import java.util.Map;
import java.util.Set;

/**
 * @author kaede
 * @create 2023-02-12
 */

public class AtBeanPostProcessor implements BeanFactoryPostProcessor {

    @Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory configurableListableBeanFactory) throws BeansException {
        try {
            // 获取类的元信息
            CachingMetadataReaderFactory factory = new CachingMetadataReaderFactory();
            // 这种方式不走类加载，效率比反射高
            MetadataReader reader = factory.getMetadataReader(new ClassPathResource("com/kaede/a05/Config.class"));
            // 获取被@Bean注解标注的方法
            Set<MethodMetadata> methods = reader.getAnnotationMetadata().getAnnotatedMethods(Bean.class.getName());
            for (MethodMetadata method : methods) {
                // System.out.println(method.getMethodName());
                // 获取注解中的属性
                Map<String, Object> attributes = method.getAnnotationAttributes(Bean.class.getName());
                String initMethod = attributes.get("initMethod").toString();
                BeanDefinitionBuilder builder = BeanDefinitionBuilder.genericBeanDefinition();
                // 传入工厂方法的方法名和工厂对象对应的名字
                builder.setFactoryMethodOnBean(method.getMethodName(), "config");
                // 指定自动装配模式，默认是AUTOWIRE_NO，不自动装配，遇到工厂方法参数直接跳过
                // 对于构造方法参数和工厂方法的参数如果想自动装配，选择AUTOWIRE_CONSTRUCTOR
                builder.setAutowireMode(AbstractBeanDefinition.AUTOWIRE_CONSTRUCTOR);
                if (initMethod.length() > 0) {
                    // 若配置了初始化方法，则将方法名传入
                    // builder.setInitMethodName(initMethod);
                }
                AbstractBeanDefinition bd = builder.getBeanDefinition();
                // 将bean注册至BeanFactory
                DefaultListableBeanFactory beanFactory = (DefaultListableBeanFactory) configurableListableBeanFactory;
                beanFactory.registerBeanDefinition(method.getMethodName(), bd);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
