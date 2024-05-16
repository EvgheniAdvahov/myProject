package com.myProject.myProject.service;

import com.myProject.myProject.aspect.ToLogAdd;
import com.myProject.myProject.aspect.ToLogDelete;
import com.myProject.myProject.aspect.ToLogEdit;
import com.myProject.myProject.model.Item;
import com.myProject.myProject.repositories.ItemRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class ItemService {

    private final ItemRepository itemRepository;

    @ToLogAdd
    public void saveToDb(Item item){
        itemRepository.save(item);
    }

    @ToLogEdit
    public void editInDb(Item item, StringBuilder description){
        itemRepository.save(item);
    }

    @ToLogDelete
    public void deleteById(int id){
        itemRepository.deleteById(id);
    }

    public List<Item> getAllItems(){
        return itemRepository.findAll();
    }

    public Optional<Item> getById(int id) {
        return itemRepository.findById(id);
    }

}
