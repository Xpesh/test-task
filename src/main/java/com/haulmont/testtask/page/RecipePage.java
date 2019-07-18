package com.haulmont.testtask.page;

import com.haulmont.testtask.ConverterDate;
import com.haulmont.testtask.MainUI;
import com.haulmont.testtask.dao.Dao;
import com.haulmont.testtask.entities.Doctor;
import com.haulmont.testtask.entities.Patient;
import com.haulmont.testtask.entities.Priority;
import com.haulmont.testtask.entities.Recipe;
import com.vaadin.event.ItemClickEvent;
import com.vaadin.shared.ui.combobox.FilteringMode;
import com.vaadin.ui.*;
import com.vaadin.ui.themes.ValoTheme;

import java.time.LocalDate;
import java.util.LinkedList;
import java.util.List;
import java.util.function.Predicate;

public class RecipePage extends AbstractPage {
    private static final String ID_COLUMN_NAME="Id";
    private static final String DOCTOR_COLUMN_NAME="Доктор";
    private static final String PATIENT_COLUMN_NAME="Пациент";
    private static final String DESCRIPTION_COLUMN_NAME="Описание";
    private static final String DATE_OF_CREATION_COLUMN_NAME="Дата создания";
    private static final String VALIDITY_COLUMN_NAME="Срок действия до";
    private static final String PRIORITY_COLUMN_NAME="Приоритет";
    private static final String DELETE_BUTTON_COLUMN_NAME = "Удалить";
    private static final String UPDATE_BUTTON_COLUMN_NAME ="Изменить";
    private static final String SHOW_ALL ="Отобразить всех";
    private static final float WIDTH_WINDOW =550.0f;

    private MainUI ui;
    private Grid grid;
    private Dao<Recipe> recipeDao;
    private Dao<Patient> patientDao;
    private Dao<Doctor> doctorDao;
    private TextField descriptionFilter;
    private ComboBox priorityFilter;
    private ComboBox patientFilter;

    public RecipePage(Dao<Recipe> recipeDao, Dao<Patient> patientDao, Dao<Doctor> doctorDao, MainUI ui) {
        this.recipeDao = recipeDao;
        this.ui = ui;
        this.patientDao=patientDao;
        this.doctorDao=doctorDao;
        createGrid();
        updateList();
    }

    @Override
    public Button getPageButton() {
        Button button = new Button("Рецепты",clickEvent ->
                ui.refreshPage(new RecipePage(recipeDao,patientDao,doctorDao,ui)));
        button.setStyleName(ValoTheme.BUTTON_BORDERLESS_COLORED);
        return button;
    }

    @Override
    public Component getGrid(){
        return new VerticalLayout(new HorizontalLayout(createDescriptionFilter(),createPriorityFilter(),
                createPatientFilter()),grid);
    }

    @Override
    public Button getAddButton(){
        Button addButton = new Button("Добавить",event -> createWindow("Добавить",
                getForm(true,null,null,null)));
        addButton.setStyleName(ValoTheme.BUTTON_FRIENDLY);
        return addButton;
    }

    private void createWindow(String caption, FormLayout content){
        if(descriptionFilter!=null){
            descriptionFilter.setValue(SHOW_ALL);
        }
        if(priorityFilter!=null){
            priorityFilter.setValue(SHOW_ALL);
        }
        if (patientFilter!=null){
            patientFilter.setValue(SHOW_ALL);
        }
        createWindow(ui,caption,content,WIDTH_WINDOW);
    }

