package org.springframework.boot;

import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.MapPropertySource;
import org.springframework.core.env.StandardEnvironment;
import org.springframework.core.io.DefaultResourceLoader;

import java.util.Collections;

public class Step7 {

    public static void main(String[] args) {
        ConfigurableEnvironment env = new StandardEnvironment();
        SpringApplicationBannerPrinter printer = new SpringApplicationBannerPrinter(
                new DefaultResourceLoader(),
                new SpringBootBanner()
        );
        // 默认banner实现
        printer.print(env, Step7.class, System.out);

        // 测试文字banner，指定banner位置、文件名
        env.getPropertySources().addLast(new MapPropertySource("custom",
            Collections.singletonMap("spring.banner.location", "banner1.txt")));
        printer.print(env, Step7.class, System.out);

        // 测试图片banner
        env.getPropertySources().addLast(new MapPropertySource("custom",
            Collections.singletonMap("spring.banner.image.location","banner2.jpeg")));
        printer.print(env, Step7.class, System.out);

        // 版本号的获取
        System.out.println(SpringBootVersion.getVersion());
    }

}
