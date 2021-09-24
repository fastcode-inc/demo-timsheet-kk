package com.fastcode.example.application.core.timesheetdetails.dto;

import java.math.BigDecimal;
import java.time.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateTimesheetdetailsOutput {

    private BigDecimal hours;
    private Long id;
    private String notes;
    private LocalDate workdate;
    private Long taskid;
    private String taskDescriptiveField;
    private Long timeofftypeid;
    private String timeofftypeDescriptiveField;
    private Long timesheetid;
    private Long timesheetDescriptiveField;
}
