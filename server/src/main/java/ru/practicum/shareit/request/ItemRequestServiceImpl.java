package ru.practicum.shareit.request;

import lombok.RequiredArgsConstructor;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.user.UserRepository;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.item.ItemRepository;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.user.User;

import java.time.LocalDateTime;
import java.util.Collection;

@Service
@RequiredArgsConstructor
public class ItemRequestServiceImpl implements ItemRequestService {

    private final ItemRequestRepository requestRepository;
    private final UserRepository userRepository;
    private final ItemRepository itemRepository;

    @Override
    public ItemRequestDto create(Long userId,
                                 ItemRequestDto dto) {

        User user = userRepository.findById(userId)
                .orElseThrow(() ->
                        new NotFoundException("Пользователь не найден"));

        ItemRequest request = ItemRequest.builder()
                .description(dto.getDescription())
                .created(LocalDateTime.now())
                .requestor(user)
                .build();

        return ItemRequestMapper.toDto(
                requestRepository.save(request)
        );
    }

    @Override
    public Collection<ItemRequestDto> getOwnRequests(Long userId) {

        return requestRepository
                .findByRequestorIdOrderByCreatedDesc(userId)
                .stream()
                .map(request -> {

                    ItemRequestDto dto =
                            ItemRequestMapper.toDto(request);

                    dto.setItems(
                            itemRepository.findByRequestId(
                                            request.getId())
                                    .stream()
                                    .map(ItemRequestMapper::toItemResponseDto)
                                    .toList()
                    );

                    return dto;
                })
                .toList();
    }

    @Override
    public Collection<ItemRequestDto> getAllRequests(Long userId) {

        return requestRepository
                .findByRequestorIdNotOrderByCreatedDesc(userId)
                .stream()
                .map(request -> {

                    ItemRequestDto dto =
                            ItemRequestMapper.toDto(request);

                    dto.setItems(
                            itemRepository.findByRequestId(
                                            request.getId())
                                    .stream()
                                    .map(ItemRequestMapper::toItemResponseDto)
                                    .toList()
                    );

                    return dto;
                })
                .toList();
    }

    @Override
    public ItemRequestDto getById(Long userId,
                                  Long requestId) {

        ItemRequest request = requestRepository.findById(requestId)
                .orElseThrow(() ->
                        new NotFoundException("Запрос не найден"));

        ItemRequestDto dto = ItemRequestMapper.toDto(request);

        dto.setItems(
                itemRepository.findByRequestId(requestId)
                        .stream()
                        .map(ItemRequestMapper::toItemResponseDto)
                        .toList()
        );

        return dto;
    }
}