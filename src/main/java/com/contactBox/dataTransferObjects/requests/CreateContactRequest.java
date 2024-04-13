package com.contactBox.dataTransferObjects.requests;

import lombok.Data;


@Data
public class CreateContactRequest {
    private String username;
    private String firstName;
    private String lastName;
    private String email;
    private String phoneNumber;
    private String buildingNumber;
    private String street;
    private String city;
    private String state;
    private String country;
    private String notes;
}
