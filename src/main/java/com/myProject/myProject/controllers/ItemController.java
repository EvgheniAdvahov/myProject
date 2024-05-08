package com.myProject.myProject.controllers;

import com.myProject.myProject.model.Item;
import com.myProject.myProject.model.ItemDto;
import com.myProject.myProject.repositories.ItemRepository;
import com.myProject.myProject.service.ItemService;
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
import java.security.Principal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;

@Controller
@RequestMapping("/items")
public class ItemController {

    private final ItemService itemService;

    public ItemController(ItemService itemService) {
        this.itemService = itemService;
    }

    @GetMapping({"", "/"}) // items and items/
    public String showItemList(Model model, Principal principal) {

        List<Item> items = itemService.getAllItems();
        model.addAttribute("items", items);
        //Add username to html
        String username = principal.getName();
        model.addAttribute("username",username);
        return "items/index";
    }

    // todo //??
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
        LocalDateTime createdAt = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH∶mm∶ss");
        String formattedDate = createdAt.format(formatter);

        String storageFileName = formattedDate + "_" + image.getOriginalFilename();
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
        item.setManufacturer(itemDto.getManufacturer());
        item.setCategory(itemDto.getCategory());
        item.setDepartment(itemDto.getDepartment());
        item.setSerialNumber(itemDto.getSerialNumber());
        item.setProductOrder(itemDto.getProductOrder());
        item.setInventoryNumber(itemDto.getInventoryNumber());
        item.setDescription(itemDto.getDescription());
        item.setCreatedAt(formattedDate);
        item.setImageFileName(storageFileName);

        itemService.saveToDd(item);

        return "redirect:/items";
    }

    @GetMapping("/edit")
    public String showEditPage(Model model, @RequestParam int id) {
        try {
            Item item = itemService.getById(id);
            model.addAttribute("item", item);

            ItemDto itemDto = new ItemDto();
            itemDto.setName(item.getName());
            itemDto.setManufacturer(item.getManufacturer());
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

    @PostMapping("/edit")
    public String updateProduct(
            Model model,
            @RequestParam int id,
            @Valid @ModelAttribute ItemDto itemDto,
            BindingResult result
    ) {
        try {
            Item item = itemService.getById(id);
            model.addAttribute("item", item);

            if (result.hasErrors()) {
                return "items/editItem";
            }

            if (!itemDto.getImageFile().isEmpty()) {
                //delete old image
                String uploadDir = "src/main/resources/static/images/";
                Path oldImagePath = Paths.get(uploadDir + item.getImageFileName());

                //Todo: files разобраться + Exception
                try {
                    Files.delete(oldImagePath);
                } catch (Exception ex) {
                    System.out.println("Exception: " + ex.getMessage());
                }

                // save new image file
                //todo moified insted of created + loging would be not bad
                MultipartFile image = itemDto.getImageFile();
                Date createdAt = new Date();
                String storageFileName = createdAt.getTime() + "_" + image.getOriginalFilename();

                try (InputStream inputStream = image.getInputStream()) {
                    Files.copy(inputStream, Paths.get(uploadDir + storageFileName),
                            StandardCopyOption.REPLACE_EXISTING);
                }
                item.setImageFileName(storageFileName);
            }
            //todo Where Inventory number, probably add modified at

            if (!item.getName().equals(itemDto.getName())){
                System.out.println("Name modified");
            }
            if (!item.getStatus().equals(itemDto.getStatus())) {
                System.out.println("Status number modified");
            }
            if (!item.getManufacturer().equals(itemDto.getManufacturer())) {
                System.out.println("Manufacturer modified");
            }
            if (!item.getCategory().equals(itemDto.getCategory())) {
                System.out.println("Category modified");
            }
            if (!item.getDepartment().equals(itemDto.getDepartment())) {
                System.out.println("Department modified");
            }
            if (!item.getSerialNumber().equals(itemDto.getSerialNumber())) {
                System.out.println("Serial number modified");
            }
            if (!item.getProductOrder().equals(itemDto.getProductOrder())) {
                System.out.println("Product number modified");
            }
            if (!item.getInventoryNumber().equals(itemDto.getInventoryNumber())) {
                System.out.println("Inventory number modified");
            }
            if (!item.getDescription().equals(itemDto.getDescription())) {
                System.out.println("Description number modified");
            }


            item.setName(itemDto.getName());
            item.setStatus(itemDto.getStatus());
            item.setManufacturer(itemDto.getManufacturer());
            item.setCategory(itemDto.getCategory());
            item.setDepartment(itemDto.getDepartment());
            item.setSerialNumber(itemDto.getSerialNumber());
            item.setProductOrder(itemDto.getProductOrder());
            item.setInventoryNumber(itemDto.getInventoryNumber());
            item.setDescription(itemDto.getDescription());

            itemService.saveToDd(item);

        } catch (Exception ex) {
            System.out.println("Exception: " + ex.getMessage());
        }
        return "redirect:/items";
    }

    @GetMapping("/delete")
    public String deleteProduct(@RequestParam int id) {
        //todo Exception .... deleting photo
        try {
//            Item item = itemRepository.findById(id).get();
            Item item = itemService.getById(id);

            //delete item image
            Path imagePath = Paths.get("src/main/resources/static/images/" + item.getImageFileName());

            try {
                Files.delete(imagePath);
            } catch (Exception ex) {
                System.out.println("Exception: " + ex.getMessage());
            }

            //deleting product
//            itemRepository.deleteById(id);
            itemService.deleteById(id);
        } catch (Exception ex) {
            System.out.println("Exception" + ex.getMessage());
        }
        return "redirect:/items";
    }





}
