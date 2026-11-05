package ru.practicum.shareit.request.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

<<<<<<< HEAD
=======
import javax.validation.constraints.NotBlank;
>>>>>>> 68e9c73 (Fix add item requests and gateway)
import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ItemRequestDto {

    private Long id;

<<<<<<< HEAD
=======
    @NotBlank
>>>>>>> 68e9c73 (Fix add item requests and gateway)
    private String description;

    private LocalDateTime created;

    private List<ItemResponseDto> items;
}