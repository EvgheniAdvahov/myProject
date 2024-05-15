package com.myProject.myProject.controllers;

import com.myProject.myProject.model.Item;
import com.myProject.myProject.model.ItemDto;
import com.myProject.myProject.model.MyLog;
import com.myProject.myProject.model.User;
import com.myProject.myProject.service.ItemService;
import com.myProject.myProject.service.LogService;
import com.myProject.myProject.service.UserService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
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
import java.util.List;

@Controller
@RequestMapping("/")
@AllArgsConstructor
public class ItemController {

    private final ItemService itemService;
    private final UserService userService;
    private final LogService logService;


    @GetMapping("/login")
    public String loginPage() {
        return "login";
    }

    @GetMapping
    public String showMainPage(Model model, Principal principal) {
        model.addAttribute("username", userFullName(principal));
        return "main";
    }

    @GetMapping("/itemList")
    public String showItemList(Model model, Principal principal) {

        List<Item> items = itemService.getAllItems();
        model.addAttribute("items", items);
        //Add username to html
        model.addAttribute("username", userFullName(principal));
        return "items/itemList";
    }

    // todo //??
    @GetMapping("/items/create")
    public String showCreatePage(Model model, Principal principal) {
        ItemDto itemDto = new ItemDto();
        model.addAttribute("itemDto", itemDto);
        //Add username to html
        model.addAttribute("username", userFullName(principal));
        return "items/createItem";
    }

    @PostMapping("/items/create")
    public String createItem(@Valid @ModelAttribute ItemDto itemDto,
                             BindingResult result,
                             Principal principal) { // Данные из запроса привязываются к dto и валидируются. BindingResult- инфо об ошибках валидации
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
        String formattedDate = dateTime();

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
        item.setStatus(itemDto.getStatus());
        item.setManufacturer(itemDto.getManufacturer());
        item.setCategory(itemDto.getCategory());
        item.setDepartment(itemDto.getDepartment());
        item.setModel(itemDto.getModel());
        item.setSerialNumber(itemDto.getSerialNumber());
        item.setProductOrder(itemDto.getProductOrder());
        item.setInventoryNumber(itemDto.getInventoryNumber());
        item.setDescription(itemDto.getDescription());
        item.setCreatedAt(formattedDate);
        item.setModifiedAt(formattedDate);
        item.setImageFileName(storageFileName);
        itemService.saveToDb(item);


        //creating log
        MyLog myLog = new MyLog();
        myLog.setDescription(userFullName(principal) + " created " + item.getName()
                + ", Status= " + item.getStatus()
                + ", Manufacturer= " + item.getManufacturer()
                + ", Category= " + item.getCategory()
                + ", Department= " + item.getDepartment()
                + ", Model= " + item.getModel()
                + ", S/N= " + item.getSerialNumber()
                + ", PO= " + item.getProductOrder()
                + ", Inv. nr.= " + item.getInventoryNumber()
                + ", Desc: " + item.getDescription());
        myLog.setItem(item);
        myLog.setUser(userService.getUserByUsername(principal.getName()));
        myLog.setCreatedAt(formattedDate);
        logService.saveLogToDb(myLog);

        return "redirect:/itemList";
    }

    @GetMapping("/items/info")
    public String showInfoPage(Model model, @RequestParam int id, Principal principal) {
        model.addAttribute("username", userFullName(principal));

        List<MyLog> myLogList = logService.itemLogs(id);
        model.addAttribute("itemlog", myLogList);
        try {
            Item item = itemService.getById(id).orElseThrow(() -> new RuntimeException("Item with id " + id + " not found"));
            model.addAttribute("item", item);

            ItemDto itemDto = new ItemDto();
            itemDto.setName(item.getName());
            itemDto.setStatus(item.getStatus());
            itemDto.setManufacturer(item.getManufacturer());
            itemDto.setCategory(item.getCategory());
            itemDto.setDepartment(item.getDepartment());
            itemDto.setModel(item.getModel());
            itemDto.setSerialNumber(item.getSerialNumber());
            itemDto.setProductOrder(item.getProductOrder());
            itemDto.setInventoryNumber(item.getInventoryNumber());
            itemDto.setDescription(item.getDescription());

            model.addAttribute("itemDto", itemDto);
        } catch (Exception ex) {
            System.out.println("Exception: " + ex.getMessage());
            return "redirect:/itemList";
        }

        return "items/infoItem";
    }

    @GetMapping("/items/edit")
    public String showEditPage(Model model, @RequestParam int id, Principal principal) {
        model.addAttribute("username", userFullName(principal));
        try {
            Item item = itemService.getById(id).orElseThrow(() -> new RuntimeException("Item with id " + id + " not found"));
            model.addAttribute("item", item);

            ItemDto itemDto = new ItemDto();
            itemDto.setName(item.getName());
            itemDto.setStatus(item.getStatus());
            itemDto.setManufacturer(item.getManufacturer());
            itemDto.setCategory(item.getCategory());
            itemDto.setDepartment(item.getDepartment());
            itemDto.setModel(item.getModel());
            itemDto.setSerialNumber(item.getSerialNumber());
            itemDto.setProductOrder(item.getProductOrder());
            itemDto.setInventoryNumber(item.getInventoryNumber());
            itemDto.setDescription(item.getDescription());

            model.addAttribute("itemDto", itemDto);
        } catch (Exception ex) {
            System.out.println("Exception: " + ex.getMessage());
            return "redirect:/itemList";
        }

        return "items/editItem";
    }

