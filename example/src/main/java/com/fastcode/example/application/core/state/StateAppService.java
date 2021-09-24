package com.fastcode.example.application.core.state;

import com.fastcode.example.application.core.state.dto.*;
import com.fastcode.example.commons.logging.LoggingHelper;
import com.fastcode.example.commons.search.*;
import com.fastcode.example.domain.core.state.IStateRepository;
import com.fastcode.example.domain.core.state.QState;
import com.fastcode.example.domain.core.state.State;
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

@Service("stateAppService")
@RequiredArgsConstructor
public class StateAppService implements IStateAppService {

    @Qualifier("stateRepository")
    @NonNull
    protected final IStateRepository _stateRepository;

    @Qualifier("IStateMapperImpl")
    @NonNull
    protected final IStateMapper mapper;

    @NonNull
    protected final LoggingHelper logHelper;

    @Transactional(propagation = Propagation.REQUIRED)
    public CreateStateOutput create(CreateStateInput input) {
        State state = mapper.createStateInputToState(input);

        State createdState = _stateRepository.save(state);
        return mapper.stateToCreateStateOutput(createdState);
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public UpdateStateOutput update(Long stateId, UpdateStateInput input) {
        State existing = _stateRepository.findById(stateId).get();

        State state = mapper.updateStateInputToState(input);

        State updatedState = _stateRepository.save(state);
        return mapper.stateToUpdateStateOutput(updatedState);
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public void delete(Long stateId) {
        State existing = _stateRepository.findById(stateId).orElse(null);

        _stateRepository.delete(existing);
    }

    @Transactional(propagation = Propagation.NOT_SUPPORTED)
    public FindStateByIdOutput findById(Long stateId) {
        State foundState = _stateRepository.findById(stateId).orElse(null);
        if (foundState == null) return null;

        return mapper.stateToFindStateByIdOutput(foundState);
    }

    @Transactional(propagation = Propagation.NOT_SUPPORTED)
    public List<FindStateByIdOutput> find(SearchCriteria search, Pageable pageable) throws Exception {
        Page<State> foundState = _stateRepository.findAll(search(search), pageable);
        List<State> stateList = foundState.getContent();
        Iterator<State> stateIterator = stateList.iterator();
        List<FindStateByIdOutput> output = new ArrayList<>();

        while (stateIterator.hasNext()) {
            State state = stateIterator.next();
            output.add(mapper.stateToFindStateByIdOutput(state));
        }
        return output;
    }

    protected BooleanBuilder search(SearchCriteria search) throws Exception {
        QState state = QState.stateEntity;
        if (search != null) {
            Map<String, SearchFields> map = new HashMap<>();
            for (SearchFields fieldDetails : search.getFields()) {
                map.put(fieldDetails.getFieldName(), fieldDetails);
            }
            List<String> keysList = new ArrayList<String>(map.keySet());
            checkProperties(keysList);
            return searchKeyValuePair(state, map, search.getJoinColumns());
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
        QState state,
        Map<String, SearchFields> map,
        Map<String, String> joinColumns
    ) {
        BooleanBuilder builder = new BooleanBuilder();

        for (Map.Entry<String, SearchFields> details : map.entrySet()) {
            if (details.getKey().replace("%20", "").trim().equals("id")) {
                if (details.getValue().getOperator().equals("contains")) {
                    builder.and(state.id.like(details.getValue().getSearchValue() + "%"));
                } else if (
                    details.getValue().getOperator().equals("equals") &&
                    StringUtils.isNumeric(details.getValue().getSearchValue())
                ) {
                    builder.and(state.id.eq(Long.valueOf(details.getValue().getSearchValue())));
                } else if (
                    details.getValue().getOperator().equals("notEqual") &&
                    StringUtils.isNumeric(details.getValue().getSearchValue())
                ) {
                    builder.and(state.id.ne(Long.valueOf(details.getValue().getSearchValue())));
                } else if (details.getValue().getOperator().equals("range")) {
                    if (
                        StringUtils.isNumeric(details.getValue().getStartingValue()) &&
                        StringUtils.isNumeric(details.getValue().getEndingValue())
                    ) {
                        builder.and(
                            state.id.between(
                                Long.valueOf(details.getValue().getStartingValue()),
                                Long.valueOf(details.getValue().getEndingValue())
                            )
                        );
                    } else if (StringUtils.isNumeric(details.getValue().getStartingValue())) {
                        builder.and(state.id.goe(Long.valueOf(details.getValue().getStartingValue())));
                    } else if (StringUtils.isNumeric(details.getValue().getEndingValue())) {
                        builder.and(state.id.loe(Long.valueOf(details.getValue().getEndingValue())));
                    }
                }
            }
            if (details.getKey().replace("%20", "").trim().equals("name")) {
                if (details.getValue().getOperator().equals("contains")) {
                    builder.and(state.name.likeIgnoreCase("%" + details.getValue().getSearchValue() + "%"));
                } else if (details.getValue().getOperator().equals("equals")) {
                    builder.and(state.name.eq(details.getValue().getSearchValue()));
                } else if (details.getValue().getOperator().equals("notEqual")) {
                    builder.and(state.name.ne(details.getValue().getSearchValue()));
                }
            }
        }

        return builder;
    }
}
