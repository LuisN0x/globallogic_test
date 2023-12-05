package com.desafiobci.userapi.models;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.HashMap;
import java.util.Map;

public class ResponseHandler {

    public static ResponseEntity<Object> generateResponse(String message, HttpStatus status,
                                                          String nameObj ,Object responseObj) {
        Map<String, Object> res = new HashMap<String, Object>();
        res.put("message",message);
        res.put("status",status.value());
        res.put(nameObj,responseObj);

        return new ResponseEntity<Object>(res,status);
    }
}
