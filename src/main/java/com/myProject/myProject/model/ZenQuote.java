package com.myProject.myProject.model;

import lombok.Data;

@Data
public class ZenQuote {

    private String q;
    private String a;
    private String h;

    public ZenQuote(String q, String a) {
        this.q = q;
        this.a = a;
    }
    public ZenQuote(){}
}
