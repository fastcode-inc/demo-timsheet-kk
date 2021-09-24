package com.fastcode.example.domain.extended.state;

import com.fastcode.example.domain.core.state.IStateRepository;
import org.javers.spring.annotation.JaversSpringDataAuditable;
import org.springframework.stereotype.Repository;

@JaversSpringDataAuditable
@Repository("stateRepositoryExtended")
public interface IStateRepositoryExtended extends IStateRepository {
    //Add your custom code here
}
