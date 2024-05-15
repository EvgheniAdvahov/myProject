package com.myProject.myProject.service;


import com.myProject.myProject.model.Item;
import com.myProject.myProject.repositories.ItemRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

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

        // Сохраняем объект в базу данных
        itemService.saveToDb(item);

        // Проверяем, что метод save был вызван один раз с переданным item
        verify(itemRepository, times(1)).save(item);
    }

    @Test
    public void deletyByIdTest(){
        // Создаем объект Item для сохранения в базе данных
        Item item = new Item();

        // Мокируем itemRepository
        when(itemRepository.save(item)).thenReturn(item);
        when(itemRepository.findById(item.getId())).thenReturn(Optional.of(item));

        // Вызываем метод сохранения в базе данных
        itemService.saveToDb(item);

        // Проверяем, что метод save был вызван один раз с переданным item
        verify(itemRepository, times(1)).save(item);

        int id = item.getId();
        // Удаляем объект из данных
        itemService.deleteById(item.getId());

        // Проверяем, что метод deleteById был вызван один раз с правильным ID
        verify(itemRepository, times(1)).deleteById(item.getId());

        // После удаления объекта, он не должен быть найден
        Item foundItem = itemRepository.findById(id).orElse(null);

        assertThat(foundItem).isNull();
    }
}
