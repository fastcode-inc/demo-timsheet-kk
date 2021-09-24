package com.fastcode.example.restcontrollers.extended;

import com.fastcode.example.application.extended.state.IStateAppServiceExtended;
import com.fastcode.example.commons.logging.LoggingHelper;
import com.fastcode.example.restcontrollers.core.StateController;
import org.springframework.core.env.Environment;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/state/extended")
public class StateControllerExtended extends StateController {

    public StateControllerExtended(
        IStateAppServiceExtended stateAppServiceExtended,
        LoggingHelper helper,
        Environment env
    ) {
        super(stateAppServiceExtended, helper, env);
    }
    //Add your custom code here

}
