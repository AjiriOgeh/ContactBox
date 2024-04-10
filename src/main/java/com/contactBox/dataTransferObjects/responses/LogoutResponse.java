package com.contactBox.dataTransferObjects.responses;

import lombok.Data;

@Data
public class LogoutResponse {
    private String userId;
    private String username;
}
