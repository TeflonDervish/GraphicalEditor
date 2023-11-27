module com.example.graphicaleditor {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.desktop;


    opens com.example.graphicaleditor to javafx.fxml;
    exports com.example.graphicaleditor;
}