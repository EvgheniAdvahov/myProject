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

    @AfterReturning("@annotation(ToLogAdd)  && args(item)")
    public void addToLog(Item item) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated()) {
            String username = authentication.getName();
            System.out.println(dateTime() + " " + username + " added " + item.getName());
        } else {
            System.out.println("Saved to DB: Unknown user");
        }
    }

    @AfterReturning("@annotation(ToLogEdit)  && args(item)")
    public void editToLog(Item item) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated()) {
            String username = authentication.getName();
            System.out.println(dateTime() + " " + username + " edited " + item.getName());
        } else {
            System.out.println("Edite in DB: Unknown user");
        }
    }

    @AfterReturning("@annotation(ToLogDelete)  && args(item)")
    public void deleteToLog(Item item) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated()) {
            String username = authentication.getName();
            System.out.println(dateTime() + " " + username + " deleted " + item.getName());
        } else {
            System.out.println("Delete in DB: Unknown user");
        }
    }

    private String dateTime(){
        LocalDateTime createdAt = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH∶mm∶ss");
        return createdAt.format(formatter);
    }
}
