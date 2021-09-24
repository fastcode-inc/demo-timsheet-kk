package com.fastcode.example.application.core.processingfeesoptions.dto;

import java.time.*;
import javax.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdateProcessingFeesOptionsInput {

    @NotNull(message = "id Should not be null")
    private Long id;

    private String name;

    private Long versiono;
}
