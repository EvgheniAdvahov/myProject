package com.myProject.myProject.controllers;

import com.myProject.myProject.model.Item;
import com.myProject.myProject.model.ItemDto;
import com.myProject.myProject.repositories.ItemRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/items")
public class ItemController {

    private final ItemRepository itemRepository;

    public ItemController(ItemRepository itemRepository) {
        this.itemRepository = itemRepository;
    }

    @GetMapping({"", "/"}) // to learn about
    public String showItemList(Model model){
        List<Item> items = itemRepository.findAll();
        model.addAttribute("items", items);
        return "items/index";
    }

    @GetMapping("/create")
    public String showCreatePage(Model model){
        ItemDto itemDto = new ItemDto();
        model.addAttribute("itemDto", itemDto);
        return "items/createItem";
    }


}
