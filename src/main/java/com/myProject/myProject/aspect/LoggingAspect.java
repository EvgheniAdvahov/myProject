package com.myProject.myProject.aspect;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;

@Aspect
public class LoggingAspect {

    @AfterReturning(value = "@annotation(ToLog)")
    public void addToLog(){
        System.out.println("MESSAGE TO CONSOLE");
    }

}
