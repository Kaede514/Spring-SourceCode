package com.kaede.a12;

import org.springframework.boot.web.servlet.context.AnnotationConfigServletWebServerApplicationContext;

public class MainApplication2 {

    public static void main(String[] args) {
        AnnotationConfigServletWebServerApplicationContext context
                = new AnnotationConfigServletWebServerApplicationContext(WebConfig2.class);
    }

}
