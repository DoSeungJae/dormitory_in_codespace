package com.DormitoryBack.global;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

public class StrUtils {
    public static <T> String toJsonString(T c){
        ObjectMapper objectMapper=new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());

        String jsonString="";
        try {
            jsonString = objectMapper.writeValueAsString(c);
        }catch(JsonProcessingException e){
            e.printStackTrace();
        }
        return jsonString;

    }
}
