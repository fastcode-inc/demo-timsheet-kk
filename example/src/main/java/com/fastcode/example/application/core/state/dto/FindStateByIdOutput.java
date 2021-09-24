package com.fastcode.example.application.core.state.dto;

import java.time.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FindStateByIdOutput {

    private Long id;
    private String name;
    private Long versiono;
}
