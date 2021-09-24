package com.fastcode.example.application.core.state.dto;

import java.time.*;
import javax.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdateStateInput {

    @NotNull(message = "id Should not be null")
    private Long id;

    private String name;

    private Long versiono;
}
