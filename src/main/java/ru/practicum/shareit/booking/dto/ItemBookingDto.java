package ru.practicum.shareit.booking.dto;

import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ItemBookingDto {

    private Long id;

    private String name;
}