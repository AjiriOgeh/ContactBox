package com.contactBox.dataTransferObjects.responses;

import com.contactBox.data.models.Contact;
import lombok.Data;

@Data
public class FindContactByIdResponse {
    private String userId;
    private Contact contact;
}
