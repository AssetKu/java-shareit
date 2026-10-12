package ru.practicum.shareit.item.model;

import ru.practicum.shareit.item.dto.ItemDto;

import java.util.Collections;

public class ItemMapper {

    public static ItemDto toDto(Item item) {
        if (item == null) {
            return null;
        }

        return ItemDto.builder()
                .id(item.getId())
                .name(item.getName())
                .description(item.getDescription())
                .available(item.getAvailable())
                .comments(Collections.emptyList())
                .build();
    }
}
