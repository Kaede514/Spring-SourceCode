package com.kaede.a02;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.TypedStringValue;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.SimpleBeanDefinitionRegistry;
import org.springframework.beans.factory.xml.XmlBeanDefinitionReader;
import org.springframework.core.ResolvableType;
import org.springframework.core.convert.ConversionService;
import org.springframework.core.convert.converter.Converter;
import org.springframework.core.convert.converter.ConverterFactory;
import org.springframework.core.convert.support.DefaultConversionService;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.ParameterizedType;
import java.util.Arrays;
import java.util.List;

/**
 * @author kaede
 * @create 2023-02-17
 */

@Slf4j
public class ConversionTest {

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
            // 4.需要自定义转化服务
            DefaultConversionService conversionService = new DefaultConversionService();
            // conversionService.addConverter(new TypedStringValueToIntegerConverter());
            // conversionService.addConverter(new TypedStringValueToIntegerConverter2());
            conversionService.addConverterFactory(new ConverterFactory<TypedStringValue, Integer>() {
                @Override
                public <T extends Integer> Converter<TypedStringValue, T> getConverter(Class<T> targetType) {
                    return new Converter<TypedStringValue, T>() {
                        @Override
                        public T convert(TypedStringValue source) {
                            try {
                                return targetType.getConstructor(String.class).newInstance(source.getValue());
                            } catch (Exception e) {
                                throw new RuntimeException(e);
                            }
                        }
                    };
                }
            });
            beanWrapper.setConversionService(conversionService);
            beanWrapper.setPropertyValues(beanDefinition.getPropertyValues());
            Object bean = beanWrapper.getWrappedInstance();
            System.out.println(bean);
        }
    }

    @Test
    public void testConvertInteger() {
        String source = "100";
        ConversionService conversionService = new DefaultConversionService();
        if (conversionService.canConvert(String.class, Integer.class)) {
            Integer target = conversionService.convert(source, Integer.class);
            log.info("The number is {}.", target);
        }
    }

    @Test
    public void testConvertList() {
        String source = "100, 12, 23, 54, 56";
        ConversionService conversionService = new DefaultConversionService();
        if (conversionService.canConvert(String.class, List.class)) {
            List target = conversionService.convert(source, List.class);
            log.info("The array is {}.", target);
        }
    }

    @Test
    public void testGeneric(){
        ParameterizedType genericInterfaces = (ParameterizedType)
            (TypedStringValueToIntegerConverter2.class.getGenericInterfaces()[0]);
        Arrays.stream(genericInterfaces.getActualTypeArguments()).forEach(System.out::println);
    }

    @Test
    public void testGeneri2(){
        ResolvableType c = ResolvableType.forClass(TypedStringValueToIntegerConverter2.class);
        ResolvableType[] generics = c.getInterfaces()[0].getGenerics();
        Arrays.stream(generics).forEach(System.out::println);
    }

}
