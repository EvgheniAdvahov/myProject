package com.myProject.myProject.aspect;

import com.myProject.myProject.model.Item;
import com.myProject.myProject.model.User;
import com.myProject.myProject.service.ItemService;
import com.myProject.myProject.service.UserService;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Aspect
public class LoggingAspect {

    private static final String UPLOAD_DIR_LOG = "Logs/Application.log";

    @AfterReturning("@annotation(com.myProject.myProject.aspect.ToLogSave) && args(item, description)")
    public void addToLog(Item item, String description) {
        writeLogToFile(dateTime() + " " + description);
    }

    @Before("@annotation(ToLogDelete) && args(id, description)")
    public void deleteToLog(int id, String description) {
        writeLogToFile(dateTime() + " " + description);
    }


    // Метод для записи строки в лог
    private void writeLogToFile(String message) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(UPLOAD_DIR_LOG, true))) {
            writer.write(message);
            writer.newLine();
        } catch (IOException e) {
            System.err.println("Error when writing log to file: " + e.getMessage());
        }
    }

    private String dateTime() {
        LocalDateTime createdAt = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH∶mm∶ss");
        return createdAt.format(formatter);
    }
}
