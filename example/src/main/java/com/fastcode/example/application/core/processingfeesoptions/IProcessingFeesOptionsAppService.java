package com.fastcode.example.application.core.processingfeesoptions;

import com.fastcode.example.application.core.processingfeesoptions.dto.*;
import com.fastcode.example.commons.search.SearchCriteria;
import java.util.*;
import org.springframework.data.domain.Pageable;

public interface IProcessingFeesOptionsAppService {
    //CRUD Operations
    CreateProcessingFeesOptionsOutput create(CreateProcessingFeesOptionsInput processingfeesoptions);

    void delete(Long id);

    UpdateProcessingFeesOptionsOutput update(Long id, UpdateProcessingFeesOptionsInput input);

    FindProcessingFeesOptionsByIdOutput findById(Long id);
    List<FindProcessingFeesOptionsByIdOutput> find(SearchCriteria search, Pageable pageable) throws Exception;
    //Join Column Parsers
}
