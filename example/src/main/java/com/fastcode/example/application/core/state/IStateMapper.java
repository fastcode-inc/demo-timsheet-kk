package com.fastcode.example.application.core.state;

import com.fastcode.example.application.core.state.dto.*;
import com.fastcode.example.domain.core.state.State;
import java.time.*;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface IStateMapper {
    State createStateInputToState(CreateStateInput stateDto);
    CreateStateOutput stateToCreateStateOutput(State entity);

    State updateStateInputToState(UpdateStateInput stateDto);

    UpdateStateOutput stateToUpdateStateOutput(State entity);
    FindStateByIdOutput stateToFindStateByIdOutput(State entity);
}
