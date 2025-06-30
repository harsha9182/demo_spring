package com.example.demo.service;


import com.example.demo.model.APIActionInput;
import com.example.demo.model.APIRequestValidation;
import com.example.demo.repository.MasterStationRepository;
import com.example.demo.table.MasterStation;
import com.example.demo.utils.AppUtils;
import com.example.demo.utils.ControllerUtils;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.OutputStream;
import java.util.*;


@Service
public class MasterStationService {
    @Autowired
    private MasterStationRepository stationMasterRepository;



    @Autowired
    private APIRequestValidationService apiRequestValidationService;

    @Autowired
    private ErrorLogService errorLogService;

    @Autowired
    private AppUtils appUtils;

    @Autowired
    private ControllerUtils controllerUtils;



    public Map getAll(Map<String, String> headerMap) {
            try {
                APIRequestValidation requestValidation=apiRequestValidationService.requestValidation(headerMap);

                if(requestValidation.isInvalid()){
                    return requestValidation.getMap();
                }
                List<MasterStation> allStationMaster=stationMasterRepository.findAllOrderByName();
                return controllerUtils.successResponseList(allStationMaster);
            }
            catch (Exception e){
                errorLogService.saveErrorLog("StationMasterService/getAll","","",e.getMessage(),"server");
                return controllerUtils.failedResponse(""+e.getMessage());
            }
    }

    public Map getById(String inputEncryptedId, Map<String, String> headerMap) {
        try{
            APIRequestValidation requestValidation=apiRequestValidationService.requestValidation(headerMap);
            if (requestValidation.isInvalid()) {

                return requestValidation.getMap();
            }

            String inputDecryptedId = appUtils.decryptedString(inputEncryptedId);

            if(appUtils.isNull(inputDecryptedId)){
                return controllerUtils.failedResponse("Invalid Id");
            }else {
                Optional<MasterStation> stationMasterId=stationMasterRepository.findById(inputDecryptedId);
                if(stationMasterId.isEmpty()){
                    return controllerUtils.failedResponse("No Data Found");
                }
                else {
                    return controllerUtils.successResponse(stationMasterId);
                }
            }

        }catch (Exception e) {

            errorLogService.saveErrorLog("StationMasterService/getById", inputEncryptedId, "", e.getMessage(), "server");

            return controllerUtils.failedResponse("" + e.getMessage());
        }
    }

