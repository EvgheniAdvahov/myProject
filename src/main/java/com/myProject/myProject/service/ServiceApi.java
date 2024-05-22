package com.myProject.myProject.service;

import com.myProject.myProject.model.ZenQuote;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Service
@AllArgsConstructor
public class ServiceApi {
    private RestTemplate template;

    public ZenQuote[] getZenQuote(String param){
        return template.getForObject(param, ZenQuote[].class);

    }

}
