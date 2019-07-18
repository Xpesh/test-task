package com.haulmont.testtask.dao;

import com.haulmont.testtask.entities.Patient;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class PatientDao implements Dao<Patient> {
    private Connection connection;

    public PatientDao(Connection connection) {
        this.connection = connection;
    }

    @Override
    public List<Patient> findAll() {
        List<Patient> patients = new ArrayList<>();
        try {
            PreparedStatement statement = connection.prepareStatement(
                    "SELECT * FROM PUBLIC.PATIENT");
            statement.execute();
            ResultSet rs = statement.getResultSet();

            while (rs.next())
                patients.add(mapRow(rs));

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return patients;
    }

    @Override
    public Patient findById(long id) {
        try {
            PreparedStatement statement = connection.prepareStatement(
                    "SELECT * FROM PUBLIC.PATIENT WHERE PATIENT_ID=?");
            statement.setLong(1, id);
            statement.execute();
            ResultSet rs = statement.getResultSet();
            rs.next();
            return mapRow(rs);
        } catch (SQLException e) {
            return null;
        }
    }

    @Override
    public void save(Patient patient) {
        try {
            PreparedStatement statement;
            if (patient.getId()<0) {
                statement = connection.prepareStatement(
                        "INSERT INTO PUBLIC.PATIENT (FIRST_NAME, LAST_NAME, MIDDLE_NAME, PHONE_NUMBER)" +
                                " VALUES(?,?,?,?)");
            } else {
                statement = connection.prepareStatement(
                        "UPDATE PUBLIC.PATIENT SET FIRST_NAME=?, LAST_NAME=?, " +
                                "MIDDLE_NAME=?, PHONE_NUMBER=? " +
                                "WHERE PATIENT_ID=?");
                statement.setLong(5, patient.getId());
            }
            statement.setString(1, patient.getFirstName());
            statement.setString(2, patient.getLastName());
            statement.setString(3, patient.getMiddleName());
            statement.setString(4, patient.getPhoneNumber());
            statement.execute();
            statement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean delete(Patient patient) {
        try {
            PreparedStatement statement = connection.prepareStatement(
                    "DELETE FROM PUBLIC.PATIENT WHERE PATIENT_ID=?");
            statement.setLong(1, patient.getId());
            statement.execute();
            return true;
        } catch (SQLException e) {
            return false;
        }
    }

    private Patient mapRow(ResultSet rs) throws SQLException {
        Patient patient = new Patient(
                rs.getString("FIRST_NAME"),
                rs.getString("MIDDLE_NAME"),
                rs.getString("LAST_NAME"),
                rs.getString("PHONE_NUMBER")
        );
        patient.setId(rs.getLong("patient_id"));
        return patient;
    }
}
