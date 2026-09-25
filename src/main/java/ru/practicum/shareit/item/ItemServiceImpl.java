package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
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

    @Override
    public ItemDto create(Long userId, ItemDto dto) {

        User user = userRepository.findById(userId);

        if (user == null) {
            throw new NotFoundException("Пользователь не найден");
        }

        Item item = Item.builder()
                .name(dto.getName())
                .description(dto.getDescription())
                .available(dto.getAvailable())
                .ownerId(userId)
                .build();

        item = itemRepository.save(item);

        return ItemMapper.toDto(item);
    }

    @Override
    public ItemDto update(Long userId, Long itemId, ItemDto itemDto) {

        Item item = itemRepository.findById(itemId);

        if (item == null) {
            throw new NotFoundException("Вещь не найдена");
        }

        if (!item.getOwnerId().equals(userId)) {
            throw new NotFoundException("Вещь принадлежит другому пользователю");
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

        return ItemMapper.toDto(itemRepository.update(item));
    }

    @Override
    public ItemDto getById(Long itemId) {

        Item item = itemRepository.findById(itemId);

        if (item == null) {
            throw new NotFoundException("Вещь не найдена");
        }

        return ItemMapper.toDto(item);
    }

    @Override
    public Collection<ItemDto> getOwnerItems(Long userId) {

        return itemRepository.findAll()
                .stream()
                .filter(item -> item.getOwnerId().equals(userId))
                .map(ItemMapper::toDto)
                .toList();
    }

    @Override
    public Collection<ItemDto> search(String text) {

        if (text == null || text.isBlank()) {
            return Collections.emptyList();
        }

        String query = text.toLowerCase();

        return itemRepository.findAll()
                .stream()
                .filter(Item::getAvailable)
                .filter(item ->
                        item.getName().toLowerCase().contains(query)
                                || item.getDescription().toLowerCase().contains(query))
                .map(ItemMapper::toDto)
                .toList();
    }
}
