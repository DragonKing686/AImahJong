package com.github.esrrhs.majiang_algorithm;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

@SpringBootApplication
public class SpringbootApplicationDemo extends SpringBootServletInitializer {
    public static void main(String[] args) {
        SpringApplication.run(SpringbootApplicationDemo.class, args);
    }

    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
        return builder.sources(SpringbootApplicationDemo.class);
    }
}
