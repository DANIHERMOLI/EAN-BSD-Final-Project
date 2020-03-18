package com.todolist;

import javafx.fxml.FXML;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

import java.time.LocalDate;

public class DialogController {
    @FXML
    private TextField shortDescriptionField;
    @FXML
    private TextArea fullDescriptionField;
    @FXML
    private DatePicker deadLineField;

    public ToDoItem processData() {
        String shortDescription = shortDescriptionField.getText().trim();
        String fullDescription = fullDescriptionField.getText().trim();
        LocalDate deadLiveValue = deadLineField.getValue();

        ToDoItem newItem = new ToDoItem(shortDescription, fullDescription, deadLiveValue);
        AppData.getInstance().addData(newItem);
        return newItem;
    }
}
