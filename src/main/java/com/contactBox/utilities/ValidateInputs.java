package com.contactBox.utilities;

import com.contactBox.dataTransferObjects.requests.CreateContactRequest;

public class ValidateInputs {

    public static boolean areAllFieldsNullOrEmpty(CreateContactRequest createContactRequest) {
        String[] createContactInputs = {createContactRequest.getFirstName(), createContactRequest.getLastName(),
                createContactRequest.getPhoneNumber(), createContactRequest.getEmail(), createContactRequest.getNotes(),
                createContactRequest.getBuildingNumber(), createContactRequest.getStreet(), createContactRequest.getCity(),
                createContactRequest.getState(), createContactRequest.getCountry()};

        for (String createContactInput : createContactInputs) {
            if (createContactInput != null && !createContactInput.isEmpty()) return false;
        }
        return true;
    }

    public static boolean doesPhoneNumberContainNonDigitCharacters(String phoneNumber) {
        return !phoneNumber.matches("\\d+");
    }


}
