package com.contactBox.dataTransferObjects.requests;

import lombok.Data;

@Data
public class DeleteContactRequest {
    private String username;
    private String contactId;
    private String password;
}
