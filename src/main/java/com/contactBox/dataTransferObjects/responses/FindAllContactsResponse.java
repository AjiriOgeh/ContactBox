package com.contactBox.dataTransferObjects.responses;

import com.contactBox.data.models.Contact;
import lombok.Data;

import java.util.List;

@Data
public class FindAllContactsResponse {
    private String userId;
    private String username;
    private List<Contact> contacts;
}
