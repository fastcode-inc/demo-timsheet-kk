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
import com.fastcode.example.application.core.state.StateAppService;
import com.fastcode.example.application.core.state.dto.*;
import com.fastcode.example.commons.logging.LoggingHelper;
import com.fastcode.example.domain.core.*;
import com.fastcode.example.domain.core.state.IStateRepository;
import com.fastcode.example.domain.core.state.State;
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
public class StateControllerTest extends DatabaseContainerConfig {

    @Autowired
    protected SortHandlerMethodArgumentResolver sortArgumentResolver;

    @Autowired
    @Qualifier("stateRepository")
    protected IStateRepository state_repository;

    @SpyBean
    @Qualifier("stateAppService")
    protected StateAppService stateAppService;

    @SpyBean
    protected LoggingHelper logHelper;

    @SpyBean
    protected Environment env;

    @Mock
    protected Logger loggerMock;

    protected State state;

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
        em.createNativeQuery("truncate table timesheet.state CASCADE").executeUpdate();
        em.getTransaction().commit();
    }

    public State createEntity() {
        State stateEntity = new State();
        stateEntity.setId(1L);
        stateEntity.setName("1");
        stateEntity.setVersiono(0L);

        return stateEntity;
    }

    public CreateStateInput createStateInput() {
        CreateStateInput stateInput = new CreateStateInput();
        stateInput.setName("5");

        return stateInput;
    }

    public State createNewEntity() {
        State state = new State();
        state.setId(3L);
        state.setName("3");

        return state;
    }

    public State createUpdateEntity() {
        State state = new State();
        state.setId(4L);
        state.setName("4");

        return state;
    }

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);

        final StateController stateController = new StateController(stateAppService, logHelper, env);
        when(logHelper.getLogger()).thenReturn(loggerMock);
        doNothing().when(loggerMock).error(anyString());

        this.mvc =
            MockMvcBuilders
                .standaloneSetup(stateController)
                .setCustomArgumentResolvers(sortArgumentResolver)
                .setControllerAdvice()
                .build();
    }

    @Before
    public void initTest() {
        state = createEntity();
        List<State> list = state_repository.findAll();
        if (!list.contains(state)) {
            state = state_repository.save(state);
        }
    }

    @Test
    public void FindById_IdIsValid_ReturnStatusOk() throws Exception {
        mvc
            .perform(get("/state/" + state.getId() + "/").contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk());
    }

    @Test
    public void FindById_IdIsNotValid_ReturnStatusNotFound() {
        org.assertj.core.api.Assertions
            .assertThatThrownBy(
                () -> mvc.perform(get("/state/999").contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk())
            )
            .hasCause(new EntityNotFoundException("Not found"));
    }

    @Test
    public void CreateState_StateDoesNotExist_ReturnStatusOk() throws Exception {
        CreateStateInput stateInput = createStateInput();

        ObjectWriter ow = new ObjectMapper()
            .registerModule(new JavaTimeModule())
            .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
            .writer()
            .withDefaultPrettyPrinter();

        String json = ow.writeValueAsString(stateInput);

        mvc.perform(post("/state").contentType(MediaType.APPLICATION_JSON).content(json)).andExpect(status().isOk());
    }

    @Test
    public void DeleteState_IdIsNotValid_ThrowEntityNotFoundException() {
        doReturn(null).when(stateAppService).findById(999L);
        org.assertj.core.api.Assertions
            .assertThatThrownBy(
                () ->
                    mvc.perform(delete("/state/999").contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk())
            )
            .hasCause(new EntityNotFoundException("There does not exist a state with a id=999"));
    }

    @Test
    public void Delete_IdIsValid_ReturnStatusNoContent() throws Exception {
        State entity = createNewEntity();
        entity.setVersiono(0L);
        entity = state_repository.save(entity);

        FindStateByIdOutput output = new FindStateByIdOutput();
        output.setId(entity.getId());

        Mockito.doReturn(output).when(stateAppService).findById(entity.getId());

        //    Mockito.when(stateAppService.findById(entity.getId())).thenReturn(output);

        mvc
            .perform(delete("/state/" + entity.getId() + "/").contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());
    }

    @Test
    public void UpdateState_StateDoesNotExist_ReturnStatusNotFound() throws Exception {
        doReturn(null).when(stateAppService).findById(999L);

        UpdateStateInput state = new UpdateStateInput();
        state.setId(999L);
        state.setName("999");

        ObjectWriter ow = new ObjectMapper()
            .registerModule(new JavaTimeModule())
            .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
            .writer()
            .withDefaultPrettyPrinter();
        String json = ow.writeValueAsString(state);

        org.assertj.core.api.Assertions
            .assertThatThrownBy(
                () ->
                    mvc
                        .perform(put("/state/999").contentType(MediaType.APPLICATION_JSON).content(json))
                        .andExpect(status().isOk())
            )
            .hasCause(new EntityNotFoundException("Unable to update. State with id=999 not found."));
    }

    @Test
    public void UpdateState_StateExists_ReturnStatusOk() throws Exception {
        State entity = createUpdateEntity();
        entity.setVersiono(0L);

        entity = state_repository.save(entity);
        FindStateByIdOutput output = new FindStateByIdOutput();
        output.setId(entity.getId());
        output.setName(entity.getName());
        output.setVersiono(entity.getVersiono());

        Mockito.when(stateAppService.findById(entity.getId())).thenReturn(output);

        UpdateStateInput stateInput = new UpdateStateInput();
        stateInput.setId(entity.getId());

        ObjectWriter ow = new ObjectMapper()
            .registerModule(new JavaTimeModule())
            .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
            .writer()
            .withDefaultPrettyPrinter();
        String json = ow.writeValueAsString(stateInput);

        mvc
            .perform(put("/state/" + entity.getId() + "/").contentType(MediaType.APPLICATION_JSON).content(json))
            .andExpect(status().isOk());

        State de = createUpdateEntity();
        de.setId(entity.getId());
        state_repository.delete(de);
    }

    @Test
    public void FindAll_SearchIsNotNullAndPropertyIsValid_ReturnStatusOk() throws Exception {
        mvc
            .perform(get("/state?search=id[equals]=1&limit=10&offset=1").contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk());
    }

    @Test
    public void FindAll_SearchIsNotNullAndPropertyIsNotValid_ThrowException() throws Exception {
        org.assertj.core.api.Assertions
            .assertThatThrownBy(
                () ->
                    mvc
                        .perform(
                            get("/state?search=stateid[equals]=1&limit=10&offset=1")
                                .contentType(MediaType.APPLICATION_JSON)
                        )
                        .andExpect(status().isOk())
            )
            .hasCause(new Exception("Wrong URL Format: Property stateid not found!"));
    }
}
