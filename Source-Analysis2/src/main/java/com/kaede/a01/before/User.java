package com.kaede.a01.before;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.stereotype.Component;

/**
 * @author kaede
 * @create 2023-02-19
 */

@Component
@Data
@AllArgsConstructor
public class User {
    private String name;
}
