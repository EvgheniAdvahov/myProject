package com.myProject.myProject.controllers;


import com.myProject.myProject.model.User;
import com.myProject.myProject.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.security.Principal;
import java.util.List;

@Controller
@AllArgsConstructor
public class UserController {

    private final UserService userService;
    @GetMapping("/userList")
    public String showAdminPanel(Model model, Principal principal){
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
    public String updateUser(User user) {
        userService.saveToDb(user);
        return "redirect:/userList";
    }




    private String getUserFullName(Principal principal) {
        User user = userService.getUserByUsername(principal.getName()).get();
        return user.getFullName();
    }

}
