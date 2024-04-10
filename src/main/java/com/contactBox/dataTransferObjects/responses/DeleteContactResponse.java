package com.contactBox.dataTransferObjects.responses;

import lombok.Data;

@Data
public class DeleteContactResponse {
    private String userId;
    private String username;
    private String contactId;
}
