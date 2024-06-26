package com.sonic.sonic_backend.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@JsonPropertyOrder({"success","code","msg","data"})
@JsonInclude(JsonInclude.Include.NON_NULL)
@AllArgsConstructor
public class Response {
    private int status;
    private boolean success;
    private String message;
    private Object data;

    public Response(int status, boolean success, String message) {
        this.status = status;
        this.success = success;
        this.message = message;
    }

    public static Response success(String message, Object data) {
        return new Response(HttpStatus.OK.value(), true, message, data);
    }
    public static Response success(String message) {
        return new Response(HttpStatus.OK.value(), true, message,null);
    }
    public static Response fail(HttpStatus status, String message) {
        return new Response(status.value(), false, message,null);
    }
}
