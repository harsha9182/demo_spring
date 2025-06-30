package com.example.demo.table;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Data
@Entity
@Table(name = "master_station")
public class MasterStation {

    @Id
    @Column(length = 50)
    private String uuid;

    @Column(length = 50)
    private int sortingId;

    @Column(length = 50)
    private String divId;

    @Column(length = 50, unique = true)
    private String code;

    @Column(length = 250, unique = true)
    private String name;

    @Column(length = 50)
    private String createdDate;

    @Column(length = 50)
    private String createdBy;

    @Column(length =5)
    private int status;

}
