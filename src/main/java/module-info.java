module com.pdfeditor.pdfeditor {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.pdfeditor.pdfeditor to javafx.fxml;
    exports com.pdfeditor.pdfeditor;

}