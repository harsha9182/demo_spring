package com.example.demo.controller;


import com.example.demo.service.LocalStorageService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

@RestController
@RequestMapping("local")
public class LocalStorageController {

    @Autowired
    private LocalStorageService localStorageService;
    @PostMapping(value = "uploadFile", consumes = "multipart/form-data")
    public Map localFileUpload(@RequestParam(name = "file", required = false) MultipartFile multiPartFile, @RequestHeader Map<String, String> headerMap, HttpServletRequest request) {

        return  localStorageService.localFileUpload(multiPartFile,headerMap,request);
    }

    @DeleteMapping("/deleteFile")
    public Map deleteFile(@RequestParam String url,@RequestHeader Map<String, String> headerMap) {
      return localStorageService.deleteFile(url,headerMap);
    }

    @GetMapping("getFilePathInSystem")
    public String getFilePathInSystem(){
        return localStorageService.getFilePathInSystem();
    }

    @PostMapping(value = "uploadFileWithFolder", consumes = "multipart/form-data")
    public Map<String, Object> uploadFileFolderWithParams(
            @RequestParam(name = "file", required = false) MultipartFile multiPartFile,
            @RequestHeader Map<String, String> headerMap,
            HttpServletRequest request,
            @RequestParam(name = "folderName", required = false) String folderName) {

        return localStorageService.localFileUploadFolder(multiPartFile, headerMap, request, folderName);
    }

}


