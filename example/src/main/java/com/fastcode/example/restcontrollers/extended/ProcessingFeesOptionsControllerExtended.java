package com.fastcode.example.restcontrollers.extended;

import com.fastcode.example.application.extended.processingfeesoptions.IProcessingFeesOptionsAppServiceExtended;
import com.fastcode.example.commons.logging.LoggingHelper;
import com.fastcode.example.restcontrollers.core.ProcessingFeesOptionsController;
import org.springframework.core.env.Environment;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/processingFeesOptions/extended")
public class ProcessingFeesOptionsControllerExtended extends ProcessingFeesOptionsController {

    public ProcessingFeesOptionsControllerExtended(
        IProcessingFeesOptionsAppServiceExtended processingFeesOptionsAppServiceExtended,
        LoggingHelper helper,
        Environment env
    ) {
        super(processingFeesOptionsAppServiceExtended, helper, env);
    }
    //Add your custom code here

}
