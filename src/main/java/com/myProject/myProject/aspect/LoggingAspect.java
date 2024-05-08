package com.myProject.myProject.aspect;

import com.myProject.myProject.model.Item;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Aspect
public class LoggingAspect {

    @AfterReturning("@annotation(ToLog)  && args(item)")
    public void addToLog(Item item) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated()) {
            String username = authentication.getName();
            System.out.println(dateTime() + " " + username + " added " + item.getName());
        } else {
            System.out.println("Saved to DB: Unknown user");
        }
    }

    private String dateTime(){
        LocalDateTime createdAt = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH∶mm∶ss");
        return createdAt.format(formatter);
    }
}
