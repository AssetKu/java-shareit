package ru.practicum.shareit.booking;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.booking.dto.BookingCreateDto;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.exception.ForbiddenException;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.exception.ValidationException;
import ru.practicum.shareit.item.ItemRepository;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.UserRepository;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

@Service
@RequiredArgsConstructor
public class BookingServiceImpl implements BookingService {

    private final BookingRepository bookingRepository;
    private final UserRepository userRepository;
    private final ItemRepository itemRepository;

    @Override
    public BookingDto create(Long userId, BookingCreateDto dto) {

        User booker = userRepository.findById(userId)
                .orElseThrow(() ->
                        new NotFoundException("Пользователь не найден"));

        Item item = itemRepository.findById(dto.getItemId())
                .orElseThrow(() ->
                        new NotFoundException("Вещь не найдена"));

        if (item.getOwner().getId().equals(userId)) {
            throw new NotFoundException("Владелец не может бронировать свою вещь");
        }

        if (!item.getAvailable()) {
            throw new ValidationException("Вещь недоступна для бронирования");
        }

        if (dto.getStart() == null || dto.getEnd() == null) {
            throw new ValidationException("Дата начала и окончания обязательны");
        }

        if (!dto.getStart().isBefore(dto.getEnd())) {
            throw new ValidationException(
                    "Дата начала должна быть раньше даты окончания");
        }

        if (dto.getEnd().isBefore(LocalDateTime.now())) {
            throw new ValidationException(
                    "Дата окончания не может быть в прошлом");
        }

        Booking booking = Booking.builder()
                .start(dto.getStart())
                .end(dto.getEnd())
                .status(BookingStatus.WAITING)
                .item(item)
                .booker(booker)
                .build();

        Booking savedBooking = bookingRepository.save(booking);

        return BookingMapper.toDto(savedBooking);
    }

    @Override
    public BookingDto approve(Long userId,
                              Long bookingId,
                              Boolean approved) {

        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() ->
                        new NotFoundException("Бронирование не найдено"));

        if (!booking.getItem().getOwner().getId().equals(userId)) {
            throw new ForbiddenException(
                    "Подтверждать бронирование может только владелец вещи");
        }

        if (booking.getStatus() != BookingStatus.WAITING) {
            throw new ValidationException(
                    "Статус бронирования уже изменён");
        }

        booking.setStatus(
                approved
                        ? BookingStatus.APPROVED
                        : BookingStatus.REJECTED
        );

        return BookingMapper.toDto(
                bookingRepository.save(booking)
        );
    }

    @Override
    public BookingDto getById(Long userId,
                              Long bookingId) {

        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() ->
                        new NotFoundException("Бронирование не найдено"));

        boolean owner = booking.getItem()
                .getOwner()
                .getId()
                .equals(userId);

        boolean booker = booking.getBooker()
                .getId()
                .equals(userId);

        if (!owner && !booker) {
            throw new NotFoundException(
                    "Нет доступа к бронированию");
        }

        return BookingMapper.toDto(booking);
    }

    @Override
    public Collection<BookingDto> getBookings(Long userId,
                                              String state) {

        LocalDateTime now = LocalDateTime.now();

        List<Booking> bookings;

        switch (state.toUpperCase()) {

            case "CURRENT":
                bookings =
                        bookingRepository
                                .findByBookerIdAndStartBeforeAndEndAfterOrderByStartDesc(
                                        userId,
                                        now,
                                        now);
                break;

            case "PAST":
                bookings =
                        bookingRepository
                                .findByBookerIdAndEndBeforeOrderByStartDesc(
                                        userId,
                                        now);
                break;

            case "FUTURE":
                bookings =
                        bookingRepository
                                .findByBookerIdAndStartAfterOrderByStartDesc(
                                        userId,
                                        now);
                break;

            case "WAITING":
                bookings =
                        bookingRepository
                                .findByBookerIdAndStatusOrderByStartDesc(
                                        userId,
                                        BookingStatus.WAITING);
                break;

            case "REJECTED":
                bookings =
                        bookingRepository
                                .findByBookerIdAndStatusOrderByStartDesc(
                                        userId,
                                        BookingStatus.REJECTED);
                break;

            default:
                bookings =
                        bookingRepository
                                .findByBookerIdOrderByStartDesc(userId);
        }

        return bookings.stream()
                .map(BookingMapper::toDto)
                .toList();
    }

    @Override
    public Collection<BookingDto> getOwnerBookings(Long userId,
                                                   String state) {

        userRepository.findById(userId)
                .orElseThrow(() ->
                        new NotFoundException("Пользователь не найден"));

        LocalDateTime now = LocalDateTime.now();

        List<Booking> bookings;

        switch (state.toUpperCase()) {

            case "CURRENT":
                bookings =
                        bookingRepository
                                .findByItemOwnerIdAndStartBeforeAndEndAfterOrderByStartDesc(
                                        userId,
                                        now,
                                        now);
                break;

            case "PAST":
                bookings =
                        bookingRepository
                                .findByItemOwnerIdAndEndBeforeOrderByStartDesc(
                                        userId,
                                        now);
                break;

            case "FUTURE":
                bookings =
                        bookingRepository
                                .findByItemOwnerIdAndStartAfterOrderByStartDesc(
                                        userId,
                                        now);
                break;

            case "WAITING":
                bookings =
                        bookingRepository
                                .findByItemOwnerIdAndStatusOrderByStartDesc(
                                        userId,
                                        BookingStatus.WAITING);
                break;

            case "REJECTED":
                bookings =
                        bookingRepository
                                .findByItemOwnerIdAndStatusOrderByStartDesc(
                                        userId,
                                        BookingStatus.REJECTED);
                break;

            default:
                bookings =
                        bookingRepository
                                .findByItemOwnerIdOrderByStartDesc(userId);
        }

        return bookings.stream()
                .map(BookingMapper::toDto)
                .toList();
    }
}