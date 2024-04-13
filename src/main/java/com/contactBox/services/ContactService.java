package com.contactBox.services;

import com.contactBox.data.models.Contact;
import com.contactBox.data.models.User;
import com.contactBox.dataTransferObjects.requests.*;
import com.contactBox.dataTransferObjects.responses.DeleteContactResponse;

import java.util.List;

public interface ContactService {
    Contact createContact(CreateContactRequest createContactRequest);

    Contact updateContact(UpdateContactRequest updateContactRequest, User user);

    DeleteContactResponse deleteContact(DeleteContactRequest deleteContactRequest, User user);

    List<Contact> findContactByName(FindContactByNameRequest findContactByNameRequest, User user);

    List<Contact> findContactByPhoneNumber(FindContactByPhoneNumberRequest findContactByPhoneNumberRequest, User user);
}
