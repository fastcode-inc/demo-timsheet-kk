package com.fastcode.example.application.core.state;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.fastcode.example.application.core.state.dto.*;
import com.fastcode.example.commons.logging.LoggingHelper;
import com.fastcode.example.commons.search.*;
import com.fastcode.example.domain.core.state.*;
import com.fastcode.example.domain.core.state.QState;
import com.fastcode.example.domain.core.state.State;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Predicate;
import java.time.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import org.assertj.core.api.Assertions;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;
import org.slf4j.Logger;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
public class StateAppServiceTest {

    @InjectMocks
    @Spy
    protected StateAppService _appService;

    @Mock
    protected IStateRepository _stateRepository;

    @Mock
    protected IStateMapper _mapper;

    @Mock
    protected Logger loggerMock;

    @Mock
    protected LoggingHelper logHelper;

    protected static Long ID = 15L;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(_appService);
        when(logHelper.getLogger()).thenReturn(loggerMock);
        doNothing().when(loggerMock).error(anyString());
    }

    @Test
    public void findStateById_IdIsNotNullAndIdDoesNotExist_ReturnNull() {
        Optional<State> nullOptional = Optional.ofNullable(null);
        Mockito.when(_stateRepository.findById(anyLong())).thenReturn(nullOptional);
        Assertions.assertThat(_appService.findById(ID)).isEqualTo(null);
    }

    @Test
    public void findStateById_IdIsNotNullAndIdExists_ReturnState() {
        State state = mock(State.class);
        Optional<State> stateOptional = Optional.of((State) state);
        Mockito.when(_stateRepository.findById(anyLong())).thenReturn(stateOptional);

        Assertions.assertThat(_appService.findById(ID)).isEqualTo(_mapper.stateToFindStateByIdOutput(state));
    }

    @Test
    public void createState_StateIsNotNullAndStateDoesNotExist_StoreState() {
        State stateEntity = mock(State.class);
        CreateStateInput stateInput = new CreateStateInput();

        Mockito.when(_mapper.createStateInputToState(any(CreateStateInput.class))).thenReturn(stateEntity);
        Mockito.when(_stateRepository.save(any(State.class))).thenReturn(stateEntity);

        Assertions.assertThat(_appService.create(stateInput)).isEqualTo(_mapper.stateToCreateStateOutput(stateEntity));
    }

    @Test
    public void updateState_StateIdIsNotNullAndIdExists_ReturnUpdatedState() {
        State stateEntity = mock(State.class);
        UpdateStateInput state = mock(UpdateStateInput.class);

        Optional<State> stateOptional = Optional.of((State) stateEntity);
        Mockito.when(_stateRepository.findById(anyLong())).thenReturn(stateOptional);

        Mockito.when(_mapper.updateStateInputToState(any(UpdateStateInput.class))).thenReturn(stateEntity);
        Mockito.when(_stateRepository.save(any(State.class))).thenReturn(stateEntity);
        Assertions.assertThat(_appService.update(ID, state)).isEqualTo(_mapper.stateToUpdateStateOutput(stateEntity));
    }

    @Test
    public void deleteState_StateIsNotNullAndStateExists_StateRemoved() {
        State state = mock(State.class);
        Optional<State> stateOptional = Optional.of((State) state);
        Mockito.when(_stateRepository.findById(anyLong())).thenReturn(stateOptional);

        _appService.delete(ID);
        verify(_stateRepository).delete(state);
    }

    @Test
    public void find_ListIsEmpty_ReturnList() throws Exception {
        List<State> list = new ArrayList<>();
        Page<State> foundPage = new PageImpl(list);
        Pageable pageable = mock(Pageable.class);
        List<FindStateByIdOutput> output = new ArrayList<>();
        SearchCriteria search = new SearchCriteria();

        Mockito.when(_appService.search(any(SearchCriteria.class))).thenReturn(new BooleanBuilder());
        Mockito.when(_stateRepository.findAll(any(Predicate.class), any(Pageable.class))).thenReturn(foundPage);
        Assertions.assertThat(_appService.find(search, pageable)).isEqualTo(output);
    }

    @Test
    public void find_ListIsNotEmpty_ReturnList() throws Exception {
        List<State> list = new ArrayList<>();
        State state = mock(State.class);
        list.add(state);
        Page<State> foundPage = new PageImpl(list);
        Pageable pageable = mock(Pageable.class);
        List<FindStateByIdOutput> output = new ArrayList<>();
        SearchCriteria search = new SearchCriteria();

        output.add(_mapper.stateToFindStateByIdOutput(state));

        Mockito.when(_appService.search(any(SearchCriteria.class))).thenReturn(new BooleanBuilder());
        Mockito.when(_stateRepository.findAll(any(Predicate.class), any(Pageable.class))).thenReturn(foundPage);
        Assertions.assertThat(_appService.find(search, pageable)).isEqualTo(output);
    }

    @Test
    public void searchKeyValuePair_PropertyExists_ReturnBooleanBuilder() {
        QState state = QState.stateEntity;
        SearchFields searchFields = new SearchFields();
        searchFields.setOperator("equals");
        searchFields.setSearchValue("xyz");
        Map<String, SearchFields> map = new HashMap<>();
        map.put("name", searchFields);
        Map<String, String> searchMap = new HashMap<>();
        searchMap.put("xyz", String.valueOf(ID));
        BooleanBuilder builder = new BooleanBuilder();
        builder.and(state.name.eq("xyz"));
        Assertions.assertThat(_appService.searchKeyValuePair(state, map, searchMap)).isEqualTo(builder);
    }

    @Test(expected = Exception.class)
    public void checkProperties_PropertyDoesNotExist_ThrowException() throws Exception {
        List<String> list = new ArrayList<>();
        list.add("xyz");
        _appService.checkProperties(list);
    }

    @Test
    public void checkProperties_PropertyExists_ReturnNothing() throws Exception {
        List<String> list = new ArrayList<>();
        list.add("name");
        _appService.checkProperties(list);
    }

    @Test
    public void search_SearchIsNotNullAndSearchContainsCaseThree_ReturnBooleanBuilder() throws Exception {
        Map<String, SearchFields> map = new HashMap<>();
        QState state = QState.stateEntity;
        List<SearchFields> fieldsList = new ArrayList<>();
        SearchFields fields = new SearchFields();
        SearchCriteria search = new SearchCriteria();
        search.setType(3);
        search.setValue("xyz");
        search.setOperator("equals");
        fields.setFieldName("name");
        fields.setOperator("equals");
        fields.setSearchValue("xyz");
        fieldsList.add(fields);
        search.setFields(fieldsList);
        BooleanBuilder builder = new BooleanBuilder();
        builder.or(state.name.eq("xyz"));
        Mockito.doNothing().when(_appService).checkProperties(any(List.class));
        Mockito
            .doReturn(builder)
            .when(_appService)
            .searchKeyValuePair(any(QState.class), any(HashMap.class), any(HashMap.class));

        Assertions.assertThat(_appService.search(search)).isEqualTo(builder);
    }

    @Test
    public void search_StringIsNull_ReturnNull() throws Exception {
        Assertions.assertThat(_appService.search(null)).isEqualTo(null);
    }
}
