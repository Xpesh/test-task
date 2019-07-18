package com.haulmont.testtask.dao;

import com.haulmont.testtask.entities.Patient;
import org.junit.Assert;
import org.junit.jupiter.api.Test;

import java.util.List;

class PatientDaoTest {

    @Test
    void findAll() {
        PatientDao patientDao = new PatientDao(DataBaseManager.getConnection());
        List<Patient> list = patientDao.findAll();
        list.forEach(System.out::println);
    }

    @Test
    void findById() {
        PatientDao patientDao = new PatientDao(DataBaseManager.getConnection());
        System.out.println(patientDao.findById(0));
    }

    @Test
    void save() {
        PatientDao patientDao = new PatientDao(DataBaseManager.getConnection());
        Patient patient = new Patient("Александр","Борисович",
                "Власенко","+71234567890");
        int before = patientDao.findAll().size();
        patientDao.save(patient);
        Assert.assertEquals(before+1,patientDao.findAll().size());
    }

    @Test
    void delete() {
        PatientDao patientDao = new PatientDao(DataBaseManager.getConnection());
        int n = patientDao.findAll().size();
        Patient patient = patientDao.findById(n-1);
        patientDao.delete(patient);
        Assert.assertEquals(n-1,patientDao.findAll().size());
    }
}