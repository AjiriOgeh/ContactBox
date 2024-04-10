package com.contactBox.controllers;

import com.contactBox.dataTransferObjects.requests.*;
import com.contactBox.dataTransferObjects.responses.ApiResponse;
import com.contactBox.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping("/SignUp")
    public ResponseEntity<?> signUp(@RequestBody SignUpRequest signUpRequest) {
        try {
            return new ResponseEntity<>(new ApiResponse(true, userService.signUp(signUpRequest)), HttpStatus.CREATED);
        }
        catch (Exception error) {
            return new ResponseEntity<>(new ApiResponse(false, error.getMessage()), HttpStatus.BAD_REQUEST);
        }
    }

    @PatchMapping("/Logout")
    public ResponseEntity<?> logout(@RequestBody LogoutRequest logoutRequest) {
        try {
            return new ResponseEntity<>(new ApiResponse(true, userService.logout(logoutRequest)), HttpStatus.OK );
        }
        catch (Exception error) {
            return new ResponseEntity<>(new ApiResponse(false, error.getMessage()), HttpStatus.BAD_REQUEST);
        }
    }

    @PatchMapping("/Login")
    public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest) {
        try {
            return new ResponseEntity<>(new ApiResponse(true, userService.login(loginRequest)), HttpStatus.OK );
        }
        catch (Exception error) {
            return new ResponseEntity<>(new ApiResponse(false, error.getMessage()), HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/CreateContact")
    public ResponseEntity<?> createContact(@RequestBody CreateContactRequest createContactRequest) {
        try {
            return new ResponseEntity<>(new ApiResponse(true, userService.createContact(createContactRequest)), HttpStatus.CREATED);

        } catch (Exception error) {
            return new ResponseEntity<>(new ApiResponse(false, error.getMessage()), HttpStatus.BAD_REQUEST);
        }
    }

    @PatchMapping("/UpdateContact")
    public ResponseEntity<?> updateContact(@RequestBody UpdateContactRequest updateContactRequest) {
        try {
            return new ResponseEntity<>(new ApiResponse(true, userService.updateContact(updateContactRequest)), HttpStatus.OK);

        } catch (Exception error) {
            return new ResponseEntity<>(new ApiResponse(false, error.getMessage()), HttpStatus.BAD_REQUEST);
        }

    }

    @GetMapping("/ViewContact")
    public ResponseEntity<?> viewContact(@RequestBody ViewContactRequest viewContactRequest) {
        try {
            return new ResponseEntity<>(new ApiResponse(true, userService.viewContact(viewContactRequest)), HttpStatus.OK);
        }
        catch (Exception error) {
            return new ResponseEntity<>(new ApiResponse(false, error.getMessage()), HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping("/DeleteContact")
    public ResponseEntity<?> deleteContact(@RequestBody DeleteContactRequest deleteContactRequest) {
        try {
            return new ResponseEntity<>(new ApiResponse(true, userService.deleteContact(deleteContactRequest)), HttpStatus.OK);
        } catch (Exception error) {
            return new ResponseEntity<>(new ApiResponse(false, error.getMessage()), HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/FindAllContacts")
    public ResponseEntity<?> findAllContacts(@RequestBody FindAllContactRequest findAllContactRequest) {
        try {
            return new ResponseEntity<>(new ApiResponse(true, userService.findAllContacts(findAllContactRequest)), HttpStatus.OK);
        }
        catch (Exception error) {
            return new ResponseEntity<>(new ApiResponse(false, error.getMessage()), HttpStatus.BAD_REQUEST);
        }
    }
}
