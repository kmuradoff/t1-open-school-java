package org.kmuradoff.openschooljava;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

@SpringBootApplication
@EnableAspectJAutoProxy
public class OpenSchoolJavaTask1Application {

    public static void main(String[] args) {
        SpringApplication.run(OpenSchoolJavaTask1Application.class, args);
    }

}
