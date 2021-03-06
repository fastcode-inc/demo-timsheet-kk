package com.fastcode.example.application.core.project.dto;

import java.time.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FindProjectByIdOutput {

    private String description;
    private LocalDate enddate;
    private Long id;
    private String name;
    private LocalDate startdate;
    private Long customerid;
    private String customerDescriptiveField;
    private Long versiono;
}
