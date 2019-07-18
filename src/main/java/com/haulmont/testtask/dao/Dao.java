package com.haulmont.testtask.dao;

import java.util.List;

public interface Dao<E> {
    List<E> findAll();
    E findById(long id);
    void save(E obj);
    boolean delete(E obj);
}
