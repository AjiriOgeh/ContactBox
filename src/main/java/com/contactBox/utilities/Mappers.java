package com.contactBox.utilities;

import com.contactBox.data.models.Address;
import com.contactBox.data.models.Contact;
import com.contactBox.data.models.User;
import com.contactBox.dataTransferObjects.requests.CreateContactRequest;
import com.contactBox.dataTransferObjects.requests.SignUpRequest;
import com.contactBox.dataTransferObjects.requests.UpdateContactRequest;
import com.contactBox.dataTransferObjects.responses.*;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class Mappers {

    public static User signUpRequestMap(SignUpRequest signUpRequest) {
        User newUser = new User();
        newUser.setUsername(signUpRequest.getUsername().toLowerCase());
        newUser.setPassword(signUpRequest.getPassword());
        return newUser;
    }

    public static SignUpResponse signUpResponseMap(User user) {
        SignUpResponse signUpResponse = new SignUpResponse();
        signUpResponse.setUserId(user.getId());
        signUpResponse.setUsername(user.getUsername());
        signUpResponse.setDateOfRegistration(user.getDateOfRegistration().format(DateTimeFormatter.ofPattern("MMM dd, yyyy")));
        return signUpResponse;
    }

    public static LogoutResponse logoutResponseMap(User user) {
        LogoutResponse logoutResponse = new LogoutResponse();
        logoutResponse.setUserId(user.getId());
        logoutResponse.setUsername(user.getUsername());
        return logoutResponse;
    }

    public static LoginResponse loginResponseMap(User user) {
        LoginResponse loginResponse = new LoginResponse();
        loginResponse.setUserId(user.getId());
        loginResponse.setUsername(user.getUsername());
        return loginResponse;
    }

    public static Contact createContactRequestMap(CreateContactRequest createContactRequest) {
        Contact contact = new Contact();
        contact.setHeader(createContactRequest.getHeader());
        contact.setFirstName(createContactRequest.getFirstName());
        contact.setLastName(createContactRequest.getLastName());
        contact.setPhoneNumber(createContactRequest.getPhoneNumber());
        contact.setEmail(createContactRequest.getEmail());
        contact.setNotes(createContactRequest.getNotes());
        addressMap(createContactRequest, contact.getAddress());
        return contact;
    }

    private static void addressMap(CreateContactRequest createContactRequest, Address address) {
        address.setBuildingNumber(createContactRequest.getBuildingNumber());
        address.setState(createContactRequest.getState());
        address.setCountry(createContactRequest.getCountry());
        address.setStreet(createContactRequest.getStreet());
    }

    public static CreateContactResponse createContactResponseMap(Contact contact, User user){
        CreateContactResponse createContactResponse = new CreateContactResponse();
        createContactResponse.setUserId(user.getId());
        createContactResponse.setUsername(user.getUsername());
        createContactResponse.setContactId(contact.getId());
        createContactResponse.setHeader(contact.getHeader());
        return createContactResponse;
    }

    public static Contact updateContactRequestMap(UpdateContactRequest updateContactRequest, Contact contact) {
        if (updateContactRequest.getHeader() != null) contact.setHeader(updateContactRequest.getHeader());
        if (updateContactRequest.getFirstName() != null) contact.setFirstName(updateContactRequest.getFirstName());
        if (updateContactRequest.getLastName() != null) contact.setLastName(updateContactRequest.getLastName());
        if (updateContactRequest.getPhoneNumber() != null) contact.setPhoneNumber(updateContactRequest.getPhoneNumber());
        if (updateContactRequest.getEmail() != null) contact.setEmail(updateContactRequest.getEmail());
        if (updateContactRequest.getNotes() != null) contact.setNotes(updateContactRequest.getNotes());
        if (updateContactRequest.getBuildingNumber() != null) contact.getAddress().setBuildingNumber(updateContactRequest.getBuildingNumber());
        if (updateContactRequest.getStreet() != null) contact.getAddress().setStreet(updateContactRequest.getStreet());
        if (updateContactRequest.getState() != null) contact.getAddress().setState(updateContactRequest.getState());
        if (updateContactRequest.getCountry() != null) contact.getAddress().setCountry(updateContactRequest.getCountry());
        return contact;
    }

    public static UpdateContactResponse updateContactResponseMap(Contact contact, User user) {
        UpdateContactResponse updateContactResponse = new UpdateContactResponse();
        updateContactResponse.setUserId(user.getId());
        updateContactResponse.setContactId(contact.getId());
        updateContactResponse.setHeader(contact.getHeader());
        return updateContactResponse;
    }
    public static ViewContactResponse viewContactResponseMap(Contact contact, User user) {
        ViewContactResponse viewContactResponse = new ViewContactResponse();
        viewContactResponse.setUserId(user.getId());
        viewContactResponse.setContact(contact);
        return viewContactResponse;
    }

    public static DeleteContactResponse deleteContactResponseMap(Contact contact, User user ) {
        DeleteContactResponse deleteContactResponse = new DeleteContactResponse();
        deleteContactResponse.setUserId(user.getId());
        deleteContactResponse.setUsername(user.getUsername());
        deleteContactResponse.setContactId(contact.getId());
        return deleteContactResponse;
    }

    public static FindAllContactsResponse findAllContactsResponseMap(List<Contact> contacts, User user) {
        FindAllContactsResponse findAllContactsResponse = new FindAllContactsResponse();
        findAllContactsResponse.setUserId(user.getId());
        findAllContactsResponse.setUsername(user.getUsername());
        findAllContactsResponse.setContacts(contacts);
        return findAllContactsResponse;
    }


}
