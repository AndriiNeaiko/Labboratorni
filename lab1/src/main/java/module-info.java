module com.example.recognitionsystem_lab1 {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;

    opens com.example.recognitionsystem_lab1 to javafx.fxml;
    exports com.example.recognitionsystem_lab1;
}