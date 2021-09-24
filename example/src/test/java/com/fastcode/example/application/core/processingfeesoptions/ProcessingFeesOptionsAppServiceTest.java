package com.fastcode.example.application.core.processingfeesoptions;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.fastcode.example.application.core.processingfeesoptions.dto.*;
import com.fastcode.example.commons.logging.LoggingHelper;
import com.fastcode.example.commons.search.*;
import com.fastcode.example.domain.core.processingfeesoptions.*;
import com.fastcode.example.domain.core.processingfeesoptions.ProcessingFeesOptions;
import com.fastcode.example.domain.core.processingfeesoptions.QProcessingFeesOptions;
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
public class ProcessingFeesOptionsAppServiceTest {

    @InjectMocks
    @Spy
    protected ProcessingFeesOptionsAppService _appService;

    @Mock
    protected IProcessingFeesOptionsRepository _processingFeesOptionsRepository;

    @Mock
    protected IProcessingFeesOptionsMapper _mapper;

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
    public void findProcessingFeesOptionsById_IdIsNotNullAndIdDoesNotExist_ReturnNull() {
        Optional<ProcessingFeesOptions> nullOptional = Optional.ofNullable(null);
        Mockito.when(_processingFeesOptionsRepository.findById(anyLong())).thenReturn(nullOptional);
        Assertions.assertThat(_appService.findById(ID)).isEqualTo(null);
    }

    @Test
    public void findProcessingFeesOptionsById_IdIsNotNullAndIdExists_ReturnProcessingFeesOptions() {
        ProcessingFeesOptions processingFeesOptions = mock(ProcessingFeesOptions.class);
        Optional<ProcessingFeesOptions> processingFeesOptionsOptional = Optional.of(
            (ProcessingFeesOptions) processingFeesOptions
        );
        Mockito.when(_processingFeesOptionsRepository.findById(anyLong())).thenReturn(processingFeesOptionsOptional);

        Assertions
            .assertThat(_appService.findById(ID))
            .isEqualTo(_mapper.processingFeesOptionsToFindProcessingFeesOptionsByIdOutput(processingFeesOptions));
    }

    @Test
    public void createProcessingFeesOptions_ProcessingFeesOptionsIsNotNullAndProcessingFeesOptionsDoesNotExist_StoreProcessingFeesOptions() {
        ProcessingFeesOptions processingFeesOptionsEntity = mock(ProcessingFeesOptions.class);
        CreateProcessingFeesOptionsInput processingFeesOptionsInput = new CreateProcessingFeesOptionsInput();

        Mockito
            .when(
                _mapper.createProcessingFeesOptionsInputToProcessingFeesOptions(
                    any(CreateProcessingFeesOptionsInput.class)
                )
            )
            .thenReturn(processingFeesOptionsEntity);
        Mockito
            .when(_processingFeesOptionsRepository.save(any(ProcessingFeesOptions.class)))
            .thenReturn(processingFeesOptionsEntity);

        Assertions
            .assertThat(_appService.create(processingFeesOptionsInput))
            .isEqualTo(_mapper.processingFeesOptionsToCreateProcessingFeesOptionsOutput(processingFeesOptionsEntity));
    }

    @Test
    public void updateProcessingFeesOptions_ProcessingFeesOptionsIdIsNotNullAndIdExists_ReturnUpdatedProcessingFeesOptions() {
        ProcessingFeesOptions processingFeesOptionsEntity = mock(ProcessingFeesOptions.class);
        UpdateProcessingFeesOptionsInput processingFeesOptions = mock(UpdateProcessingFeesOptionsInput.class);

        Optional<ProcessingFeesOptions> processingFeesOptionsOptional = Optional.of(
            (ProcessingFeesOptions) processingFeesOptionsEntity
        );
        Mockito.when(_processingFeesOptionsRepository.findById(anyLong())).thenReturn(processingFeesOptionsOptional);

        Mockito
            .when(
                _mapper.updateProcessingFeesOptionsInputToProcessingFeesOptions(
                    any(UpdateProcessingFeesOptionsInput.class)
                )
            )
            .thenReturn(processingFeesOptionsEntity);
        Mockito
            .when(_processingFeesOptionsRepository.save(any(ProcessingFeesOptions.class)))
            .thenReturn(processingFeesOptionsEntity);
        Assertions
            .assertThat(_appService.update(ID, processingFeesOptions))
            .isEqualTo(_mapper.processingFeesOptionsToUpdateProcessingFeesOptionsOutput(processingFeesOptionsEntity));
    }

