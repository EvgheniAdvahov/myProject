package com.myProject.myProject.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Autowired
    private AuthHandler authHandler;
    @Bean
    SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        //todo favicon
        http
                .authorizeHttpRequests((authorize) -> authorize
                        .requestMatchers("/css/**", "/favicon.ico").permitAll()
                        .requestMatchers("/user-profile").hasAnyRole("USER", "ADMIN")
                        .anyRequest().authenticated()
                )
                .formLogin(login -> login
                        //todo login page
//                        .loginPage("/")
                        .successHandler(authHandler)
                        .permitAll())
                .logout(logout -> logout
                        .logoutSuccessUrl("/"))
                .csrf().disable();
        return http.build();
    }

    //создаем кодировщик паролей
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }




}
