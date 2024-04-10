package com.contactBox.dataTransferObjects.responses;

import lombok.Data;

@Data
public class UpdateContactResponse {
    private String userId;
    private String contactId;
    private String header;
}
