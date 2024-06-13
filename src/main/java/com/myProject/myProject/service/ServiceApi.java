package com.myProject.myProject.service;

import com.myProject.myProject.model.ZenQuote;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
@AllArgsConstructor
public class ServiceApi {

    private RestTemplate template;

    public ZenQuote[] getZenQuote(String param) {
        return template.getForObject(param, ZenQuote[].class);
    }
}
