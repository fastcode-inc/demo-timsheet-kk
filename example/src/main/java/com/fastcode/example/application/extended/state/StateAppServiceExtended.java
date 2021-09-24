package com.fastcode.example.application.extended.state;

import com.fastcode.example.application.core.state.StateAppService;
import com.fastcode.example.commons.logging.LoggingHelper;
import com.fastcode.example.domain.extended.state.IStateRepositoryExtended;
import org.springframework.stereotype.Service;

@Service("stateAppServiceExtended")
public class StateAppServiceExtended extends StateAppService implements IStateAppServiceExtended {

    public StateAppServiceExtended(
        IStateRepositoryExtended stateRepositoryExtended,
        IStateMapperExtended mapper,
        LoggingHelper logHelper
    ) {
        super(stateRepositoryExtended, mapper, logHelper);
    }
    //Add your custom code here

}
