package com.contactBox.dataTransferObjects.requests;

import lombok.Data;

@Data
public class SignUpRequest {
    private String username;
    private String password;
    private String confirmPassword;
}
