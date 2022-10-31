module com.example.flame {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.example.flame to javafx.fxml;
    exports com.example.flame;
}