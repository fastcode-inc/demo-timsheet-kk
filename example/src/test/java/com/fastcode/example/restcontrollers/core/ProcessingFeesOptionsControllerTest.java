package com.fastcode.example.restcontrollers.core;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fastcode.example.DatabaseContainerConfig;
import com.fastcode.example.application.core.processingfeesoptions.ProcessingFeesOptionsAppService;
import com.fastcode.example.application.core.processingfeesoptions.dto.*;
import com.fastcode.example.commons.logging.LoggingHelper;
import com.fastcode.example.domain.core.*;
import com.fastcode.example.domain.core.processingfeesoptions.IProcessingFeesOptionsRepository;
import com.fastcode.example.domain.core.processingfeesoptions.ProcessingFeesOptions;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import java.math.BigDecimal;
import java.time.*;
import java.util.*;
import javax.annotation.PostConstruct;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityNotFoundException;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.core.env.Environment;
import org.springframework.data.web.SortHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, properties = "spring.profiles.active=test")
public class ProcessingFeesOptionsControllerTest extends DatabaseContainerConfig {

    @Autowired
    protected SortHandlerMethodArgumentResolver sortArgumentResolver;

    @Autowired
    @Qualifier("processingFeesOptionsRepository")
    protected IProcessingFeesOptionsRepository processingFeesOptions_repository;

    @SpyBean
    @Qualifier("processingFeesOptionsAppService")
    protected ProcessingFeesOptionsAppService processingFeesOptionsAppService;

    @SpyBean
    protected LoggingHelper logHelper;

    @SpyBean
    protected Environment env;

    @Mock
    protected Logger loggerMock;

    protected ProcessingFeesOptions processingFeesOptions;

    protected MockMvc mvc;

    @Autowired
    EntityManagerFactory emf;

    static EntityManagerFactory emfs;

    static int relationCount = 10;
    static int yearCount = 1971;
    static int dayCount = 10;
    private BigDecimal bigdec = new BigDecimal(1.2);

    @PostConstruct
    public void init() {
        emfs = emf;
    }

    @AfterClass
    public static void cleanup() {
        EntityManager em = emfs.createEntityManager();
        em.getTransaction().begin();
        em.createNativeQuery("truncate table timesheet.processing_fees_options CASCADE").executeUpdate();
        em.getTransaction().commit();
    }

    public ProcessingFeesOptions createEntity() {
        ProcessingFeesOptions processingFeesOptionsEntity = new ProcessingFeesOptions();
        processingFeesOptionsEntity.setId(1L);
        processingFeesOptionsEntity.setName("1");
        processingFeesOptionsEntity.setVersiono(0L);

        return processingFeesOptionsEntity;
    }

    public CreateProcessingFeesOptionsInput createProcessingFeesOptionsInput() {
        CreateProcessingFeesOptionsInput processingFeesOptionsInput = new CreateProcessingFeesOptionsInput();
        processingFeesOptionsInput.setName("5");

        return processingFeesOptionsInput;
    }

    public ProcessingFeesOptions createNewEntity() {
        ProcessingFeesOptions processingFeesOptions = new ProcessingFeesOptions();
        processingFeesOptions.setId(3L);
        processingFeesOptions.setName("3");

        return processingFeesOptions;
    }

