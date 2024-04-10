package com.contactBox.services;

import com.contactBox.data.models.Contact;
import com.contactBox.data.models.User;
import com.contactBox.data.repositories.ContactRepository;
import com.contactBox.dataTransferObjects.requests.CreateContactRequest;
import com.contactBox.dataTransferObjects.requests.DeleteContactRequest;
import com.contactBox.dataTransferObjects.requests.UpdateContactRequest;
import com.contactBox.dataTransferObjects.responses.DeleteContactResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import static com.contactBox.utilities.ContactFinder.findContactInUserList;
import static com.contactBox.utilities.Mappers.*;

@Service
public class ContactServiceImplementation implements ContactService{

    @Autowired
    private ContactRepository contactRepository;

    @Override
    public Contact createContact(CreateContactRequest createContactRequest) {
        if (validateCreateContactInputs(createContactRequest)) throw new IllegalArgumentException("All Fields are null or empty. Please enter a valid Input to create contact.");
        Contact contact = createContactRequestMap(createContactRequest);
        contactRepository.save(contact);
        return contact;
    }

    @Override
    public Contact updateContact(UpdateContactRequest updateContactRequest, User user) {
        Contact contact = findContactInUserList(updateContactRequest.getId(), user);
        Contact updatedContact = updateContactRequestMap(updateContactRequest, contact);
        contactRepository.save(updatedContact);
        return updatedContact;
    }

    @Override
    public DeleteContactResponse deleteContact(DeleteContactRequest deleteContactRequest, User user) {
        Contact contact = findContactInUserList(deleteContactRequest.getContactId(), user);
        DeleteContactResponse deleteContactResponse = deleteContactResponseMap(contact, user);
        contactRepository.delete(contact);
        return deleteContactResponse;
    }

    private static boolean validateCreateContactInputs(CreateContactRequest createContactRequest) {
        String[] createContactInputs = {createContactRequest.getHeader(), createContactRequest.getFirstName(), createContactRequest.getLastName(),
                createContactRequest.getPhoneNumber(), createContactRequest.getHeader(), createContactRequest.getNotes(), createContactRequest.getBuildingNumber(),
                createContactRequest.getStreet(), createContactRequest.getState(), createContactRequest.getCountry()};

        for (String createContactInput : createContactInputs) {
            if (createContactInput != null && !createContactInput.isEmpty()) return false;
        }
        return true;
    }




}
