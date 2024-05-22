package com.myProject.myProject;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpHeaders;
import org.springframework.web.client.RestTemplate;

@SpringBootApplication
public class MyProjectApplication {

    //for Api
    @Bean
    public RestTemplate template(){
        return new RestTemplate();
    };


    public static void main(String[] args) {
        SpringApplication.run(MyProjectApplication.class, args);
    }

}
