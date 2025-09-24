package com.tsj;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.tsj.Mapper")
public class TsjProjectApplication {

    public static void main(String[] args) {
        SpringApplication.run(TsjProjectApplication.class, args);
    }

}
