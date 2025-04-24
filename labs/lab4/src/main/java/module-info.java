module com.example.lab4 {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.logging;

    opens com.example.lab4.Controller to javafx.fxml;

    exports com.example.lab4.Model;
    exports com.example.lab4.View;
    exports com.example.lab4.Utils;
    exports com.example.lab4.Exceptions;
}
