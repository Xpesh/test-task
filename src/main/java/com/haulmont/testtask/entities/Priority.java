package com.haulmont.testtask.entities;

import java.util.LinkedList;
import java.util.List;

public enum Priority {
    NORMAL ("NORMAL"),
    CITO ("CITO"),
    STATIM("STATIM");

    private String title;

    Priority(String title) {
        this.title = title;
    }

    @Override
    public String toString() {
        return title;
    }

    public static List<Priority> getList(){
        List<Priority> priorityList = new LinkedList<>();
        priorityList.add(NORMAL);
        priorityList.add(CITO);
        priorityList.add(STATIM);
        return priorityList;
    }
}
