package com.example.demo.service;

import com.example.demo.model.APIActionInput;
import com.example.demo.model.APIRequestValidation;
import com.example.demo.repository.MasterStudentRepository;
import com.example.demo.table.MasterStation;
import com.example.demo.table.MasterStudent;
import com.example.demo.utils.AppUtils;
import com.example.demo.utils.ControllerUtils;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

@Service
public class MasterStudentService {
    
    @Autowired
    private MasterStudentRepository masterStudentRepository;

    @Autowired
    private APIRequestValidationService apiRequestValidationService;

    @Autowired
    private ErrorLogService errorLogService;

    @Autowired
    private AppUtils appUtils;

    @Autowired
    private ControllerUtils controllerUtils;



    public Map getAll() {
        try {

            List<MasterStudent> masterStudentList=masterStudentRepository.findAll();
            return controllerUtils.successResponseList(masterStudentList);
        }
        catch (Exception e){
            errorLogService.saveErrorLog("MasterStudentService/getAll","","",e.getMessage(),"server");
            return controllerUtils.failedResponse(""+e.getMessage());
        }
    }

    public Map getById(String inputEncryptedId) {
        try{


            String inputDecryptedId = appUtils.decryptedString(inputEncryptedId);

            if(appUtils.isNull(inputDecryptedId)){
                return controllerUtils.failedResponse("Invalid Id");
            }else {
                Optional<MasterStudent> studentOptional=masterStudentRepository.findById(inputDecryptedId);
                if(studentOptional.isEmpty()){
                    return controllerUtils.failedResponse("No Data Found");
                }
                else {
                    return controllerUtils.successResponse(studentOptional);
                }
            }

        }catch (Exception e) {

            errorLogService.saveErrorLog("MasterStudentService/getById", inputEncryptedId, "", e.getMessage(), "server");

            return controllerUtils.failedResponse("" + e.getMessage());
        }
    }

    public Map addUpdate(String inputEncryptedString) {
        try{
            ObjectMapper objectMapper = new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES,
                    false);


            //--encrypted to decrypted
            String inputDecryptedString = appUtils.decryptedString(inputEncryptedString);

            if(appUtils.isNull(inputDecryptedString)){
                return controllerUtils.failedResponse("Invalid Data");
            }
            MasterStudent masterStudent=objectMapper.readValue(inputDecryptedString, MasterStudent.class);



            if(Objects.isNull(masterStudent)){
                return controllerUtils.failedResponse("Invalid Object");
            } else if (appUtils.isNull(masterStudent.getFirstName())) {
                return controllerUtils.failedResponse("First name is empty");
            }


                if (appUtils.isNull(masterStudent.getUuid())) {
                    masterStudent.setUuid(appUtils.generateUUID());
                }
                if (appUtils.isNull(masterStudent.getCreatedDate())) {
                    masterStudent.setCreatedDate(appUtils.getCurrentDateTime());
                }

            MasterStudent masterStudentObj = masterStudentRepository.save(masterStudent);
                return controllerUtils.successResponse(masterStudentObj);



        }catch (Exception e) {
            String inputDecryptedString = appUtils.decryptedString(inputEncryptedString);
            errorLogService.saveErrorLog("MasterStudentService/addUpdate", inputDecryptedString, "", e.getMessage(), "server");
            return controllerUtils.failedResponse("" + e.getMessage());
        }
    }

    public Map actions(String inputEncryptedString) {
        try {
            ObjectMapper objectMapper = new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES,
                    false);

            //---request validation---


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

                masterStudentRepository.updateStatusForUuids(apiActionInput.getIds(), apiActionInput.getStatus());

                return controllerUtils.successResponseList(apiActionInput.getIds());
            }
        } catch (Exception e) {
            errorLogService.saveErrorLog("MasterStudentService/actions", inputEncryptedString, "", e.getMessage(), "server");
            return controllerUtils.failedResponse("" + e.getMessage());
        }
    }


    public Map deleteByIds(String inputEncryptedString) {
        try {
            ObjectMapper objectMapper = new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES,
                    false);
            //---request validation---

            String inputDecryptedString = appUtils.decryptedString(inputEncryptedString);

            if (appUtils.isNull(inputEncryptedString)) {
                return controllerUtils.failedResponse("Invalid Data");
            }

            List<String> ides = objectMapper.readValue(inputDecryptedString, new TypeReference<List<String>>() {
            });
            masterStudentRepository.deleteAllByIds(ides);
            return controllerUtils.successResponseEmptyData("successfully deleted");
        } catch (Exception e) {
            errorLogService.saveErrorLog("MasterStudentService/deleteByIds", inputEncryptedString, "", e.getMessage(), "server");
            return controllerUtils.failedResponse("" + e.getMessage());
        }
    }

}
