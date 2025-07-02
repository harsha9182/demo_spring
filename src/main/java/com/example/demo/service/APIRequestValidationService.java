package com.example.demo.service;

import com.example.demo.model.APIRequestValidation;
import com.example.demo.utils.AppUtils;
import com.example.demo.utils.ControllerUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Optional;

@Service
public class APIRequestValidationService {

    @Autowired
    private ControllerUtils controllerUtils;
    @Autowired
    private AppUtils appUtils;



    public APIRequestValidation requestValidation(Map<String, String> headerMap){
        APIRequestValidation apiRequestValidation=new APIRequestValidation();

        String token=headerMap.get("authorization");
        String uuid=headerMap.get("uuid");

        if (appUtils.isNull(token)){
            apiRequestValidation.setMap(controllerUtils.failedTokenResponse("Invalid or Expired session. please login again"));
            apiRequestValidation.setInvalid(false);
        }else{

//            boolean isValid=jwtService.validateToken(token);

                apiRequestValidation.setInvalid(false);

        }
        return apiRequestValidation;
    }
}
