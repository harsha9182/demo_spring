package com.example.demo.table;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Data
@Entity
@Table(name = "master_student")
public class MasterStudent {
    @Id
    @Column(length = 50)
    private String uuid;

    @Column(length = 5)
    private int zone;

    @Column(length = 50)
    private String divId;

    @Column(length = 250)
    private String firstName;

    @Column(length = 250)
    private String lastName;

    @Column(length = 50)
    private String dob;

    @Column(length =5)
    private int gender;

    @Column(length = 50)
    private String phoneNo;

    @Column(length =5)
    private int nationality;

    @Column(length = 5)
    private int secEducation;


    @Column(length = 50)
    private String secHTNo;

    @Column(length = 50)
    private String secFromYear;

    @Column(length = 50)
    private String secToYear;

    @Column(length = 50)
    private String secMarks;

    @Column(length = 5)
    private int intEducation;


    @Column(length = 50)
    private String intHTNo;

    @Column(length = 50)
    private String intFromYear;

    @Column(length = 50)
    private String intToYear;

    @Column(length = 50)
    private String intMarks;

    @Column(length = 5)
    private int college;

    @Column(length = 5)
    private int course;

    @Column(columnDefinition = "TEXT")
    private String des;

    @Column(length = 200)
    private String fileUrl;

    @Column(length = 50)
    private String createdDate;

    @Column(length = 50)
    private String createdBy;


    @Column(length =5)
    private int status;
}
