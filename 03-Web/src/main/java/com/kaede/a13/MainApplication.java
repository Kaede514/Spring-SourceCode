package com.kaede.a13;

import org.springframework.boot.web.servlet.context.AnnotationConfigServletWebServerApplicationContext;

public class MainApplication {

    public static void main(String[] args) {
        AnnotationConfigServletWebServerApplicationContext context =
                new AnnotationConfigServletWebServerApplicationContext(WebConfig.class);
    }

}
