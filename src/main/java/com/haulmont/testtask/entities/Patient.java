package com.haulmont.testtask.entities;

public class Patient extends Human {
    private String phoneNumber;

    public Patient(String firstName, String middleName, String lastName, String phoneNumber) {
        super(firstName, middleName, lastName);
        this.phoneNumber = phoneNumber;
    }

    @Override
    public String toString() {
        final StringBuffer sb = new StringBuffer(super.toString());
        sb.append(" (").append(phoneNumber).append(')');
        return sb.toString();
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }
}
