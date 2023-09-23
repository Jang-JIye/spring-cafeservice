package com.sparta.springcafeservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@SpringBootApplication
@EnableAspectJAutoProxy
public class SpringCafeserviceApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringCafeserviceApplication.class, args);
    }

}
