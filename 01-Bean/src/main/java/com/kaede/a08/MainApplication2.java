package com.kaede.a08;

import com.kaede.a08.sub.E;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.ComponentScan;

@ComponentScan("com.kaede.a08.sub")
@Slf4j
public class MainApplication2 {

    public static void main(String[] args) {
        AnnotationConfigApplicationContext context =
            new AnnotationConfigApplicationContext(MainApplication2.class);

        E e = context.getBean(E.class);
        log.info("{}", e.getF1().getClass());
        log.info("{}", e.getF1());
        log.info("{}", e.getF1());

        log.info("{}", e.getF2().getClass());
        log.info("{}", e.getF2());
        log.info("{}", e.getF2());

        log.info("{}", e.getF3());
        log.info("{}", e.getF3());

        log.info("{}", e.getF4());
        log.info("{}", e.getF4());

        context.close();
    }

}
