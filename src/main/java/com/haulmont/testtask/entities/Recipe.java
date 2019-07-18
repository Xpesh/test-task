package com.haulmont.testtask.entities;

import java.time.LocalDate;

public class Recipe {
    private long id =-1;
    private long idPatient;
    private long idDoctor;
    private String description;
    private LocalDate dateOfCreation;
    private LocalDate validity;
    private Priority priority;

    public Recipe(String description, long idPatient, long idDoctor, LocalDate dateOfCreation, LocalDate validity, Priority priority) {
        this.description = description;
        this.idPatient = idPatient;
        this.idDoctor = idDoctor;
        this.dateOfCreation = dateOfCreation;
        this.validity = validity;
        this.priority = priority;
    }

    public String getDescription() {
        return description;
    }

    public long getIdPatient() {
        return idPatient;
    }

    public long getIdDoctor() {
        return idDoctor;
    }

    public LocalDate getCreationDate() {
        return dateOfCreation;
    }

    public LocalDate getValidity() {
        return validity;
    }

    public Priority getPriority() {
        return priority;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "RecipeDao{" +
                "description='" + description + '\'' +
                ", patient=" + idPatient +
                ", doctor=" + idDoctor +
                ", dateOfCreation=" + dateOfCreation +
                ", validity=" + validity +
                ", priority=" + priority +
                '}';
    }
}
