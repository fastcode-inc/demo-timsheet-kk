package com.fastcode.example.domain.extended.processingfeesoptions;

import com.fastcode.example.domain.core.processingfeesoptions.IProcessingFeesOptionsRepository;
import org.javers.spring.annotation.JaversSpringDataAuditable;
import org.springframework.stereotype.Repository;

@JaversSpringDataAuditable
@Repository("processingFeesOptionsRepositoryExtended")
public interface IProcessingFeesOptionsRepositoryExtended extends IProcessingFeesOptionsRepository {
    //Add your custom code here
}
