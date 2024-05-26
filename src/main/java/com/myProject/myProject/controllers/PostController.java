package com.myProject.myProject.controllers;

import com.myProject.myProject.model.Item;
import com.myProject.myProject.model.Post;
import com.myProject.myProject.model.PostDto;
import com.myProject.myProject.model.User;
import com.myProject.myProject.service.PostService;
import com.myProject.myProject.service.UserService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.security.Principal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@AllArgsConstructor
@Controller
@RequestMapping("/posts")
public class PostController {

    private final UserService userService;
    private final PostService postService;

    @GetMapping
    public String showPostsPage(Model model, Principal principal) {
        model.addAttribute("username", getUserFullName(principal));
        return "posts/postList";
    }

    @GetMapping("/create")
    public String newPostPage(Model model, Principal principal) {
        model.addAttribute("postDto", new PostDto());
        //Add user full name to html page
        model.addAttribute("username", getUserFullName(principal));
        return "posts/createPost";
    }

    @PostMapping("/create")
    public String createNewPost(@Valid @ModelAttribute PostDto postDto,
                                BindingResult result,
                                Model model,
                                Principal principal) {
        //Add user full name to html page
        model.addAttribute("username", getUserFullName(principal));

        if (result.hasErrors()) {
            return "posts/createPost";
        }

        Post post = new Post();
        post.setTitle(postDto.getTitle());
        post.setContent(postDto.getContent());
        post.setCreatedAt(dateTime());
        post.setCreatedBy(getUserFullName(principal));

        postService.savePostToDb(post);

        return "redirect:/posts";
    }


    //Date and time
    private String dateTime() {
        LocalDateTime createdAt = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH∶mm∶ss");
        return createdAt.format(formatter);
    }
    private String getUserFullName(Principal principal) {
        User user = userService.getUserByUsername(principal.getName()).get();
        return user.getFullName();
    }
}
