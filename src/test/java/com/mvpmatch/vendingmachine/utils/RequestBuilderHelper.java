package com.mvpmatch.vendingmachine.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.mvpmatch.vendingmachine.util.JsonUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

@Component
public class RequestBuilderHelper {

    @Autowired
    private JsonUtils jsonUtils;

    public MockHttpServletRequestBuilder postRequest(String apiEndpoint, Object body)
            throws JsonProcessingException {
        return MockMvcRequestBuilders
                .post(apiEndpoint)
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonUtils.toJson(body));
    }

    public MockHttpServletRequestBuilder putRequest(String apiEndpoint, Object body)
            throws JsonProcessingException {
        return MockMvcRequestBuilders
                .put(apiEndpoint)
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonUtils.toJson(body));
    }

}
