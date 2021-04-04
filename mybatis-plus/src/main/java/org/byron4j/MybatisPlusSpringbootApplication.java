package org.byron4j;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@MapperScan("org.byron4j.mapper")
@SpringBootApplication
public class MybatisPlusSpringbootApplication {
    public static void main(String[] args) {
        SpringApplication.run(MybatisPlusSpringbootApplication.class);
    }
}
