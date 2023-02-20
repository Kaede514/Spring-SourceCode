package com.kaede.a02;

import org.springframework.boot.web.embedded.tomcat.TomcatServletWebServerFactory;
import org.springframework.boot.web.server.WebServer;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.boot.web.servlet.ServletRegistrationBean;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;

/**
 * @author kaede
 * @create 2023-02-20
 */

public class MyServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.getWriter().print("hello tomcat!");
    }

    public static void main(String[] args) {
        TomcatServletWebServerFactory webServerFactory = new TomcatServletWebServerFactory("/test", 8080);

        ServletRegistrationBean<MyServlet> registrationBean = new ServletRegistrationBean<>();
        registrationBean.setServlet(new MyServlet());
        registrationBean.setUrlMappings(Arrays.asList("/hello"));

        FilterRegistrationBean<MyFilter> registrationBean1 = new FilterRegistrationBean<>();
        registrationBean1.setFilter(new MyFilter());
        registrationBean1.setUrlPatterns(Arrays.asList("/*"));

        WebServer webServer = webServerFactory.getWebServer(registrationBean, registrationBean1);
        webServer.start();
    }

}
