package com.fastcode.example.application.core.customer.dto;

import java.time.*;
import javax.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

@Getter
@Setter
public class UpdateCustomerInput {

    @NotNull(message = "customerid Should not be null")
    private Long customerid;

    private String description;

    @NotNull(message = "isactive Should not be null")
    private Boolean isactive;

    @NotNull(message = "name Should not be null")
    @Length(max = 255, message = "name must be less than 255 characters")
    private String name;

    private Long versiono;
}