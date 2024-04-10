package com.contactBox.data.models;

import lombok.Data;

@Data
public class Address {
    private String buildingNumber;
    private String street;
    private String state;
    private String country;
}
