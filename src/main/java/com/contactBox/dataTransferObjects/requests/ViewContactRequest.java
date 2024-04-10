package com.contactBox.dataTransferObjects.requests;

import lombok.Data;

@Data
public class ViewContactRequest {
    private String username;
    private String contactId;
}
