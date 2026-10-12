package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.ForbiddenException;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.model.ItemMapper;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.UserRepository;

import java.util.Collection;
import java.util.Collections;


@Service
@RequiredArgsConstructor
public class ItemServiceImpl implements ItemService {

    private final ItemRepository itemRepository;
    private final UserRepository userRepository;
    private final CommentRepository commentRepository;

    @Override
    public ItemDto create(Long userId, ItemDto itemDto) {

        User owner = userRepository.findById(userId)
                .orElseThrow(() ->
                        new NotFoundException("Пользователь не найден"));

        Item item = Item.builder()
                .name(itemDto.getName())
                .description(itemDto.getDescription())
                .available(itemDto.getAvailable())
                .owner(owner)
                .build();

        return ItemMapper.toDto(
                itemRepository.save(item)
        );
    }

    @Override
    public ItemDto update(Long userId,
                          Long itemId,
                          ItemDto itemDto) {

        Item item = itemRepository.findById(itemId)
                .orElseThrow(() ->
                        new NotFoundException("Вещь не найдена"));

        if (!item.getOwner().getId().equals(userId)) {
            throw new ForbiddenException(
                    "Редактировать может только владелец"
            );
        }

        if (itemDto.getName() != null) {
            item.setName(itemDto.getName());
        }

        if (itemDto.getDescription() != null) {
            item.setDescription(itemDto.getDescription());
        }

        if (itemDto.getAvailable() != null) {
            item.setAvailable(itemDto.getAvailable());
        }

        return ItemMapper.toDto(
                itemRepository.save(item)
        );
    }

    @Override
    public ItemDto getById(Long itemId) {

        Item item = itemRepository.findById(itemId)
                .orElseThrow(() ->
                        new NotFoundException("Вещь не найдена"));

        ItemDto itemDto = ItemMapper.toDto(item);

        itemDto.setComments(
                commentRepository.findByItemId(itemId)
                        .stream()
                        .map(CommentMapper::toDto)
                        .toList()
        );

        return itemDto;
    }

    @Override
    public Collection<ItemDto> getOwnerItems(Long userId) {

        return itemRepository.findByOwnerId(userId)
                .stream()
                .map(item -> {

                    ItemDto itemDto = ItemMapper.toDto(item);

                    itemDto.setComments(
                            commentRepository.findByItemId(item.getId())
                                    .stream()
                                    .map(CommentMapper::toDto)
                                    .toList()
                    );

                    return itemDto;
                })
                .toList();
    }

    @Override
    public Collection<ItemDto> search(String text) {

        if (text == null || text.isBlank()) {
            return Collections.emptyList();
        }

        String query = text.toLowerCase();

        return itemRepository.search(query)
                .stream()
                .map(ItemMapper::toDto)
                .toList();
    }
}