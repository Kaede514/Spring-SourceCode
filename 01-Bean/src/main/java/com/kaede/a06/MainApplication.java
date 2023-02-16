package com.kaede.a06;

import org.springframework.beans.factory.annotation.AutowiredAnnotationBeanPostProcessor;
import org.springframework.context.annotation.CommonAnnotationBeanPostProcessor;
import org.springframework.context.annotation.ConfigurationClassPostProcessor;
import org.springframework.context.support.GenericApplicationContext;

public class MainApplication {

    public static void main(String[] args) {
        /**
         * Aware接口用于注入一些与容器相关信息，例如
         * - BeanNameAware：注入bean的名字
         * - BeanFactoryAware：注入BeanFactory容器
         * - ApplicationContextAware：注入ApplicationContext容器
         * - EmbeddedValueResolverAware：注入解析${}的解析器
         * InitializingBean接口用来做初始化
         */
        GenericApplicationContext context = new GenericApplicationContext();
        // context.registerBean(MyBean.class);
        context.registerBean(MyConfig1.class);
        context.registerBean(MyConfig2.class);
        context.registerBean(AutowiredAnnotationBeanPostProcessor.class);
        context.registerBean(CommonAnnotationBeanPostProcessor.class);
        context.registerBean(ConfigurationClassPostProcessor.class);

        /**
         * 某些情况下，扩展功能会失效，而内置功能不会失效
         * - @Autowired的解析需要用到bean后处理器，属于扩展功能
         * - 而Aware接口属于内置功能，不加任何扩展，Spring就能识别
         */

        // 容器初始化
        /**
         * 执行顺序：
         * 1.在容器中找到所有的BeanFactory后置处理器执行
         * 2.添加Bean的后置处理器
         * 3.初始化单例
         */
        context.refresh();
        // 关闭容器
        context.close();
    }

}
