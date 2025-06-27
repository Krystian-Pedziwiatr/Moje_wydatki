module com.example.moje_wydatki {
    requires javafx.controls;
    requires javafx.fxml;

    requires com.dlsc.formsfx;
    requires java.sql;
    requires mysql.connector.j;
    requires org.apache.poi.ooxml;

    opens com.moje_wydatki to javafx.fxml;
    exports com.moje_wydatki;
}