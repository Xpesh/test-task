package com.haulmont.testtask.page;

import com.vaadin.data.Validator;
import com.vaadin.data.validator.NullValidator;
import com.vaadin.data.validator.RegexpValidator;
import com.vaadin.data.validator.StringLengthValidator;
import com.vaadin.server.Sizeable;
import com.vaadin.shared.ui.combobox.FilteringMode;
import com.vaadin.ui.*;

import java.util.Collection;


abstract class AbstractPage implements Page {
    private Window window;

    Window createWindow(UI ui, String caption, FormLayout content, float width){
        if(window!=null){
            window.close();
        }
        window = new Window(caption);
        window.setWidth(width, Sizeable.Unit.PIXELS);
        content.setMargin(true);
        window.setContent(content);
        window.setModal(true);
        window.setClosable(false);
        ui.addWindow(window);
        return window;
    }


    void windowClose(){
        if(window!=null){
            window.close();
            window=null;
        }
    }

    TextField getTextField(String caption, boolean visible, Validator...validators){
        TextField textFiled = new TextField(caption);
        textFiled.setVisible(visible);
        textFiled.setSizeFull();
        for(Validator validator : validators){
            textFiled.addValidator(validator);
        }
        return textFiled;
    }

    Validator getValidatorOnlyWords(){
        return new RegexpValidator("[a-zA-Zа-яА-Я]+",
                "Может содержать только буквы кириллицы или латиницы");
    }
    Validator getValidatorIsNotNull(){
        return new NullValidator("Не может быть пустым",false);
    }
    Validator getValidatorIsNotEmpty(int maxLength){
        return new StringLengthValidator("Не может быть пустым",1,
                maxLength,false);
    }

    ComboBox getComboBox(String caption, Collection<?> collection){
        ComboBox comboBox = new ComboBox(caption,collection);
        comboBox.setFilteringMode(FilteringMode.CONTAINS);
        comboBox.setSizeFull();
        comboBox.addValidator(getValidatorIsNotNull());
        return comboBox;
    }
}
