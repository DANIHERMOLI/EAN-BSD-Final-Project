package com.todolist;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("mainWindow.fxml"));
        primaryStage.setTitle("To Do List");
        primaryStage.setScene(new Scene(root, 640, 400));
        primaryStage.show();
    }


    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void init() throws Exception {
        try {
            AppData.getInstance().loadData();
        } catch(IOException e) {
            System.out.println(e.getMessage());
        }
    }

    @Override
    public void stop() throws Exception {
        try {
            AppData.getInstance().saveData();
        } catch(IOException e) {
            System.out.println(e.getMessage());
        }
    }
}
