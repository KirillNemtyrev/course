module com.project.course {
    requires javafx.controls;
    requires javafx.fxml;
    requires com.google.gson;

    opens com.project.course.entity to com.google.gson;

    opens com.project.course to javafx.fxml;
    exports com.project.course;

    opens com.project.course.controller to javafx.fxml;
    exports com.project.course.controller;

}