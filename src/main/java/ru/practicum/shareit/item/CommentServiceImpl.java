package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.booking.BookingRepository;
import ru.practicum.shareit.booking.BookingStatus;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.exception.ValidationException;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.UserRepository;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService {

    private final CommentRepository commentRepository;
    private final BookingRepository bookingRepository;
    private final UserRepository userRepository;
    private final ItemRepository itemRepository;

    @Override
    public CommentDto create(Long userId,
                             Long itemId,
                             CommentDto dto) {

        User user = userRepository.findById(userId)
                .orElseThrow(() ->
                        new NotFoundException("Пользователь не найден"));

        Item item = itemRepository.findById(itemId)
                .orElseThrow(() ->
                        new NotFoundException("Вещь не найдена"));

        boolean booked = bookingRepository
                .existsByBookerIdAndItemIdAndStatusAndEndBefore(
                        userId,
                        itemId,
                        BookingStatus.APPROVED,
                        LocalDateTime.now());

        if (!booked) {
            throw new ValidationException(
                    "Пользователь не арендовал вещь");
        }

        Comment comment = Comment.builder()
                .text(dto.getText())
                .author(user)
                .item(item)
                .created(LocalDateTime.now())
                .build();

        return CommentMapper.toDto(
                commentRepository.save(comment)
        );
    }

    @Override
    public List<CommentDto> getByItemId(Long itemId) {

        return commentRepository.findByItemId(itemId)
                .stream()
                .map(CommentMapper::toDto)
                .toList();
    }
}