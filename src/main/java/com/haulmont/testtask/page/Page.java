package com.haulmont.testtask.page;

import com.vaadin.ui.Button;
import com.vaadin.ui.Component;

public interface Page {
    Button getPageButton();
    Button getAddButton();
    Component getGrid();
}
