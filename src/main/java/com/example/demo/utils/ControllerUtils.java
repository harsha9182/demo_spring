package com.example.demo.utils;

import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class ControllerUtils {
    public void successMap(Map map, Object obj) {
        map.put("mssg", "Success");
        map.put("status", 1);
        map.put("content", obj);
    }

    public void failedMessageMap(Map map) {
        map.put("mssg", "Failed");
        map.put("status", 0);
        map.put("content", "[]");
    }

    public void failedMessageMap(Map map,String message) {
        map.put("message",message);
        map.put("status", 200);
        map.put("body",null);
    }


    public void successMapWithList(Map map, Object s) {
        List listContent = new ArrayList<>();
        listContent.add(s);
        map.put("mssg", "Success");
        map.put("status", 1);
        map.put("content", listContent);
    }

    public Map failedResponse(String mssg) {
        Map map = new HashMap<>();
        List listContent = new ArrayList<>();
        map.put("mssg", mssg);
        map.put("status", 0);
        map.put("content", listContent);
        return map;
    }

    public Map failedTokenResponse(String mssg) {
        Map map = new HashMap<>();
        List listContent = new ArrayList<>();
        map.put("mssg", mssg);
        map.put("status", 2);
        map.put("content", listContent);
        return map;
    }

    public Map successResponseList(Object obj) {
        Map map = new HashMap<>();

        map.put("mssg", "Success");
        map.put("status", 1);
        map.put("content", obj);

        return map;
    }
    public Map failureResponseList(Object obj) {
        Map map = new HashMap<>();

        map.put("mssg", "failed");
        map.put("status", 0);
        map.put("content", obj);

        return map;
    }
    public Map successResponse(Object obj) {
        Map map = new HashMap<>();

        List<Object> listObj = new ArrayList<>();
        listObj.add(obj);

        map.put("mssg", "Success");
        map.put("status", 1);
        map.put("content", listObj);

        return map;
    }
    public Map successResponse(Object obj,String mssg) {
        Map map = new HashMap<>();

        List<Object> listObj = new ArrayList<>();
        listObj.add(obj);

        map.put("mssg", mssg);
        map.put("status", 1);
        map.put("content", listObj);

        return map;
    }
    public Map successResponseEmptyData(String mssg) {
        Map map = new HashMap<>();
        List listContent = new ArrayList<>();
        map.put("mssg", mssg);
        map.put("status", 1);
        map.put("content", listContent);

        return map;
    }

}
