package ru.practicum.shareit.booking;

import ru.practicum.shareit.booking.dto.BookerDto;
import ru.practicum.shareit.booking.dto.ItemBookingDto;
import ru.practicum.shareit.booking.dto.BookingDto;

public class BookingMapper {

    public static BookingDto toDto(Booking booking) {

        return BookingDto.builder()
                .id(booking.getId())
                .start(booking.getStart())
                .end(booking.getEnd())
                .status(booking.getStatus())
                .booker(
                        BookerDto.builder()
                                .id(booking.getBooker().getId())
                                .build()
                )
                .item(
                        ItemBookingDto.builder()
                                .id(booking.getItem().getId())
                                .name(booking.getItem().getName())
                                .build()
                )
                .build();
    }
}