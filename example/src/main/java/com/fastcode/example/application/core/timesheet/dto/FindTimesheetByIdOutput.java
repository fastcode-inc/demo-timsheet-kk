package com.fastcode.example.application.core.timesheet.dto;

import java.time.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FindTimesheetByIdOutput {

    private Long id;
    private String notes;
    private LocalDate periodendingdate;
    private LocalDate periodstartingdate;
    private Long timesheetstatusid;
    private String timesheetstatusDescriptiveField;
    private Long userid;
    private String usersDescriptiveField;
    private Long versiono;
}
