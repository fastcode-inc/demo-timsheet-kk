package com.fastcode.example.restcontrollers.core;

import com.fastcode.example.application.core.state.IStateAppService;
import com.fastcode.example.application.core.state.dto.*;
import com.fastcode.example.commons.logging.LoggingHelper;
import com.fastcode.example.commons.search.OffsetBasedPageRequest;
import com.fastcode.example.commons.search.SearchCriteria;
import com.fastcode.example.commons.search.SearchUtils;
import java.time.*;
import java.util.*;
import javax.persistence.EntityNotFoundException;
import javax.validation.Valid;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.env.Environment;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/state")
@RequiredArgsConstructor
public class StateController {

    @Qualifier("stateAppService")
    @NonNull
    protected final IStateAppService _stateAppService;

    @NonNull
    protected final LoggingHelper logHelper;

    @NonNull
    protected final Environment env;

    @PreAuthorize("hasAnyAuthority('STATEENTITY_CREATE')")
    @RequestMapping(method = RequestMethod.POST, consumes = { "application/json" }, produces = { "application/json" })
    public ResponseEntity<CreateStateOutput> create(@RequestBody @Valid CreateStateInput state) {
        CreateStateOutput output = _stateAppService.create(state);
        return new ResponseEntity(output, HttpStatus.OK);
    }

    // ------------ Delete state ------------
    @PreAuthorize("hasAnyAuthority('STATEENTITY_DELETE')")
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE, consumes = { "application/json" })
    public void delete(@PathVariable String id) {
        FindStateByIdOutput output = _stateAppService.findById(Long.valueOf(id));
        Optional
            .ofNullable(output)
            .orElseThrow(
                () -> new EntityNotFoundException(String.format("There does not exist a state with a id=%s", id))
            );

        _stateAppService.delete(Long.valueOf(id));
    }

    // ------------ Update state ------------
    @PreAuthorize("hasAnyAuthority('STATEENTITY_UPDATE')")
    @RequestMapping(
        value = "/{id}",
        method = RequestMethod.PUT,
        consumes = { "application/json" },
        produces = { "application/json" }
    )
    public ResponseEntity<UpdateStateOutput> update(
        @PathVariable String id,
        @RequestBody @Valid UpdateStateInput state
    ) {
        FindStateByIdOutput currentState = _stateAppService.findById(Long.valueOf(id));
        Optional
            .ofNullable(currentState)
            .orElseThrow(
                () -> new EntityNotFoundException(String.format("Unable to update. State with id=%s not found.", id))
            );

        state.setVersiono(currentState.getVersiono());
        UpdateStateOutput output = _stateAppService.update(Long.valueOf(id), state);
        return new ResponseEntity(output, HttpStatus.OK);
    }

    @PreAuthorize("hasAnyAuthority('STATEENTITY_READ')")
    @RequestMapping(
        value = "/{id}",
        method = RequestMethod.GET,
        consumes = { "application/json" },
        produces = { "application/json" }
    )
    public ResponseEntity<FindStateByIdOutput> findById(@PathVariable String id) {
        FindStateByIdOutput output = _stateAppService.findById(Long.valueOf(id));
        Optional.ofNullable(output).orElseThrow(() -> new EntityNotFoundException(String.format("Not found")));

        return new ResponseEntity(output, HttpStatus.OK);
    }

    @PreAuthorize("hasAnyAuthority('STATEENTITY_READ')")
    @RequestMapping(method = RequestMethod.GET, consumes = { "application/json" }, produces = { "application/json" })
    public ResponseEntity find(
        @RequestParam(value = "search", required = false) String search,
        @RequestParam(value = "offset", required = false) String offset,
        @RequestParam(value = "limit", required = false) String limit,
        Sort sort
    )
        throws Exception {
        if (offset == null) {
            offset = env.getProperty("fastCode.offset.default");
        }
        if (limit == null) {
            limit = env.getProperty("fastCode.limit.default");
        }

        Pageable Pageable = new OffsetBasedPageRequest(Integer.parseInt(offset), Integer.parseInt(limit), sort);
        SearchCriteria searchCriteria = SearchUtils.generateSearchCriteriaObject(search);

        return ResponseEntity.ok(_stateAppService.find(searchCriteria, Pageable));
    }
}
