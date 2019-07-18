package com.haulmont.testtask.entities;

public class Human {
    private String firstName;
    private String middleName;
    private String lastName;
    private long id = -1;

    public Human(String firstName, String middleName, String lastName) {
        this.firstName = firstName;
        this.middleName = middleName;
        this.lastName = lastName;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getMiddleName() {
        return middleName;
    }

    public String getLastName() {
        return lastName;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    @Override
    public String toString() {
        final StringBuffer sb = new StringBuffer(lastName);
        sb.append(" ").append(firstName).append(" ").append(middleName);
        return sb.toString();
    }
}
