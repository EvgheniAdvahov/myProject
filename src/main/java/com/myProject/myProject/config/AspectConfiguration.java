package com.myProject.myProject.config;

import com.myProject.myProject.aspect.LoggingAspect;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;


@Configuration
@EnableAspectJAutoProxy
public class AspectConfiguration {

    @Bean
    public LoggingAspect loggingAspect(){
        return new LoggingAspect();
    }

}