    public Map addUpdate(String inputEncryptedString, Map<String, String> headerMap) {
        try{
            ObjectMapper objectMapper = new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES,
                 false);
            APIRequestValidation requestValidation = apiRequestValidationService.requestValidation(headerMap);

            if (requestValidation.isInvalid()) {
                return requestValidation.getMap();
            }

        //--encrypted to decrypted
            String inputDecryptedString = appUtils.decryptedString(inputEncryptedString);

            if(appUtils.isNull(inputDecryptedString)){
                return controllerUtils.failedResponse("Invalid Data");
            }
            MasterStation masterStation=objectMapper.readValue(inputDecryptedString, MasterStation.class);

            MasterStation masterStationExist =  stationMasterRepository.findByNameIgnoreCase(masterStation.getName());
//            MasterStation masterStationCodeExist = stationMasterRepository.findByCode(masterStation.getCode().trim());

            if(Objects.isNull(masterStation)){
                return controllerUtils.failedResponse("Invalid Object");
            } else if (appUtils.isNull(masterStation.getCode())) {
                return controllerUtils.failedResponse("Code is empty");
            }else if (appUtils.isNull(masterStation.getName())) {
                return controllerUtils.failedResponse("Name is empty");
            }


            if (masterStationExist==null){

                if (appUtils.isNull(masterStation.getUuid())) {
                    masterStation.setUuid(appUtils.generateUUID());
                }
                if (appUtils.isNull(masterStation.getCreatedDate())) {
                    masterStation.setCreatedDate(appUtils.getCurrentDateTime());
                }
                masterStation.setName(masterStation.getName().trim().toUpperCase());
                masterStation.setCode(masterStation.getCode().trim().toUpperCase());
                MasterStation masterStationObj = stationMasterRepository.save(masterStation);
                return controllerUtils.successResponse(masterStationObj);

            }else if(!appUtils.isNull(masterStation.getUuid()) && masterStation.getUuid().equals(masterStationExist.getUuid())){
                masterStation.setName(masterStation.getName().trim().toUpperCase());
                masterStation.setCode(masterStation.getCode().trim().toUpperCase());
                MasterStation masterStationObj = stationMasterRepository.save(masterStation);
                return controllerUtils.successResponse(masterStationObj);
            }else{
                return controllerUtils.failedResponse("Station name is existed");
            }

        }catch (Exception e) {
            String inputDecryptedString = appUtils.decryptedString(inputEncryptedString);
            errorLogService.saveErrorLog("StationMasterService/addUpdate", inputDecryptedString, "", e.getMessage(), "server");
            return controllerUtils.failedResponse("" + e.getMessage());
        }
    }

    public Map actions(String inputEncryptedString, Map<String, String> headerMap) {
        try {
        ObjectMapper objectMapper = new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES,
                false);

        //---request validation---
        APIRequestValidation requestValidation = apiRequestValidationService.requestValidation(headerMap);

        if (requestValidation.isInvalid()) {
            return requestValidation.getMap();
        }

        //--encrypted to decrypted
        String inputDecryptedString = appUtils.decryptedString(inputEncryptedString);

            if (appUtils.isNull(inputEncryptedString)) {
                return controllerUtils.failedResponse("Invalid Data");
            }

            APIActionInput apiActionInput = objectMapper.readValue(inputDecryptedString, APIActionInput.class);

            if (Objects.isNull(apiActionInput)) {
                return controllerUtils.failedResponse("Invalid object");
            } else if (apiActionInput.getIds() == null || apiActionInput.getIds().size() == 0) {
                return controllerUtils.failedResponse("Ids is empty");
            } else {

               stationMasterRepository.updateStatusForUuids(apiActionInput.getIds(), apiActionInput.getStatus());

                return controllerUtils.successResponseList(apiActionInput.getIds());
            }
        } catch (Exception e) {
            errorLogService.saveErrorLog("StationMasterService/actions", inputEncryptedString, "", e.getMessage(), "server");
            return controllerUtils.failedResponse("" + e.getMessage());
        }
    }


    public Map addBulkData( String inputEncryptedString , Map<String, String> headerMap) {
        try {
        ObjectMapper objectMapper = new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES,
                false);
        APIRequestValidation requestValidation = apiRequestValidationService.requestValidation(headerMap);

        if (requestValidation.isInvalid()) {
            return requestValidation.getMap();
        }
        String inputDecryptedString = appUtils.decryptedString(inputEncryptedString);

            List<MasterStation> stations=objectMapper.readValue(inputDecryptedString, new TypeReference<List<MasterStation>>(){});
            for (MasterStation masterStation : stations) {
                if (Objects.isNull(masterStation)) {
                    return controllerUtils.failedResponse("Invalid Object");
                } else if (appUtils.isNull(masterStation.getCode())) {
                    return controllerUtils.failedResponse("Code is empty");
                } else if (appUtils.isNull(masterStation.getName())) {
                    return controllerUtils.failedResponse("Name is empty");
                } else {
                    if (appUtils.isNull(masterStation.getUuid())) {
                        masterStation.setUuid(appUtils.generateUUID());
                    }
                    if (appUtils.isNull(masterStation.getCreatedDate())) {
                        masterStation.setCreatedDate(appUtils.getCurrentDateTime());
                    }
                }
                masterStation.setName(masterStation.getName().trim().toUpperCase());
                masterStation.setCode(masterStation.getCode().trim().toUpperCase());
                MasterStation masterStationObj=stationMasterRepository.save(masterStation);
            }
            return controllerUtils.successResponseList(stations);
        } catch (Exception e) {
            errorLogService.saveErrorLog("StationMasterService/addBulkData",inputEncryptedString, "", e.getMessage(), "server");
            return controllerUtils.failedResponse("" + e.getMessage());
        }
    }


}
