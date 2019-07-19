package com.haulmont.testtask.page;

import com.haulmont.testtask.MainUI;
import com.haulmont.testtask.dao.Dao;
import com.haulmont.testtask.entities.Patient;
import com.vaadin.data.validator.RegexpValidator;
import com.vaadin.event.ItemClickEvent;
import com.vaadin.ui.*;
import com.vaadin.ui.themes.ValoTheme;

import java.util.List;

public class PatientPage extends AbstractPage {
    private static final String FIRST_NAME_COLUMN_NAME="Имя";
    private static final String MIDDLE_NAME_COLUMN_NAME="Отчество";
    private static final String LAST_NAME_COLUMN_NAME="Фамилия";
    private static final String ID_COLUMN_NAME="Id";
    private static final String UPDATE_BUTTON_COLUMN_NAME ="Изменить";
    private static final String PHONE_NUMBER_BUTTON_COLUMN_NAME = "Номер телефона";
    private static final String DELETE_BUTTON_COLUMN_NAME = "Удалить";
    private static final float WIDTH_WINDOW =400.0f;

    private MainUI ui;
    private Grid grid;
    private Dao<Patient> patientDao;
    public PatientPage(Dao<Patient> patientDao, MainUI ui) {
        this.patientDao = patientDao;
        this.ui = ui;
        createGrid();
        updateList();
    }
    @Override
    public Button getPageButton() {
        Button button = new Button("Пациенты");
        button.addClickListener(clickEvent ->
                ui.refreshPage(new PatientPage(patientDao,ui)));
        button.setStyleName(ValoTheme.BUTTON_BORDERLESS_COLORED);
        return button;
    }

    @Override
    public Grid getGrid(){
        return grid;
    }

    @Override
    public Button getAddButton(){
        Button addButton = new Button("Добавить");
        addButton.setStyleName(ValoTheme.BUTTON_FRIENDLY);
        addButton.addClickListener(event -> createWindow(ui,"Добавить",getForm(true,null),WIDTH_WINDOW));
        return addButton;
    }

    private FormLayout getForm(boolean isAdd, String[] args){
        TextField textFiledId = getTextField(ID_COLUMN_NAME,false);
        TextField textFiledFirstName = getTextField(FIRST_NAME_COLUMN_NAME,true,getValidatorOnlyWords(),getValidatorIsNotEmpty(64));
        TextField textFiledMiddleName = getTextField(MIDDLE_NAME_COLUMN_NAME,true,getValidatorOnlyWords(),getValidatorIsNotEmpty(64));
        TextField textFiledLastName = getTextField(LAST_NAME_COLUMN_NAME,true,getValidatorOnlyWords(),getValidatorIsNotEmpty(64));
        TextField textFiledPhoneNumber = getTextField(PHONE_NUMBER_BUTTON_COLUMN_NAME,true,getValidatorIsNotEmpty(16),
                new RegexpValidator("\\+\\d{1}\\(\\d{3}\\)\\d{3}-\\d{2}-\\d{2}",
                        "Может содержать только номер телефона формата +X(XXX)XXX-XX-XX"));
        Button buttonCancel = new Button("Отмена",event -> windowClose());
        Button buttonOk = new Button("Ок");
        buttonOk.setStyleName(ValoTheme.BUTTON_FRIENDLY);
        buttonOk.addClickListener(event -> {
            if(!textFiledFirstName.isValid() || !textFiledMiddleName.isValid() ||
                    !textFiledLastName.isValid() || !textFiledPhoneNumber.isValid()){
                Notification.show("Введите корректные данные");
            }else {
                windowClose();
                Patient patient = new Patient(textFiledFirstName.getValue(),textFiledMiddleName.getValue(),
                        textFiledLastName.getValue(), textFiledPhoneNumber.getValue());
                patient.setId(Long.parseLong(textFiledId.getValue()));
                patientDao.save(patient);
                updateList();
            }
        });
        if(isAdd){
            textFiledId.setValue("-1");
        }else {
            textFiledId.setValue(args[0]);
            textFiledFirstName.setValue(args[1]);
            textFiledMiddleName.setValue(args[2]);
            textFiledLastName.setValue(args[3]);
            textFiledPhoneNumber.setValue(args[4]);
        }
        HorizontalLayout horizontalLayoutButton = new HorizontalLayout(buttonCancel,buttonOk);
        horizontalLayoutButton.setDefaultComponentAlignment(Alignment.MIDDLE_LEFT);
        return new FormLayout(textFiledId,textFiledFirstName,textFiledLastName,textFiledMiddleName,
                textFiledPhoneNumber,horizontalLayoutButton);
    }

    private void updateList() {
        grid.getContainerDataSource().removeAllItems();
        List<Patient> list = patientDao.findAll();
        list.forEach(e->grid.addRow(String.valueOf(e.getId()), e.getFirstName(), e.getMiddleName(),
                e.getLastName(),e.getPhoneNumber(),
                UPDATE_BUTTON_COLUMN_NAME,DELETE_BUTTON_COLUMN_NAME));
    }

    private void createGrid(){
        grid = new Grid("Список пациентов");

        grid.setColumns(ID_COLUMN_NAME, FIRST_NAME_COLUMN_NAME, MIDDLE_NAME_COLUMN_NAME,
                LAST_NAME_COLUMN_NAME, PHONE_NUMBER_BUTTON_COLUMN_NAME,
                UPDATE_BUTTON_COLUMN_NAME,DELETE_BUTTON_COLUMN_NAME);
        grid.getColumn(ID_COLUMN_NAME).setHidden(true);
        grid.setSizeFull();
        grid.addItemClickListener((ItemClickEvent.ItemClickListener) event -> {
            switch ((String)event.getPropertyId()){
                case UPDATE_BUTTON_COLUMN_NAME :{
                    String[] args = getValueRow(event,ID_COLUMN_NAME,FIRST_NAME_COLUMN_NAME,MIDDLE_NAME_COLUMN_NAME,
                            LAST_NAME_COLUMN_NAME,PHONE_NUMBER_BUTTON_COLUMN_NAME);
                    createWindow(ui,"Изменение",getForm(false,args),WIDTH_WINDOW);
                    break;
                }
                case DELETE_BUTTON_COLUMN_NAME:{
                    boolean isDelete = patientDao.delete(patientDao.findById(Long.valueOf(
                            event.getItem().getItemProperty(ID_COLUMN_NAME).getValue().toString())));
                    if(!isDelete){
                        Notification.show("Нельзя удалить пациента для которого существует рецепт");
                    }else {
                        ui.refreshPage(new PatientPage(patientDao,ui));
                    }
                    break;
                }
            }
        });
    }
}
