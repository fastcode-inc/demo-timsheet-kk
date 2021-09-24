package com.fastcode.example.application.extended.processingfeesoptions;

import com.fastcode.example.application.core.processingfeesoptions.ProcessingFeesOptionsAppService;
import com.fastcode.example.commons.logging.LoggingHelper;
import com.fastcode.example.domain.extended.processingfeesoptions.IProcessingFeesOptionsRepositoryExtended;
import org.springframework.stereotype.Service;

@Service("processingFeesOptionsAppServiceExtended")
public class ProcessingFeesOptionsAppServiceExtended
    extends ProcessingFeesOptionsAppService
    implements IProcessingFeesOptionsAppServiceExtended {

    public ProcessingFeesOptionsAppServiceExtended(
        IProcessingFeesOptionsRepositoryExtended processingFeesOptionsRepositoryExtended,
        IProcessingFeesOptionsMapperExtended mapper,
        LoggingHelper logHelper
    ) {
        super(processingFeesOptionsRepositoryExtended, mapper, logHelper);
    }
    //Add your custom code here

}
