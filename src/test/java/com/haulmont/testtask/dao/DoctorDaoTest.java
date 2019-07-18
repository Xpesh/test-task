package com.haulmont.testtask.dao;

import com.haulmont.testtask.entities.Doctor;
import org.junit.Assert;
import java.util.List;
import org.junit.jupiter.api.Test;

class DoctorDaoTest {

    @Test
    void findAll() {
        DoctorDao doctorDao = new DoctorDao(DataBaseManager.getConnection());
        List<Doctor> list = doctorDao.findAll();
        list.forEach(System.out::println);
    }

    @Test
    void findById() {
        DoctorDao doctorDao = new DoctorDao(DataBaseManager.getConnection());
        System.out.println(doctorDao.findById(0));
    }

    @Test
    void save() {
        DoctorDao doctorDao = new DoctorDao(DataBaseManager.getConnection());
        Doctor doctor = new Doctor("Александр","Борисович",
                "Власенко","Гастроэнтеролог");
        int before = doctorDao.findAll().size();
        doctorDao.save(doctor);
        Assert.assertEquals(before+1,doctorDao.findAll().size());
    }

    @Test
    void delete() {
        DoctorDao doctorDao = new DoctorDao(DataBaseManager.getConnection());
        int n = doctorDao.findAll().size();
        Doctor doctor = doctorDao.findById(n-1);
        doctorDao.delete(doctor);
        Assert.assertEquals(n-1,doctorDao.findAll().size());
    }
}