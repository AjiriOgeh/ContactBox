package com.contactBox.services;

import com.contactBox.data.models.User;
import com.contactBox.data.repositories.ContactRepository;
import com.contactBox.data.repositories.UserRepository;
import com.contactBox.dataTransferObjects.requests.*;
import com.contactBox.dataTransferObjects.responses.*;
import com.contactBox.exceptions.ContactNotFoundException;
import com.contactBox.exceptions.InvalidPasswordException;
import com.contactBox.exceptions.ProfileLockException;
import com.contactBox.exceptions.UserNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class UserServiceImplementationTest {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserService userService;

    @Autowired
    private ContactRepository contactRepository;

    @BeforeEach
    public void setUp(){
        userRepository.deleteAll();
        contactRepository.deleteAll();
    }

    @Test
    public void userSignsUp_UserIsSavedTest() {
        assertEquals(0, userRepository.count());

        SignUpRequest signUpRequest = new SignUpRequest();
        signUpRequest.setUsername("jane123");
        signUpRequest.setPassword("password");
        signUpRequest.setConfirmPassword("password");
        SignUpResponse janeSignUpResponse = userService.signUp(signUpRequest);

        User janeContactBox = userRepository.findByUsername("jane123");

        assertEquals(1, userRepository.count());
        assertEquals("jane123", janeContactBox.getUsername());
        assertEquals("jane123", janeSignUpResponse.getUsername());
    }

    @Test
    public void userSignsUp_UsernameIsNull_ThrowsExceptionTest() {
        assertEquals(0, userRepository.count());

        SignUpRequest signUpRequest = new SignUpRequest();
        signUpRequest.setUsername(null);
        signUpRequest.setPassword("password");
        signUpRequest.setConfirmPassword("password");

        assertThrows(IllegalArgumentException.class, ()->userService.signUp(signUpRequest));
    }

    @Test
    public void userSignsUp_UsernameIsEmpty_ThrowsExceptionTest() {
        assertEquals(0, userRepository.count());

        SignUpRequest signUpRequest = new SignUpRequest();
        signUpRequest.setUsername("");
        signUpRequest.setPassword("password");
        signUpRequest.setConfirmPassword("password");

        assertThrows(IllegalArgumentException.class, ()->userService.signUp(signUpRequest));
    }

    @Test
    public void userSignsUp_UsernameContainsSpaceCharacter_ThrowsExceptionTest() {
        assertEquals(0, userRepository.count());

        SignUpRequest signUpRequest = new SignUpRequest();
        signUpRequest.setUsername("jane 123");
        signUpRequest.setPassword("password");
        signUpRequest.setConfirmPassword("password");

        assertThrows(IllegalArgumentException.class, ()->userService.signUp(signUpRequest));
    }

    @Test
    public void userSignsUp_UsernameExists_ThrowsExceptionTest() {
        assertEquals(0, userRepository.count());

        SignUpRequest signUpRequest = new SignUpRequest();
        signUpRequest.setUsername("jane123");
        signUpRequest.setPassword("password");
        signUpRequest.setConfirmPassword("password");
        SignUpResponse janeSignUpResponse = userService.signUp(signUpRequest);

        User janeContactBox = userRepository.findByUsername("jane123");

        assertEquals(1, userRepository.count());
        assertEquals("jane123", janeContactBox.getUsername());
        assertEquals("jane123", janeSignUpResponse.getUsername());

        signUpRequest.setUsername("Jane123");
        signUpRequest.setPassword("password123");
        signUpRequest.setConfirmPassword("password123");

        assertThrows(IllegalArgumentException.class, ()->userService.signUp(signUpRequest));
    }

    @Test
    public void userSignsUp_PasswordIsNull_ThrowsExceptionTest() {
        assertEquals(0, userRepository.count());

        SignUpRequest signUpRequest = new SignUpRequest();
        signUpRequest.setUsername("jane123");
        signUpRequest.setPassword(null);
        signUpRequest.setConfirmPassword(null);

        assertThrows(IllegalArgumentException.class, ()->userService.signUp(signUpRequest));
    }

    @Test
    public void userSignsUp_PasswordIsEmpty_ThrowsExceptionTest() {
        assertEquals(0, userRepository.count());

        SignUpRequest signUpRequest = new SignUpRequest();
        signUpRequest.setUsername("jane123");
        signUpRequest.setPassword("");
        signUpRequest.setConfirmPassword("");

        assertThrows(IllegalArgumentException.class, ()->userService.signUp(signUpRequest));
    }

    @Test
    public void userSignsUp_PasswordIsLessThan6Characters_ThrowsExceptionTest() {
        assertEquals(0, userRepository.count());

        SignUpRequest signUpRequest = new SignUpRequest();
        signUpRequest.setUsername("jane123");
        signUpRequest.setPassword("word");
        signUpRequest.setConfirmPassword("word");

        assertThrows(IllegalArgumentException.class, ()->userService.signUp(signUpRequest));
    }

    @Test
    public void userSignsUp_PasswordDoesNotMatch_ConfirmPasswordThrowsExceptionTest() {
        assertEquals(0, userRepository.count());

        SignUpRequest signUpRequest = new SignUpRequest();
        signUpRequest.setUsername("jane123");
        signUpRequest.setPassword("password");
        signUpRequest.setConfirmPassword("word");

        assertThrows(IllegalArgumentException.class, ()->userService.signUp(signUpRequest));
    }

    @Test
    public void userSignsUp_UserLogsOutTest() {
        assertEquals(0, userRepository.count());

        SignUpRequest signUpRequest = new SignUpRequest();
        signUpRequest.setUsername("jane123");
        signUpRequest.setPassword("password");
        signUpRequest.setConfirmPassword("password");
        SignUpResponse janeSignUpResponse = userService.signUp(signUpRequest);

        User janeContactBox = userRepository.findByUsername("jane123");

        assertEquals(1, userRepository.count());
        assertEquals("jane123", janeContactBox.getUsername());
        assertEquals("jane123", janeSignUpResponse.getUsername());

        LogoutRequest logoutRequest = new LogoutRequest();
        logoutRequest.setUsername("jane123");
        LogoutResponse janeLogoutResponse = userService.logout(logoutRequest);

        janeContactBox = userRepository.findByUsername("jane123");

        assertTrue(janeContactBox.isLocked());
        assertEquals(janeContactBox.getId(), janeLogoutResponse.getUserId());
        assertEquals("jane123", janeLogoutResponse.getUsername());
    }

    @Test
    public void nonExistentUserLogsOut_ThrowsExceptionTest() {
        assertEquals(0, userRepository.count());

        SignUpRequest signUpRequest = new SignUpRequest();
        signUpRequest.setUsername("jane123");
        signUpRequest.setPassword("password");
        signUpRequest.setConfirmPassword("password");
        SignUpResponse janeSignUpResponse = userService.signUp(signUpRequest);

        User janeContactBox = userRepository.findByUsername("jane123");

        assertEquals(1, userRepository.count());
        assertEquals("jane123", janeContactBox.getUsername());
        assertEquals("jane123", janeSignUpResponse.getUsername());

        LogoutRequest logoutRequest = new LogoutRequest();
        logoutRequest.setUsername("jill123");

        assertThrows(UserNotFoundException.class, ()->userService.logout(logoutRequest));
    }

    @Test
    public void userLogsOut_UserLogsInTest() {
        assertEquals(0, userRepository.count());

        SignUpRequest signUpRequest = new SignUpRequest();
        signUpRequest.setUsername("jane123");
        signUpRequest.setPassword("password");
        signUpRequest.setConfirmPassword("password");
        SignUpResponse janeSignUpResponse = userService.signUp(signUpRequest);

        User janeContactBox = userRepository.findByUsername("jane123");

        assertEquals(1, userRepository.count());
        assertEquals("jane123", janeContactBox.getUsername());
        assertEquals("jane123", janeSignUpResponse.getUsername());

        LogoutRequest logoutRequest = new LogoutRequest();
        logoutRequest.setUsername("jane123");
        LogoutResponse janeLogoutResponse = userService.logout(logoutRequest);

        janeContactBox = userRepository.findByUsername("jane123");

        assertTrue(janeContactBox.isLocked());
        assertEquals(janeContactBox.getId(), janeLogoutResponse.getUserId());
        assertEquals("jane123", janeLogoutResponse.getUsername());

        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setUsername("jane123");
        loginRequest.setPassword("password");

        LoginResponse janeLoginResponse = userService.login(loginRequest);

        janeContactBox = userRepository.findByUsername("jane123");

        assertFalse(janeContactBox.isLocked());
        assertEquals(janeContactBox.getId(), janeLoginResponse.getUserId());
        assertEquals("jane123", janeLoginResponse.getUsername());
    }

    @Test
    public void nonExistentUserLogIn_ThrowsExceptionTest() {
        assertEquals(0, userRepository.count());

        SignUpRequest signUpRequest = new SignUpRequest();
        signUpRequest.setUsername("jane123");
        signUpRequest.setPassword("password");
        signUpRequest.setConfirmPassword("password");
        SignUpResponse janeSignUpResponse = userService.signUp(signUpRequest);

        User janeContactBox = userRepository.findByUsername("jane123");

        assertEquals(1, userRepository.count());
        assertEquals("jane123", janeContactBox.getUsername());
        assertEquals("jane123", janeSignUpResponse.getUsername());

        LogoutRequest logoutRequest = new LogoutRequest();
        logoutRequest.setUsername("jane123");
        LogoutResponse janeLogoutResponse = userService.logout(logoutRequest);

        janeContactBox = userRepository.findByUsername("jane123");

        assertTrue(janeContactBox.isLocked());
        assertEquals(janeContactBox.getId(), janeLogoutResponse.getUserId());
        assertEquals("jane123", janeLogoutResponse.getUsername());

        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setUsername("jill123");
        loginRequest.setPassword("password");

        assertThrows(UserNotFoundException.class, ()->userService.login(loginRequest));
    }

    @Test
    public void userLogsIn_PasswordIsInvalid_ThrowsExceptionTest() {
        assertEquals(0, userRepository.count());

        SignUpRequest signUpRequest = new SignUpRequest();
        signUpRequest.setUsername("jane123");
        signUpRequest.setPassword("password");
        signUpRequest.setConfirmPassword("password");
        SignUpResponse janeSignUpResponse = userService.signUp(signUpRequest);

        User janeContactBox = userRepository.findByUsername("jane123");

        assertEquals(1, userRepository.count());
        assertEquals("jane123", janeContactBox.getUsername());
        assertEquals("jane123", janeSignUpResponse.getUsername());

        LogoutRequest logoutRequest = new LogoutRequest();
        logoutRequest.setUsername("jane123");
        LogoutResponse janeLogoutResponse = userService.logout(logoutRequest);

        janeContactBox = userRepository.findByUsername("jane123");

        assertTrue(janeContactBox.isLocked());
        assertEquals(janeContactBox.getId(), janeLogoutResponse.getUserId());
        assertEquals("jane123", janeLogoutResponse.getUsername());

        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setUsername("jane123");
        loginRequest.setPassword("word");

        assertThrows(InvalidPasswordException.class, ()->userService.login(loginRequest));
    }

    @Test
    public void userAddsContactTest() {
        assertEquals(0, userRepository.count());

        SignUpRequest signUpRequest = new SignUpRequest();
        signUpRequest.setUsername("jane123");
        signUpRequest.setPassword("password");
        signUpRequest.setConfirmPassword("password");
        SignUpResponse janeSignUpResponse = userService.signUp(signUpRequest);

        User janeContactBox = userRepository.findByUsername("jane123");

        assertEquals(1, userRepository.count());
        assertEquals("jane123", janeContactBox.getUsername());
        assertEquals("jane123", janeSignUpResponse.getUsername());

        CreateContactRequest createContactRequest = new CreateContactRequest();
        createContactRequest.setHeader("my best friend");
        createContactRequest.setUsername("jane123");
        createContactRequest.setFirstName("jill");
        createContactRequest.setLastName("smith");
        createContactRequest.setPhoneNumber("09123456789");
        createContactRequest.setEmail("jillsmith@gmail.com");
        createContactRequest.setBuildingNumber("1");
        createContactRequest.setStreet("broadway");
        createContactRequest.setState("new york");
        createContactRequest.setCountry("usa");
        createContactRequest.setNotes("Always there for me in good and bad times.");
        CreateContactResponse janeCreateContactResponse = userService.createContact(createContactRequest);

        janeContactBox = userRepository.findByUsername("jane123");

        assertEquals(1, janeContactBox.getContacts().size());
        assertEquals("jill", janeContactBox.getContacts().getFirst().getFirstName());
        assertEquals(1, contactRepository.count());
        assertEquals("09123456789", contactRepository.findAll().getFirst().getPhoneNumber());
        assertEquals("jane123", janeCreateContactResponse.getUsername());
    }

    @Test
    public void userLogsOut_UserCreatesContact_ThrowsExceptionTest() {
        assertEquals(0, userRepository.count());

        SignUpRequest signUpRequest = new SignUpRequest();
        signUpRequest.setUsername("jane123");
        signUpRequest.setPassword("password");
        signUpRequest.setConfirmPassword("password");
        SignUpResponse janeSignUpResponse = userService.signUp(signUpRequest);

        User janeContactBox = userRepository.findByUsername("jane123");

        assertEquals(1, userRepository.count());
        assertEquals("jane123", janeContactBox.getUsername());
        assertEquals("jane123", janeSignUpResponse.getUsername());

        LogoutRequest logoutRequest = new LogoutRequest();
        logoutRequest.setUsername("jane123");
        LogoutResponse janeLogoutResponse = userService.logout(logoutRequest);

        janeContactBox = userRepository.findByUsername("jane123");

        assertTrue(janeContactBox.isLocked());
        assertEquals(janeContactBox.getId(), janeLogoutResponse.getUserId());
        assertEquals("jane123", janeLogoutResponse.getUsername());

        CreateContactRequest createContactRequest = new CreateContactRequest();
        createContactRequest.setHeader("my best friend");
        createContactRequest.setUsername("jane123");
        createContactRequest.setFirstName("jill");
        createContactRequest.setPhoneNumber("09123456789");

        assertThrows(ProfileLockException.class, ()->userService.createContact(createContactRequest));
    }

    @Test
    public void nonExistentUserCreatesContact_ThrowsExceptionTest() {
        assertEquals(0, userRepository.count());

        SignUpRequest signUpRequest = new SignUpRequest();
        signUpRequest.setUsername("jane123");
        signUpRequest.setPassword("password");
        signUpRequest.setConfirmPassword("password");
        SignUpResponse janeSignUpResponse = userService.signUp(signUpRequest);

        User janeContactBox = userRepository.findByUsername("jane123");

        assertEquals(1, userRepository.count());
        assertEquals("jane123", janeContactBox.getUsername());
        assertEquals("jane123", janeSignUpResponse.getUsername());

        CreateContactRequest createContactRequest = new CreateContactRequest();
        createContactRequest.setHeader("my best friend");
        createContactRequest.setUsername("jessica123");
        createContactRequest.setFirstName("jill");
        createContactRequest.setPhoneNumber("09123456789");

        assertThrows(UserNotFoundException.class, ()->userService.createContact(createContactRequest));
    }

    @Test
    public void userCreatesContact_AllFieldsAreNullOrEmpty_ThrowsExceptionTest() {
        assertEquals(0, userRepository.count());

        SignUpRequest signUpRequest = new SignUpRequest();
        signUpRequest.setUsername("jane123");
        signUpRequest.setPassword("password");
        signUpRequest.setConfirmPassword("password");
        SignUpResponse janeSignUpResponse = userService.signUp(signUpRequest);

        User janeContactBox = userRepository.findByUsername("jane123");

        assertEquals(1, userRepository.count());
        assertEquals("jane123", janeContactBox.getUsername());
        assertEquals("jane123", janeSignUpResponse.getUsername());

        CreateContactRequest createContactRequest = new CreateContactRequest();
        createContactRequest.setUsername("jane123");

        assertThrows(IllegalArgumentException.class, ()->userService.createContact(createContactRequest));
    }

    @Test
    public void userCreatesContact_UserEditsContactTest() {
        assertEquals(0, userRepository.count());

        SignUpRequest signUpRequest = new SignUpRequest();
        signUpRequest.setUsername("jane123");
        signUpRequest.setPassword("password");
        signUpRequest.setConfirmPassword("password");
        SignUpResponse janeSignUpResponse = userService.signUp(signUpRequest);

        User janeContactBox = userRepository.findByUsername("jane123");

        assertEquals(1, userRepository.count());
        assertEquals("jane123", janeContactBox.getUsername());
        assertEquals("jane123", janeSignUpResponse.getUsername());

        CreateContactRequest createContactRequest = new CreateContactRequest();
        createContactRequest.setHeader("my best friend");
        createContactRequest.setUsername("jane123");
        createContactRequest.setFirstName("jill");
        createContactRequest.setPhoneNumber("09123456789");

        CreateContactResponse janeCreateContactResponse = userService.createContact(createContactRequest);

        janeContactBox = userRepository.findByUsername("jane123");

        assertEquals(1, janeContactBox.getContacts().size());
        assertEquals("jill", janeContactBox.getContacts().getFirst().getFirstName());
        assertEquals("09123456789", janeContactBox.getContacts().getFirst().getPhoneNumber());
        assertEquals(1, contactRepository.count());
        assertEquals("09123456789", contactRepository.findAll().getFirst().getPhoneNumber());
        assertEquals("jane123", janeCreateContactResponse.getUsername());

        assertNull(janeContactBox.getContacts().getFirst().getLastName());
        assertNull(janeContactBox.getContacts().getFirst().getAddress().getBuildingNumber());
        assertNull(janeContactBox.getContacts().getFirst().getAddress().getStreet());

        String contactId = janeContactBox.getContacts().getFirst().getId();

        UpdateContactRequest updateContactRequest = new UpdateContactRequest();
        updateContactRequest.setUsername("jane123");
        updateContactRequest.setId(contactId);
        updateContactRequest.setHeader("my best friend");
        updateContactRequest.setPhoneNumber("07123456789");
        updateContactRequest.setStreet("broadway");
        UpdateContactResponse janeUpdateContactResponse = userService.updateContact(updateContactRequest);

        janeContactBox = userRepository.findByUsername("jane123");

        assertEquals(1, janeContactBox.getContacts().size());
        assertEquals("jill", janeContactBox.getContacts().getFirst().getFirstName());
        assertEquals("07123456789", janeContactBox.getContacts().getFirst().getPhoneNumber());
        assertEquals(1, contactRepository.count());
        assertEquals("07123456789", contactRepository.findAll().getFirst().getPhoneNumber());
        assertEquals("my best friend", janeUpdateContactResponse.getHeader());
        assertEquals("broadway", janeContactBox.getContacts().getFirst().getAddress().getStreet());
    }

    @Test
    public void nonExistentUserEditsContact_ThrowsExceptionTest() {
        assertEquals(0, userRepository.count());

        SignUpRequest signUpRequest = new SignUpRequest();
        signUpRequest.setUsername("jane123");
        signUpRequest.setPassword("password");
        signUpRequest.setConfirmPassword("password");
        SignUpResponse janeSignUpResponse = userService.signUp(signUpRequest);

        User janeContactBox = userRepository.findByUsername("jane123");

        assertEquals(1, userRepository.count());
        assertEquals("jane123", janeContactBox.getUsername());
        assertEquals("jane123", janeSignUpResponse.getUsername());

        CreateContactRequest createContactRequest = new CreateContactRequest();
        createContactRequest.setHeader("my best friend");
        createContactRequest.setUsername("jane123");
        createContactRequest.setFirstName("jill");
        createContactRequest.setPhoneNumber("09123456789");

        CreateContactResponse janeCreateContactResponse = userService.createContact(createContactRequest);

        janeContactBox = userRepository.findByUsername("jane123");

        assertEquals(1, janeContactBox.getContacts().size());
        assertEquals("jill", janeContactBox.getContacts().getFirst().getFirstName());
        assertEquals("09123456789", janeContactBox.getContacts().getFirst().getPhoneNumber());
        assertEquals(1, contactRepository.count());
        assertEquals("09123456789", contactRepository.findAll().getFirst().getPhoneNumber());
        assertEquals("jane123", janeCreateContactResponse.getUsername());

        assertNull(janeContactBox.getContacts().getFirst().getLastName());
        assertNull(janeContactBox.getContacts().getFirst().getAddress().getBuildingNumber());
        assertNull(janeContactBox.getContacts().getFirst().getAddress().getStreet());

        String contactId = janeContactBox.getContacts().getFirst().getId();

        UpdateContactRequest updateContactRequest = new UpdateContactRequest();
        updateContactRequest.setUsername("jessica123");
        updateContactRequest.setId(contactId);
        updateContactRequest.setPhoneNumber("07123456789");
        updateContactRequest.setStreet("broadway");

        assertThrows(UserNotFoundException.class, ()->userService.updateContact(updateContactRequest));
    }

    @Test
    public void userLogsOutEditsContact_ThrowsExceptionTest() {
        assertEquals(0, userRepository.count());

        SignUpRequest signUpRequest = new SignUpRequest();
        signUpRequest.setUsername("jane123");
        signUpRequest.setPassword("password");
        signUpRequest.setConfirmPassword("password");
        SignUpResponse janeSignUpResponse = userService.signUp(signUpRequest);

        User janeContactBox = userRepository.findByUsername("jane123");

        assertEquals(1, userRepository.count());
        assertEquals("jane123", janeContactBox.getUsername());
        assertEquals("jane123", janeSignUpResponse.getUsername());

        CreateContactRequest createContactRequest = new CreateContactRequest();
        createContactRequest.setHeader("my best friend");
        createContactRequest.setUsername("jane123");
        createContactRequest.setFirstName("jill");
        createContactRequest.setPhoneNumber("09123456789");

        CreateContactResponse janeCreateContactResponse = userService.createContact(createContactRequest);

        janeContactBox = userRepository.findByUsername("jane123");

        assertEquals(1, janeContactBox.getContacts().size());
        assertEquals("jill", janeContactBox.getContacts().getFirst().getFirstName());
        assertEquals("09123456789", janeContactBox.getContacts().getFirst().getPhoneNumber());
        assertEquals(1, contactRepository.count());
        assertEquals("09123456789", contactRepository.findAll().getFirst().getPhoneNumber());
        assertEquals("jane123", janeCreateContactResponse.getUsername());

        assertNull(janeContactBox.getContacts().getFirst().getLastName());
        assertNull(janeContactBox.getContacts().getFirst().getAddress().getBuildingNumber());
        assertNull(janeContactBox.getContacts().getFirst().getAddress().getStreet());

        String contactId = janeContactBox.getContacts().getFirst().getId();

        LogoutRequest logoutRequest = new LogoutRequest();
        logoutRequest.setUsername("jane123");
        LogoutResponse janeLogoutResponse = userService.logout(logoutRequest);

        janeContactBox = userRepository.findByUsername("jane123");

        assertTrue(janeContactBox.isLocked());
        assertEquals(janeContactBox.getId(), janeLogoutResponse.getUserId());
        assertEquals("jane123", janeLogoutResponse.getUsername());

        UpdateContactRequest updateContactRequest = new UpdateContactRequest();
        updateContactRequest.setUsername("jane123");
        updateContactRequest.setId(contactId);
        updateContactRequest.setPhoneNumber("07123456789");
        updateContactRequest.setStreet("broadway");

        assertThrows(ProfileLockException.class, ()->userService.updateContact(updateContactRequest));
    }

    @Test
    public void userCreatesContact_UserViewsContactTest() {
        assertEquals(0, userRepository.count());

        SignUpRequest signUpRequest = new SignUpRequest();
        signUpRequest.setUsername("jane123");
        signUpRequest.setPassword("password");
        signUpRequest.setConfirmPassword("password");
        SignUpResponse janeSignUpResponse = userService.signUp(signUpRequest);

        User janeContactBox = userRepository.findByUsername("jane123");

        assertEquals(1, userRepository.count());
        assertEquals("jane123", janeContactBox.getUsername());
        assertEquals("jane123", janeSignUpResponse.getUsername());

        CreateContactRequest createContactRequest = new CreateContactRequest();
        createContactRequest.setHeader("my best friend");
        createContactRequest.setUsername("jane123");
        createContactRequest.setFirstName("jill");
        createContactRequest.setPhoneNumber("09123456789");

        CreateContactResponse janeCreateContactResponse = userService.createContact(createContactRequest);

        janeContactBox = userRepository.findByUsername("jane123");

        assertEquals(1, janeContactBox.getContacts().size());
        assertEquals("jill", janeContactBox.getContacts().getFirst().getFirstName());
        assertEquals("09123456789", janeContactBox.getContacts().getFirst().getPhoneNumber());
        assertEquals(1, contactRepository.count());
        assertEquals("09123456789", contactRepository.findAll().getFirst().getPhoneNumber());
        assertEquals("jane123", janeCreateContactResponse.getUsername());

        String contactId = janeContactBox.getContacts().getFirst().getId();

        ViewContactRequest viewContactRequest = new ViewContactRequest();
        viewContactRequest.setUsername("jane123");
        viewContactRequest.setContactId(contactId);
        ViewContactResponse viewContactResponse = userService.viewContact(viewContactRequest);

        janeContactBox = userRepository.findByUsername("jane123");

        assertEquals(1, janeContactBox.getContacts().size());
        assertEquals(janeContactBox.getId(), viewContactResponse.getUserId());
        assertEquals(janeContactBox.getContacts().getFirst(), viewContactResponse.getContact());
    }

    @Test
    public void userLogsOut_UserViewsContact_ThrowsExceptionTest() {
        assertEquals(0, userRepository.count());

        SignUpRequest signUpRequest = new SignUpRequest();
        signUpRequest.setUsername("jane123");
        signUpRequest.setPassword("password");
        signUpRequest.setConfirmPassword("password");
        SignUpResponse janeSignUpResponse = userService.signUp(signUpRequest);

        User janeContactBox = userRepository.findByUsername("jane123");

        assertEquals(1, userRepository.count());
        assertEquals("jane123", janeContactBox.getUsername());
        assertEquals("jane123", janeSignUpResponse.getUsername());

        CreateContactRequest createContactRequest = new CreateContactRequest();
        createContactRequest.setHeader("my best friend");
        createContactRequest.setUsername("jane123");
        createContactRequest.setFirstName("jill");
        createContactRequest.setPhoneNumber("09123456789");

        CreateContactResponse janeCreateContactResponse = userService.createContact(createContactRequest);

        janeContactBox = userRepository.findByUsername("jane123");

        assertEquals(1, janeContactBox.getContacts().size());
        assertEquals("jill", janeContactBox.getContacts().getFirst().getFirstName());
        assertEquals("09123456789", janeContactBox.getContacts().getFirst().getPhoneNumber());
        assertEquals(1, contactRepository.count());
        assertEquals("09123456789", contactRepository.findAll().getFirst().getPhoneNumber());
        assertEquals("jane123", janeCreateContactResponse.getUsername());

        String contactId = janeContactBox.getContacts().getFirst().getId();

        LogoutRequest logoutRequest = new LogoutRequest();
        logoutRequest.setUsername("jane123");
        LogoutResponse janeLogoutResponse = userService.logout(logoutRequest);

        janeContactBox = userRepository.findByUsername("jane123");

        assertTrue(janeContactBox.isLocked());
        assertEquals(janeContactBox.getId(), janeLogoutResponse.getUserId());
        assertEquals("jane123", janeLogoutResponse.getUsername());

        ViewContactRequest viewContactRequest = new ViewContactRequest();
        viewContactRequest.setUsername("jane123");
        viewContactRequest.setContactId(contactId);

        assertThrows(ProfileLockException.class, ()->userService.viewContact(viewContactRequest));
    }

    @Test
    public void nonExistentUserViewsContact_ThrowsExceptionTest() {
        assertEquals(0, userRepository.count());

        SignUpRequest signUpRequest = new SignUpRequest();
        signUpRequest.setUsername("jane123");
        signUpRequest.setPassword("password");
        signUpRequest.setConfirmPassword("password");
        SignUpResponse janeSignUpResponse = userService.signUp(signUpRequest);

        User janeContactBox = userRepository.findByUsername("jane123");

        assertEquals(1, userRepository.count());
        assertEquals("jane123", janeContactBox.getUsername());
        assertEquals("jane123", janeSignUpResponse.getUsername());

        CreateContactRequest createContactRequest = new CreateContactRequest();
        createContactRequest.setHeader("my best friend");
        createContactRequest.setUsername("jane123");
        createContactRequest.setFirstName("jill");
        createContactRequest.setPhoneNumber("09123456789");

        CreateContactResponse janeCreateContactResponse = userService.createContact(createContactRequest);

        janeContactBox = userRepository.findByUsername("jane123");
        String contactId = janeContactBox.getContacts().getFirst().getId();

        assertEquals(1, janeContactBox.getContacts().size());
        assertEquals("jill", janeContactBox.getContacts().getFirst().getFirstName());
        assertEquals("09123456789", janeContactBox.getContacts().getFirst().getPhoneNumber());
        assertEquals(1, contactRepository.count());
        assertEquals("09123456789", contactRepository.findAll().getFirst().getPhoneNumber());
        assertEquals("jane123", janeCreateContactResponse.getUsername());

        ViewContactRequest viewContactRequest = new ViewContactRequest();
        viewContactRequest.setUsername("jessica123");
        viewContactRequest.setContactId(contactId);

        assertThrows(UserNotFoundException.class, ()->userService.viewContact(viewContactRequest));
    }

    @Test
    public void userViewsNonExistentContact_ThrowsExceptionTest() {
        assertEquals(0, userRepository.count());

        SignUpRequest signUpRequest = new SignUpRequest();
        signUpRequest.setUsername("jane123");
        signUpRequest.setPassword("password");
        signUpRequest.setConfirmPassword("password");
        SignUpResponse janeSignUpResponse = userService.signUp(signUpRequest);

        User janeContactBox = userRepository.findByUsername("jane123");

        assertEquals(1, userRepository.count());
        assertEquals("jane123", janeContactBox.getUsername());
        assertEquals("jane123", janeSignUpResponse.getUsername());

        CreateContactRequest createContactRequest = new CreateContactRequest();
        createContactRequest.setHeader("my best friend");
        createContactRequest.setUsername("jane123");
        createContactRequest.setFirstName("jill");
        createContactRequest.setPhoneNumber("09123456789");

        CreateContactResponse janeCreateContactResponse = userService.createContact(createContactRequest);

        janeContactBox = userRepository.findByUsername("jane123");

        assertEquals(1, janeContactBox.getContacts().size());
        assertEquals("jill", janeContactBox.getContacts().getFirst().getFirstName());
        assertEquals("09123456789", janeContactBox.getContacts().getFirst().getPhoneNumber());
        assertEquals(1, contactRepository.count());
        assertEquals("09123456789", contactRepository.findAll().getFirst().getPhoneNumber());
        assertEquals("jane123", janeCreateContactResponse.getUsername());

        ViewContactRequest viewContactRequest = new ViewContactRequest();
        viewContactRequest.setUsername("jane123");
        viewContactRequest.setContactId("non existent contact");

        assertThrows(ContactNotFoundException.class, ()->userService.viewContact(viewContactRequest));
    }

    @Test
    public void userCreatesContact_UserDeletesContactTest() {
        assertEquals(0, userRepository.count());

        SignUpRequest signUpRequest = new SignUpRequest();
        signUpRequest.setUsername("jane123");
        signUpRequest.setPassword("password");
        signUpRequest.setConfirmPassword("password");
        SignUpResponse janeSignUpResponse = userService.signUp(signUpRequest);

        User janeContactBox = userRepository.findByUsername("jane123");

        assertEquals(1, userRepository.count());
        assertEquals("jane123", janeContactBox.getUsername());
        assertEquals("jane123", janeSignUpResponse.getUsername());

        CreateContactRequest createContactRequest = new CreateContactRequest();
        createContactRequest.setHeader("my best friend");
        createContactRequest.setUsername("jane123");
        createContactRequest.setFirstName("jill");
        createContactRequest.setPhoneNumber("09123456789");

        CreateContactResponse janeCreateContactResponse = userService.createContact(createContactRequest);

        janeContactBox = userRepository.findByUsername("jane123");
        String contactId = janeContactBox.getContacts().getFirst().getId();

        assertEquals(1, janeContactBox.getContacts().size());
        assertEquals("jill", janeContactBox.getContacts().getFirst().getFirstName());
        assertEquals("09123456789", janeContactBox.getContacts().getFirst().getPhoneNumber());
        assertEquals(1, contactRepository.count());
        assertEquals("09123456789", contactRepository.findAll().getFirst().getPhoneNumber());
        assertEquals("jane123", janeCreateContactResponse.getUsername());

        DeleteContactRequest deleteContactRequest = new DeleteContactRequest();
        deleteContactRequest.setContactId(contactId);
        deleteContactRequest.setUsername("JANE123");
        deleteContactRequest.setPassword("password");
        DeleteContactResponse deleteContactResponse = userService.deleteContact(deleteContactRequest);

        janeContactBox = userRepository.findByUsername("jane123");

        assertEquals(0, contactRepository.count());
        assertEquals("jane123", deleteContactResponse.getUsername());
        assertEquals(janeContactBox.getId(), deleteContactResponse.getUserId());
        assertEquals(0, janeContactBox.getContacts().size());
    }

    @Test
    public void nonExistentUserDeletesContact_ThrowsExceptionTest() {
        assertEquals(0, userRepository.count());

        SignUpRequest signUpRequest = new SignUpRequest();
        signUpRequest.setUsername("jane123");
        signUpRequest.setPassword("password");
        signUpRequest.setConfirmPassword("password");
        SignUpResponse janeSignUpResponse = userService.signUp(signUpRequest);

        User janeContactBox = userRepository.findByUsername("jane123");

        assertEquals(1, userRepository.count());
        assertEquals("jane123", janeContactBox.getUsername());
        assertEquals("jane123", janeSignUpResponse.getUsername());

        CreateContactRequest createContactRequest = new CreateContactRequest();
        createContactRequest.setHeader("my best friend");
        createContactRequest.setUsername("jane123");
        createContactRequest.setFirstName("jill");
        createContactRequest.setPhoneNumber("09123456789");

        CreateContactResponse janeCreateContactResponse = userService.createContact(createContactRequest);

        janeContactBox = userRepository.findByUsername("jane123");
        String contactId = janeContactBox.getContacts().getFirst().getId();

        assertEquals(1, janeContactBox.getContacts().size());
        assertEquals("jill", janeContactBox.getContacts().getFirst().getFirstName());
        assertEquals("09123456789", janeContactBox.getContacts().getFirst().getPhoneNumber());
        assertEquals(1, contactRepository.count());
        assertEquals("09123456789", contactRepository.findAll().getFirst().getPhoneNumber());
        assertEquals("jane123", janeCreateContactResponse.getUsername());

        DeleteContactRequest deleteContactRequest = new DeleteContactRequest();
        deleteContactRequest.setContactId(contactId);
        deleteContactRequest.setUsername("jessica123");
        deleteContactRequest.setPassword("password");

        assertThrows(UserNotFoundException.class, ()->userService.deleteContact(deleteContactRequest));
    }

    @Test
    public void userDeletesNonExistentContact_ThrowsExceptionTest() {
        assertEquals(0, userRepository.count());

        SignUpRequest signUpRequest = new SignUpRequest();
        signUpRequest.setUsername("jane123");
        signUpRequest.setPassword("password");
        signUpRequest.setConfirmPassword("password");
        SignUpResponse janeSignUpResponse = userService.signUp(signUpRequest);

        User janeContactBox = userRepository.findByUsername("jane123");

        assertEquals(1, userRepository.count());
        assertEquals("jane123", janeContactBox.getUsername());
        assertEquals("jane123", janeSignUpResponse.getUsername());

        CreateContactRequest createContactRequest = new CreateContactRequest();
        createContactRequest.setHeader("my best friend");
        createContactRequest.setUsername("jane123");
        createContactRequest.setFirstName("jill");
        createContactRequest.setPhoneNumber("09123456789");

        CreateContactResponse janeCreateContactResponse = userService.createContact(createContactRequest);

        janeContactBox = userRepository.findByUsername("jane123");

        assertEquals(1, janeContactBox.getContacts().size());
        assertEquals("jill", janeContactBox.getContacts().getFirst().getFirstName());
        assertEquals("09123456789", janeContactBox.getContacts().getFirst().getPhoneNumber());
        assertEquals(1, contactRepository.count());
        assertEquals("09123456789", contactRepository.findAll().getFirst().getPhoneNumber());
        assertEquals("jane123", janeCreateContactResponse.getUsername());

        DeleteContactRequest deleteContactRequest = new DeleteContactRequest();
        deleteContactRequest.setContactId("non existent contact id");
        deleteContactRequest.setUsername("jane123");
        deleteContactRequest.setPassword("password");

        assertThrows(ContactNotFoundException.class, ()->userService.deleteContact(deleteContactRequest));
    }

    @Test
    public void userDeletesContact_PasswordIsInvalid_ThrowsExceptionTest() {
        assertEquals(0, userRepository.count());

        SignUpRequest signUpRequest = new SignUpRequest();
        signUpRequest.setUsername("jane123");
        signUpRequest.setPassword("password");
        signUpRequest.setConfirmPassword("password");
        SignUpResponse janeSignUpResponse = userService.signUp(signUpRequest);

        User janeContactBox = userRepository.findByUsername("jane123");

        assertEquals(1, userRepository.count());
        assertEquals("jane123", janeContactBox.getUsername());
        assertEquals("jane123", janeSignUpResponse.getUsername());

        CreateContactRequest createContactRequest = new CreateContactRequest();
        createContactRequest.setHeader("my best friend");
        createContactRequest.setUsername("jane123");
        createContactRequest.setFirstName("jill");
        createContactRequest.setPhoneNumber("09123456789");

        CreateContactResponse janeCreateContactResponse = userService.createContact(createContactRequest);

        janeContactBox = userRepository.findByUsername("jane123");
        String contactId = janeContactBox.getContacts().getFirst().getId();

        assertEquals(1, janeContactBox.getContacts().size());
        assertEquals("jill", janeContactBox.getContacts().getFirst().getFirstName());
        assertEquals("09123456789", janeContactBox.getContacts().getFirst().getPhoneNumber());
        assertEquals(1, contactRepository.count());
        assertEquals("09123456789", contactRepository.findAll().getFirst().getPhoneNumber());
        assertEquals("jane123", janeCreateContactResponse.getUsername());

        LogoutRequest logoutRequest = new LogoutRequest();
        logoutRequest.setUsername("jane123");
        LogoutResponse janeLogoutResponse = userService.logout(logoutRequest);

        janeContactBox = userRepository.findByUsername("jane123");

        assertTrue(janeContactBox.isLocked());
        assertEquals(janeContactBox.getId(), janeLogoutResponse.getUserId());
        assertEquals("jane123", janeLogoutResponse.getUsername());

        DeleteContactRequest deleteContactRequest = new DeleteContactRequest();
        deleteContactRequest.setContactId(contactId);
        deleteContactRequest.setUsername("jane123");
        deleteContactRequest.setPassword("password");

        assertThrows(ProfileLockException.class, ()->userService.deleteContact(deleteContactRequest));
    }

    @Test
    public void userLogsOut_DeletesContact_ThrowsExceptionTest() {
        assertEquals(0, userRepository.count());

        SignUpRequest signUpRequest = new SignUpRequest();
        signUpRequest.setUsername("jane123");
        signUpRequest.setPassword("password");
        signUpRequest.setConfirmPassword("password");
        SignUpResponse janeSignUpResponse = userService.signUp(signUpRequest);

        User janeContactBox = userRepository.findByUsername("jane123");

        assertEquals(1, userRepository.count());
        assertEquals("jane123", janeContactBox.getUsername());
        assertEquals("jane123", janeSignUpResponse.getUsername());

        CreateContactRequest createContactRequest = new CreateContactRequest();
        createContactRequest.setHeader("my best friend");
        createContactRequest.setUsername("jane123");
        createContactRequest.setFirstName("jill");
        createContactRequest.setPhoneNumber("09123456789");

        CreateContactResponse janeCreateContactResponse = userService.createContact(createContactRequest);

        janeContactBox = userRepository.findByUsername("jane123");
        String contactId = janeContactBox.getContacts().getFirst().getId();

        assertEquals(1, janeContactBox.getContacts().size());
        assertEquals("jill", janeContactBox.getContacts().getFirst().getFirstName());
        assertEquals("09123456789", janeContactBox.getContacts().getFirst().getPhoneNumber());
        assertEquals(1, contactRepository.count());
        assertEquals("09123456789", contactRepository.findAll().getFirst().getPhoneNumber());
        assertEquals("jane123", janeCreateContactResponse.getUsername());

        DeleteContactRequest deleteContactRequest = new DeleteContactRequest();
        deleteContactRequest.setContactId(contactId);
        deleteContactRequest.setUsername("jane123");
        deleteContactRequest.setPassword("word");

        assertThrows(InvalidPasswordException.class, ()->userService.deleteContact(deleteContactRequest));
    }

    @Test
    public void userFindAllContactsTest() {
        assertEquals(0, userRepository.count());

        SignUpRequest signUpRequest = new SignUpRequest();
        signUpRequest.setUsername("jane123");
        signUpRequest.setPassword("password");
        signUpRequest.setConfirmPassword("password");
        SignUpResponse janeSignUpResponse = userService.signUp(signUpRequest);

        User janeContactBox = userRepository.findByUsername("jane123");

        assertEquals(1, userRepository.count());
        assertEquals("jane123", janeContactBox.getUsername());
        assertEquals("jane123", janeSignUpResponse.getUsername());

        CreateContactRequest createContactRequest = new CreateContactRequest();
        createContactRequest.setHeader("my best friend");
        createContactRequest.setUsername("jane123");
        createContactRequest.setFirstName("jill");
        createContactRequest.setLastName("smith");
        createContactRequest.setPhoneNumber("09123456789");
        createContactRequest.setEmail("jillsmith@gmail.com");
        CreateContactResponse janeCreateContactResponse = userService.createContact(createContactRequest);

        janeContactBox = userRepository.findByUsername("jane123");

        assertEquals(1, janeContactBox.getContacts().size());
        assertEquals("jill", janeContactBox.getContacts().getFirst().getFirstName());
        assertEquals(1, contactRepository.count());
        assertEquals("09123456789", contactRepository.findAll().getFirst().getPhoneNumber());
        assertEquals("jane123", janeCreateContactResponse.getUsername());

        FindAllContactRequest findAllContactRequest = new FindAllContactRequest();
        findAllContactRequest.setUsername("jane123");
        FindAllContactsResponse janeFindAllContactsResponse = userService.findAllContacts(findAllContactRequest);

        assertEquals(1, janeFindAllContactsResponse.getContacts().size());
        assertEquals("jane123", janeFindAllContactsResponse.getUsername());
    }

    @Test
    public void nonExistentUser_FindsContact_ThrowsExceptionTest() {
        assertEquals(0, userRepository.count());

        SignUpRequest signUpRequest = new SignUpRequest();
        signUpRequest.setUsername("jane123");
        signUpRequest.setPassword("password");
        signUpRequest.setConfirmPassword("password");
        SignUpResponse janeSignUpResponse = userService.signUp(signUpRequest);

        User janeContactBox = userRepository.findByUsername("jane123");

        assertEquals(1, userRepository.count());
        assertEquals("jane123", janeContactBox.getUsername());
        assertEquals("jane123", janeSignUpResponse.getUsername());

        CreateContactRequest createContactRequest = new CreateContactRequest();
        createContactRequest.setHeader("my best friend");
        createContactRequest.setUsername("jane123");
        createContactRequest.setFirstName("jill");
        createContactRequest.setLastName("smith");
        createContactRequest.setPhoneNumber("09123456789");
        createContactRequest.setEmail("jillsmith@gmail.com");
        CreateContactResponse janeCreateContactResponse = userService.createContact(createContactRequest);

        janeContactBox = userRepository.findByUsername("jane123");

        assertEquals(1, janeContactBox.getContacts().size());
        assertEquals("jill", janeContactBox.getContacts().getFirst().getFirstName());
        assertEquals(1, contactRepository.count());
        assertEquals("09123456789", contactRepository.findAll().getFirst().getPhoneNumber());
        assertEquals("jane123", janeCreateContactResponse.getUsername());

        FindAllContactRequest findAllContactRequest = new FindAllContactRequest();
        findAllContactRequest.setUsername("jessica123");

        assertThrows(UserNotFoundException.class, ()->userService.findAllContacts(findAllContactRequest));

    }


}