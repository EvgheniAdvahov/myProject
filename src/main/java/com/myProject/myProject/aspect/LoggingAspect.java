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

    @Autowired
    private ItemService itemService;
    @Autowired
    private UserService userService;

    @AfterReturning("@annotation(ToLogAdd) && args(item, description)")
    public void addToLog(Item item, String description) {
        writeLogToFile(dateTime() + " " + description);
    }

    @Around("@annotation(ToLogEdit) && execution(* *(..)) && args(item, description)")
    public void editToLog(ProceedingJoinPoint joinPoint, Item item, StringBuilder description) throws Throwable {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated()) {
            String username = authentication.getName();
            User user = userService.getUserByUsername(username);
            if (!description.isEmpty()) {
                System.out.println(dateTime() + " " + user.getFullName() + " edited " + item.getName());
                description.setCharAt(description.length() - 1, '.');
                writeLogToFile(dateTime() + " " + user.getFullName() + " modified " + description);
            }
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
            User user = userService.getUserByUsername(username);
            System.out.println(dateTime() + " " + user.getFullName() + " deleted " + item.getName());
            writeLogToFile(dateTime() + " " + user.getFullName() + " deleted " + item.getName());
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
