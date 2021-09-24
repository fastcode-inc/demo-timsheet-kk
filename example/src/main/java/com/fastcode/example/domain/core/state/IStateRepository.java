package com.fastcode.example.domain.core.state;

import java.time.*;
import java.util.*;
import org.javers.spring.annotation.JaversSpringDataAuditable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.stereotype.Repository;

@JaversSpringDataAuditable
@Repository("stateRepository")
public interface IStateRepository extends JpaRepository<State, Long>, QuerydslPredicateExecutor<State> {}
