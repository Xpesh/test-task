package com.haulmont.testtask.dao;

import com.haulmont.testtask.entities.Doctor;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class DoctorDao implements Dao<Doctor> {

    private Connection connection;
    public DoctorDao(Connection connection) {
        this.connection = connection;
    }

    @Override
    public List<Doctor> findAll() {
        List<Doctor> doctors = new ArrayList<>();
        try {
            PreparedStatement statement = connection.prepareStatement(
                    "SELECT * FROM PUBLIC.DOCTOR");
            statement.execute();
            ResultSet rs = statement.getResultSet();

            while (rs.next())
                doctors.add(mapRow(rs));

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return doctors;
    }

    @Override
    public Doctor findById(long id) {
        try {
            PreparedStatement statement = connection.prepareStatement(
                    "SELECT * FROM PUBLIC.DOCTOR WHERE DOCTOR_ID=?");
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
    public void save(Doctor doctor) {
        try {
            PreparedStatement statement;
            if (doctor.getId()<0) {
                statement = connection.prepareStatement(
                        "INSERT INTO PUBLIC.DOCTOR (FIRST_NAME, LAST_NAME, MIDDLE_NAME, SPECIALIZATION)" +
                                " VALUES(?,?,?,?)");
            } else {
                statement = connection.prepareStatement(
                        "UPDATE PUBLIC.DOCTOR SET FIRST_NAME=?, LAST_NAME=?, " +
                                "MIDDLE_NAME=?, SPECIALIZATION=? " +
                                "WHERE DOCTOR_ID=?");
                statement.setLong(5, doctor.getId());
            }
            statement.setString(1, doctor.getFirstName());
            statement.setString(2, doctor.getLastName());
            statement.setString(3, doctor.getMiddleName());
            statement.setString(4, doctor.getSpecialization());
            statement.execute();
            statement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean delete(Doctor doctor) {
        try {
            PreparedStatement statement = connection.prepareStatement(
                    "DELETE FROM PUBLIC.DOCTOR WHERE DOCTOR_ID=?");
            statement.setLong(1, doctor.getId());
            statement.execute();
            return true;
        } catch (SQLException e) {
            return false;
        }
    }

    private Doctor mapRow(ResultSet rs) throws SQLException {
        Doctor doctor = new Doctor(
                rs.getString("FIRST_NAME"),
                rs.getString("MIDDLE_NAME"),
                rs.getString("LAST_NAME"),
                rs.getString("SPECIALIZATION")
        );
        doctor.setId(rs.getLong("doctor_id"));
        return doctor;
    }
}
