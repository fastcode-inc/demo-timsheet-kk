package com.fastcode.example.domain.core.state;

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
@Config(defaultVariableName = "stateEntity")
@Table(name = "state")
@Getter
@Setter
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@NoArgsConstructor
@TypeDefs({})
public class State extends AbstractEntity {

    @Id
    @EqualsAndHashCode.Include
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Basic
    @Column(name = "name", nullable = true)
    private String name;
}
