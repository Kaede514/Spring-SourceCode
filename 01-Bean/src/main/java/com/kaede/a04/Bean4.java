package com.kaede.a04;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "java")
@Data
public class Bean4 {
    private String home;
    private String version;
}
