package com.fastcode.example.restcontrollers.core;

import com.fastcode.example.application.core.processingfeesoptions.IProcessingFeesOptionsAppService;
import com.fastcode.example.application.core.processingfeesoptions.dto.*;
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
@RequestMapping("/processingFeesOptions")
@RequiredArgsConstructor
public class ProcessingFeesOptionsController {

    @Qualifier("processingFeesOptionsAppService")
    @NonNull
    protected final IProcessingFeesOptionsAppService _processingFeesOptionsAppService;

    @NonNull
    protected final LoggingHelper logHelper;

    @NonNull
    protected final Environment env;

    @PreAuthorize("hasAnyAuthority('PROCESSINGFEESOPTIONSENTITY_CREATE')")
    @RequestMapping(method = RequestMethod.POST, consumes = { "application/json" }, produces = { "application/json" })
    public ResponseEntity<CreateProcessingFeesOptionsOutput> create(
        @RequestBody @Valid CreateProcessingFeesOptionsInput processingFeesOptions
    ) {
        CreateProcessingFeesOptionsOutput output = _processingFeesOptionsAppService.create(processingFeesOptions);
        return new ResponseEntity(output, HttpStatus.OK);
    }

    // ------------ Delete processingFeesOptions ------------
    @PreAuthorize("hasAnyAuthority('PROCESSINGFEESOPTIONSENTITY_DELETE')")
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE, consumes = { "application/json" })
    public void delete(@PathVariable String id) {
        FindProcessingFeesOptionsByIdOutput output = _processingFeesOptionsAppService.findById(Long.valueOf(id));
        Optional
            .ofNullable(output)
            .orElseThrow(
                () ->
                    new EntityNotFoundException(
                        String.format("There does not exist a processingFeesOptions with a id=%s", id)
                    )
            );

        _processingFeesOptionsAppService.delete(Long.valueOf(id));
    }

    // ------------ Update processingFeesOptions ------------
    @PreAuthorize("hasAnyAuthority('PROCESSINGFEESOPTIONSENTITY_UPDATE')")
    @RequestMapping(
        value = "/{id}",
        method = RequestMethod.PUT,
        consumes = { "application/json" },
        produces = { "application/json" }
    )
    public ResponseEntity<UpdateProcessingFeesOptionsOutput> update(
        @PathVariable String id,
        @RequestBody @Valid UpdateProcessingFeesOptionsInput processingFeesOptions
    ) {
        FindProcessingFeesOptionsByIdOutput currentProcessingFeesOptions = _processingFeesOptionsAppService.findById(
            Long.valueOf(id)
        );
        Optional
            .ofNullable(currentProcessingFeesOptions)
            .orElseThrow(
                () ->
                    new EntityNotFoundException(
                        String.format("Unable to update. ProcessingFeesOptions with id=%s not found.", id)
                    )
            );

        processingFeesOptions.setVersiono(currentProcessingFeesOptions.getVersiono());
        UpdateProcessingFeesOptionsOutput output = _processingFeesOptionsAppService.update(
            Long.valueOf(id),
            processingFeesOptions
        );
        return new ResponseEntity(output, HttpStatus.OK);
    }

    @PreAuthorize("hasAnyAuthority('PROCESSINGFEESOPTIONSENTITY_READ')")
    @RequestMapping(
        value = "/{id}",
        method = RequestMethod.GET,
        consumes = { "application/json" },
        produces = { "application/json" }
    )
    public ResponseEntity<FindProcessingFeesOptionsByIdOutput> findById(@PathVariable String id) {
        FindProcessingFeesOptionsByIdOutput output = _processingFeesOptionsAppService.findById(Long.valueOf(id));
        Optional.ofNullable(output).orElseThrow(() -> new EntityNotFoundException(String.format("Not found")));

        return new ResponseEntity(output, HttpStatus.OK);
    }

    @PreAuthorize("hasAnyAuthority('PROCESSINGFEESOPTIONSENTITY_READ')")
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

        return ResponseEntity.ok(_processingFeesOptionsAppService.find(searchCriteria, Pageable));
    }
}
