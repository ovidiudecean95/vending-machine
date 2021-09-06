package com.mvpmatch.vendingmachine.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class JsonUtils {

    @Autowired
    private ObjectMapper objectMapper;

    public <T> String toJson(T object) throws JsonProcessingException {
        return objectMapper.writeValueAsString(object);
    }

    public <T> T fromJson(String payload, Class<T> clazz) throws JsonProcessingException {
        return objectMapper.readValue(payload, clazz);
    }
}
