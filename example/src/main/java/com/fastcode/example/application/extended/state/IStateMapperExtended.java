package com.fastcode.example.application.extended.state;

import com.fastcode.example.application.core.state.IStateMapper;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface IStateMapperExtended extends IStateMapper {}
