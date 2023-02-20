package com.kaede.a02;

import cn.hutool.core.io.FastByteArrayOutputStream;
import cn.hutool.core.io.IoUtil;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * @author kaede
 * @create 2023-02-17
 */

@Slf4j
public class ResourceTest {

    @Test
    public void testUrl() throws IOException {
        Resource resource = new UrlResource("https://pic1.vqs.com/2017/0714/20170714065240785.jpg");
        FileOutputStream fos = new FileOutputStream("E://" + resource.getFilename());
        IoUtil.copy(resource.getInputStream(), fos);
    }

    @Test
    public void testFileSystem() throws IOException {
        Resource resource = new FileSystemResource("D:/Workspace/IDEA/Spring-SourceCode/Source-Analysis/src/main/resources/spring.xml");
        InputStream inputStream = resource.getInputStream();
        IoUtil.copy(inputStream, new FileOutputStream("E:/spring.xml"));
    }

    @Test
    public void testClassPath() throws IOException {
        Resource resource = new ClassPathResource("spring.xml");
        InputStream is = resource.getInputStream();
        byte[] buf = new byte[1024];
        int len = 0;
        while ((len = is.read(buf)) > -1) {
            System.out.println(new String(buf, 0, len));
        }
    }

    @Test
    public void testPathMatchingResourcePatternResolver() throws IOException {
        PathMatchingResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
        Resource resource = resolver.getResource("classpath:spring.xml");
        System.out.println(resource.getURI());
        Resource resource1 = resolver.getResource("file:/E:/spring.xml");
        System.out.println(resource1.getURI());
        Resource[] resources = resolver.getResources("classpath*:META-INF/spring.factories");
        for (Resource resource2 : resources) {
            System.out.println(resource2.getURI());
        }
    }

}
