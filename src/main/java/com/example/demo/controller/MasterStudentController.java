package com.example.demo.controller;

import com.example.demo.service.MasterStationService;
import com.example.demo.service.MasterStudentService;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
@RequestMapping("masterStudent/")
@RestController
public class MasterStudentController {

    @Autowired
    private MasterStudentService masterStudentService;

    @GetMapping("getAll")
    public Map getAll(@RequestHeader Map<String,String> headerMap)
    {
        return masterStudentService.getAll(headerMap);
    }

    @GetMapping("getById/{id}")
    @Operation(summary = " ",
            description ="stationId")
    public Map getById(@PathVariable(required = false) String id, @RequestHeader Map<String,String> headerMap) {
        return masterStudentService.getById(id,headerMap);
    }

    @PostMapping("addUpdate")
    @Operation(summary = " ",
            description ="{\"zone\":1,\"code\":\"abc\",\"name\":\"xyz\",\"status\":\"1\",\"createdBy\":\"Admin\"}")
    public Map addUpdate(@RequestBody(required = false) String inputEncryptedString, @RequestHeader Map<String,String> headerMap){
        return masterStudentService.addUpdate(inputEncryptedString,headerMap);
    }

    @PostMapping("actions")
    @Operation(summary = " ",
            description ="{\"ids\":[\"ids\"],\"status\":\"0\"}")
    public Map actions(@RequestBody(required = false) String inputEncryptedString,@RequestHeader Map<String,String> headerMap){
        return masterStudentService.actions(inputEncryptedString,headerMap);
    }

    @PostMapping("deleteByIds")
    @Operation(summary = " ",
            description = "[\"id2\",\"id2\"]")
    public Map deleteByIds(@RequestBody(required = false) String inputEncryptedString, @RequestHeader Map<String, String> headerMap) {
        return masterStudentService.deleteByIds(inputEncryptedString, headerMap);
    }

}
