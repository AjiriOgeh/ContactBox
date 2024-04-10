package com.contactBox.data.repositories;

import com.contactBox.data.models.Contact;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class ContactRepositoryTest {

    @Autowired
    private ContactRepository contactRepository;

    @BeforeEach
    public void setUp() {
        contactRepository.deleteAll();
    }

    @Test
    public void contactIsCreated_ContactIsStored() {
        Contact contact = new Contact();
        contactRepository.save(contact);
        assertEquals(1, contactRepository.count());
    }

}