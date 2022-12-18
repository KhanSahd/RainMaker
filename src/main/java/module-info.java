module com.example.flame {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.media;


    opens com.example.RainMaker to javafx.fxml;
    exports com.example.RainMaker;
}