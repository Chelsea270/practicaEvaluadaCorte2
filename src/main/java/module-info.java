module org.test.practicaevaluadacorte2 {
    requires javafx.controls;
    requires javafx.fxml;


    opens org.test.practicaevaluadacorte2 to javafx.fxml;
    exports org.test.practicaevaluadacorte2;
}