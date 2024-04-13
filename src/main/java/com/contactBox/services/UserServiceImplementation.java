package com.contactBox.services;

import com.contactBox.data.models.Contact;
import com.contactBox.data.models.User;
import com.contactBox.data.repositories.UserRepository;
import com.contactBox.dataTransferObjects.requests.*;
import com.contactBox.dataTransferObjects.responses.*;
import com.contactBox.exceptions.ContactNotFoundException;
import com.contactBox.exceptions.InvalidPasswordException;
import com.contactBox.exceptions.ProfileLockException;
import com.contactBox.exceptions.UserNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.contactBox.utilities.FindContact.findContactInUserList;
import static com.contactBox.utilities.Mappers.*;

@Service
public class UserServiceImplementation implements UserService{

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ContactService contactService;

    @Override
    public SignUpResponse signUp(SignUpRequest signUpRequest) {
        validateUsername(signUpRequest.getUsername());
        validatePassword(signUpRequest);
        User user = signUpRequestMap(signUpRequest);
        userRepository.save(user);
        return signUpResponseMap(user);
    }

    private void validateUsername(String username) {
        if (username == null) throw new IllegalArgumentException("Username cannot be null. Please enter a valid input.");
        if (username.isEmpty()) throw new IllegalArgumentException("Username cannot be empty. Please enter a valid input.");
        if (username.contains(" ")) throw new IllegalArgumentException("Username cannot contain spaces. Please enter a valid input.");
        if (doesUsernameExist(username.toLowerCase())) throw new IllegalArgumentException("Username exists. Please try again.");
    }

    private void validatePassword(SignUpRequest signUpRequest) {
        if (signUpRequest.getPassword() == null) throw new IllegalArgumentException("Password cannot be null. Please enter a valid input.");
        if (!signUpRequest.getPassword().equals(signUpRequest.getConfirmPassword())) throw new IllegalArgumentException("Passwords do not match. Please try again.");
        if (signUpRequest.getPassword().isEmpty()) throw new IllegalArgumentException("Password field cannot be empty. Please enter a valid input.");
        if (signUpRequest.getPassword().length() < 6) throw new IllegalArgumentException("Password must be at least 6 characters. Please enter a valid input.");
    }

    private boolean doesUsernameExist(String username) {
        User user = userRepository.findByUsername(username);
        return user != null;
    }

    @Override
    public LogoutResponse logout(LogoutRequest logoutRequest) {
        User user = userRepository.findByUsername(logoutRequest.getUsername().toLowerCase());
        if (user == null) throw new UserNotFoundException(String.format("User %s does not exist.", logoutRequest.getUsername()));
        user.setLocked(true);
        userRepository.save(user);
        return logoutResponseMap(user);
    }

    @Override
    public LoginResponse login(LoginRequest loginRequest) {
        User user = userRepository.findByUsername(loginRequest.getUsername().toLowerCase());
        if (user == null) throw new UserNotFoundException("Invalid login details. Please try again.");
        if (!user.getPassword().equals(loginRequest.getPassword())) throw new InvalidPasswordException("Invalid login details. Please try again.");
        user.setLocked(false);
        userRepository.save(user);
        return loginResponseMap(user);
    }

    @Override
    public CreateContactResponse createContact(CreateContactRequest createContactRequest) {
        User user = userRepository.findByUsername(createContactRequest.getUsername().toLowerCase());
        if (user == null) throw new UserNotFoundException(String.format("User %s does not exist. Please signup.",createContactRequest.getUsername()));
        if (user.isLocked()) throw new ProfileLockException("Please login to create contact.");
        Contact contact = contactService.createContact(createContactRequest);
        user.getContacts().add(contact);
        userRepository.save(user);
        return createContactResponseMap(contact, user);
    }

