package com.kaede.a03;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

@Slf4j
@Component
public class LifeCycleBean {
    public LifeCycleBean() {
        log.info("构造");
    }

    @Autowired
    // @Value注解做值注入，注入的内容来自环境变量、配置文件等
    public void autowire(@Value("${JAVA_HOME}") String home) {
        log.info("依赖注入: {}", home);
    }

    @PostConstruct
    public void init() {
        log.info("初始化");
    }

    // Spring容器关闭时调用（默认在Bean是单例的情况下，其他Scope的Bean的销毁方法调用的时机不同）
    @PreDestroy
    public void destroy() {
        log.info("销毁");
    }
}
