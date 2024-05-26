package com.myProject.myProject.service;

import com.myProject.myProject.model.Post;
import com.myProject.myProject.repositories.PostRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class PostService {

    private final PostRepository postRepository;

    public void savePostToDb(Post post){
        postRepository.save(post);
    }

}
