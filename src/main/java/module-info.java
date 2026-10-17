module org.test.practicaevaluadacorte2 {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires org.postgresql.jdbc;
    requires java.desktop;

    opens org.test.practicaevaluadacorte2 to javafx.fxml;
    opens org.test.practicaevaluadacorte2.controller to javafx.fxml;
    opens org.test.practicaevaluadacorte2.model to javafx.fxml;
    exports org.test.practicaevaluadacorte2;
}