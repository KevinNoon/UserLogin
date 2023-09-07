package com.optimised.backup.data.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalTime;

@Entity
@Data
@Table
public class Settings {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    Boolean autoBackup;
    LocalTime backupTime;
}
