package com.myProject.myProject.model;

import jakarta.validation.constraints.NotBlank;
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
    private String brand;
    @NotEmpty(message = "The category is required")
    private String category;
    @NotEmpty(message = "The department required")
    private String department;

    //Todo: валидация
    @NotNull(message = "The serial number required")
    private int serialNumber;

    private int productOrder;

    private int inventoryNumber;

    @Size(max = 2000, message = "The description can't exceed 2000 characters")
    private String description;

    private MultipartFile imageFile;

}
