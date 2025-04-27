module com.pdfeditor.pdfeditor {
    requires javafx.controls;
    requires javafx.fxml;
    requires de.jensd.fx.glyphs.fontawesome;
    requires org.apache.pdfbox;
    requires org.apache.pdfbox.io;


    opens com.pdfeditor.pdfeditor to javafx.fxml;
    exports com.pdfeditor.pdfeditor;
    exports com.pdfeditor.pdfeditor.Controllers;
    exports com.pdfeditor.pdfeditor.Models;

}