package ru.yandex.practicum.filmorate.model;

import jakarta.validation.constraints.*;
import lombok.Data;

import java.time.LocalDate;

@Data
public class User {
    @Positive
    private Long id;

    @Email
    private String email;

    @NotNull
    @NotBlank
    private String login;

    private String name;

    @PastOrPresent
    private LocalDate birthday;
}
