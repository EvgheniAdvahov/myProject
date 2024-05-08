package com.myProject.myProject.aspect;

import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

@Aspect
public class LoggingAspect {

    @AfterReturning("@annotation(ToLog)")
    public void addToLog() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated()) {
            String username = authentication.getName();
            System.out.println("Saved to DB: " + username);
        } else {
            System.out.println("Saved to DB: Unknown user");
        }
    }
}
