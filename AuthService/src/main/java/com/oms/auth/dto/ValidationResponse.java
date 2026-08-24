package com.oms.auth.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ValidationResponse {

    private boolean valid;

    private String username;

    private String role;

    private String message;

}
