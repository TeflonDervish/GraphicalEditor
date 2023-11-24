module com.example.graphicaleditor {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.example.graphicaleditor to javafx.fxml;
    exports com.example.graphicaleditor;
}