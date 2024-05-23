package com.myProject.myProject.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/posts")
public class PostController {

    @GetMapping
    public String showPostsPage(){
        return "posts/postList";
    }

    @GetMapping("/create")
    public String newPost(){
        return "posts/createPost";
    }

}
