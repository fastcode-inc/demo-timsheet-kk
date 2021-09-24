package com.fastcode.example.application.core.processingfeesoptions;

import com.fastcode.example.application.core.processingfeesoptions.dto.*;
import com.fastcode.example.commons.logging.LoggingHelper;
import com.fastcode.example.commons.search.*;
import com.fastcode.example.domain.core.processingfeesoptions.IProcessingFeesOptionsRepository;
import com.fastcode.example.domain.core.processingfeesoptions.ProcessingFeesOptions;
import com.fastcode.example.domain.core.processingfeesoptions.QProcessingFeesOptions;
import com.querydsl.core.BooleanBuilder;
import java.time.*;
import java.util.*;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service("processingFeesOptionsAppService")
@RequiredArgsConstructor
public class ProcessingFeesOptionsAppService implements IProcessingFeesOptionsAppService {

    @Qualifier("processingFeesOptionsRepository")
    @NonNull
    protected final IProcessingFeesOptionsRepository _processingFeesOptionsRepository;

    @Qualifier("IProcessingFeesOptionsMapperImpl")
    @NonNull
    protected final IProcessingFeesOptionsMapper mapper;

    @NonNull
    protected final LoggingHelper logHelper;

    @Transactional(propagation = Propagation.REQUIRED)
    public CreateProcessingFeesOptionsOutput create(CreateProcessingFeesOptionsInput input) {
        ProcessingFeesOptions processingFeesOptions = mapper.createProcessingFeesOptionsInputToProcessingFeesOptions(
            input
        );

        ProcessingFeesOptions createdProcessingFeesOptions = _processingFeesOptionsRepository.save(
            processingFeesOptions
        );
        return mapper.processingFeesOptionsToCreateProcessingFeesOptionsOutput(createdProcessingFeesOptions);
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public UpdateProcessingFeesOptionsOutput update(
        Long processingFeesOptionsId,
        UpdateProcessingFeesOptionsInput input
    ) {
        ProcessingFeesOptions existing = _processingFeesOptionsRepository.findById(processingFeesOptionsId).get();

        ProcessingFeesOptions processingFeesOptions = mapper.updateProcessingFeesOptionsInputToProcessingFeesOptions(
            input
        );

        ProcessingFeesOptions updatedProcessingFeesOptions = _processingFeesOptionsRepository.save(
            processingFeesOptions
        );
        return mapper.processingFeesOptionsToUpdateProcessingFeesOptionsOutput(updatedProcessingFeesOptions);
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public void delete(Long processingFeesOptionsId) {
        ProcessingFeesOptions existing = _processingFeesOptionsRepository
            .findById(processingFeesOptionsId)
            .orElse(null);

        _processingFeesOptionsRepository.delete(existing);
    }

    @Transactional(propagation = Propagation.NOT_SUPPORTED)
    public FindProcessingFeesOptionsByIdOutput findById(Long processingFeesOptionsId) {
        ProcessingFeesOptions foundProcessingFeesOptions = _processingFeesOptionsRepository
            .findById(processingFeesOptionsId)
            .orElse(null);
        if (foundProcessingFeesOptions == null) return null;

        return mapper.processingFeesOptionsToFindProcessingFeesOptionsByIdOutput(foundProcessingFeesOptions);
    }

    @Transactional(propagation = Propagation.NOT_SUPPORTED)
    public List<FindProcessingFeesOptionsByIdOutput> find(SearchCriteria search, Pageable pageable) throws Exception {
        Page<ProcessingFeesOptions> foundProcessingFeesOptions = _processingFeesOptionsRepository.findAll(
            search(search),
            pageable
        );
        List<ProcessingFeesOptions> processingFeesOptionsList = foundProcessingFeesOptions.getContent();
        Iterator<ProcessingFeesOptions> processingFeesOptionsIterator = processingFeesOptionsList.iterator();
        List<FindProcessingFeesOptionsByIdOutput> output = new ArrayList<>();

        while (processingFeesOptionsIterator.hasNext()) {
            ProcessingFeesOptions processingFeesOptions = processingFeesOptionsIterator.next();
            output.add(mapper.processingFeesOptionsToFindProcessingFeesOptionsByIdOutput(processingFeesOptions));
        }
        return output;
    }

    protected BooleanBuilder search(SearchCriteria search) throws Exception {
        QProcessingFeesOptions processingFeesOptions = QProcessingFeesOptions.processingFeesOptionsEntity;
        if (search != null) {
            Map<String, SearchFields> map = new HashMap<>();
            for (SearchFields fieldDetails : search.getFields()) {
                map.put(fieldDetails.getFieldName(), fieldDetails);
            }
            List<String> keysList = new ArrayList<String>(map.keySet());
            checkProperties(keysList);
            return searchKeyValuePair(processingFeesOptions, map, search.getJoinColumns());
        }
        return null;
    }

    protected void checkProperties(List<String> list) throws Exception {
        for (int i = 0; i < list.size(); i++) {
            if (
                !(
                    list.get(i).replace("%20", "").trim().equals("id") ||
                    list.get(i).replace("%20", "").trim().equals("name")
                )
            ) {
                throw new Exception("Wrong URL Format: Property " + list.get(i) + " not found!");
            }
        }
    }

    protected BooleanBuilder searchKeyValuePair(
        QProcessingFeesOptions processingFeesOptions,
        Map<String, SearchFields> map,
        Map<String, String> joinColumns
    ) {
        BooleanBuilder builder = new BooleanBuilder();

        for (Map.Entry<String, SearchFields> details : map.entrySet()) {
            if (details.getKey().replace("%20", "").trim().equals("id")) {
                if (details.getValue().getOperator().equals("contains")) {
                    builder.and(processingFeesOptions.id.like(details.getValue().getSearchValue() + "%"));
                } else if (
                    details.getValue().getOperator().equals("equals") &&
                    StringUtils.isNumeric(details.getValue().getSearchValue())
                ) {
                    builder.and(processingFeesOptions.id.eq(Long.valueOf(details.getValue().getSearchValue())));
                } else if (
                    details.getValue().getOperator().equals("notEqual") &&
                    StringUtils.isNumeric(details.getValue().getSearchValue())
                ) {
                    builder.and(processingFeesOptions.id.ne(Long.valueOf(details.getValue().getSearchValue())));
                } else if (details.getValue().getOperator().equals("range")) {
                    if (
                        StringUtils.isNumeric(details.getValue().getStartingValue()) &&
                        StringUtils.isNumeric(details.getValue().getEndingValue())
                    ) {
                        builder.and(
                            processingFeesOptions.id.between(
                                Long.valueOf(details.getValue().getStartingValue()),
                                Long.valueOf(details.getValue().getEndingValue())
                            )
                        );
                    } else if (StringUtils.isNumeric(details.getValue().getStartingValue())) {
                        builder.and(processingFeesOptions.id.goe(Long.valueOf(details.getValue().getStartingValue())));
                    } else if (StringUtils.isNumeric(details.getValue().getEndingValue())) {
                        builder.and(processingFeesOptions.id.loe(Long.valueOf(details.getValue().getEndingValue())));
                    }
                }
            }
            if (details.getKey().replace("%20", "").trim().equals("name")) {
                if (details.getValue().getOperator().equals("contains")) {
                    builder.and(
                        processingFeesOptions.name.likeIgnoreCase("%" + details.getValue().getSearchValue() + "%")
                    );
                } else if (details.getValue().getOperator().equals("equals")) {
                    builder.and(processingFeesOptions.name.eq(details.getValue().getSearchValue()));
                } else if (details.getValue().getOperator().equals("notEqual")) {
                    builder.and(processingFeesOptions.name.ne(details.getValue().getSearchValue()));
                }
            }
        }

        return builder;
    }
}
