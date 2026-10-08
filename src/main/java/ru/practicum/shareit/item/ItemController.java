package ru.practicum.shareit.item;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;

import java.util.Collection;

@RestController
@RequestMapping("/items")
@RequiredArgsConstructor
public class ItemController {

    private final ItemService itemService;
    private final CommentService commentService;

    @PostMapping
    public ItemDto create(
            @RequestHeader("X-Sharer-User-Id") Long userId,
            @Valid @RequestBody ItemDto itemDto) {

        return itemService.create(userId, itemDto);
    }

    @PatchMapping("/{itemId}")
    public ItemDto update(
            @RequestHeader("X-Sharer-User-Id") Long userId,
            @PathVariable Long itemId,
            @RequestBody ItemDto itemDto) {

        return itemService.update(userId, itemId, itemDto);
    }

    @GetMapping("/{itemId}")
    public ItemDto getById(@PathVariable Long itemId) {
        return itemService.getById(itemId);
    }

    @GetMapping
    public Collection<ItemDto> getOwnerItems(
            @RequestHeader("X-Sharer-User-Id") Long userId) {

        return itemService.getOwnerItems(userId);
    }

    @GetMapping("/search")
    public Collection<ItemDto> search(
            @RequestParam String text) {

        return itemService.search(text);
    }

    @PostMapping("/{itemId}/comment")
    public CommentDto createComment(
            @RequestHeader("X-Sharer-User-Id") Long userId,
            @PathVariable Long itemId,
            @RequestBody CommentDto dto) {

        return commentService.create(userId, itemId, dto);
    }
}