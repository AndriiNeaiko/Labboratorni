module com.example.vanderpolapp {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.example.vanderpolapp to javafx.fxml;
    exports com.example.vanderpolapp;
}