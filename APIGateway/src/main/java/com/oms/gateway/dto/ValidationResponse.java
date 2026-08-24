package com.oms.gateway.dto;

import lombok.Data;

@Data
public class ValidationResponse {

    private boolean valid;

    private String username;

    private String role;

    private String message;

}