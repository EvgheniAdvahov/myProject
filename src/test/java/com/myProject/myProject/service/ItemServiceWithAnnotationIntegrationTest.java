package com.myProject.myProject.service;


import com.myProject.myProject.model.Item;
import com.myProject.myProject.repositories.ItemRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

//Аннотация @AutoConfigureTestDatabase используется для указания того,
// что тест должен использовать реальную базу данных, а не встроенную
// (например, H2).
@SpringBootTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class ItemServiceWithAnnotationIntegrationTest {

    @Autowired
    private ItemService itemService;

    @MockBean
    private ItemRepository itemRepository;

    @Test
    public void saveToDbTest(){
        // Создаем объект Item для сохранения в базе данных
        Item item = new Item();
        item.setName("Название товара");
        item.setDescription("Описание товара");

        // Сохраняем объект в базу данных
        itemService.saveToDd(item);

        // Получаем объект из базы данных по его ID
        Item savedItem = itemService.getById(item.getId()).get();

        // Проверяем, что объект был успешно сохранен и получен из базы данных
        assertNotNull(savedItem);
        assertEquals("Название товара", savedItem.getName());
        assertEquals("Описание товара", savedItem.getDescription());

    }
}
