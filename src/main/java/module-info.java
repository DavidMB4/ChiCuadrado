module com.example.chicuadrado {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires org.kordamp.ikonli.javafx;

    opens com.example.chicuadrado to javafx.fxml;
    exports com.example.chicuadrado;

    requires commons.math3;
}
