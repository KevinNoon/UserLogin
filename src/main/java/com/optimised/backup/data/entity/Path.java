package com.optimised.backup.data.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
@Table
public class Path {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String name;
    private String path_value;
    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }
}
