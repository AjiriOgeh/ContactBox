package com.contactBox.services;

import com.contactBox.data.models.Contact;
import com.contactBox.data.models.User;
import com.contactBox.dataTransferObjects.requests.CreateContactRequest;
import com.contactBox.dataTransferObjects.requests.DeleteContactRequest;
import com.contactBox.dataTransferObjects.requests.UpdateContactRequest;
import com.contactBox.dataTransferObjects.responses.DeleteContactResponse;

public interface ContactService {
    Contact createContact(CreateContactRequest createContactRequest);

    Contact updateContact(UpdateContactRequest updateContactRequest, User user);

    DeleteContactResponse deleteContact(DeleteContactRequest deleteContactRequest, User user);
}
