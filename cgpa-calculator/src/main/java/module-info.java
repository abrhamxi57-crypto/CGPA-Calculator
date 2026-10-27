module com.seng3063.cgpa {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.graphics;

    opens com.seng3063.cgpa to javafx.fxml;
    opens com.seng3063.cgpa.controller to javafx.fxml;
    opens com.seng3063.cgpa.model to javafx.base;

    exports com.seng3063.cgpa;
    exports com.seng3063.cgpa.model;
    exports com.seng3063.cgpa.service;
    exports com.seng3063.cgpa.controller;
    exports com.seng3063.cgpa.validation;
    exports com.seng3063.cgpa.util;
}
