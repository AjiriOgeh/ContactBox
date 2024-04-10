package com.contactBox.dataTransferObjects.responses;

import lombok.Data;

@Data
public class CreateContactResponse {
    private String userId;
    private String username;
    private String contactId;
    private String header;
}
