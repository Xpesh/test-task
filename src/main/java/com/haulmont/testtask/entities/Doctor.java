package com.haulmont.testtask.entities;

public class Doctor extends Human {
    private String specialization;

    public Doctor(String firstName, String middleName, String lastName, String specialization) {
        super(firstName, middleName, lastName);
        this.specialization = specialization;
    }

    public String getSpecialization() {
        return specialization;
    }

    @Override
    public String toString() {
        final StringBuffer sb = new StringBuffer(specialization);
        sb.append(" (").append(super.toString()).append(')');
        return sb.toString();
    }
}
