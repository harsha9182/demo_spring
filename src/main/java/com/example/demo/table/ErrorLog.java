package com.example.demo.table;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Data
@Entity
@Table(name = "error_logs")
public class ErrorLog {

    @Id
    @Column(length = 50)
    private String uuid;

    @Column(length = 250)
    private String input;

    @Column(length = 50)
    private String divId;

    @Column(length = 250)
    private String output;

    @Column(length = 50)
    private String companyId;

    @Column(length = 250)
    private String methodName;

    @Column(length = 250)
    private String deviceType;

    @Column(columnDefinition = "TEXT")
    private String errorDes;

    @Column(length = 20)
    private String createdDate;

    @Column(length = 50)
    private String createdBy;

}
