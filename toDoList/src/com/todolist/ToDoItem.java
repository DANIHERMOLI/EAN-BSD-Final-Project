package com.todolist;

import java.time.LocalDate;

public class ToDoItem {
    private String shortDescription;
    private String fullDescription;
    private LocalDate deadLine;

    public ToDoItem(String shortDescription, String fullDescription, LocalDate deadLine) {
        this.shortDescription = shortDescription;
        this.fullDescription = fullDescription;
        this.deadLine = deadLine;
    }

    public String getShortDescription() {
        return shortDescription;
    }

    public void setShortDescription(String shortDescription) {
        this.shortDescription = shortDescription;
    }

    public String getFullDescription() {
        return fullDescription;
    }

    public void setFullDescription(String fullDescription) {
        this.fullDescription = fullDescription;
    }

    public LocalDate getDeadLine() {
        return deadLine;
    }

    public void setDeadLine(LocalDate deadLine) {
        this.deadLine = deadLine;
    }
}