    @Override
    public UpdateContactResponse updateContact(UpdateContactRequest updateContactRequest) {
        User user = userRepository.findByUsername(updateContactRequest.getUsername().toLowerCase());
        if (user == null) throw new UserNotFoundException(String.format("User %s does not exist. Please signup.", updateContactRequest.getUsername()));
        if (user.isLocked()) throw new ProfileLockException("Please login to update contact.");
        Contact contact = contactService.updateContact(updateContactRequest, user);
        return updateContactResponseMap(contact, user);
    }

    @Override
    public FindContactByIdResponse findContactById(FindContactByIdRequest findContactByIdRequest) {
        User user = userRepository.findByUsername(findContactByIdRequest.getUsername().toLowerCase());
        if (user == null) throw new UserNotFoundException(String.format("User %s does not exist. Please signup.", findContactByIdRequest.getUsername()));
        if (user.isLocked()) throw new ProfileLockException("Please login to view contact.");
        Contact contact = findContactInUserList(findContactByIdRequest.getContactId(), user);
        return viewContactResponseMap(contact,user);
    }

    @Override
    public DeleteContactResponse deleteContact(DeleteContactRequest deleteContactRequest) {
        User user = userRepository.findByUsername(deleteContactRequest.getUsername().toLowerCase());
        if (user == null) throw new UserNotFoundException(String.format("User %s does not exist. Please signup.", deleteContactRequest.getUsername()));
        if (user.isLocked()) throw new ProfileLockException("Please login to delete contact.");
        if (!user.getPassword().equals(deleteContactRequest.getPassword())) throw new InvalidPasswordException("Incorrect password. Please try again.");
        Contact contact = findContactInUserList(deleteContactRequest.getContactId(), user);
        DeleteContactResponse deleteContactResponse = contactService.deleteContact(deleteContactRequest, user);
        user.getContacts().remove(contact);
        userRepository.save(user);
        return deleteContactResponse;
    }

    @Override
    public FindAllContactsResponse findAllContacts(FindAllContactRequest findAllContactRequest) {
        User user = userRepository.findByUsername(findAllContactRequest.getUsername().toLowerCase());
        if (user == null) throw new UserNotFoundException(String.format("User %s does not exist. Please signup.", findAllContactRequest.getUsername()));
        if (user.isLocked()) throw new ProfileLockException("Please login to view all your contacts.");
        if (user.getContacts().isEmpty()) throw new ContactNotFoundException("Contacts list is empty. Please create contact.");
        return findAllContactsResponseMap(user.getContacts(), user);
    }

    @Override
    public FindContactByNameResponse findContactByName(FindContactByNameRequest findContactByNameRequest) {
        User user = userRepository.findByUsername(findContactByNameRequest.getUsername().toLowerCase().trim());
        if (user == null) throw new UserNotFoundException(String.format("User %s does not exist. Please signup.", findContactByNameRequest.getUsername()));
        if (user.isLocked()) throw new ProfileLockException("Please login to view your contacts.");
        List<Contact> contacts = contactService.findContactByName(findContactByNameRequest, user);
        if (contacts.isEmpty()) throw new ContactNotFoundException(String.format("Contact with name %s does not exist", findContactByNameRequest.getName()));
        return findContactByNameResponseMap(contacts, user);
    }

    @Override
    public FindContactByPhoneNumberResponse findContactByPhoneNumber(FindContactByPhoneNumberRequest findContactByPhoneNumberRequest) {
        User user = userRepository.findByUsername(findContactByPhoneNumberRequest.getUsername().toLowerCase());
        if (user == null) throw new UserNotFoundException(String.format("User %s does not exist. Please signup.", findContactByPhoneNumberRequest.getUsername()));
        if (user.isLocked()) throw new ProfileLockException("Please login to view your contacts.");
        List<Contact> contacts = contactService.findContactByPhoneNumber(findContactByPhoneNumberRequest, user);
        if (contacts.isEmpty()) throw new ContactNotFoundException(String.format("Contact with phone number %s does not exist", findContactByPhoneNumberRequest.getPhoneNumber()));
        return findContactByPhoneNumberResponseMap(contacts, user);
    }

}
