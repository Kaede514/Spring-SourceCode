package org.springframework.boot;

import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.SimpleCommandLinePropertySource;
import org.springframework.core.env.StandardEnvironment;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.support.ResourcePropertySource;

import java.io.IOException;

/**
 * @author kaede
 * @create 2023-02-15
 */

public class Step3 {

    public static void main(String[] args) throws IOException {
        // 对配置信息的抽象，配置信息的来源：系统环境变量、properties、yaml、命令行...
        // 默认来源只有系统属性和系统环境变量
        ConfigurableEnvironment env = new StandardEnvironment();
        // 添加配置文件来源，添加至末尾，优先级最低
        env.getPropertySources().addLast(
            new ResourcePropertySource(new ClassPathResource("application.properties")));
        System.out.println("server.port = " + env.getProperty("server.port"));
        // 添加命令行来源，添加至开头，优先级最高
        env.getPropertySources().addFirst(new SimpleCommandLinePropertySource(args));
        env.getPropertySources().forEach(ps -> {
            System.out.println(ps.getName());
        });
        System.out.println("JAVA_HOME = " + env.getProperty("JAVA_HOME"));
        System.out.println("server.port = " + env.getProperty("server.port"));
    }

}
