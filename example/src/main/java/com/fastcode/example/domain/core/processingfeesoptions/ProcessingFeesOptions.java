package com.fastcode.example.domain.core.processingfeesoptions;

import com.fastcode.example.domain.core.abstractentity.AbstractEntity;
import com.querydsl.core.annotations.Config;
import java.time.*;
import javax.persistence.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.TypeDefs;

@Entity
@Config(defaultVariableName = "processingFeesOptionsEntity")
@Table(name = "processing_fees_options")
@Getter
@Setter
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@NoArgsConstructor
@TypeDefs({})
public class ProcessingFeesOptions extends AbstractEntity {

    @Id
    @EqualsAndHashCode.Include
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Basic
    @Column(name = "name", nullable = true)
    private String name;
}
