package com.contactBox.controllers;


import com.contactBox.data.repositories.ContactRepository;
import com.contactBox.data.repositories.UserRepository;
import com.contactBox.dataTransferObjects.requests.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class UserControllerTest {

    @Autowired
    private UserController userController;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ContactRepository contactRepository;

    @BeforeEach
    public void setUp() {
        userRepository.deleteAll();
        contactRepository.deleteAll();

        SignUpRequest signUpRequest = new SignUpRequest();
        signUpRequest.setUsername("jane123");
        signUpRequest.setPassword("password");
        signUpRequest.setConfirmPassword("password");
        userController.signUp(signUpRequest);

        CreateContactRequest createContactRequest = new CreateContactRequest();
        createContactRequest.setUsername("jane123");
        createContactRequest.setFirstName("jill");
        createContactRequest.setLastName("smith");
        createContactRequest.setPhoneNumber("09123456789");
        createContactRequest.setEmail("jillsmith@gmail.com");
        createContactRequest.setBuildingNumber("1");
        createContactRequest.setStreet("broadway");
        createContactRequest.setCity("new york city");
        createContactRequest.setState("new york");
        createContactRequest.setCountry("usa");
        createContactRequest.setNotes("always there for me in good and bad times.");
        userController.createContact(createContactRequest);
    }

    @Test
    public void userSignsUpTest() {
        SignUpRequest signUpRequest = new SignUpRequest();
        signUpRequest.setUsername("jessica123");
        signUpRequest.setPassword("password");
        signUpRequest.setConfirmPassword("password");

        var response = userController.signUp(signUpRequest);
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
    }

    @Test
    public void usersSignsUp_UsernameIsNullTest() {
        SignUpRequest signUpRequest = new SignUpRequest();
        signUpRequest.setUsername(null);
        signUpRequest.setPassword("password");
        signUpRequest.setConfirmPassword("password");

        var response = userController.signUp(signUpRequest);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    public void usersSignsUp_UsernameIsEmptyTest() {
        SignUpRequest signUpRequest = new SignUpRequest();
        signUpRequest.setUsername("");
        signUpRequest.setPassword("password");
        signUpRequest.setConfirmPassword("password");

        var response = userController.signUp(signUpRequest);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    public void usersSignsUp_UsernameExistsTest() {
        SignUpRequest signUpRequest = new SignUpRequest();
        signUpRequest.setUsername("jane123");
        signUpRequest.setPassword("password");
        signUpRequest.setConfirmPassword("password");

        var response = userController.signUp(signUpRequest);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    public void userSignsUp_UsernameContains_SpaceCharacterTest() {
        SignUpRequest signUpRequest = new SignUpRequest();
        signUpRequest.setUsername("jessica 123");
        signUpRequest.setPassword("password");
        signUpRequest.setConfirmPassword("password");

        var response = userController.signUp(signUpRequest);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    public void userSignsUp_PasswordIsNullTest() {
        SignUpRequest signUpRequest = new SignUpRequest();
        signUpRequest.setUsername("jessica123");
        signUpRequest.setPassword(null);
        signUpRequest.setConfirmPassword(null);

        var response = userController.signUp(signUpRequest);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    public void userSignsUp_PasswordIsEmptyTest() {
        SignUpRequest signUpRequest = new SignUpRequest();
        signUpRequest.setUsername("jessica123");
        signUpRequest.setPassword("");
        signUpRequest.setConfirmPassword("");

        var response = userController.signUp(signUpRequest);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    public void userSignsUp_PasswordIsLessThanSixCharactersTest() {
        SignUpRequest signUpRequest = new SignUpRequest();
        signUpRequest.setUsername("jessica123");
        signUpRequest.setPassword("word");
        signUpRequest.setConfirmPassword("word");

        var response = userController.signUp(signUpRequest);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    public void userSignsUp_PasswordsDoesNotMatchTest() {
        SignUpRequest signUpRequest = new SignUpRequest();
        signUpRequest.setUsername("jessica123");
        signUpRequest.setPassword("password");
        signUpRequest.setConfirmPassword("word");

        var response = userController.signUp(signUpRequest);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    public void userSignsUp_UserLogsOutTest() {
        LogoutRequest logoutRequest = new LogoutRequest();
        logoutRequest.setUsername("jane123");

        var response = userController.logout(logoutRequest);
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    public void nonExistentUserLogsOutTest() {
        LogoutRequest logoutRequest = new LogoutRequest();
        logoutRequest.setUsername("jill123");

        var response = userController.logout(logoutRequest);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    public void userLogsOut_UserLogsInTest() {
        LogoutRequest logoutRequest = new LogoutRequest();
        logoutRequest.setUsername("jill123");

        var response = userController.logout(logoutRequest);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());

        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setUsername("jane123");
        loginRequest.setPassword("password");

        response = userController.login(loginRequest);
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    public void nonExistentUserLogsInTest() {
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setUsername("jill123");
        loginRequest.setPassword("password");

        var response = userController.login(loginRequest);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    public void userCreatesContactTest() {
        CreateContactRequest createContactRequest = new CreateContactRequest();
        createContactRequest.setUsername("jane123");
        createContactRequest.setFirstName("jessica");
        createContactRequest.setLastName("brown");
        createContactRequest.setPhoneNumber("08123456789");
        createContactRequest.setEmail("jessicabrown@gmail.com");
        createContactRequest.setBuildingNumber("2");
        createContactRequest.setStreet("hollywood boulevard");
        createContactRequest.setCity("los angeles");
        createContactRequest.setState("california");
        createContactRequest.setCountry("usa");
        createContactRequest.setNotes("my kind and funny friend who i can count on");

        var response = userController.createContact(createContactRequest);
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
    }

    @Test
    public void nonExistentUser_CreatesContactTest() {
        CreateContactRequest createContactRequest = new CreateContactRequest();
        createContactRequest.setUsername("jill123");
        createContactRequest.setFirstName("jessica");
        createContactRequest.setLastName("brown");
        createContactRequest.setPhoneNumber("08123456789");
        createContactRequest.setEmail("jessicabrown@gmail.com");
        createContactRequest.setBuildingNumber("2");
        createContactRequest.setStreet("hollywood boulevard");
        createContactRequest.setCity("los angeles");
        createContactRequest.setState("california");
        createContactRequest.setCountry("usa");
        createContactRequest.setNotes("my kind and funny friend who i can count on");

        var response = userController.createContact(createContactRequest);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    public void userLogsOut_UserCreatesContactTest() {
        LogoutRequest logoutRequest = new LogoutRequest();
        logoutRequest.setUsername("jane123");

        var response = userController.logout(logoutRequest);
        assertEquals(HttpStatus.OK, response.getStatusCode());

        CreateContactRequest createContactRequest = new CreateContactRequest();
        createContactRequest.setUsername("jane123");
        createContactRequest.setFirstName("jessica");
        createContactRequest.setLastName("brown");
        createContactRequest.setPhoneNumber("08123456789");
        createContactRequest.setEmail("jessicabrown@gmail.com");
        createContactRequest.setBuildingNumber("2");
        createContactRequest.setStreet("hollywood boulevard");
        createContactRequest.setCity("los angeles");
        createContactRequest.setState("california");
        createContactRequest.setCountry("usa");
        createContactRequest.setNotes("my kind and funny friend who i can count on");

        response = userController.createContact(createContactRequest);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    public void userCreatesContact_PhoneNumberIsInvalidTest() {
        CreateContactRequest createContactRequest = new CreateContactRequest();
        createContactRequest.setUsername("jane123");
        createContactRequest.setFirstName("jessica");
        createContactRequest.setLastName("brown");
        createContactRequest.setPhoneNumber("08123abcdef");
        createContactRequest.setEmail("jessicabrown@gmail.com");
        createContactRequest.setBuildingNumber("2");
        createContactRequest.setStreet("hollywood boulevard");
        createContactRequest.setCity("los angeles");
        createContactRequest.setState("california");
        createContactRequest.setCountry("usa");
        createContactRequest.setNotes("my kind and funny friend who i can count on");

        var response = userController.createContact(createContactRequest);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    public void userUpdatesContactTest() {
        String contactId = contactRepository.findAll().getFirst().getId();

        UpdateContactRequest updateContactRequest = new UpdateContactRequest();
        updateContactRequest.setUsername("jane123");
        updateContactRequest.setId(contactId);
        updateContactRequest.setEmail("jillsmith@yahoo.com");
        updateContactRequest.setPhoneNumber("07123456789");
        updateContactRequest.setStreet("wall street");

        var response = userController.updateContact(updateContactRequest);
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    public void nonExistentUser_UpdatesContactTest() {
        String contactId = contactRepository.findAll().getFirst().getId();

        UpdateContactRequest updateContactRequest = new UpdateContactRequest();
        updateContactRequest.setUsername("jessica123");
        updateContactRequest.setId(contactId);
        updateContactRequest.setEmail("jillsmith@yahoo.com");
        updateContactRequest.setPhoneNumber("07123456789");
        updateContactRequest.setStreet("wall street");

        var response = userController.updateContact(updateContactRequest);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    public void userUpdates_NonExistentContactTest() {
        UpdateContactRequest updateContactRequest = new UpdateContactRequest();
        updateContactRequest.setUsername("jane123");
        updateContactRequest.setId("non existent contact id");
        updateContactRequest.setEmail("jillsmith@yahoo.com");
        updateContactRequest.setPhoneNumber("07123456789");
        updateContactRequest.setStreet("wall street");

        var response = userController.updateContact(updateContactRequest);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    public void userLogsOut_UpdatesContactTest() {
        LogoutRequest logoutRequest = new LogoutRequest();
        logoutRequest.setUsername("jane123");

        var response = userController.logout(logoutRequest);
        assertEquals(HttpStatus.OK, response.getStatusCode());

        String contactId = contactRepository.findAll().getFirst().getId();

        UpdateContactRequest updateContactRequest = new UpdateContactRequest();
        updateContactRequest.setUsername("jane123");
        updateContactRequest.setId(contactId);
        updateContactRequest.setEmail("jillsmith@yahoo.com");
        updateContactRequest.setPhoneNumber("07123456789");
        updateContactRequest.setStreet("wall street");

        response = userController.updateContact(updateContactRequest);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    public void userUpdatesContact_PhoneNumberIsInvalidTest() {
        String contactId = contactRepository.findAll().getFirst().getId();

        UpdateContactRequest updateContactRequest = new UpdateContactRequest();
        updateContactRequest.setUsername("jane123");
        updateContactRequest.setId(contactId);
        updateContactRequest.setEmail("jillsmith@yahoo.com");
        updateContactRequest.setPhoneNumber("07123abcef");
        updateContactRequest.setStreet("wall street");

        var response = userController.updateContact(updateContactRequest);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    public void userFindsContactByIdTest() {
        String contactId = contactRepository.findAll().getFirst().getId();

        FindContactByIdRequest findContactByIdRequest = new FindContactByIdRequest();
        findContactByIdRequest.setContactId(contactId);
        findContactByIdRequest.setUsername("jane123");

        var response = userController.findContactById(findContactByIdRequest);
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    public void userFindsContact_WithNonExistentIdTest() {
        FindContactByIdRequest findContactByIdRequest = new FindContactByIdRequest();
        findContactByIdRequest.setContactId("non existent contactId");
        findContactByIdRequest.setUsername("jane123");

        var response = userController.findContactById(findContactByIdRequest);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    public void nonExistentUser_FindsContactByIdTest() {
        String contactId = contactRepository.findAll().getFirst().getId();

        FindContactByIdRequest findContactByIdRequest = new FindContactByIdRequest();
        findContactByIdRequest.setContactId(contactId);
        findContactByIdRequest.setUsername("jill123");

        var response = userController.findContactById(findContactByIdRequest);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    public void userLogsOut_FindsContactByIdTest() {
        LogoutRequest logoutRequest = new LogoutRequest();
        logoutRequest.setUsername("jane123");

        var response = userController.logout(logoutRequest);
        assertEquals(HttpStatus.OK, response.getStatusCode());

        String contactId = contactRepository.findAll().getFirst().getId();

        FindContactByIdRequest findContactByIdRequest = new FindContactByIdRequest();
        findContactByIdRequest.setContactId(contactId);
        findContactByIdRequest.setUsername("jane123");

        response = userController.findContactById(findContactByIdRequest);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    public void userDeletesContactTest() {
        String contactId = contactRepository.findAll().getFirst().getId();

        DeleteContactRequest deleteContactRequest = new DeleteContactRequest();
        deleteContactRequest.setContactId(contactId);
        deleteContactRequest.setUsername("jane123");
        deleteContactRequest.setPassword("password");

        var response = userController.deleteContact(deleteContactRequest);
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    public void userDeletes_NonExistentContactTest() {
        DeleteContactRequest deleteContactRequest = new DeleteContactRequest();
        deleteContactRequest.setContactId("non existent contactId");
        deleteContactRequest.setUsername("jane123");
        deleteContactRequest.setPassword("password");

        var response = userController.deleteContact(deleteContactRequest);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    public void nonExistentUserDeletesContactTest() {
        String contactId = contactRepository.findAll().getFirst().getId();

        DeleteContactRequest deleteContactRequest = new DeleteContactRequest();
        deleteContactRequest.setContactId(contactId);
        deleteContactRequest.setUsername("jill123");
        deleteContactRequest.setPassword("password");

        var response = userController.deleteContact(deleteContactRequest);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    public void userLogsOut_DeletesContactTest() {
        LogoutRequest logoutRequest = new LogoutRequest();
        logoutRequest.setUsername("jane123");

        var response = userController.logout(logoutRequest);
        assertEquals(HttpStatus.OK, response.getStatusCode());

        String contactId = contactRepository.findAll().getFirst().getId();

        DeleteContactRequest deleteContactRequest = new DeleteContactRequest();
        deleteContactRequest.setContactId(contactId);
        deleteContactRequest.setUsername("jane123");
        deleteContactRequest.setPassword("password");

        response = userController.deleteContact(deleteContactRequest);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    public void userDeletesContact_PasswordIsInvalidTest() {
        String contactId = contactRepository.findAll().getFirst().getId();

        DeleteContactRequest deleteContactRequest = new DeleteContactRequest();
        deleteContactRequest.setContactId(contactId);
        deleteContactRequest.setUsername("jane123");
        deleteContactRequest.setPassword("word");

        var response = userController.deleteContact(deleteContactRequest);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    public void userFindsAllContactsTest() {
        FindAllContactRequest findAllContactRequest = new FindAllContactRequest();
        findAllContactRequest.setUsername("jane123");

        var response = userController.findAllContacts(findAllContactRequest);
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    public void nonExistentUserFindsAllContactsTest() {
        FindAllContactRequest findAllContactRequest = new FindAllContactRequest();
        findAllContactRequest.setUsername("jessica123");

        var response = userController.findAllContacts(findAllContactRequest);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    public void userLogsOut_FindsAllContactsTest() {
        LogoutRequest logoutRequest = new LogoutRequest();
        logoutRequest.setUsername("jane123");

        var response = userController.logout(logoutRequest);
        assertEquals(HttpStatus.OK, response.getStatusCode());

        FindAllContactRequest findAllContactRequest = new FindAllContactRequest();
        findAllContactRequest.setUsername("jane123");

        response = userController.findAllContacts(findAllContactRequest);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    public void userFindsContactByNameTest() {
        FindContactByNameRequest findContactByNameRequest = new FindContactByNameRequest();
        findContactByNameRequest.setUsername("jane123");
        findContactByNameRequest.setName("jill");

        var response = userController.findContactByName(findContactByNameRequest);
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    public void nonExistentUser_FindsContactByNameTest() {
        FindContactByNameRequest findContactByNameRequest = new FindContactByNameRequest();
        findContactByNameRequest.setUsername("jessica123");
        findContactByNameRequest.setName("jill");

        var response = userController.findContactByName(findContactByNameRequest);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    public void userLogsOut_FindsContactByNameTest() {
        LogoutRequest logoutRequest = new LogoutRequest();
        logoutRequest.setUsername("jane123");

        var response = userController.logout(logoutRequest);
        assertEquals(HttpStatus.OK, response.getStatusCode());

        FindContactByNameRequest findContactByNameRequest = new FindContactByNameRequest();
        findContactByNameRequest.setUsername("jane123");
        findContactByNameRequest.setName("jill");

        response = userController.findContactByName(findContactByNameRequest);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    public void userFindsContactByPhoneNumberTest() {
        FindContactByPhoneNumberRequest findContactByPhoneNumberRequest = new FindContactByPhoneNumberRequest();
        findContactByPhoneNumberRequest.setUsername("jane123");
        findContactByPhoneNumberRequest.setPhoneNumber("09123456789");

        var response = userController.findContactByPhoneNumber(findContactByPhoneNumberRequest);
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    public void nonExistentUser_FindsContactByPhoneNumberTest() {
        FindContactByPhoneNumberRequest findContactByPhoneNumberRequest = new FindContactByPhoneNumberRequest();
        findContactByPhoneNumberRequest.setUsername("jessica");
        findContactByPhoneNumberRequest.setPhoneNumber("09123456789");

        var response = userController.findContactByPhoneNumber(findContactByPhoneNumberRequest);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    public void userLogs_FindsContactByPhoneNumberTest() {
        LogoutRequest logoutRequest = new LogoutRequest();
        logoutRequest.setUsername("jane123");

        var response = userController.logout(logoutRequest);
        assertEquals(HttpStatus.OK, response.getStatusCode());

        FindContactByPhoneNumberRequest findContactByPhoneNumberRequest = new FindContactByPhoneNumberRequest();
        findContactByPhoneNumberRequest.setUsername("jane123");
        findContactByPhoneNumberRequest.setPhoneNumber("09123456789");

        response = userController.findContactByPhoneNumber(findContactByPhoneNumberRequest);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());

    }

}