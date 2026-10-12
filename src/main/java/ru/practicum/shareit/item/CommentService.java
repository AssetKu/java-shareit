package ru.practicum.shareit.item;

import ru.practicum.shareit.item.dto.CommentDto;

import java.util.List;

public interface CommentService {

    CommentDto create(Long userId,
                      Long itemId,
                      CommentDto commentDto);

    List<CommentDto> getByItemId(Long itemId);
}