package com.fastcode.example.application.core.state;

import com.fastcode.example.application.core.state.dto.*;
import com.fastcode.example.commons.search.SearchCriteria;
import java.util.*;
import org.springframework.data.domain.Pageable;

public interface IStateAppService {
    //CRUD Operations
    CreateStateOutput create(CreateStateInput state);

    void delete(Long id);

    UpdateStateOutput update(Long id, UpdateStateInput input);

    FindStateByIdOutput findById(Long id);
    List<FindStateByIdOutput> find(SearchCriteria search, Pageable pageable) throws Exception;
    //Join Column Parsers
}
