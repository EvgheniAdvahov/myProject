package com.myProject.myProject.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@Entity
@Table(name = "items")
public class Item {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String name;
    private String manufacturer;
    private String department;
    private String model;
    private int productOrder;
    private int inventoryNumber;
    private int serialNumber;
    private String category;
    @Column(columnDefinition = "TEXT")
    private String description;
    private Date createdAt;
    private Date modifiedAt;
    private String imageFileName;
    private String status;

}
