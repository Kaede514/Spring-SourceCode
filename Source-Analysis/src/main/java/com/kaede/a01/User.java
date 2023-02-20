package com.kaede.a01;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

/**
 * @author kaede
 * @create 2023-02-16
 */

@Data
@NoArgsConstructor
@AllArgsConstructor
@Component
public class User {
    private String name;
    private Integer age;
}
