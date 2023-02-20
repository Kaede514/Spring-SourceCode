package com.kaede.a04;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.junit.jupiter.api.Test;
import org.springframework.expression.Expression;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;

/**
 * @author kaede
 * @create 2023-02-17
 */

public class SpELTest {

    @Test
    public void test1() {
        ExpressionParser parser = new SpelExpressionParser();
        Expression exp = parser.parseExpression("'Hello World'");
        String message = (String) exp.getValue();
        System.out.println(message);
    }

    @Test
    public void test2() {
        ExpressionParser parser = new SpelExpressionParser();
        Expression exp = parser.parseExpression("'Hello World'.concat('!')");
        String message = (String) exp.getValue();
        System.out.println(message);
    }

    @Test
    public void test3() {
        ExpressionParser parser = new SpelExpressionParser();
        // invokes 'getBytes()'
        Expression exp = parser.parseExpression("'Hello World'.bytes");
        byte[] bytes = (byte[]) exp.getValue();
        System.out.println(new String(bytes));
    }

    @Test
    public void test4() {
        ExpressionParser parser = new SpelExpressionParser();
        // invokes 'getBytes().length'
        Expression exp = parser.parseExpression("'Hello World'.bytes.length");
        int length = (Integer) exp.getValue();
        System.out.println(length);
    }

    @Test
    public void test5() {
        ExpressionParser parser = new SpelExpressionParser();
        Expression exp = parser.parseExpression("new String('hello world').toUpperCase()");
        String message = exp.getValue(String.class);
        System.out.println(message);
    }

    @Data
    @AllArgsConstructor
    class User {
        private String name;
        private Integer age;
    }
    @Test
    public void test6() {
        User user = new User("kaede", 20);
        ExpressionParser parser = new SpelExpressionParser();
        Expression exp = parser.parseExpression("name");
        Expression exp2 = parser.parseExpression("age");
        System.out.println(exp.getValue(user, String.class));
        System.out.println(exp2.getValue(user, String.class));
    }

}
