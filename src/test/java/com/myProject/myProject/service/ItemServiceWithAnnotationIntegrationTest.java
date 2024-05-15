package com.myProject.myProject.service;


import com.myProject.myProject.model.Item;
import com.myProject.myProject.repositories.ItemRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;

import java.util.Date;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@ActiveProfiles("test")
public class ItemServiceWithAnnotationIntegrationTest {

    @Autowired
    private ItemRepository itemRepository;

    @Autowired
    private ItemService itemService;

    @Test
    public void saveToDbIntegrationTest() {
        // Создаем объект Item для сохранения в базе данных
        Item item = new Item();
        // Установите необходимые поля item, если нужно
        item.setName("Sample Item");
        item.setCreatedAt(String.valueOf(new Date()));
        item.setModifiedAt(String.valueOf(new Date()));

        // Сохраняем объект в базу данных
        itemService.saveToDd(item);

        // Проверяем, что объект был сохранен в базу данных
        Item foundItem = itemRepository.findById(item.getId()).orElse(null);
        assertThat(foundItem).isNotNull();
        assertThat(foundItem.getName()).isEqualTo("Sample Item");
    }

}
