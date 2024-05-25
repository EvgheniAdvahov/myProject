package com.myProject.myProject.security;

import lombok.AllArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class PasswordService {
    private final PasswordEncoder passwordEncoder;
    public String encryptPassword(String text) {
        return passwordEncoder.encode(text);
    }
}
