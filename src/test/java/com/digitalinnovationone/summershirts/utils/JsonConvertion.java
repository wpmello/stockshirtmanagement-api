package com.digitalinnovationone.summershirts.utils;

import com.digitalinnovationone.summershirts.dto.ShirtDTO;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

/*
    It's makes the conversion from 'String' to 'Json'
 */
public class JsonConvertion {

    public static String asJsonString(ShirtDTO shirtDTO) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
            objectMapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
            objectMapper.registerModule(new JavaTimeModule());

            return objectMapper.writeValueAsString(shirtDTO);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
