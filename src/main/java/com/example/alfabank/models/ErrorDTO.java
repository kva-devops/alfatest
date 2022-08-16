package com.example.alfabank.models;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.HashMap;

/**
 * DTO-class for correctly displaying errors that occur
 */
@Getter
@Setter
@AllArgsConstructor
public class ErrorDTO {
    private final String shortMessage;
    private final String longMessage;
    private final String anchor;

    public JsonNode getError() {
        HashMap<String, Object> errorMap = new HashMap<String, Object>() {{
            put("error", new HashMap<String, Object>() {{
                put("shortMessage", shortMessage);
                put("longMessage", longMessage);
                put("anchor", anchor);
            }});
        }};
        ObjectMapper mapper = new ObjectMapper();
        return mapper.valueToTree(errorMap);
    }


}
