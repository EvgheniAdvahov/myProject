package com.myProject.myProject.service;


import com.myProject.myProject.model.Item;
import com.myProject.myProject.repositories.ItemRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.ActiveProfiles;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ItemServiceWithAnnotationTest {

    @Mock
    private ItemRepository itemRepository;

    @InjectMocks
    private ItemService itemService;

    @Test
    public void saveToDbTest(){
        // Создаем объект Item для сохранения в базе данных
        Item item = new Item();
        //
        String description = "Item saved";

        // Сохраняем объект в базу данных
        itemService.saveToDb(item, description);

        // Проверяем, что метод save был вызван один раз с переданным item
        verify(itemRepository, times(1)).save(item);
    }

    @Test
    public void deleteByIdTest(){
        // Создаем объект Item для сохранения в базе данных
        Item item = new Item();
        item.setName("Sample name");
        //
        String descriptionOnSave = "Item saved";
        String descriptionOnDelete = "Item deleted";


        // Мокируем itemRepository
        when(itemRepository.save(item)).thenReturn(item);
        when(itemRepository.findById(item.getId())).thenReturn(Optional.empty());
        // Добавляем мок для метода deleteById
        doNothing().when(itemRepository).deleteById(item.getId());

        // Вызываем метод сохранения в базе данных
        itemService.saveToDb(item, descriptionOnSave);

        // Проверяем, что метод save был вызван один раз с переданным item
        verify(itemRepository, times(1)).save(item);

        // Удаляем объект из данных
        itemService.deleteById(item.getId(), descriptionOnDelete);

        // Проверяем, что метод deleteById был вызван один раз с правильным ID
        verify(itemRepository, times(1)).deleteById(item.getId());

        // После удаления объекта, он не должен быть найден
        Optional<Item> foundItem = itemService.getById(item.getId());

        assertThat(foundItem).isEmpty();
    }
}