    private FormLayout getForm(boolean isAdd, String[] args, LocalDate dateOfCreate, LocalDate validity){
        List<Patient> patients = patientDao.findAll();
        List<Doctor> doctors = doctorDao.findAll();
        TextField textFiledId = new TextField(ID_COLUMN_NAME);
        textFiledId.setVisible(false);

        TextField textFiledDescription = getTextField(DESCRIPTION_COLUMN_NAME,true, getValidatorIsNotEmpty(256));

        ComboBox patientFind = getComboBox("Пациент",patients);
        ComboBox doctorFind = getComboBox("Врач",doctors);

        DateField dateFieldOfCreate = new DateField(DATE_OF_CREATION_COLUMN_NAME);
        dateFieldOfCreate.addValidator(getValidatorIsNotNull());
        dateFieldOfCreate.setSizeFull();

        DateField dateFieldValidity = new DateField(VALIDITY_COLUMN_NAME);
        dateFieldValidity.addValidator(getValidatorIsNotNull());
        dateFieldValidity.setSizeFull();

        ComboBox prioritiesComboBox = getComboBox("Приоритет",Priority.getList());

        Button buttonCancel = new Button("Отмена",event -> windowClose());
        Button buttonOk = new Button("Ок");
        buttonOk.setStyleName(ValoTheme.BUTTON_FRIENDLY);
        buttonOk.addClickListener(event -> {
            if(!textFiledDescription.isValid() || !dateFieldOfCreate.isValid() || !dateFieldValidity.isValid() ||
                    !prioritiesComboBox.isValid() || !patientFind.isValid() || !doctorFind.isValid() ||
                    !dateFieldOfCreate.isValid() || !dateFieldValidity.isValid()){
                Notification.show("Введите корректные данные");
            }else {
                windowClose();
                Recipe recipe = new Recipe(textFiledDescription.getValue(), ((Patient) patientFind.getValue()).getId(),
                        ((Doctor) doctorFind.getValue()).getId(), ConverterDate.convert(dateFieldOfCreate.getValue()),
                        ConverterDate.convert(dateFieldValidity.getValue()),(Priority) prioritiesComboBox.getValue());
                recipe.setId(Long.parseLong(textFiledId.getValue()));
                recipeDao.save(recipe);
                updateList();
            }
        });

        if(isAdd){
            textFiledId.setValue("-1");
        }else {
            textFiledId.setValue(args[0]);
            textFiledDescription.setValue(args[1]);
            for(Patient patient : patients){
                if(patient.getId()==Long.parseLong(args[2])){
                    patientFind.setValue(patient);
                }
            }
            for(Doctor doctor : doctors){
                if(doctor.getId()==Long.parseLong(args[3])){
                    doctorFind.setValue(doctor);
                }
            }
            dateFieldOfCreate.setValue(ConverterDate.convert(dateOfCreate));
            dateFieldValidity.setValue(ConverterDate.convert(validity));
            prioritiesComboBox.setValue(Priority.valueOf(args[4]));
        }
        HorizontalLayout horizontalLayoutButton = new HorizontalLayout(buttonCancel,buttonOk);
        horizontalLayoutButton.setDefaultComponentAlignment(Alignment.MIDDLE_LEFT);
        return new FormLayout(textFiledId,textFiledDescription,patientFind,doctorFind,dateFieldOfCreate,
                dateFieldValidity,prioritiesComboBox,horizontalLayoutButton);
    }

    private void updateList() {
        grid.getContainerDataSource().removeAllItems();
        List<Recipe> list = recipeDao.findAll();
        list.forEach(e->grid.addRow(String.valueOf(e.getId()),e.getDescription(),
                patientDao.findById(e.getIdPatient()).toString(),
                doctorDao.findById(e.getIdDoctor()).toString(), e.getCreationDate().toString(),
                e.getValidity().toString(),e.getPriority().toString(),
                UPDATE_BUTTON_COLUMN_NAME,DELETE_BUTTON_COLUMN_NAME));
    }

