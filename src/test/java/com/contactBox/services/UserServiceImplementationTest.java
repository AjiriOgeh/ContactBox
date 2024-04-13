package com.contactBox.services;

import com.contactBox.data.models.User;
import com.contactBox.data.repositories.ContactRepository;
import com.contactBox.data.repositories.UserRepository;
import com.contactBox.dataTransferObjects.requests.*;
import com.contactBox.dataTransferObjects.responses.*;
import com.contactBox.exceptions.*;
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

        SignUpRequest signUpRequest = new SignUpRequest();
        signUpRequest.setUsername("jane123");
        signUpRequest.setPassword("password");
        signUpRequest.setConfirmPassword("password");
        userService.signUp(signUpRequest);

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
        userService.createContact(createContactRequest);
    }

    @Test
    public void userSignsUp_UserIsSavedTest() {
        SignUpRequest signUpRequest = new SignUpRequest();
        signUpRequest.setUsername("jessica123");
        signUpRequest.setPassword("password");
        signUpRequest.setConfirmPassword("password");
        SignUpResponse jessicaSignUpResponse = userService.signUp(signUpRequest);

        User jessicaContactBox = userRepository.findByUsername("jessica123");

        assertEquals(2, userRepository.count());
        assertEquals("jessica123", jessicaContactBox.getUsername());
        assertEquals("jessica123", jessicaSignUpResponse.getUsername());
    }

    @Test
    public void userSignsUp_UsernameIsNull_ThrowsExceptionTest() {
        SignUpRequest signUpRequest = new SignUpRequest();
        signUpRequest.setUsername(null);
        signUpRequest.setPassword("password");
        signUpRequest.setConfirmPassword("password");

        assertThrows(IllegalArgumentException.class, ()->userService.signUp(signUpRequest));
    }

    @Test
    public void userSignsUp_UsernameIsEmpty_ThrowsExceptionTest() {
        SignUpRequest signUpRequest = new SignUpRequest();
        signUpRequest.setUsername("");
        signUpRequest.setPassword("password");
        signUpRequest.setConfirmPassword("password");

        assertThrows(IllegalArgumentException.class, ()->userService.signUp(signUpRequest));
    }

    @Test
    public void userSignsUp_UsernameContainsSpaceCharacter_ThrowsExceptionTest() {
        SignUpRequest signUpRequest = new SignUpRequest();
        signUpRequest.setUsername("jessica 123");
        signUpRequest.setPassword("password");
        signUpRequest.setConfirmPassword("password");

        assertThrows(IllegalArgumentException.class, ()->userService.signUp(signUpRequest));
    }

    @Test
    public void userSignsUp_UsernameExists_ThrowsExceptionTest() {
        SignUpRequest signUpRequest = new SignUpRequest();
        signUpRequest.setUsername("jane123");
        signUpRequest.setPassword("password123");
        signUpRequest.setConfirmPassword("password123");

        assertThrows(IllegalArgumentException.class, ()->userService.signUp(signUpRequest));
    }

    @Test
    public void userSignsUp_PasswordIsNull_ThrowsExceptionTest() {
        SignUpRequest signUpRequest = new SignUpRequest();
        signUpRequest.setUsername("jessica123");
        signUpRequest.setPassword(null);
        signUpRequest.setConfirmPassword(null);

        assertThrows(IllegalArgumentException.class, ()->userService.signUp(signUpRequest));
    }

    @Test
    public void userSignsUp_PasswordIsEmpty_ThrowsExceptionTest() {
        SignUpRequest signUpRequest = new SignUpRequest();
        signUpRequest.setUsername("jessica123");
        signUpRequest.setPassword("");
        signUpRequest.setConfirmPassword("");

        assertThrows(IllegalArgumentException.class, ()->userService.signUp(signUpRequest));
    }

    @Test
    public void userSignsUp_PasswordIsLessThan6Characters_ThrowsExceptionTest() {
        SignUpRequest signUpRequest = new SignUpRequest();
        signUpRequest.setUsername("jessica123");
        signUpRequest.setPassword("word");
        signUpRequest.setConfirmPassword("word");

        assertThrows(IllegalArgumentException.class, ()->userService.signUp(signUpRequest));
    }

    @Test
    public void userSignsUp_PasswordDoesNotMatch_ConfirmPassword_ThrowsExceptionTest() {
        SignUpRequest signUpRequest = new SignUpRequest();
        signUpRequest.setUsername("jane123");
        signUpRequest.setPassword("password");
        signUpRequest.setConfirmPassword("word");

        assertThrows(IllegalArgumentException.class, ()->userService.signUp(signUpRequest));
    }

    @Test
    public void userSignsUp_UserLogsOutTest() {
        LogoutRequest logoutRequest = new LogoutRequest();
        logoutRequest.setUsername("jane123");
        LogoutResponse janeLogoutResponse = userService.logout(logoutRequest);

        User janeContactBox = userRepository.findByUsername("jane123");

        assertTrue(janeContactBox.isLocked());
        assertEquals(janeContactBox.getId(), janeLogoutResponse.getUserId());
        assertEquals("jane123", janeLogoutResponse.getUsername());
    }

    @Test
    public void nonExistentUserLogsOut_ThrowsExceptionTest() {
        LogoutRequest logoutRequest = new LogoutRequest();
        logoutRequest.setUsername("jill123");

        assertThrows(UserNotFoundException.class, ()->userService.logout(logoutRequest));
    }

    @Test
    public void userLogsOut_UserLogsInTest() {
        LogoutRequest logoutRequest = new LogoutRequest();
        logoutRequest.setUsername("jane123");
        LogoutResponse janeLogoutResponse = userService.logout(logoutRequest);

        User janeContactBox = userRepository.findByUsername("jane123");

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
    public void nonExistentUserLogsIn_ThrowsExceptionTest() {
        LogoutRequest logoutRequest = new LogoutRequest();
        logoutRequest.setUsername("jane123");
        LogoutResponse janeLogoutResponse = userService.logout(logoutRequest);

        User janeContactBox = userRepository.findByUsername("jane123");

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
        LogoutRequest logoutRequest = new LogoutRequest();
        logoutRequest.setUsername("jane123");
        LogoutResponse janeLogoutResponse = userService.logout(logoutRequest);

        User janeContactBox = userRepository.findByUsername("jane123");

        assertTrue(janeContactBox.isLocked());
        assertEquals(janeContactBox.getId(), janeLogoutResponse.getUserId());
        assertEquals("jane123", janeLogoutResponse.getUsername());

        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setUsername("jane123");
        loginRequest.setPassword("word");

        assertThrows(InvalidPasswordException.class, ()->userService.login(loginRequest));
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
        CreateContactResponse janeCreateContactResponse = userService.createContact(createContactRequest);

        User janeContactBox = userRepository.findByUsername("jane123");

        assertEquals("jessica", janeContactBox.getContacts().get(1).getFirstName());
        assertEquals(2, contactRepository.count());
        assertEquals("08123456789", contactRepository.findAll().get(1).getPhoneNumber());
        assertEquals(2, janeContactBox.getContacts().size());
        assertEquals("jane123", janeCreateContactResponse.getUsername());
    }

    @Test
    public void userLogsOut_UserCreatesContact_ThrowsExceptionTest() {
        LogoutRequest logoutRequest = new LogoutRequest();
        logoutRequest.setUsername("jane123");
        LogoutResponse janeLogoutResponse = userService.logout(logoutRequest);

        User janeContactBox = userRepository.findByUsername("jane123");

        assertTrue(janeContactBox.isLocked());
        assertEquals(janeContactBox.getId(), janeLogoutResponse.getUserId());
        assertEquals("jane123", janeLogoutResponse.getUsername());

        CreateContactRequest createContactRequest = new CreateContactRequest();
        createContactRequest.setUsername("jane123");
        createContactRequest.setFirstName("jessica");
        createContactRequest.setLastName("brown");
        createContactRequest.setPhoneNumber("08123456789");
        createContactRequest.setEmail("jessicabrown@gmail.com");

        assertThrows(ProfileLockException.class, ()->userService.createContact(createContactRequest));
    }

    @Test
    public void nonExistentUserCreatesContact_ThrowsExceptionTest() {
        CreateContactRequest createContactRequest = new CreateContactRequest();
        createContactRequest.setUsername("jill123");
        createContactRequest.setFirstName("jessica");
        createContactRequest.setLastName("brown");
        createContactRequest.setPhoneNumber("08123456789");
        createContactRequest.setEmail("jessicabrown@gmail.com");

        assertThrows(UserNotFoundException.class, ()->userService.createContact(createContactRequest));
    }

    @Test
    public void userCreatesContact_PhoneNumberIsInvalid_ThrowsExceptionTest() {
        CreateContactRequest createContactRequest = new CreateContactRequest();
        createContactRequest.setUsername("jane123");
        createContactRequest.setFirstName("jessica");
        createContactRequest.setLastName("brown");
        createContactRequest.setPhoneNumber("08123abcdef");
        createContactRequest.setEmail("jessicabrown@gmail.com");

        assertThrows(IllegalArgumentException.class, ()->userService.createContact(createContactRequest));
    }

    @Test
    public void userCreatesContact_AllFieldsAreNullOrEmpty_ThrowsExceptionTest() {
        CreateContactRequest createContactRequest = new CreateContactRequest();
        createContactRequest.setUsername("jane123");

        assertThrows(IllegalArgumentException.class, ()->userService.createContact(createContactRequest));
    }

    @Test
    public void userUpdatesContactTest() {
        User janeContactBox = userRepository.findByUsername("jane123");
        String contactId = janeContactBox.getContacts().getFirst().getId();

        UpdateContactRequest updateContactRequest = new UpdateContactRequest();
        updateContactRequest.setUsername("jane123");
        updateContactRequest.setId(contactId);
        updateContactRequest.setEmail("jillsmith@yahoo.com");
        updateContactRequest.setPhoneNumber("07123456789");
        updateContactRequest.setStreet("wall street");
        UpdateContactResponse janeUpdateContactResponse = userService.updateContact(updateContactRequest);

        janeContactBox = userRepository.findByUsername("jane123");

        assertEquals(1, janeContactBox.getContacts().size());
        assertEquals("jill", janeContactBox.getContacts().getFirst().getFirstName());
        assertEquals("jillsmith@yahoo.com", janeContactBox.getContacts().getFirst().getEmail());
        assertEquals("07123456789", contactRepository.findAll().getFirst().getPhoneNumber());
        assertEquals("wall street", janeContactBox.getContacts().getFirst().getAddress().getStreet());
        assertEquals(janeContactBox.getId(), janeUpdateContactResponse.getUserId());
    }

    @Test
    public void nonExistentUser_UpdatesContact_ThrowsExceptionTest() {
        User janeContactBox = userRepository.findByUsername("jane123");
        String contactId = janeContactBox.getContacts().getFirst().getId();

        UpdateContactRequest updateContactRequest = new UpdateContactRequest();
        updateContactRequest.setUsername("jessica123");
        updateContactRequest.setId(contactId);
        updateContactRequest.setEmail("jillsmith@yahoo.com");
        updateContactRequest.setPhoneNumber("07123456789");
        updateContactRequest.setStreet("wall street");

        assertThrows(UserNotFoundException.class, ()->userService.updateContact(updateContactRequest));
    }

    @Test
    public void userUpdatesNonExistentContact_ThrowsExceptionTest() {
        UpdateContactRequest updateContactRequest = new UpdateContactRequest();
        updateContactRequest.setUsername("jane123");
        updateContactRequest.setId("non existent contact id");
        updateContactRequest.setEmail("jillsmith@yahoo.com");
        updateContactRequest.setPhoneNumber("07123456789");
        updateContactRequest.setStreet("wall street");

        assertThrows(ContactNotFoundException.class, ()->userService.updateContact(updateContactRequest));
    }

    @Test
    public void userUpdatesContact_PhoneNumberIsInvalid_ThrowsExceptionTest() {
        User janeContactBox = userRepository.findByUsername("jane123");
        String contactId = janeContactBox.getContacts().getFirst().getId();

        UpdateContactRequest updateContactRequest = new UpdateContactRequest();
        updateContactRequest.setUsername("jane123");
        updateContactRequest.setId(contactId);
        updateContactRequest.setEmail("jillsmith@yahoo.com");
        updateContactRequest.setPhoneNumber("07123abcdef");
        updateContactRequest.setStreet("wall street");

        assertThrows(IllegalArgumentException.class, ()->userService.updateContact(updateContactRequest));
    }

    @Test
    public void userLogsOut_UpdatesContact_ThrowsExceptionTest() {
        User janeContactBox = userRepository.findByUsername("jane123");
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
    public void userFindsContactByIdTest() {
        User janeContactBox = userRepository.findByUsername("jane123");
        String contactId = janeContactBox.getContacts().getFirst().getId();

        FindContactByIdRequest findContactByIdRequest = new FindContactByIdRequest();
        findContactByIdRequest.setUsername("jane123");
        findContactByIdRequest.setContactId(contactId);
        FindContactByIdResponse janeFindContactByIdResponse = userService.findContactById(findContactByIdRequest);

        janeContactBox = userRepository.findByUsername("jane123");

        assertEquals(1, janeContactBox.getContacts().size());
        assertEquals(janeContactBox.getId(), janeFindContactByIdResponse.getUserId());
        assertEquals(janeContactBox.getContacts().getFirst(), janeFindContactByIdResponse.getContact());
    }

    @Test
    public void userLogsOut_UserFindsContactById_ThrowsExceptionTest() {
        User janeContactBox = userRepository.findByUsername("jane123");
        String contactId = janeContactBox.getContacts().getFirst().getId();

        LogoutRequest logoutRequest = new LogoutRequest();
        logoutRequest.setUsername("jane123");
        LogoutResponse janeLogoutResponse = userService.logout(logoutRequest);

        janeContactBox = userRepository.findByUsername("jane123");

        assertTrue(janeContactBox.isLocked());
        assertEquals(janeContactBox.getId(), janeLogoutResponse.getUserId());
        assertEquals("jane123", janeLogoutResponse.getUsername());

        FindContactByIdRequest findContactByIdRequest = new FindContactByIdRequest();
        findContactByIdRequest.setUsername("jane123");
        findContactByIdRequest.setContactId(contactId);

        assertThrows(ProfileLockException.class, ()->userService.findContactById(findContactByIdRequest));
    }

    @Test
    public void nonExistentUserFindsContactById_ThrowsExceptionTest() {
        User janeContactBox = userRepository.findByUsername("jane123");
        String contactId = janeContactBox.getContacts().getFirst().getId();

        FindContactByIdRequest findContactByIdRequest = new FindContactByIdRequest();
        findContactByIdRequest.setUsername("jessica123");
        findContactByIdRequest.setContactId(contactId);

        assertThrows(UserNotFoundException.class, ()->userService.findContactById(findContactByIdRequest));
    }

    @Test
    public void userFindsContact_WithNonExistentId_ThrowsExceptionTest() {
        FindContactByIdRequest findContactByIdRequest = new FindContactByIdRequest();
        findContactByIdRequest.setUsername("jane123");
        findContactByIdRequest.setContactId("non existent contact");

        assertThrows(ContactNotFoundException.class, ()->userService.findContactById(findContactByIdRequest));
    }

    @Test
    public void userDeletesContactTest() {
        User janeContactBox = userRepository.findByUsername("jane123");
        String contactId = janeContactBox.getContacts().getFirst().getId();

        DeleteContactRequest deleteContactRequest = new DeleteContactRequest();
        deleteContactRequest.setContactId(contactId);
        deleteContactRequest.setUsername("jane123");
        deleteContactRequest.setPassword("password");
        DeleteContactResponse deleteContactResponse = userService.deleteContact(deleteContactRequest);

        janeContactBox = userRepository.findByUsername("jane123");

        assertEquals(0, contactRepository.count());
        assertEquals("jane123", deleteContactResponse.getUsername());
        assertEquals(janeContactBox.getId(), deleteContactResponse.getUserId());
        assertEquals(0, janeContactBox.getContacts().size());
    }

    @Test
    public void nonExistentUser_DeletesContact_ThrowsExceptionTest() {
        User janeContactBox = userRepository.findByUsername("jane123");
        String contactId = janeContactBox.getContacts().getFirst().getId();

        DeleteContactRequest deleteContactRequest = new DeleteContactRequest();
        deleteContactRequest.setContactId(contactId);
        deleteContactRequest.setUsername("jessica123");
        deleteContactRequest.setPassword("password");

        assertThrows(UserNotFoundException.class, ()->userService.deleteContact(deleteContactRequest));
    }

    @Test
    public void userDeletes_NonExistentContact_ThrowsExceptionTest() {
        DeleteContactRequest deleteContactRequest = new DeleteContactRequest();
        deleteContactRequest.setUsername("jane123");
        deleteContactRequest.setContactId("non existent contact id");
        deleteContactRequest.setPassword("password");

        assertThrows(ContactNotFoundException.class, ()->userService.deleteContact(deleteContactRequest));
    }

    @Test
    public void userLogsOut_DeletesContact_ThrowsExceptionTest() {
        User janeContactBox = userRepository.findByUsername("jane123");
        String contactId = janeContactBox.getContacts().getFirst().getId();

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
    public void userDeletesContact_PasswordIsInvalid_ThrowsExceptionTest() {
        User janeContactBox = userRepository.findByUsername("jane123");
        String contactId = janeContactBox.getContacts().getFirst().getId();

        DeleteContactRequest deleteContactRequest = new DeleteContactRequest();
        deleteContactRequest.setContactId(contactId);
        deleteContactRequest.setUsername("jane123");
        deleteContactRequest.setPassword("word");

        assertThrows(InvalidPasswordException.class, ()->userService.deleteContact(deleteContactRequest));
    }

    @Test
    public void userFindsAllContactsTest() {
        FindAllContactRequest findAllContactRequest = new FindAllContactRequest();
        findAllContactRequest.setUsername("jane123");
        FindAllContactsResponse janeFindAllContactsResponse = userService.findAllContacts(findAllContactRequest);

        assertEquals(1, janeFindAllContactsResponse.getContacts().size());
        assertEquals("jill", janeFindAllContactsResponse.getContacts().getFirst().getFirstName());
        assertEquals("jane123", janeFindAllContactsResponse.getUsername());
    }

    @Test
    public void nonExistentUser_FindsAllContact_ThrowsExceptionTest() {
        FindAllContactRequest findAllContactRequest = new FindAllContactRequest();
        findAllContactRequest.setUsername("jessica123");

        assertThrows(UserNotFoundException.class, ()->userService.findAllContacts(findAllContactRequest));
    }

    @Test
    public void userLogsOut_FindsAllContacts_ThrowsExceptionTest() {
        LogoutRequest logoutRequest = new LogoutRequest();
        logoutRequest.setUsername("jane123");
        LogoutResponse janeLogoutResponse = userService.logout(logoutRequest);

        User janeContactBox = userRepository.findByUsername("jane123");

        assertTrue(janeContactBox.isLocked());
        assertEquals(janeContactBox.getId(), janeLogoutResponse.getUserId());
        assertEquals("jane123", janeLogoutResponse.getUsername());

        FindAllContactRequest findAllContactRequest = new FindAllContactRequest();
        findAllContactRequest.setUsername("jane123");

        assertThrows(ProfileLockException.class, ()->userService.findAllContacts(findAllContactRequest));
    }

    @Test
    public void userFindsContactByNameTest() {
        FindContactByNameRequest findContactByNameRequest = new FindContactByNameRequest();
        findContactByNameRequest.setUsername("jane123");
        findContactByNameRequest.setName("jill");
        FindContactByNameResponse janeFindContactByNameResponse = userService.findContactByName(findContactByNameRequest);

        assertEquals(1, janeFindContactByNameResponse.getContacts().size());
        assertEquals("jill", janeFindContactByNameResponse.getContacts().getFirst().getFirstName());
        assertEquals("jane123", janeFindContactByNameResponse.getUsername());
    }

    @Test
    public void userFindsTwoContactsByNameTest() {
        CreateContactRequest createContactRequest = new CreateContactRequest();
        createContactRequest.setUsername("jane123");
        createContactRequest.setFirstName("jill");
        createContactRequest.setLastName("green");
        createContactRequest.setPhoneNumber("06123456789");
        createContactRequest.setEmail("jillgreen@gmail.com");
        createContactRequest.setBuildingNumber("3");
        createContactRequest.setStreet("fifth avenue");
        createContactRequest.setCity("new york city");
        createContactRequest.setState("new york");
        createContactRequest.setCountry("usa");
        createContactRequest.setNotes("a wonderful listener");
        CreateContactResponse janeCreateContactResponse = userService.createContact(createContactRequest);

        User janeContactBox = userRepository.findByUsername("jane123");

        assertEquals("green", janeContactBox.getContacts().get(1).getLastName());
        assertEquals(2, contactRepository.count());
        assertEquals("06123456789", contactRepository.findAll().get(1).getPhoneNumber());
        assertEquals(2, janeContactBox.getContacts().size());
        assertEquals("jane123", janeCreateContactResponse.getUsername());

        FindContactByNameRequest findContactByNameRequest = new FindContactByNameRequest();
        findContactByNameRequest.setUsername("jane123");
        findContactByNameRequest.setName("jill");
        FindContactByNameResponse janeFindContactByNameResponse = userService.findContactByName(findContactByNameRequest);

        assertEquals(2, janeFindContactByNameResponse.getContacts().size());
        assertEquals("jane123", janeFindContactByNameResponse.getUsername());
        assertEquals("jill", janeFindContactByNameResponse.getContacts().getFirst().getFirstName());
        assertEquals("green", janeFindContactByNameResponse.getContacts().get(1).getLastName());
    }

    @Test
    public void nonExistentUser_FindContactByName_ThrowsExceptionTest() {
        FindContactByNameRequest findContactByNameRequest = new FindContactByNameRequest();
        findContactByNameRequest.setUsername("jessica123");
        findContactByNameRequest.setName("jill");

        assertThrows(UserNotFoundException.class, ()->userService.findContactByName(findContactByNameRequest));
    }

    @Test
    public void userFindsContact_WithNonExistentName_ThrowsExceptionTest() {
        FindContactByNameRequest findContactByNameRequest = new FindContactByNameRequest();
        findContactByNameRequest.setUsername("jane123");
        findContactByNameRequest.setName("jessica");

        assertThrows(ContactNotFoundException.class,()-> userService.findContactByName(findContactByNameRequest));
    }

    @Test
    public void userFindsContactByPhoneNumberTest() {
        FindContactByPhoneNumberRequest findContactByPhoneNumberRequest = new FindContactByPhoneNumberRequest();
        findContactByPhoneNumberRequest.setUsername("jane123");
        findContactByPhoneNumberRequest.setPhoneNumber("09123456789");
        FindContactByPhoneNumberResponse janeFindContactByPhoneNumberResponse = userService.findContactByPhoneNumber(findContactByPhoneNumberRequest);

        assertEquals(1, janeFindContactByPhoneNumberResponse.getContacts().size());
        assertEquals("jill", janeFindContactByPhoneNumberResponse.getContacts().getFirst().getFirstName());
        assertEquals("jane123", janeFindContactByPhoneNumberResponse.getUsername());
    }

    @Test
    public void nonExistentUser_FindsContactByPhoneNumber_ThrowsExceptionTest() {
        FindContactByPhoneNumberRequest findContactByPhoneNumberRequest = new FindContactByPhoneNumberRequest();
        findContactByPhoneNumberRequest.setUsername("jessica123");
        findContactByPhoneNumberRequest.setPhoneNumber("09123456789");

        assertThrows(UserNotFoundException.class, ()-> userService.findContactByPhoneNumber(findContactByPhoneNumberRequest));
    }

    @Test
    public void userFindsContact_WithNonExistentPhoneNumber_ThrowsExceptionTest() {
        FindContactByPhoneNumberRequest findContactByPhoneNumberRequest = new FindContactByPhoneNumberRequest();
        findContactByPhoneNumberRequest.setUsername("jane123");
        findContactByPhoneNumberRequest.setPhoneNumber("06123456789");

        assertThrows(ContactNotFoundException.class, ()-> userService.findContactByPhoneNumber(findContactByPhoneNumberRequest));
    }

}