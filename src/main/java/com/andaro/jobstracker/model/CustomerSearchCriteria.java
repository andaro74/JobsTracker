package com.andaro.jobstracker.model;

import java.util.Objects;

public final class CustomerSearchCriteria {

    private final String firstName;
    private final String lastName;
    private final String zipCode;
    private final String phoneNumber;
    private final String emailAddress;

    private CustomerSearchCriteria(String firstName,
                                   String lastName,
                                   String zipCode,
                                   String phoneNumber,
                                   String emailAddress) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.zipCode = zipCode;
        this.phoneNumber = phoneNumber;
        this.emailAddress = emailAddress;
    }

    public static CustomerSearchCriteria from(String firstName,
                                              String lastName,
                                              String zipCode,
                                              String phoneNumber,
                                              String emailAddress) {
        return new CustomerSearchCriteria(normalize(firstName),
                normalize(lastName),
                normalize(zipCode),
                normalize(phoneNumber),
                normalize(emailAddress));
    }

    public boolean hasAnyFilter() {
        return firstName != null
                || lastName != null
                || zipCode != null
                || phoneNumber != null
                || emailAddress != null;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getZipCode() {
        return zipCode;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public String getEmailAddress() {
        return emailAddress;
    }

    public CustomerSearchCriteria requireFilters() {
        if (!hasAnyFilter()) {
            throw new IllegalArgumentException("At least one search parameter must be provided");
        }
        return this;
    }

    private static String normalize(String value) {
        if (value == null) {
            return null;
        }
        String trimmed = value.trim();
        return trimmed.isEmpty() ? null : trimmed;
    }

    @Override
    public String toString() {
        return "CustomerSearchCriteria{" +
                "firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", zipCode='" + zipCode + '\'' +
                ", phoneNumber='" + phoneNumber + '\'' +
                ", emailAddress='" + emailAddress + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CustomerSearchCriteria that = (CustomerSearchCriteria) o;
        return Objects.equals(firstName, that.firstName) &&
                Objects.equals(lastName, that.lastName) &&
                Objects.equals(zipCode, that.zipCode) &&
                Objects.equals(phoneNumber, that.phoneNumber) &&
                Objects.equals(emailAddress, that.emailAddress);
    }

    @Override
    public int hashCode() {
        return Objects.hash(firstName, lastName, zipCode, phoneNumber, emailAddress);
    }
}

