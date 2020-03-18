package com.todolist;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Iterator;


public class AppData {
    private static AppData selfInstance = new AppData();
    private static String filename = "AppData.txt";
    private DateTimeFormatter dateFormat;
    private ObservableList<ToDoItem> toDoItems;

    public static AppData getInstance() {
        return selfInstance;
    }

    private AppData() {
        dateFormat = DateTimeFormatter.ofPattern("dd-MM-yyyy");
    }

    public void addData(ToDoItem item) {
        toDoItems.add(item);
    }

    public ObservableList<ToDoItem> getToDoItems() {
        return toDoItems;
    }

    public void setToDoItems(ObservableList<ToDoItem> toDoItems) {
        this.toDoItems = toDoItems;
    }

    public void loadData() throws IOException {
        toDoItems = FXCollections.observableArrayList();
        Path path = Paths.get(filename);
        BufferedReader buffer = Files.newBufferedReader(path);
        String input;

        try{
            while ((input = buffer.readLine()) != null) {
                String [] itemSections = input.split("\t");
                String shortDescription = itemSections[0];
                String fullDescription = itemSections[1];
                String dateString = itemSections[2];

                LocalDate date = LocalDate.parse(dateString, dateFormat);
                ToDoItem item = new ToDoItem(shortDescription, fullDescription, date);
                toDoItems.add(item);
            }
        } finally {
            if(buffer != null) {
                buffer.close();
            }
        }
    }

    public void saveData() throws IOException {
        Path path = Paths.get(filename);
        BufferedWriter buffer = Files.newBufferedWriter(path);

        try{
            Iterator<ToDoItem> iterator = toDoItems.iterator();
            while(iterator.hasNext()) {
                ToDoItem item = iterator.next();
                buffer.write(String.format("%s\t%s\t%s",
                        item.getShortDescription(),
                        item.getFullDescription(),
                        item.getDeadLine().format(dateFormat)));
                buffer.newLine();
            }
        } finally {
            if(buffer != null) {
                buffer.close();
            }
        }
    }

    public void deleteData(ToDoItem item) {
        toDoItems.remove(item);
    }
}