    public ProcessingFeesOptions createUpdateEntity() {
        ProcessingFeesOptions processingFeesOptions = new ProcessingFeesOptions();
        processingFeesOptions.setId(4L);
        processingFeesOptions.setName("4");

        return processingFeesOptions;
    }

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);

        final ProcessingFeesOptionsController processingFeesOptionsController = new ProcessingFeesOptionsController(
            processingFeesOptionsAppService,
            logHelper,
            env
        );
        when(logHelper.getLogger()).thenReturn(loggerMock);
        doNothing().when(loggerMock).error(anyString());

        this.mvc =
            MockMvcBuilders
                .standaloneSetup(processingFeesOptionsController)
                .setCustomArgumentResolvers(sortArgumentResolver)
                .setControllerAdvice()
                .build();
    }

    @Before
    public void initTest() {
        processingFeesOptions = createEntity();
        List<ProcessingFeesOptions> list = processingFeesOptions_repository.findAll();
        if (!list.contains(processingFeesOptions)) {
            processingFeesOptions = processingFeesOptions_repository.save(processingFeesOptions);
        }
    }

    @Test
    public void FindById_IdIsValid_ReturnStatusOk() throws Exception {
        mvc
            .perform(
                get("/processingFeesOptions/" + processingFeesOptions.getId() + "/")
                    .contentType(MediaType.APPLICATION_JSON)
            )
            .andExpect(status().isOk());
    }

    @Test
    public void FindById_IdIsNotValid_ReturnStatusNotFound() {
        org.assertj.core.api.Assertions
            .assertThatThrownBy(
                () ->
                    mvc
                        .perform(get("/processingFeesOptions/999").contentType(MediaType.APPLICATION_JSON))
                        .andExpect(status().isOk())
            )
            .hasCause(new EntityNotFoundException("Not found"));
    }

    @Test
    public void CreateProcessingFeesOptions_ProcessingFeesOptionsDoesNotExist_ReturnStatusOk() throws Exception {
        CreateProcessingFeesOptionsInput processingFeesOptionsInput = createProcessingFeesOptionsInput();

        ObjectWriter ow = new ObjectMapper()
            .registerModule(new JavaTimeModule())
            .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
            .writer()
            .withDefaultPrettyPrinter();

        String json = ow.writeValueAsString(processingFeesOptionsInput);

        mvc
            .perform(post("/processingFeesOptions").contentType(MediaType.APPLICATION_JSON).content(json))
            .andExpect(status().isOk());
    }

    @Test
    public void DeleteProcessingFeesOptions_IdIsNotValid_ThrowEntityNotFoundException() {
        doReturn(null).when(processingFeesOptionsAppService).findById(999L);
        org.assertj.core.api.Assertions
            .assertThatThrownBy(
                () ->
                    mvc
                        .perform(delete("/processingFeesOptions/999").contentType(MediaType.APPLICATION_JSON))
                        .andExpect(status().isOk())
            )
            .hasCause(new EntityNotFoundException("There does not exist a processingFeesOptions with a id=999"));
    }

    @Test
    public void Delete_IdIsValid_ReturnStatusNoContent() throws Exception {
        ProcessingFeesOptions entity = createNewEntity();
        entity.setVersiono(0L);
        entity = processingFeesOptions_repository.save(entity);

        FindProcessingFeesOptionsByIdOutput output = new FindProcessingFeesOptionsByIdOutput();
        output.setId(entity.getId());

        Mockito.doReturn(output).when(processingFeesOptionsAppService).findById(entity.getId());

        //    Mockito.when(processingFeesOptionsAppService.findById(entity.getId())).thenReturn(output);

        mvc
            .perform(delete("/processingFeesOptions/" + entity.getId() + "/").contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());
    }

    @Test
    public void UpdateProcessingFeesOptions_ProcessingFeesOptionsDoesNotExist_ReturnStatusNotFound() throws Exception {
        doReturn(null).when(processingFeesOptionsAppService).findById(999L);

        UpdateProcessingFeesOptionsInput processingFeesOptions = new UpdateProcessingFeesOptionsInput();
        processingFeesOptions.setId(999L);
        processingFeesOptions.setName("999");

        ObjectWriter ow = new ObjectMapper()
            .registerModule(new JavaTimeModule())
            .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
            .writer()
            .withDefaultPrettyPrinter();
        String json = ow.writeValueAsString(processingFeesOptions);

        org.assertj.core.api.Assertions
            .assertThatThrownBy(
                () ->
                    mvc
                        .perform(
                            put("/processingFeesOptions/999").contentType(MediaType.APPLICATION_JSON).content(json)
                        )
                        .andExpect(status().isOk())
            )
            .hasCause(new EntityNotFoundException("Unable to update. ProcessingFeesOptions with id=999 not found."));
    }

    @Test
    public void UpdateProcessingFeesOptions_ProcessingFeesOptionsExists_ReturnStatusOk() throws Exception {
        ProcessingFeesOptions entity = createUpdateEntity();
        entity.setVersiono(0L);

        entity = processingFeesOptions_repository.save(entity);
        FindProcessingFeesOptionsByIdOutput output = new FindProcessingFeesOptionsByIdOutput();
        output.setId(entity.getId());
        output.setName(entity.getName());
        output.setVersiono(entity.getVersiono());

        Mockito.when(processingFeesOptionsAppService.findById(entity.getId())).thenReturn(output);

        UpdateProcessingFeesOptionsInput processingFeesOptionsInput = new UpdateProcessingFeesOptionsInput();
        processingFeesOptionsInput.setId(entity.getId());

        ObjectWriter ow = new ObjectMapper()
            .registerModule(new JavaTimeModule())
            .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
            .writer()
            .withDefaultPrettyPrinter();
        String json = ow.writeValueAsString(processingFeesOptionsInput);

        mvc
            .perform(
                put("/processingFeesOptions/" + entity.getId() + "/")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(json)
            )
            .andExpect(status().isOk());

        ProcessingFeesOptions de = createUpdateEntity();
        de.setId(entity.getId());
        processingFeesOptions_repository.delete(de);
    }

    @Test
    public void FindAll_SearchIsNotNullAndPropertyIsValid_ReturnStatusOk() throws Exception {
        mvc
            .perform(
                get("/processingFeesOptions?search=id[equals]=1&limit=10&offset=1")
                    .contentType(MediaType.APPLICATION_JSON)
            )
            .andExpect(status().isOk());
    }

    @Test
    public void FindAll_SearchIsNotNullAndPropertyIsNotValid_ThrowException() throws Exception {
        org.assertj.core.api.Assertions
            .assertThatThrownBy(
                () ->
                    mvc
                        .perform(
                            get("/processingFeesOptions?search=processingFeesOptionsid[equals]=1&limit=10&offset=1")
                                .contentType(MediaType.APPLICATION_JSON)
                        )
                        .andExpect(status().isOk())
            )
            .hasCause(new Exception("Wrong URL Format: Property processingFeesOptionsid not found!"));
    }
}
