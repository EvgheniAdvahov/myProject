package com.myProject.myProject.controllers;

import com.myProject.myProject.model.Item;
import com.myProject.myProject.model.ItemDto;
import com.myProject.myProject.repositories.ItemRepository;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Date;
import java.util.List;

@Controller
@RequestMapping("/items")
public class ItemController {

    private final ItemRepository itemRepository;

    public ItemController(ItemRepository itemRepository) {
        this.itemRepository = itemRepository;
    }

    @GetMapping({"", "/"}) // items and items/
    public String showItemList(Model model) {
        List<Item> items = itemRepository.findAll();
        model.addAttribute("items", items);
        return "items/index";
    }

    @GetMapping("/create")
    public String showCreatePage(Model model) {
        ItemDto itemDto = new ItemDto();
        model.addAttribute("itemDto", itemDto);
        return "items/createItem";
    }

    @PostMapping("/create")
    public String createItem(@Valid @ModelAttribute ItemDto itemDto,
                             BindingResult result) { // Данные из запроса привязываются к dto и валидируются. BindingResult- инфо об ошибках валидации
        if (itemDto.getImageFile().isEmpty()) {
            result.addError(new FieldError("itemDto", "imageFile", "The image file is required"));
            //вручную добавляем Валидацию для ImageFile
        }

        if (result.hasErrors()) {
            System.out.println("error" + result);
            return "items/createItem";
        }
        //save image file
        MultipartFile image = itemDto.getImageFile();
        Date createdAt = new Date();
        //Todo: remove miliseconds
        String storageFileName = createdAt.getTime() + "_" + image.getOriginalFilename();
        try {
            String uploadDir = "src/main/resources/static/images/";
            Path uploadPath = Paths.get(uploadDir);

            if (!Files.exists(uploadPath)) {
                Files.createDirectories(uploadPath);
            }
            //TODO: Разобраться как и куда копируем
            try (InputStream inputStream = image.getInputStream()) {
                Files.copy(inputStream, Paths.get(uploadDir + storageFileName),
                        StandardCopyOption.REPLACE_EXISTING);
            }
            //Todo: Exception - слишком обобщённо
        } catch (Exception ex) {
            System.out.println("Exception: " + ex.getMessage());
        }

        Item item = new Item();
        item.setName(itemDto.getName());
        item.setBrand(itemDto.getBrand());
        item.setCategory(itemDto.getCategory());
        item.setDepartment(itemDto.getDepartment());
        item.setSerialNumber(itemDto.getSerialNumber());
        item.setDescription(itemDto.getDescription());
        item.setCreatedAt(createdAt);
        item.setImageFileName(storageFileName);


        itemRepository.save(item);

        return "redirect:/items";
    }

    @GetMapping("/edit")
    public String showEditPage(Model model, @RequestParam int id) {
        try {
            Item item = itemRepository.findById(id).get();
            model.addAttribute("item", item);

            ItemDto itemDto = new ItemDto();
            itemDto.setName(item.getName());
            itemDto.setBrand(item.getBrand());
            itemDto.setCategory(item.getCategory());
            itemDto.setDepartment(item.getDepartment());
            itemDto.setSerialNumber(item.getSerialNumber());
            itemDto.setDescription(item.getDescription());

            model.addAttribute("itemDto", itemDto);
        } catch (Exception ex) {
            System.out.println("Exception: " + ex.getMessage());
            return "redirect:/items";
        }

        return "items/editItem";
    }


}
