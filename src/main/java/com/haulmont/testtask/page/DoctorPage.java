package com.haulmont.testtask.page;

import com.haulmont.testtask.MainUI;
import com.haulmont.testtask.dao.Dao;
import com.haulmont.testtask.entities.Doctor;
import com.haulmont.testtask.entities.Recipe;
import com.vaadin.event.ItemClickEvent;
import com.vaadin.ui.*;
import com.vaadin.ui.Button;
import com.vaadin.ui.TextField;
import com.vaadin.ui.themes.ValoTheme;

import java.util.List;

public class DoctorPage extends AbstractPage {
    private static final String FIRST_NAME_COLUMN_NAME="Имя";
    private static final String MIDDLE_NAME_COLUMN_NAME="Отчество";
    private static final String LAST_NAME_COLUMN_NAME="Фамилия";
    private static final String SPECIALIZATION_NAME_COLUMN_NAME="Специализация";
    private static final String ID_COLUMN_NAME="Id";
    private static final String UPDATE_BUTTON_COLUMN_NAME ="Изменить";
    private static final String SHOW_STATISTICS_BUTTON_COLUMN_NAME = "Показать Статистику";
    private static final String DELETE_BUTTON_COLUMN_NAME = "Удалить";
    private static final float WIDTH_WINDOW =400.0f;
    private MainUI ui;
    private Grid grid;
    private Dao<Doctor> doctorDao;
    private Dao<Recipe> recipeDao;

    public DoctorPage(Dao<Doctor> doctorDao, Dao<Recipe> recipeDao, MainUI ui) {
        this.doctorDao = doctorDao;
        this.recipeDao = recipeDao;
        this.ui = ui;
        createGrid();
        updateList();
    }

    private void createGrid(){
        grid = new Grid("Список врачей");

        grid.setColumns(ID_COLUMN_NAME, FIRST_NAME_COLUMN_NAME, MIDDLE_NAME_COLUMN_NAME,
                LAST_NAME_COLUMN_NAME, SPECIALIZATION_NAME_COLUMN_NAME,SHOW_STATISTICS_BUTTON_COLUMN_NAME,
                UPDATE_BUTTON_COLUMN_NAME,DELETE_BUTTON_COLUMN_NAME);
        grid.getColumn(ID_COLUMN_NAME).setHidden(true);
        grid.setSizeFull();
        grid.addItemClickListener((ItemClickEvent.ItemClickListener) event -> {
            switch ((String)event.getPropertyId()){
                case UPDATE_BUTTON_COLUMN_NAME :{
                    String[] args = new String[5];
                    args[0] = event.getItem().getItemProperty(ID_COLUMN_NAME).getValue().toString();
                    args[1] = event.getItem().getItemProperty(FIRST_NAME_COLUMN_NAME).getValue().toString();
                    args[2] = event.getItem().getItemProperty(MIDDLE_NAME_COLUMN_NAME).getValue().toString();
                    args[3] = event.getItem().getItemProperty(LAST_NAME_COLUMN_NAME).getValue().toString();
                    args[4] = event.getItem().getItemProperty(SPECIALIZATION_NAME_COLUMN_NAME).getValue().toString();
                    createWindow(ui,"Изменение",getForm(false,args),WIDTH_WINDOW);
                    break;
                }
                case SHOW_STATISTICS_BUTTON_COLUMN_NAME:{
                    long id = Long.valueOf(
                            event.getItem().getItemProperty(ID_COLUMN_NAME).getValue().toString());
                    long count=0;
                    List<Recipe> recipes = recipeDao.findAll();
                    for (Recipe e : recipes) {
                        if(e.getIdDoctor() == id){
                            count++;
                        }
                    }
                    Notification.show("Количество выписанных рецептов данным врачем : " + count);
                    break;
                }
                case DELETE_BUTTON_COLUMN_NAME:{
                    boolean isDelete = doctorDao.delete(doctorDao.findById(Long.valueOf(
                            event.getItem().getItemProperty(ID_COLUMN_NAME).getValue().toString())));
                    if(!isDelete){
                        Notification.show("Нельзя удалить врача для которого существует рецепт");
                    }else {
                        ui.refreshPage(new DoctorPage(doctorDao,recipeDao,ui));
                    }
                    break;
                }
            }
        });
    }

    private void updateList() {
        grid.getContainerDataSource().removeAllItems();
        List<Doctor> list = doctorDao.findAll();
        list.forEach(e->grid.addRow(String.valueOf(e.getId()), e.getFirstName(), e.getMiddleName(),
                e.getLastName(), e.getSpecialization(),SHOW_STATISTICS_BUTTON_COLUMN_NAME,
                UPDATE_BUTTON_COLUMN_NAME,DELETE_BUTTON_COLUMN_NAME));
    }

    private FormLayout getForm(boolean isAdd, String[] args){
        TextField textFiledId = getTextField(ID_COLUMN_NAME,false);
        TextField textFiledFirstName = getTextField(FIRST_NAME_COLUMN_NAME,true,getValidatorOnlyWords(),getValidatorIsNotEmpty(64));
        TextField textFiledMiddleName = getTextField(MIDDLE_NAME_COLUMN_NAME,true,getValidatorOnlyWords(),getValidatorIsNotEmpty(64));
        TextField textFiledLastName = getTextField(LAST_NAME_COLUMN_NAME,true,getValidatorOnlyWords(),getValidatorIsNotEmpty(64));
        TextField textFiledSpecialization = getTextField(SPECIALIZATION_NAME_COLUMN_NAME,true,
                getValidatorOnlyWords(), getValidatorIsNotEmpty(256));

        Button buttonCancel = new Button("Отмена",event -> windowClose());
        Button buttonOk = new Button("Ок");
        buttonOk.setStyleName(ValoTheme.BUTTON_FRIENDLY);
        buttonOk.addClickListener(event -> {
            if(!textFiledFirstName.isValid() || !textFiledMiddleName.isValid() ||
            !textFiledLastName.isValid() || !textFiledSpecialization.isValid()){
                Notification.show("Введите корректные данные");
            }else {
                windowClose();
                Doctor doctor = new Doctor(textFiledFirstName.getValue(),textFiledMiddleName.getValue(),
                        textFiledLastName.getValue(), textFiledSpecialization.getValue());
                doctor.setId(Long.parseLong(textFiledId.getValue()));
                doctorDao.save(doctor);
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
            textFiledSpecialization.setValue(args[4]);
        }
        HorizontalLayout horizontalLayoutButton = new HorizontalLayout(buttonCancel,buttonOk);
        horizontalLayoutButton.setDefaultComponentAlignment(Alignment.MIDDLE_LEFT);
        return new FormLayout(textFiledId,textFiledFirstName,textFiledLastName,textFiledMiddleName,
                textFiledSpecialization,horizontalLayoutButton);
    }

    @Override
    public Button getAddButton(){
        Button addButton = new Button("Добавить");
        addButton.setStyleName(ValoTheme.BUTTON_FRIENDLY);
        addButton.addClickListener(event -> createWindow(ui,"Добавить",getForm(true,null),WIDTH_WINDOW));
        return addButton;
    }

    @Override
    public Grid getGrid() {
        return grid;
    }

    @Override
    public Button getPageButton() {
        Button button = new Button("Врачи");
        button.addClickListener(clickEvent ->
                ui.refreshPage(new DoctorPage(doctorDao,recipeDao,ui)));
        button.setStyleName(ValoTheme.BUTTON_BORDERLESS_COLORED);
        return button;
    }
}
