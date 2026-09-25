package ru.practicum.shareit.item;

import ru.practicum.shareit.item.model.Item;

import java.util.Collection;

public interface ItemRepository {

    Item save(Item item);

    Item findById(Long id);

    Collection<Item> findAll();

    Item update(Item item);
}