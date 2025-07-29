package com.example;

import com.example.bytecodes.MybatisSqlDetailText;
import java.util.TimeZone;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @author suyh
 * @since 2025-05-21
 */
@SpringBootApplication
public class SimpleExampleApplication {

    private static final String runtimeTimeZone = "Asia/Kolkata";

    public static void main(String[] args) {
        setDefaultTimeZone();
        MybatisSqlDetailText.rebuildSqlDetail();
        SpringApplication.run(SimpleExampleApplication.class, args);
    }


    private static void setDefaultTimeZone() {
        System.setProperty("user.timezone", runtimeTimeZone);
        TimeZone.setDefault(TimeZone.getTimeZone(runtimeTimeZone));
        System.out.println("####====> 设置程序运行时区: " + TimeZone.getDefault().getID());
    }
}
