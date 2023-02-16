package com.kaede.a05;

import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.AbstractBeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.context.annotation.AnnotationBeanNameGenerator;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.type.AnnotationMetadata;
import org.springframework.core.type.classreading.CachingMetadataReaderFactory;
import org.springframework.core.type.classreading.MetadataReader;
import org.springframework.stereotype.Component;


/**
 * @author kaede
 * @create 2023-02-12
 */

public class ComponentScanPostProcessor implements BeanFactoryPostProcessor {

    // context.refresh()时调用
    @Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory configurableListableBeanFactory) {
        try {
            // 获得该类上的@ComponentScan注解
            ComponentScan componentScan = AnnotationUtils.findAnnotation(Config.class, ComponentScan.class);
            if (componentScan != null) {
                for (String basePackage : componentScan.basePackages()) {
                    System.out.println(basePackage);
                    // com.kaede.a05.component -> classpath*:com/kaede/a05/component/**/*.class
                    String path = "classpath*:" + basePackage.replace(".", "/") + "/**/*.class";
                    System.out.println(path);
                    // 读取类的元信息
                    CachingMetadataReaderFactory factory = new CachingMetadataReaderFactory();
                    // Resource[] resources = context.getResources(path);
                    Resource[] resources = (Resource[]) new PathMatchingResourcePatternResolver().getResources(path);
                    // 根据注解生成bean的名字，循环中使用
                    AnnotationBeanNameGenerator generator = new AnnotationBeanNameGenerator();
                    for (Resource resource : resources) {
                        // System.out.println(resource);
                        MetadataReader reader = factory.getMetadataReader(resource);
                        // System.out.println("类名:" + reader.getClassMetadata().getClassName());
                        AnnotationMetadata annotationMetadata = reader.getAnnotationMetadata();
                        // 是否加了@Component注解
                        // System.out.println("@Component:" + annotationMetadata.hasAnnotation(Component.class.getName()));
                        // 是否加了@Component的派生注解
                        // System.out.println("@Component派生注解:" + annotationMetadata.hasMetaAnnotation(Component.class.getName()));
                        if (annotationMetadata.hasAnnotation(Component.class.getName()) ||
                            annotationMetadata.hasMetaAnnotation(Component.class.getName())) {
                            // 若直接或间接加了@Component注解，创建BeanDefination
                            AbstractBeanDefinition bd = BeanDefinitionBuilder
                                .genericBeanDefinition(reader.getClassMetadata().getClassName())
                                .getBeanDefinition();
                            // 生成bean的name
                            DefaultListableBeanFactory beanFactory = (DefaultListableBeanFactory) configurableListableBeanFactory;
                            String name = generator.generateBeanName(bd, beanFactory);
                            // 注册到BeanFactory
                            beanFactory.registerBeanDefinition(name, bd);
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
