package ru.practicum.shareit.request;

import ru.practicum.shareit.request.dto.ItemResponseDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.request.dto.ItemRequestDto;

import java.util.Collections;

public class ItemRequestMapper {

    public static ItemRequestDto toDto(ItemRequest request) {

        return ItemRequestDto.builder()
                .id(request.getId())
                .description(request.getDescription())
                .created(request.getCreated())
                .items(Collections.emptyList())
                .build();
    }

    public static ItemResponseDto toItemResponseDto(Item item) {

        return ItemResponseDto.builder()
                .id(item.getId())
                .name(item.getName())
                .ownerId(item.getOwner().getId())
                .build();
    }
}