    @Test
    public void deleteProcessingFeesOptions_ProcessingFeesOptionsIsNotNullAndProcessingFeesOptionsExists_ProcessingFeesOptionsRemoved() {
        ProcessingFeesOptions processingFeesOptions = mock(ProcessingFeesOptions.class);
        Optional<ProcessingFeesOptions> processingFeesOptionsOptional = Optional.of(
            (ProcessingFeesOptions) processingFeesOptions
        );
        Mockito.when(_processingFeesOptionsRepository.findById(anyLong())).thenReturn(processingFeesOptionsOptional);

        _appService.delete(ID);
        verify(_processingFeesOptionsRepository).delete(processingFeesOptions);
    }

    @Test
    public void find_ListIsEmpty_ReturnList() throws Exception {
        List<ProcessingFeesOptions> list = new ArrayList<>();
        Page<ProcessingFeesOptions> foundPage = new PageImpl(list);
        Pageable pageable = mock(Pageable.class);
        List<FindProcessingFeesOptionsByIdOutput> output = new ArrayList<>();
        SearchCriteria search = new SearchCriteria();

        Mockito.when(_appService.search(any(SearchCriteria.class))).thenReturn(new BooleanBuilder());
        Mockito
            .when(_processingFeesOptionsRepository.findAll(any(Predicate.class), any(Pageable.class)))
            .thenReturn(foundPage);
        Assertions.assertThat(_appService.find(search, pageable)).isEqualTo(output);
    }

    @Test
    public void find_ListIsNotEmpty_ReturnList() throws Exception {
        List<ProcessingFeesOptions> list = new ArrayList<>();
        ProcessingFeesOptions processingFeesOptions = mock(ProcessingFeesOptions.class);
        list.add(processingFeesOptions);
        Page<ProcessingFeesOptions> foundPage = new PageImpl(list);
        Pageable pageable = mock(Pageable.class);
        List<FindProcessingFeesOptionsByIdOutput> output = new ArrayList<>();
        SearchCriteria search = new SearchCriteria();

        output.add(_mapper.processingFeesOptionsToFindProcessingFeesOptionsByIdOutput(processingFeesOptions));

        Mockito.when(_appService.search(any(SearchCriteria.class))).thenReturn(new BooleanBuilder());
        Mockito
            .when(_processingFeesOptionsRepository.findAll(any(Predicate.class), any(Pageable.class)))
            .thenReturn(foundPage);
        Assertions.assertThat(_appService.find(search, pageable)).isEqualTo(output);
    }

    @Test
    public void searchKeyValuePair_PropertyExists_ReturnBooleanBuilder() {
        QProcessingFeesOptions processingFeesOptions = QProcessingFeesOptions.processingFeesOptionsEntity;
        SearchFields searchFields = new SearchFields();
        searchFields.setOperator("equals");
        searchFields.setSearchValue("xyz");
        Map<String, SearchFields> map = new HashMap<>();
        map.put("name", searchFields);
        Map<String, String> searchMap = new HashMap<>();
        searchMap.put("xyz", String.valueOf(ID));
        BooleanBuilder builder = new BooleanBuilder();
        builder.and(processingFeesOptions.name.eq("xyz"));
        Assertions.assertThat(_appService.searchKeyValuePair(processingFeesOptions, map, searchMap)).isEqualTo(builder);
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
        QProcessingFeesOptions processingFeesOptions = QProcessingFeesOptions.processingFeesOptionsEntity;
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
        builder.or(processingFeesOptions.name.eq("xyz"));
        Mockito.doNothing().when(_appService).checkProperties(any(List.class));
        Mockito
            .doReturn(builder)
            .when(_appService)
            .searchKeyValuePair(any(QProcessingFeesOptions.class), any(HashMap.class), any(HashMap.class));

        Assertions.assertThat(_appService.search(search)).isEqualTo(builder);
    }

    @Test
    public void search_StringIsNull_ReturnNull() throws Exception {
        Assertions.assertThat(_appService.search(null)).isEqualTo(null);
    }
}
