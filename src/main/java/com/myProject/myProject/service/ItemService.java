package com.myProject.myProject.service;

import com.myProject.myProject.aspect.ToLog;
import com.myProject.myProject.model.Item;
import com.myProject.myProject.repositories.ItemRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class ItemService {

    private final ItemRepository itemRepository;

    @ToLog
    public void saveToDd(Item item){
        itemRepository.save(item);
    }

    public List<Item> getAllItems(){
        return itemRepository.findAll();
    }

    public Item getById(int id){
        return itemRepository.findById(id).get();
    }

    public void deleteById(int id){
        itemRepository.deleteById(id);
    }
}
