package com.contactBox.services;

import com.contactBox.dataTransferObjects.requests.*;
import com.contactBox.dataTransferObjects.responses.*;

public interface UserService {
    SignUpResponse signUp(SignUpRequest signUpRequest);

    LogoutResponse logout(LogoutRequest logoutRequest);

    LoginResponse login(LoginRequest loginRequest);

    CreateContactResponse createContact(CreateContactRequest createContactRequest);

    UpdateContactResponse updateContact(UpdateContactRequest updateContactRequest);

    FindContactByIdResponse findContactById(FindContactByIdRequest findContactByIdRequest);

    DeleteContactResponse deleteContact(DeleteContactRequest deleteContactRequest);

    FindAllContactsResponse findAllContacts(FindAllContactRequest findAllContactRequest);

    FindContactByNameResponse findContactByName(FindContactByNameRequest findContactByNameRequest);

    FindContactByPhoneNumberResponse findContactByPhoneNumber(FindContactByPhoneNumberRequest findContactByPhoneNumberRequest);
}
