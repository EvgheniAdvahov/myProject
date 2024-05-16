package com.myProject.myProject.aspect;

import com.myProject.myProject.model.Item;
import com.myProject.myProject.service.ItemService;
import lombok.extern.java.Log;
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

    @Autowired
    private ItemService itemService;

    @AfterReturning("@annotation(ToLogAdd)  && args(item)")
    public void addToLog(Item item) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated()) {
            String username = authentication.getName();
            System.out.println(dateTime() + " " + username + " added " + item.getName());
            writeLogToFile(dateTime() + " " + username + " added " + item.getName());
        } else {
            System.out.println("Saved to DB: Unknown user");
        }
    }

    @Around("@annotation(ToLogEdit) && execution(* *(..)) && args(item, description)")
    public void editToLog(ProceedingJoinPoint joinPoint, Item item, StringBuilder description) throws Throwable {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated()) {
            String username = authentication.getName();
            System.out.println(dateTime() + " " + username + " edited " + item.getName());
            //todo проверить елси пустое
            description.setCharAt(description.length() - 1, '.');
            writeLogToFile(dateTime() + " " + username + " modified " + description);

            // Вызываем метод, к которому применен аспект
            joinPoint.proceed();
        } else {
            System.out.println("Edit in DB: Unknown user");
        }
    }

    @Before("@annotation(ToLogDelete)  && args(id)")
    public void deleteToLog(int id) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Item item = itemService.getById(id).orElseThrow(() -> new RuntimeException("Item with id " + id + " not found"));
        if (authentication != null && authentication.isAuthenticated()) {
            String username = authentication.getName();
            System.out.println(dateTime() + " " + username + " deleted " + item.getName());
            writeLogToFile(dateTime() + " " + username + " deleted " + item.getName());
        } else {
            System.out.println("Delete in DB: Unknown user");
        }
    }

    // Метод для записи строки в лог
    private void writeLogToFile(String message) {
        String filePath = "Logs/Application.log";
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath, true))) {
            writer.write(message);
            writer.newLine();
            System.out.println("Сообщение успешно записано в файл.");
        } catch (IOException e) {
            System.err.println("Ошибка при записи сообщения в файл: " + e.getMessage());
        }
    }

    private String dateTime() {
        LocalDateTime createdAt = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH∶mm∶ss");
        return createdAt.format(formatter);
    }
}
