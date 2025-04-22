package com.pdfeditor.pdfeditor.Controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.paint.Color;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.stage.Window;
import org.apache.pdfbox.cos.COSStream;
import org.apache.pdfbox.io.IOUtils;
import org.apache.pdfbox.io.MemoryUsageSetting;
import org.apache.pdfbox.io.RandomAccessStreamCache;
import org.apache.pdfbox.multipdf.PDFMergerUtility;
import org.apache.pdfbox.pdfwriter.compress.CompressParameters;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;

public class Main_Controller {
    @FXML
    public Label lblError;

    public void mergePDF() {
        lblError.setText("");
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("PDF-Dateien auswählen");
        fileChooser.getExtensionFilters().add(
                new FileChooser.ExtensionFilter("PDF Dateien (*.pdf)", "*.pdf")
        );

        // Benutzer wählt mehrere Dateien aus
        Window stage = null;
        List<File> selectedFiles = fileChooser.showOpenMultipleDialog(stage);

        if (selectedFiles == null || selectedFiles.size() <= 1) {
            System.out.println("Bitte wähle mindestens zwei PDF-Dateien aus.");
            lblError.setTextFill(Color.RED);
            lblError.setText("Bitte wähle mindestens zwei PDF-Dateien aus.");

            return;
        }

        // Speicherort abfragen
        fileChooser.setTitle("Speicherort wählen");
        fileChooser.setInitialFileName("zusammengefuegt.pdf");
        File saveFile = fileChooser.showSaveDialog(stage);

        if (saveFile == null) {
            System.out.println("Speichern abgebrochen.");
            return;
        }


        try (COSStream cosStream = new COSStream();
             ByteArrayOutputStream mergedpdfOutput = new ByteArrayOutputStream()) {

            PDFMergerUtility merger = new PDFMergerUtility();
            String destinationPath = "C:\\ProgrammeSelbst\\JavaFX\\PDFEditor\\src\\main\\prevview\\merged_output.pdf";
            merger.setDestinationFileName(destinationPath);

            for (File file : selectedFiles) {
                merger.addSource(file);
            }
            IOUtils StreamCacheCreateFunction = null;
            merger.mergeDocuments(StreamCacheCreateFunction.createMemoryOnlyStreamCache(), CompressParameters.NO_COMPRESSION);

            System.out.println("PDFs erfolgreich zusammengeführt!");
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Fehler beim Zusammenführen der PDFs.");
        }
    }

    public static RandomAccessStreamCache.StreamCacheCreateFunction createMemoryOnlyStreamCache() {
        RandomAccessStreamCache.StreamCacheCreateFunction streamCache = null;
        return streamCache;
    }


    public static RandomAccessStreamCache.StreamCacheCreateFunction createTempFileOnlyStreamCache() {
        return MemoryUsageSetting.setupTempFileOnly().streamCache;
    }
    public void showPDF(){}

    public void SavePDF(){}
}



