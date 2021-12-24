package com.dch.data.filter;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.time.LocalDateTime;

@Entity
@Table(name="test_entity")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TestEntity {

    @Id
    @Column(name="test_id")
    private String id;
    @Column(name="test_type")
    private String type;
    @Column(name="test_name")
    private String name;
    @Column(name="test_date")
    private LocalDateTime date;
    @Column(name="test_check")
    private Boolean check;

}