    @PostMapping("/items/edit")
    public String updateProduct(
            Model model,
            @RequestParam int id,
            @Valid @ModelAttribute ItemDto itemDto,
            BindingResult result,
            Principal principal
    ) {
        try {
            Item item = itemService.getById(id).orElseThrow(() -> new RuntimeException("Item with id " + id + " not found"));
            model.addAttribute("item", item);

            if (result.hasErrors()) {
                return "items/editItem";
            }

            if (!itemDto.getImageFile().isEmpty()) {
                //magic variables
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
                MultipartFile image = itemDto.getImageFile();
                String formattedDate = dateTime();
                String storageFileName = formattedDate + "_" + image.getOriginalFilename();

                try (InputStream inputStream = image.getInputStream()) {
                    Files.copy(inputStream, Paths.get(uploadDir + storageFileName),
                            StandardCopyOption.REPLACE_EXISTING);
                }
                item.setImageFileName(storageFileName);
            }

            StringBuilder description = new StringBuilder();
            if (!item.getName().equals(itemDto.getName())) {
                description.append("Name to= ").append(itemDto.getName()).append(",");
                System.out.println("Name modified");
            }
            if (!item.getStatus().equals(itemDto.getStatus())) {
                description.append(" Status to= ").append(itemDto.getStatus()).append(",");
                System.out.println("Status modified");
            }
            if (!item.getManufacturer().equals(itemDto.getManufacturer())) {
                description.append(" Manufacturer to= ").append(itemDto.getManufacturer()).append(",");
                System.out.println("Manufacturer modified");
            }
            if (!item.getCategory().equals(itemDto.getCategory())) {
                description.append(" Category to= ").append(itemDto.getCategory()).append(",");
                System.out.println("Category modified");
            }
            if (!item.getDepartment().equals(itemDto.getDepartment())) {
                description.append(" Department to= ").append(itemDto.getDepartment()).append(",");
                System.out.println("Department modified");
            }
            if (!item.getModel().equals(itemDto.getModel())) {
                description.append(" Model to= ").append(itemDto.getModel()).append(",");
                System.out.println("Model modified");
            }
            if (!item.getSerialNumber().equals(itemDto.getSerialNumber())) {
                description.append(" S/N to= ").append(itemDto.getSerialNumber()).append(",");
                System.out.println("Serial number modified");
            }
            if (item.getProductOrder() != null && !item.getProductOrder().equals(itemDto.getProductOrder())) {
                description.append(" PO to= ").append(itemDto.getProductOrder()).append(",");
                System.out.println("Product number modified");
            }
            if (item.getInventoryNumber() != null && !item.getInventoryNumber().equals(itemDto.getInventoryNumber())) {
                description.append(" Inv. nr. to= ").append(itemDto.getInventoryNumber()).append(",");
                System.out.println("Inventory number modified");
            }
            if (!item.getDescription().equals(itemDto.getDescription())) {
                description.append(" Description to= ").append(itemDto.getDescription()).append(",");
                System.out.println("Description modified");
            }


            item.setName(itemDto.getName());
            item.setStatus(itemDto.getStatus());
            item.setManufacturer(itemDto.getManufacturer());
            item.setCategory(itemDto.getCategory());
            item.setDepartment(itemDto.getDepartment());
            item.setModel(itemDto.getModel());
            item.setSerialNumber(itemDto.getSerialNumber());
            item.setProductOrder(itemDto.getProductOrder());
            item.setInventoryNumber(itemDto.getInventoryNumber());
            item.setDescription(itemDto.getDescription());
            item.setModifiedAt(dateTime());
            itemService.editInDb(item);

            //creating log
            if (!description.isEmpty()) {
                description.setCharAt(description.length() - 1, '.');
                MyLog myLog = new MyLog();
                myLog.setDescription(userFullName(principal) + " modified: " + description);
                myLog.setItem(item);
                myLog.setUser(userService.getUserByUsername(principal.getName()));
                myLog.setCreatedAt(dateTime());
                logService.saveLogToDb(myLog);
            }

        } catch (Exception ex) {
            System.out.println("Exception: " + ex.getMessage());
        }

        return "redirect:/itemList";
    }

    @GetMapping("/items/delete")
    public String deleteProduct(@RequestParam int id) {
        //todo Exception .... deleting photo
        try {
            Item item = itemService.getById(id).orElseThrow(() -> new RuntimeException("Item with id " + id + " not found"));;

            //delete item image
            Path imagePath = Paths.get("src/main/resources/static/images/" + item.getImageFileName());

            try {
                Files.delete(imagePath);
            } catch (Exception ex) {
                System.out.println("Exception: " + ex.getMessage());
            }
            //deleting product
            itemService.deleteById(id);
        } catch (Exception ex) {
            System.out.println("Exception " + ex.getMessage());
        }
        return "redirect:/itemList";
    }


    private String dateTime() {
        LocalDateTime createdAt = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH∶mm∶ss");
        return createdAt.format(formatter);
    }

    private String userFullName(Principal principal) {
        User user = userService.getUserByUsername(principal.getName());
        return user.getFullName();
    }


}
