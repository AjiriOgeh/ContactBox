package com.contactBox.utilities;

import com.contactBox.data.models.Address;
import com.contactBox.data.models.Contact;
import com.contactBox.data.models.User;
import com.contactBox.dataTransferObjects.requests.CreateContactRequest;
import com.contactBox.dataTransferObjects.requests.SignUpRequest;
import com.contactBox.dataTransferObjects.requests.UpdateContactRequest;
import com.contactBox.dataTransferObjects.responses.*;

import java.time.format.DateTimeFormatter;
import java.util.List;

import static com.contactBox.utilities.ValidateInputs.areAllFieldsNullOrEmpty;
import static com.contactBox.utilities.ValidateInputs.isPhoneNumberAllDigits;

public class Mappers {

    public static User signUpRequestMap(SignUpRequest signUpRequest) {
        User user = new User();
        user.setUsername(signUpRequest.getUsername().toLowerCase());
        user.setPassword(signUpRequest.getPassword());
        return user;
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
        if (areAllFieldsNullOrEmpty(createContactRequest)) throw new IllegalArgumentException("All Fields are null or empty. Please enter a valid input to create contact.");
        if (isPhoneNumberAllDigits(createContactRequest.getPhoneNumber())) throw new IllegalArgumentException("Please enter a valid phone number.");
        Contact contact = new Contact();
        contact.setFirstName(createContactRequest.getFirstName().toLowerCase());
        contact.setLastName(createContactRequest.getLastName().toLowerCase());
        contact.setPhoneNumber(createContactRequest.getPhoneNumber());
        contact.setEmail(createContactRequest.getEmail());
        contact.setNotes(createContactRequest.getNotes());
        createContactRequestAddressMap(createContactRequest, contact.getAddress());
        return contact;
    }

    private static void createContactRequestAddressMap(CreateContactRequest createContactRequest, Address address) {
        address.setBuildingNumber(createContactRequest.getBuildingNumber());
        address.setStreet(createContactRequest.getStreet());
        address.setCity(createContactRequest.getCity());
        address.setState(createContactRequest.getState());
        address.setCountry(createContactRequest.getCountry());
    }

    public static CreateContactResponse createContactResponseMap(Contact contact, User user){
        CreateContactResponse createContactResponse = new CreateContactResponse();
        createContactResponse.setUserId(user.getId());
        createContactResponse.setUsername(user.getUsername());
        createContactResponse.setContactId(contact.getId());
        return createContactResponse;
    }

    public static Contact updateContactRequestMap(UpdateContactRequest updateContactRequest, Contact contact) {
        if (isPhoneNumberAllDigits(updateContactRequest.getPhoneNumber())) throw new IllegalArgumentException("Please enter a valid phone number.");
        if (updateContactRequest.getFirstName() != null) contact.setFirstName(updateContactRequest.getFirstName().toLowerCase());
        if (updateContactRequest.getLastName() != null) contact.setLastName(updateContactRequest.getLastName().toLowerCase());
        if (updateContactRequest.getPhoneNumber() != null) contact.setPhoneNumber(updateContactRequest.getPhoneNumber());
        if (updateContactRequest.getEmail() != null) contact.setEmail(updateContactRequest.getEmail());
        if (updateContactRequest.getNotes() != null) contact.setNotes(updateContactRequest.getNotes());
        updateContactRequestAddressMap(updateContactRequest, contact.getAddress());
        return contact;
    }

    private static void updateContactRequestAddressMap(UpdateContactRequest updateContactRequest, Address address) {
        if (updateContactRequest.getBuildingNumber() != null) address.setBuildingNumber(updateContactRequest.getBuildingNumber());
        if (updateContactRequest.getCity() != null) address.setCity(updateContactRequest.getCity());
        if (updateContactRequest.getStreet() != null) address.setStreet(updateContactRequest.getStreet());
        if (updateContactRequest.getState() != null) address.setState(updateContactRequest.getState());
        if (updateContactRequest.getCountry() != null) address.setCountry(updateContactRequest.getCountry());
    }

    public static UpdateContactResponse updateContactResponseMap(Contact contact, User user) {
        UpdateContactResponse updateContactResponse = new UpdateContactResponse();
        updateContactResponse.setUserId(user.getId());
        updateContactResponse.setContactId(contact.getId());
        return updateContactResponse;
    }
    public static FindContactByIdResponse viewContactResponseMap(Contact contact, User user) {
        FindContactByIdResponse findContactByIdResponse = new FindContactByIdResponse();
        findContactByIdResponse.setUserId(user.getId());
        findContactByIdResponse.setContact(contact);
        return findContactByIdResponse;
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

    public static FindContactByNameResponse findContactByNameResponseMap(List<Contact> contacts, User user) {
        FindContactByNameResponse findContactByNameResponse = new FindContactByNameResponse();
        findContactByNameResponse.setUserId(user.getId());
        findContactByNameResponse.setUsername(user.getUsername());
        findContactByNameResponse.setContacts(contacts);
        return findContactByNameResponse;
    }

    public static FindContactByPhoneNumberResponse findContactByPhoneNumberResponseMap(List<Contact> contacts, User user) {
        FindContactByPhoneNumberResponse findContactByPhoneNumberResponse = new FindContactByPhoneNumberResponse();
        findContactByPhoneNumberResponse.setUsername(user.getUsername());
        findContactByPhoneNumberResponse.setUserId(user.getId());
        findContactByPhoneNumberResponse.setContacts(contacts);
        return findContactByPhoneNumberResponse;
    }

}
