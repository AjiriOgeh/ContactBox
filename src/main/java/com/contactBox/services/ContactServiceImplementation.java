package com.contactBox.services;

import com.contactBox.data.models.Contact;
import com.contactBox.data.models.User;
import com.contactBox.data.repositories.ContactRepository;
import com.contactBox.dataTransferObjects.requests.*;
import com.contactBox.dataTransferObjects.responses.DeleteContactResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

import static com.contactBox.utilities.FindContact.findContactInUserList;
import static com.contactBox.utilities.Mappers.*;

@Service
public class ContactServiceImplementation implements ContactService{

    @Autowired
    private ContactRepository contactRepository;

    @Override
    public Contact createContact(CreateContactRequest createContactRequest) {
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

    @Override
    public List<Contact> findContactByName(FindContactByNameRequest findContactByNameRequest, User user) {
        List<Contact> contacts = new ArrayList<>();
        for (int count = 0; count < user.getContacts().size(); count++) {
            boolean isFirstNameExisting = user.getContacts().get(count).getFirstName().equals(findContactByNameRequest.getName().toLowerCase());
            boolean isLastNameExisting = user.getContacts().get(count).getLastName().equals(findContactByNameRequest.getName().toLowerCase());
            if (isFirstNameExisting || isLastNameExisting) {
                contacts.add(user.getContacts().get(count));
            }
        }
        return contacts;
    }

    @Override
    public List<Contact> findContactByPhoneNumber(FindContactByPhoneNumberRequest findContactByPhoneNumberRequest, User user) {
        List<Contact> contacts = new ArrayList<>();
        for (int count = 0; count < user.getContacts().size(); count++) {
            boolean isPhoneNumberExisting = user.getContacts().get(count).getPhoneNumber().equals(findContactByPhoneNumberRequest.getPhoneNumber());
            if (isPhoneNumberExisting) {
                contacts.add(user.getContacts().get(count));
            }
        }
        return contacts;
    }

}
