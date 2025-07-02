package com.example.demo.model;

import lombok.Data;

import java.util.Map;

@Data
public class APIRequestValidation {

    private boolean isInvalid;
    private Map map;

}
