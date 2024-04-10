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

import static com.contactBox.utilities.ContactFinder.findContactInUserList;
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
        if (username == null) throw new IllegalArgumentException("Username cannot be null. Please enter a valid username.");
        if (username.isEmpty()) throw new IllegalArgumentException("Username cannot be empty. Please enter a valid username.");
        if (username.contains(" ")) throw new IllegalArgumentException("Username cannot space character. Please enter a valid username.");
        if (doesUsernameExist(username.toLowerCase())) throw new IllegalArgumentException("Username Exists. Please Try Again");
    }

    private void validatePassword(SignUpRequest signUpRequest) {
        if (signUpRequest.getPassword() == null) throw new IllegalArgumentException("Password cannot be null. Please enter a valid password");
        if (!signUpRequest.getPassword().equals(signUpRequest.getConfirmPassword())) throw new IllegalArgumentException("Passwords do not match. Please Try again");
        if (signUpRequest.getPassword().isEmpty()) throw new IllegalArgumentException("Password field cannot be empty. Please enter a valid password.");
        if (signUpRequest.getPassword().length() < 6) throw new IllegalArgumentException("Your Password is less than 6 characters. Please Try again.");
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
        if (user == null) throw new UserNotFoundException("Invalid Login Details. Please Try Again");
        if (!user.getPassword().equals(loginRequest.getPassword())) throw new InvalidPasswordException("Invalid Login Details. Please Try Again");
        user.setLocked(false);
        userRepository.save(user);
        return loginResponseMap(user);
    }

    @Override
    public CreateContactResponse createContact(CreateContactRequest createContactRequest) {
        User user = userRepository.findByUsername(createContactRequest.getUsername().toLowerCase());
        if (user == null) throw new UserNotFoundException(String.format("User %s does not exist. Please Create an Account",createContactRequest.getUsername()));
        if (user.isLocked()) throw new ProfileLockException("Please Login to create Contact.");
        Contact contact = contactService.createContact(createContactRequest);
        user.getContacts().add(contact);
        userRepository.save(user);
        return createContactResponseMap(contact, user);
    }

    @Override
    public UpdateContactResponse updateContact(UpdateContactRequest updateContactRequest) {
        User user = userRepository.findByUsername(updateContactRequest.getUsername().toLowerCase());
        if (user == null) throw new UserNotFoundException(String.format("User %s does not exist. Please Create an Account", updateContactRequest.getUsername()));
        if (user.isLocked()) throw new ProfileLockException("Please Login to edit Contact.");
        Contact contact = contactService.updateContact(updateContactRequest, user);
        return updateContactResponseMap(contact, user);
    }

    @Override
    public ViewContactResponse viewContact(ViewContactRequest viewContactRequest) {
        User user = userRepository.findByUsername(viewContactRequest.getUsername().toLowerCase());
        if (user == null) throw new UserNotFoundException(String.format("User %s does not exist. Please Create an Account", viewContactRequest.getUsername()));
        if (user.isLocked()) throw new ProfileLockException("Please Login to view contact");
        Contact contact = findContactInUserList(viewContactRequest.getContactId(), user);
        return viewContactResponseMap(contact,user);
    }

    @Override
    public DeleteContactResponse deleteContact(DeleteContactRequest deleteContactRequest) {
        User user = userRepository.findByUsername(deleteContactRequest.getUsername().toLowerCase());
        if (user == null) throw new UserNotFoundException(String.format("User %s does not exist. Please Create an Account", deleteContactRequest.getUsername()));
        if (user.isLocked()) throw new ProfileLockException("Please Login to delete contact");
        if (!user.getPassword().equals(deleteContactRequest.getPassword())) throw new InvalidPasswordException("Incorrect password. Please Try again");
        Contact contact = findContactInUserList(deleteContactRequest.getContactId(), user);
        DeleteContactResponse deleteContactResponse = contactService.deleteContact(deleteContactRequest, user);
        user.getContacts().remove(contact);
        userRepository.save(user);
        return deleteContactResponse;
    }

    @Override
    public FindAllContactsResponse findAllContacts(FindAllContactRequest findAllContactRequest) {
        User user = userRepository.findByUsername(findAllContactRequest.getUsername().toLowerCase());
        if (user == null) throw new UserNotFoundException(String.format("User %s does not exist. Please Create an Account", findAllContactRequest.getUsername()));
        if (user.isLocked()) throw new ProfileLockException("Please Login to view contacts");
        if (user.getContacts().isEmpty()) throw new ContactNotFoundException("No Contact Exist. Please create contact");
        return findAllContactsResponseMap(user.getContacts(), user);
    }


}
