package com.myProject.myProject.model;

import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PostDto {

    @NotEmpty(message = "Title is required")
    private String title;
    @NotEmpty(message = "Content is required")
    private String content;
}
