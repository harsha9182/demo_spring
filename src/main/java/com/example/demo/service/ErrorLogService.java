package com.example.demo.service;



import com.example.demo.repository.ErrorLogRepository;
import com.example.demo.table.ErrorLog;
import com.example.demo.utils.AppUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ErrorLogService {

    @Autowired
    private ErrorLogRepository errorLogRepository;

    @Autowired
    private AppUtils appUtils;


    /**
     *
     * @param methodName
     * @param input
     * @param output
     * @param error
     * @param device
     */
    public void saveErrorLog(String methodName,String input,String output,String error,String device){

        try{
            ErrorLog errorLog=new ErrorLog();
            errorLog.setUuid(appUtils.generateUUID());
            errorLog.setErrorDes(error);
            errorLog.setInput(input);
            errorLog.setOutput(output);
            errorLog.setMethodName(methodName);
            errorLog.setDeviceType(device);
            errorLog.setCreatedDate(appUtils.getCurrentDateTime());
            errorLogRepository.save(errorLog);
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
