package com.optimised.backup.data.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.util.Set;

@Entity
@Data
@Table
public class Engineer {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String forename;
    private String lastname;
    private String email;
    @OneToMany(mappedBy = "engineer", fetch = FetchType.EAGER)
    private Set<Site> site;

    public String getFullName() {
        return forename + " " + lastname;
    }
}
