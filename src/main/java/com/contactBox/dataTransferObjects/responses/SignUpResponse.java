package com.contactBox.dataTransferObjects.responses;

import lombok.Data;

@Data
public class SignUpResponse {
    private String userId;
    private String username;
    private String dateOfRegistration;
}
