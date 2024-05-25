package com.myProject.myProject.controllers;


import com.myProject.myProject.model.User;
import com.myProject.myProject.security.PasswordService;
import com.myProject.myProject.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.security.Principal;
import java.util.List;
import java.util.Optional;

@Controller
@AllArgsConstructor
public class UserController {

    private final UserService userService;

    private final PasswordService passwordService;

    @GetMapping("/userList")
    public String showAdminPanel(Model model, Principal principal) {
        model.addAttribute("username", getUserFullName(principal));
        List<User> users = userService.getAllUsers();
        model.addAttribute("users", users);
        return "users/userList";
    }

    @GetMapping("/userUpdate/{id}")
    public String updateUser(@PathVariable("id") int id, Model model, Principal principal) {
        model.addAttribute("username", getUserFullName(principal));
        User user = userService.getUserById(id).get();
        model.addAttribute("user", user);
        return "users/userUpdate";
    }

    @PostMapping("/userUpdate")
    public String updateUser(User user, Model model, BindingResult result) {
        // get user from db by ID
        User existingUser = userService.getUserById(user.getId()).get();

        // verify if username already exists
        if (!existingUser.getUsername().equals(user.getUsername()) &&
                userService.existsByUsername(user.getUsername())) {
            result.addError(new FieldError("itemDto", "username", "username already exists"));
            return "users/userUpdate"; // возвращает форму редактирования с ошибкой
        } else {
//         update fields
            existingUser.setUsername(user.getUsername());
            existingUser.setPassword(encryptPassword(user.getPassword()));
            existingUser.setFullName(user.getFullName());
            // saving to db
            userService.saveToDb(existingUser);

            return "redirect:/userList";
        }
    }


    private String getUserFullName(Principal principal) {
        User user = userService.getUserByUsername(principal.getName()).get();
        return user.getFullName();
    }

    //encrypting pass
    private String encryptPassword(String password){
        return passwordService.encryptPassword(password);
    }

}
