package com.example.demo.controller;

import com.example.demo.service.MasterStationService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

@RequestMapping("station/")
@RestController
public class MasterStationController {
    @Autowired
    private MasterStationService stationMasterService;

    @GetMapping("getAll")
    public Map getAll(@RequestHeader Map<String,String> headerMap)
    {
        return stationMasterService.getAll(headerMap);
    }

    @GetMapping("getById/{id}")
    @Operation(summary = " ",
            description ="stationId")
    public Map getById(@PathVariable(required = false) String id,@RequestHeader Map<String,String> headerMap) {
        return stationMasterService.getById(id,headerMap);
    }

    @PostMapping("addUpdate")
    @Operation(summary = " ",
            description ="{\"sortingId\":1,\"code\":\"abc\",\"name\":\"xyz\",\"status\":\"1\",\"createdBy\":\"Admin\"}")
    public Map addUpdate(@RequestBody(required = false) String inputEncryptedString,@RequestHeader Map<String,String> headerMap){
        return stationMasterService.addUpdate(inputEncryptedString,headerMap);
    }

    @PostMapping("actions")
    @Operation(summary = " ",
            description ="{\"ids\":[\"ids\"],\"status\":\"0\"}")
    public Map actions(@RequestBody(required = false) String inputEncryptedString,@RequestHeader Map<String,String> headerMap){
        return stationMasterService.actions(inputEncryptedString,headerMap);
    }

    @PostMapping("addBulkData")
    @Operation(summary = "Add or update stations",
            description = "{[{\"code\":\"1\",\"name\":\"abc\",\"createdBy\":\"ZA\"},{\"code\":\"2\",\"name\":\"xyz\",\"createdBy\":\"US\"}]}")
    public Map addBulkData(@RequestBody(required = false) String  inputEncryptedString, @RequestHeader Map<String,String> headerMap){
        return stationMasterService.addBulkData(inputEncryptedString,headerMap);
    }



}
