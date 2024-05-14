package com.myProject.myProject.model;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Setter
public class ItemDto {

    @NotEmpty(message = "The name is required")
    private String name;

    @NotEmpty(message = "The brand is required")
    private String manufacturer;
    @NotEmpty(message = "The category is required")
    private String category;
    @NotEmpty(message = "The department required")
    private String department;

    @NotEmpty(message = "The serial number required")
    private String serialNumber;

    private Integer productOrder;

    private Integer inventoryNumber;

    @Size(max = 300, message = "The description can't exceed 300 characters")
    private String description;

    private MultipartFile imageFile;

    private String status;


}
