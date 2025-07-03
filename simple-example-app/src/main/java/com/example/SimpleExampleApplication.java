package com.example;

import com.example.bytecodes.MybatisSqlDetailText;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @author suyh
 * @since 2025-05-21
 */
@SpringBootApplication
public class SimpleExampleApplication {
    public static void main(String[] args) {
        MybatisSqlDetailText.rebuildSqlDetail();
        SpringApplication.run(SimpleExampleApplication.class, args);
    }
}
