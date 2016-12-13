package com.newsvisualizer.visualization.bootstrap;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.ImportResource;

/**
 * Created by rahulkhanna on 09/12/16.
 */
@SpringBootApplication
@ComponentScan(basePackages = "com.newsvisualizer.visualization.*")
@ImportResource({"classpath:*/application-beans.xml"})
public class Application {

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

}
