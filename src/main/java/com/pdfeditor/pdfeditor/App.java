package com.pdfeditor.pdfeditor;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
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



public class App extends Application {

    private Object IOUtils;

    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/FXML/Main.fxml"));
        Scene scene = null;
        try {
            scene = new Scene(fxmlLoader.load());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        stage.setScene(scene);
        stage.show();
    }

    private void mergePdfFiles(Stage stage) throws FileNotFoundException {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("PDF-Dateien auswählen");
        fileChooser.getExtensionFilters().add(
                new FileChooser.ExtensionFilter("PDF Dateien (*.pdf)", "*.pdf")
        );

        // Benutzer wählt mehrere Dateien aus
        List<File> selectedFiles = fileChooser.showOpenMultipleDialog(stage);

        if (selectedFiles == null || selectedFiles.size() <= 1) {
            System.out.println("Bitte wähle mindestens zwei PDF-Dateien aus.");
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
             ByteArrayOutputStream mergedpdfOutput = new ByteArrayOutputStream())  {

            PDFMergerUtility merger = new PDFMergerUtility();
            merger.setDestinationFileName(saveFile.getAbsolutePath());

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
    public static RandomAccessStreamCache.StreamCacheCreateFunction createMemoryOnlyStreamCache()
    {
        RandomAccessStreamCache.StreamCacheCreateFunction streamCache = null;
        return streamCache;
    }

    
    public static RandomAccessStreamCache.StreamCacheCreateFunction createTempFileOnlyStreamCache()
    {
        return MemoryUsageSetting.setupTempFileOnly().streamCache;
    }


    public static void main(String[] args) {
        launch();
    }
}
