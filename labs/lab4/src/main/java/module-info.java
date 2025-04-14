module com.example.lab4 {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.example.lab4 to javafx.fxml;
    exports com.example.lab4;
    exports com.example.lab4.View;
    opens com.example.lab4.View to javafx.fxml;
    exports com.example.lab4.Controller;
    opens com.example.lab4.Controller to javafx.fxml;
}