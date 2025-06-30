package com.example.demo.model;

import lombok.Data;

import java.util.List;

@Data
public class APIActionInput {
    private List<String> ids;
    private int status;
}
