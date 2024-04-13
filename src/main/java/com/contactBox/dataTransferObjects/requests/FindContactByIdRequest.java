package com.contactBox.dataTransferObjects.requests;

import lombok.Data;

@Data
public class FindContactByIdRequest {
    private String username;
    private String contactId;
}
