package com.andaro.jobstracker.model;

import java.util.Objects;

public final class ContractorSearchCriteria {
    private final String firstName;
    private final String lastName;
    private final String zipCode;
    private final String phoneNumber;
    private final String emailAddress;
    private final TradeType tradeType;
    private final String address;
    private final String city;
    private final String state;

    private ContractorSearchCriteria(String firstName,
                                     String lastName,
                                     String zipCode,
                                     String phoneNumber,
                                     String emailAddress,
                                     TradeType tradeType,
                                     String address,
                                     String city,
                                     String state) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.zipCode = zipCode;
        this.phoneNumber = phoneNumber;
        this.emailAddress = emailAddress;
        this.tradeType = tradeType;
        this.address = address;
        this.city = city;
        this.state = state;
    }

    public static ContractorSearchCriteria from(String firstName,
                                                String lastName,
                                                String zipCode,
                                                String phoneNumber,
                                                String emailAddress,
                                                String tradeTypeValue,
                                                String address,
                                                String city,
                                                String state) {
        return new ContractorSearchCriteria(normalize(firstName),
                normalize(lastName),
                normalize(zipCode),
                normalize(phoneNumber),
                normalize(emailAddress),
                parseTradeType(tradeTypeValue),
                normalize(address),
                normalize(city),
                normalize(state));
    }

    public ContractorSearchCriteria requireFilters() {
        if (!hasAnyFilter()) {
            throw new IllegalArgumentException("At least one search parameter must be provided");
        }
        return this;
    }

    public boolean hasAnyFilter() {
        return firstName != null
                || lastName != null
                || zipCode != null
                || phoneNumber != null
                || emailAddress != null
                || tradeType != null
                || address != null
                || city != null
                || state != null;
    }

    public String getFirstName() { return firstName; }
    public String getLastName() { return lastName; }
    public String getZipCode() { return zipCode; }
    public String getPhoneNumber() { return phoneNumber; }
    public String getEmailAddress() { return emailAddress; }
    public TradeType getTradeType() { return tradeType; }
    public String getAddress() { return address; }
    public String getCity() { return city; }
    public String getState() { return state; }

    public boolean matches(Contractor contractor) {
        if (contractor == null) return false;
        if (firstName != null && !equalsIgnoreCase(contractor.getFirstName(), firstName)) return false;
        if (lastName != null && !equalsIgnoreCase(contractor.getLastName(), lastName)) return false;
        if (zipCode != null && !equalsIgnoreCase(contractor.getZipCode(), zipCode)) return false;
        if (phoneNumber != null && !equalsIgnoreCase(contractor.getPhoneNumber(), phoneNumber)) return false;
        if (emailAddress != null && !equalsIgnoreCase(contractor.getEmailAddress(), emailAddress)) return false;
        if (tradeType != null && contractor.getTradeType() != tradeType) return false;
        if (address != null && !equalsIgnoreCase(contractor.getAddress(), address)) return false;
        if (city != null && !equalsIgnoreCase(contractor.getCity(), city)) return false;
        if (state != null && !equalsIgnoreCase(contractor.getState(), state)) return false;
        return true;
    }

    private static String normalize(String value) {
        if (value == null) return null;
        String trimmed = value.trim();
        return trimmed.isEmpty() ? null : trimmed;
    }

    private static TradeType parseTradeType(String value) {
        if (value == null) return null;
        return TradeType.fromString(value);
    }

    private static boolean equalsIgnoreCase(String a, String b) {
        if (a == null || b == null) return false;
        return a.equalsIgnoreCase(b);
    }

    @Override
    public String toString() {
        return "ContractorSearchCriteria{" +
                "firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", zipCode='" + zipCode + '\'' +
                ", phoneNumber='" + phoneNumber + '\'' +
                ", emailAddress='" + emailAddress + '\'' +
                ", tradeType=" + tradeType +
                ", address='" + address + '\'' +
                ", city='" + city + '\'' +
                ", state='" + state + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ContractorSearchCriteria that = (ContractorSearchCriteria) o;
        return Objects.equals(firstName, that.firstName) &&
                Objects.equals(lastName, that.lastName) &&
                Objects.equals(zipCode, that.zipCode) &&
                Objects.equals(phoneNumber, that.phoneNumber) &&
                Objects.equals(emailAddress, that.emailAddress) &&
                tradeType == that.tradeType &&
                Objects.equals(address, that.address) &&
                Objects.equals(city, that.city) &&
                Objects.equals(state, that.state);
    }

    @Override
    public int hashCode() {
        return Objects.hash(firstName, lastName, zipCode, phoneNumber, emailAddress, tradeType, address, city, state);
    }
}
