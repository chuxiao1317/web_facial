package com.chuxiao.web_facial;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;


@SpringBootApplication
@ServletComponentScan
public class WebFacialApplication {

    public static void main(String[] args) {
        SpringApplication.run(WebFacialApplication.class, args);
    }
}
