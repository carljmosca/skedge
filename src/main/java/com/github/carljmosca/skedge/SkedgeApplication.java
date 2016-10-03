package com.github.carljmosca.skedge;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.vaadin.spring.sidebar.annotation.EnableSideBar;

@SpringBootApplication
@EnableAutoConfiguration
@EnableSideBar
@ComponentScan
public class SkedgeApplication {

    public static void main(String[] args) {
        SpringApplication.run(SkedgeApplication.class, args);
    }
}