    private void createGrid(){
        grid = new Grid("Список рецептов");

        grid.setColumns(ID_COLUMN_NAME, DESCRIPTION_COLUMN_NAME, PATIENT_COLUMN_NAME,
                DOCTOR_COLUMN_NAME, DATE_OF_CREATION_COLUMN_NAME,VALIDITY_COLUMN_NAME,
                PRIORITY_COLUMN_NAME, UPDATE_BUTTON_COLUMN_NAME,DELETE_BUTTON_COLUMN_NAME);
        grid.getColumn(ID_COLUMN_NAME).setHidden(true);
        grid.setSizeFull();
        grid.addItemClickListener((ItemClickEvent.ItemClickListener) event -> {
            switch ((String)event.getPropertyId()){
                case UPDATE_BUTTON_COLUMN_NAME :{
                    long id = Long.valueOf(event.getItem().getItemProperty(ID_COLUMN_NAME).getValue().toString());
                    Recipe recipe = recipeDao.findById(id);
                    String[] args = new String[5];
                    args[0] = String.valueOf(id);
                    args[1] = recipe.getDescription();
                    args[2] = String.valueOf(recipe.getIdPatient());
                    args[3] = String.valueOf(recipe.getIdDoctor());
                    args[4] = recipe.getPriority().toString();
                    createWindow("Изменение",getForm(false,args,recipe.getCreationDate(),recipe.getValidity()));
                    break;
                }
                case DELETE_BUTTON_COLUMN_NAME:{
                    boolean isDelete = recipeDao.delete(recipeDao.findById(Long.valueOf(
                            event.getItem().getItemProperty(ID_COLUMN_NAME).getValue().toString())));
                    if(!isDelete){
                        Notification.show("Ошибка удаления");
                    }else {
                        ui.refreshPage(new RecipePage(recipeDao,patientDao,doctorDao,ui));
                    }
                    break;
                }
            }
        });
    }
    private TextField createDescriptionFilter(){
        List<String> list = new LinkedList<>();
        list.add(SHOW_ALL);
        recipeDao.findAll().forEach(e->list.add(e.getDescription()));
        descriptionFilter = new TextField("Фильтр по описанию");
        descriptionFilter.setValue(SHOW_ALL);
        descriptionFilter.addValueChangeListener(event -> filter(descriptionFilter.getValue(),
                descriptionFilter, recipe -> recipe.getDescription().contains(descriptionFilter.getValue())));
        return descriptionFilter;
    }

    private ComboBox createPriorityFilter(){
        List<String> list = new LinkedList<>();
        list.add(SHOW_ALL);
        recipeDao.findAll().forEach(e->list.add(e.getPriority().toString()));
        priorityFilter = new ComboBox("Фильтр по приоритету",list);
        priorityFilter.setValue(SHOW_ALL);
        priorityFilter.setFilteringMode(FilteringMode.CONTAINS);
        priorityFilter.addValueChangeListener(event -> filter((String) priorityFilter.getValue(), priorityFilter,
                recipe -> recipe.getPriority().toString().equals(priorityFilter.getValue())));
        return priorityFilter;
    }

    private ComboBox createPatientFilter(){
        List<String> list = new LinkedList<>();
        list.add(SHOW_ALL);
        patientDao.findAll().forEach(e->list.add(e.toString()));
        patientFilter = new ComboBox("Фильтр по пациенту",list);
        patientFilter.setWidth("500");
        patientFilter.setValue(SHOW_ALL);
        patientFilter.setFilteringMode(FilteringMode.CONTAINS);
        patientFilter.addValueChangeListener(event -> filter((String) patientFilter.getValue(),patientFilter,
                recipe -> patientDao.findById(recipe.getIdPatient()).toString().equals(patientFilter.getValue())));
        return patientFilter;
    }

    private void filter(String s, Component component, Predicate<Recipe> predicate){
        if(s!=null && !s.isEmpty()) {
            if(s.equals(SHOW_ALL)){
                updateList();
            }else {
                grid.getContainerDataSource().removeAllItems();
                for (Recipe recipe : recipeDao.findAll()) {
                    if (predicate.test(recipe)) {
                        if(component==descriptionFilter){
                            if(priorityFilter!=null){
                                priorityFilter.setValue(SHOW_ALL);
                            }
                            if (patientFilter!=null){
                                patientFilter.setValue(SHOW_ALL);
                            }
                        }else if(component==priorityFilter){
                            if(descriptionFilter!=null){
                                descriptionFilter.setValue(SHOW_ALL);
                            }
                            if (patientFilter!=null){
                                patientFilter.setValue(SHOW_ALL);
                            }
                        }else if(component==patientFilter){
                            if(descriptionFilter!=null){
                                descriptionFilter.setValue(SHOW_ALL);
                            }
                            if(priorityFilter!=null){
                                priorityFilter.setValue(SHOW_ALL);
                            }
                        }
                        grid.addRow(String.valueOf(recipe.getId()), recipe.getDescription(),
                                patientDao.findById(recipe.getIdPatient()).toString(),
                                doctorDao.findById(recipe.getIdDoctor()).toString(), recipe.getCreationDate().toString(),
                                recipe.getValidity().toString(), recipe.getPriority().toString(),
                                UPDATE_BUTTON_COLUMN_NAME, DELETE_BUTTON_COLUMN_NAME);
                    }
                }
            }
        }
    }
}
