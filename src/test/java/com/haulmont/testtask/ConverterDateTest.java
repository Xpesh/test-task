package com.haulmont.testtask;

import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;

class ConverterDateTest {

    @Test
    void convert() {
        Date date = ConverterDate.convert(LocalDate.now());
        System.out.println(date);
    }

    @Test
    void convert1() {
        LocalDate localDate = ConverterDate.convert(new Date());
        System.out.println(localDate);
    }
}