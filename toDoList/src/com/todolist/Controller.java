package com.todolist;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.Color;
import javafx.util.Callback;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Optional;

public class Controller {
    private ArrayList<ToDoItem> toDoItems;
    private LocalDate currentDate;
    @FXML
    private ListView<ToDoItem> listView;
    @FXML
    private TextArea contentView;
    @FXML
    private TextField dueDateView;
    @FXML
    private BorderPane mainWindow;
    @FXML
    private ContextMenu listContextMenu;

    public void initialize() {
        listContextMenu = new ContextMenu();
        MenuItem deleteOption = new MenuItem("Delete");
        deleteOption.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                ToDoItem item = listView.getSelectionModel().getSelectedItem();
                deleteItem(item);
            }
        });
        listContextMenu.getItems().addAll(deleteOption);
        listView.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<ToDoItem>() {
            @Override
            public void changed(ObservableValue<? extends ToDoItem> observable, ToDoItem oldValue, ToDoItem newValue) {
                if(newValue != null) {
                    ToDoItem item = listView.getSelectionModel().getSelectedItem();
                    contentView.setText(item.getFullDescription());
                    DateTimeFormatter prettyDate = DateTimeFormatter.ofPattern("MMMM d, yyyy");
                    dueDateView.setText("Due: " + prettyDate.format(item.getDeadLine()));
                }
            }
        });

        listView.setItems(AppData.getInstance().getToDoItems());
        listView.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
        listView.getSelectionModel().selectFirst();

        listView.setCellFactory(new Callback<ListView<ToDoItem>, ListCell<ToDoItem>>() {
            @Override
            public ListCell<ToDoItem> call(ListView<ToDoItem> toDoItemListView) {
                ListCell<ToDoItem> cell = new ListCell<ToDoItem>() {
                    @Override
                    protected void updateItem(ToDoItem toDoItem, boolean empty) {
                        super.updateItem(toDoItem, empty);
                        if(empty) {
                            setText(null);
                        } else {
                            setText(toDoItem.getShortDescription());
                            if(toDoItem.getDeadLine().isBefore(LocalDate.now())) {
                                setTextFill(Color.CRIMSON);
                            } else if(toDoItem.getDeadLine().isBefore(LocalDate.now().plusDays(3))) {
                                setTextFill(Color.ORANGE);
                            } else if(toDoItem.getDeadLine().isAfter(LocalDate.now().plusDays(4))) {
                                setTextFill(Color.GREEN);
                            } else {
                                setTextFill(Color.BLACK);
                            }
                        }
                    }
                };
                cell.emptyProperty().addListener(
                        (obs, wasEmpty, isNowEmpty) -> {
                            if(isNowEmpty) {
                                cell.setContextMenu(null);
                            } else {
                                cell.setContextMenu(listContextMenu);
                            }
                        });
                return cell;
            }
        });
    }
    @FXML
    public void showNewItemDialog() {
        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.initOwner(mainWindow.getScene().getWindow());
        dialog.setTitle("Add New Item");
        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(getClass().getResource("todoListDialog.fxml"));
        try {
            dialog.getDialogPane().setContent(fxmlLoader.load());
        } catch (IOException e) {
            System.out.println("ERROR: Unable to load dialog box");
            e.printStackTrace();
            return;
        }
        dialog.getDialogPane().getButtonTypes().add(ButtonType.CANCEL);
        dialog.getDialogPane().getButtonTypes().add(ButtonType.OK);

        Optional<ButtonType> result = dialog.showAndWait();
        if(result.isPresent() && result.get() == ButtonType.OK) {
            DialogController controller = fxmlLoader.getController();
            ToDoItem newItem = controller.processData();
            listView.getSelectionModel().select(newItem);
        }
    }
    @FXML
    public void handleKeyPressed(KeyEvent event) {
        ToDoItem selectedItem = listView.getSelectionModel().getSelectedItem();
        if(selectedItem != null) {
            if(event.getCode().equals(KeyCode.DELETE)) {
                deleteItem(selectedItem);
            }
        }
    }
    @FXML
    public void handleClickListView() {
        ToDoItem item = listView.getSelectionModel().getSelectedItem();
        contentView.setText(item.getFullDescription());
        dueDateView.setText("Due: " + item.getDeadLine().toString());
    }

    public void deleteItem(ToDoItem item) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Delete Item");
        alert.setHeaderText("Delete item: " + item.getShortDescription());
        alert.setContentText("Are you sure? Press OK to confirm or CANCEL to abort.");
        Optional<ButtonType> result = alert.showAndWait();
        if(result.isPresent() && (result.get() == ButtonType.OK)) {
            AppData.getInstance().deleteData(item);
        }
    }
}
