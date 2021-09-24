package com.fastcode.example.application.core.processingfeesoptions;

import com.fastcode.example.application.core.processingfeesoptions.dto.*;
import com.fastcode.example.domain.core.processingfeesoptions.ProcessingFeesOptions;
import java.time.*;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface IProcessingFeesOptionsMapper {
    ProcessingFeesOptions createProcessingFeesOptionsInputToProcessingFeesOptions(
        CreateProcessingFeesOptionsInput processingFeesOptionsDto
    );
    CreateProcessingFeesOptionsOutput processingFeesOptionsToCreateProcessingFeesOptionsOutput(
        ProcessingFeesOptions entity
    );

    ProcessingFeesOptions updateProcessingFeesOptionsInputToProcessingFeesOptions(
        UpdateProcessingFeesOptionsInput processingFeesOptionsDto
    );

    UpdateProcessingFeesOptionsOutput processingFeesOptionsToUpdateProcessingFeesOptionsOutput(
        ProcessingFeesOptions entity
    );
    FindProcessingFeesOptionsByIdOutput processingFeesOptionsToFindProcessingFeesOptionsByIdOutput(
        ProcessingFeesOptions entity
    );
}
