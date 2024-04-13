package com.contactBox.dataTransferObjects.requests;

import lombok.Data;

@Data
public class FindContactByPhoneNumberRequest {
    private String username;
    private String phoneNumber;
}
