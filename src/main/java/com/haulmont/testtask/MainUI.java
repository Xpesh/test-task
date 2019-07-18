package com.haulmont.testtask;

import com.haulmont.testtask.dao.*;
import com.haulmont.testtask.entities.Doctor;
import com.haulmont.testtask.entities.Patient;
import com.haulmont.testtask.entities.Recipe;
import com.haulmont.testtask.page.DoctorPage;
import com.haulmont.testtask.page.Page;
import com.haulmont.testtask.page.PatientPage;
import com.haulmont.testtask.page.RecipePage;
import com.vaadin.annotations.Theme;
import com.vaadin.server.VaadinRequest;
import com.vaadin.ui.*;
import com.vaadin.ui.themes.ValoTheme;

import java.sql.Connection;

@Theme(ValoTheme.THEME_NAME)
public class MainUI extends UI {
    private Connection connection = DataBaseManager.getConnection();
    private Dao<Doctor> doctorDao = new DoctorDao(connection);
    private Dao<Recipe> recipeDao = new RecipeDao(connection);
    private Dao<Patient> patientDao = new PatientDao(connection);
    private VerticalLayout layout;
    private HorizontalLayout horizontalLayoutButton;
    @Override
    protected void init(VaadinRequest request) {
        Page doctorPage = new DoctorPage(doctorDao,recipeDao,this);
        Page patientPage = new PatientPage(patientDao,this);
        Page recipePage = new RecipePage(recipeDao,patientDao,doctorDao,this);
        layout = new VerticalLayout();
        layout.setSizeFull();
        layout.setSizeUndefined();
        layout.setWidth(100,Unit.PERCENTAGE);
        layout.setMargin(true);
        horizontalLayoutButton = new HorizontalLayout();
        horizontalLayoutButton.addComponent(doctorPage.getPageButton());
        horizontalLayoutButton.addComponent(patientPage.getPageButton());
        horizontalLayoutButton.addComponent(recipePage.getPageButton());
        horizontalLayoutButton.setHeight(70,Unit.PIXELS);
        refreshPage(doctorPage);
    }

    public void refreshPage(Page page){
        layout.removeAllComponents();
        layout.addComponent(horizontalLayoutButton);
        layout.addComponent(page.getGrid());
        Button addButton = page.getAddButton();
        layout.addComponent(addButton);
        setContent(layout);
    }


}