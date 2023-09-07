package com.optimised.backup.data.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Entity
@Data

@Table
public class Site {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private Integer siteNumber;
    private String name;
    private String directory;
    private String iDCode;
    private String telephone;
    private Integer remote;
    private Integer network;
    private LocalDateTime BackupTime;
    private Integer internet;
    private String ipAddr;
    private Integer port;
    private Integer bacNet;
    private Integer defaultType;
    private Boolean existing;
    private Integer storeNumber;
    @ManyToOne
    @JoinColumn(name = "engineer_id")
    private Engineer engineer;

    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }
}
