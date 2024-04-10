package com.contactBox.utilities;

import com.contactBox.data.models.Contact;
import com.contactBox.data.models.User;
import com.contactBox.exceptions.ContactNotFoundException;

public class ContactFinder {

    public static Contact findContactInUserList(String contactId, User user) {
        for (int count = 0; count < user.getContacts().size(); count++) {
            if (user.getContacts().get(count).getId().equals(contactId)){
                return user.getContacts().get(count);
            }
        }
        throw new ContactNotFoundException("Contact does not exist. Please Try Again.");
    }
}
