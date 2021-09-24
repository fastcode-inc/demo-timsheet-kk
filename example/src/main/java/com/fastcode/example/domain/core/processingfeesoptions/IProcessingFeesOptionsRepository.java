package com.fastcode.example.domain.core.processingfeesoptions;

import java.time.*;
import java.util.*;
import org.javers.spring.annotation.JaversSpringDataAuditable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.stereotype.Repository;

@JaversSpringDataAuditable
@Repository("processingFeesOptionsRepository")
public interface IProcessingFeesOptionsRepository
    extends JpaRepository<ProcessingFeesOptions, Long>, QuerydslPredicateExecutor<ProcessingFeesOptions> {}
