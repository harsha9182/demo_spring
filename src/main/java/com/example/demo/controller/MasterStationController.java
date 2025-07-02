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
    public Map getAll()
    {
        return stationMasterService.getAll();
    }

    @GetMapping("getById/{id}")
    @Operation(summary = " ",
            description ="stationId")
    public Map getById(@PathVariable(required = false) String id) {
        return stationMasterService.getById(id);
    }

    @PostMapping("addUpdate")
    @Operation(summary = " ",
            description ="{\"zone\":1,\"code\":\"abc\",\"name\":\"xyz\",\"status\":\"1\",\"createdBy\":\"Admin\"}")
    public Map addUpdate(@RequestBody(required = false) String inputEncryptedString){
        return stationMasterService.addUpdate(inputEncryptedString);
    }

    @PostMapping("actions")
    @Operation(summary = " ",
            description ="{\"ids\":[\"ids\"],\"status\":\"0\"}")
    public Map actions(@RequestBody(required = false) String inputEncryptedString){
        return stationMasterService.actions(inputEncryptedString);
    }

    @PostMapping("addBulkData")
    @Operation(summary = "Add or update stations",
            description = "{[{\"code\":\"1\",\"name\":\"abc\",\"createdBy\":\"ZA\"},{\"code\":\"2\",\"name\":\"xyz\",\"createdBy\":\"US\"}]}")
    public Map addBulkData(@RequestBody(required = false) String  inputEncryptedString){
        return stationMasterService.addBulkData(inputEncryptedString);
    }



}
