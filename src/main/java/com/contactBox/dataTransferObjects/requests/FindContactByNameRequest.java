package com.contactBox.dataTransferObjects.requests;

import lombok.Data;

@Data
public class FindContactByNameRequest {
    private String username;
    private String name;
}
