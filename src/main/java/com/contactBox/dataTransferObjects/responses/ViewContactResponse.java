package com.contactBox.dataTransferObjects.responses;

import com.contactBox.data.models.Contact;
import lombok.Data;

import java.util.List;

@Data
public class ViewContactResponse {
    private String userId;
    private Contact contact;
}
