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
        createContactRequest.setHeader("my best friend");
        createContactRequest.setUsername("jane123");
        createContactRequest.setFirstName("jill");
        createContactRequest.setPhoneNumber("09123456789");
        createContactRequest.setBuildingNumber("1");
        createContactRequest.setStreet("broadway");
        userController.createContact(createContactRequest);
    }

    @Test
    public void userSignsUpTest() {
        SignUpRequest signUpRequest = new SignUpRequest();
        signUpRequest.setUsername("jill123");
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
        signUpRequest.setUsername("JANE123");
        signUpRequest.setPassword("password");
        signUpRequest.setConfirmPassword("password");

        var response = userController.signUp(signUpRequest);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    public void usersSignsUp_UsernameContainsSpaceCharacterTest() {
        SignUpRequest signUpRequest = new SignUpRequest();
        signUpRequest.setUsername("jill 123");
        signUpRequest.setPassword("password");
        signUpRequest.setConfirmPassword("password");

        var response = userController.signUp(signUpRequest);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    public void userSignsUp_PasswordIsNullTest() {
        SignUpRequest signUpRequest = new SignUpRequest();
        signUpRequest.setUsername("jill123");
        signUpRequest.setPassword(null);
        signUpRequest.setConfirmPassword(null);

        var response = userController.signUp(signUpRequest);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    public void userSignsUp_PasswordIsEmptyTest() {
        SignUpRequest signUpRequest = new SignUpRequest();
        signUpRequest.setUsername("jill123");
        signUpRequest.setPassword("");
        signUpRequest.setConfirmPassword("");

        var response = userController.signUp(signUpRequest);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    public void userSignsUp_PasswordIsLessThanSixCharactersTest() {
        SignUpRequest signUpRequest = new SignUpRequest();
        signUpRequest.setUsername("jill123");
        signUpRequest.setPassword("word");
        signUpRequest.setConfirmPassword("word");

        var response = userController.signUp(signUpRequest);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    public void userSignsUp_PasswordDoesNotMatchTest() {
        SignUpRequest signUpRequest = new SignUpRequest();
        signUpRequest.setUsername("jill123");
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
        logoutRequest.setUsername("jane123");

        var response = userController.logout(logoutRequest);
        assertEquals(HttpStatus.OK, response.getStatusCode());

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
        createContactRequest.setHeader("my best friend");
        createContactRequest.setUsername("jane123");
        createContactRequest.setFirstName("jessica");
        createContactRequest.setLastName("brown");
        createContactRequest.setPhoneNumber("08123456789");
        createContactRequest.setEmail("jessicabrown@gmail.com");
        createContactRequest.setBuildingNumber("2");
        createContactRequest.setStreet("park avenue");
        createContactRequest.setState("new york");
        createContactRequest.setCountry("usa");
        createContactRequest.setNotes("Always there for me in good and bad times.");

        var response = userController.createContact(createContactRequest);
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
    }

    @Test
    public void nonExistentUser_CreatesContactTest() {
        CreateContactRequest createContactRequest = new CreateContactRequest();
        createContactRequest.setHeader("my best friend");
        createContactRequest.setUsername("jill123");
        createContactRequest.setFirstName("jessica");
        createContactRequest.setLastName("brown");
        createContactRequest.setPhoneNumber("08123456789");
        createContactRequest.setEmail("jessicabrown@gmail.com");
        createContactRequest.setBuildingNumber("2");
        createContactRequest.setStreet("park avenue");
        createContactRequest.setState("new york");
        createContactRequest.setCountry("usa");
        createContactRequest.setNotes("Always there for me in good and bad times.");

        var response = userController.createContact(createContactRequest);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    public void userEditsContactTest() {
        String contactId = contactRepository.findAll().getFirst().getId();
        UpdateContactRequest updateContactRequest = new UpdateContactRequest();

        updateContactRequest.setUsername("jane123");
        updateContactRequest.setId(contactId);
        updateContactRequest.setHeader("my best friend");
        updateContactRequest.setPhoneNumber("07123456789");
        updateContactRequest.setStreet("broadway");

        var response = userController.updateContact(updateContactRequest);
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    public void nonExistentUserEditsContactTest() {
        String contactId = contactRepository.findAll().getFirst().getId();
        UpdateContactRequest updateContactRequest = new UpdateContactRequest();

        updateContactRequest.setUsername("jessica123");
        updateContactRequest.setId(contactId);
        updateContactRequest.setHeader("my best friend");
        updateContactRequest.setPhoneNumber("07123456789");
        updateContactRequest.setStreet("broadway");

        var response = userController.updateContact(updateContactRequest);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    public void userViewsContactTest() {
        String contactId = contactRepository.findAll().getFirst().getId();

        ViewContactRequest viewContactRequest = new ViewContactRequest();
        viewContactRequest.setContactId(contactId);
        viewContactRequest.setUsername("jane123");

        var response = userController.viewContact(viewContactRequest);
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    public void userViewsNonExistentContactTest() {
        ViewContactRequest viewContactRequest = new ViewContactRequest();
        viewContactRequest.setContactId("non existent contactId");
        viewContactRequest.setUsername("jane123");

        var response = userController.viewContact(viewContactRequest);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    public void nonExistentUserViewsContactTest() {
        String contactId = contactRepository.findAll().getFirst().getId();

        ViewContactRequest viewContactRequest = new ViewContactRequest();
        viewContactRequest.setContactId(contactId);
        viewContactRequest.setUsername("jill123");

        var response = userController.viewContact(viewContactRequest);
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
        findAllContactRequest.setUsername("jane123");

        var response = userController.findAllContacts(findAllContactRequest);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }


}