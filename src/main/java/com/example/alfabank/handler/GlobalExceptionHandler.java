package com.example.alfabank.handler;

import com.example.alfabank.models.ErrorDTO;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * The class for handling all exceptions that occur in the application
 */

@ControllerAdvice
@RequiredArgsConstructor
@Slf4j
public class GlobalExceptionHandler {

    private final ObjectMapper mapper;

    /**
     * Handler for Exception.class
     * @param e - exception object
     * @param response - HttpServletResponse object, used to form response
     * @throws IOException
     */
    @ExceptionHandler(value = {Exception.class})
    public void handleException(Exception e, HttpServletResponse response) throws IOException {
        setResponse(e, response, "Internal error");
    }

    /**
     * Handler for IllegalArgumentException.class
     * @param e - exception object
     * @param response - HttpServletResponse object, used to form response
     * @throws IOException
     */
    @ExceptionHandler(value = {IllegalArgumentException.class})
    public void handleValidationException(Exception e, HttpServletResponse response) throws IOException {
        setResponse(e, response, "Validation error");
    }

    /**
     * Private method for generating a response
     * @param e - exception instance
     * @param response - HHttpServletResponse object
     * @param shortMessage - A string object containing a brief description of the exception type
     * @throws IOException
     */
    private void setResponse(Exception e, HttpServletResponse response, String shortMessage) throws IOException {
        Map<String, String> messagesMap = extractAnchor(e, shortMessage);
        response.setStatus(HttpStatus.OK.value());
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.getWriter().write(mapper.writeValueAsString(
                new ErrorDTO(shortMessage, messagesMap.get("LongMessage"), messagesMap.get("Anchor")).getError()
        ));
    }

    /**
     * Private method for extracting the anchor from the exception message
     * @param e - exception instance
     * @param shortMessage - A string object containing a brief description of the exception type
     * @return - Map containing a long message and an anchor
     */
    private Map<String, String> extractAnchor(Exception e, String shortMessage) {
        Map<String, String> messagesMap = new HashMap<>();
        String exceptionMessage = e.getMessage();
        if ("Internal error".equals(shortMessage) && (exceptionMessage == null || !exceptionMessage.contains("anchor: "))) {
            String anchor = UUID.randomUUID().toString();
            log.error("Exception: [" + anchor + "]", e);
            messagesMap.put("LongMessage", "An internal error has occurred. Please , please contact technical support with 'Anchor'");
            messagesMap.put("Anchor", anchor);
        } else {
            String[] messages = exceptionMessage.split("anchor:");
            messagesMap.put("LongMessage", messages[0]);
            messagesMap.put("Anchor", messages[1]);
        }
        return messagesMap;
    }
}
