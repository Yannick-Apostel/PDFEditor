package com.pdfeditor.pdfeditor.Controllers;

import com.pdfeditor.pdfeditor.Models.FileItem;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.paint.Color;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.stage.Window;
import org.apache.pdfbox.Loader;
import org.apache.pdfbox.cos.COSStream;
import org.apache.pdfbox.io.IOUtils;
import org.apache.pdfbox.io.MemoryUsageSetting;
import org.apache.pdfbox.io.RandomAccessStreamCache;
import org.apache.pdfbox.multipdf.PDFMergerUtility;
import org.apache.pdfbox.multipdf.Splitter;
import org.apache.pdfbox.pdfwriter.compress.CompressParameters;
import org.apache.pdfbox.pdmodel.PDDocument;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Main_Controller {
    @FXML
    public Label lblError;
    @FXML
    public Button btnUpload;
    @FXML
    public Button btnUp;
    @FXML
    public Button btnDown;
    @FXML
    public TextField tlblStart;
    @FXML
    public TextField tlblEnd;
    @FXML
    public TableView<FileItem> fileview;
    @FXML
    public TableColumn<FileItem, Integer> colRank;
    @FXML
    public TableColumn<FileItem, String> colFilename;

    public final ObservableList<FileItem> uploadedFiles = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        fileview.setItems(uploadedFiles);

        colRank.setCellValueFactory(cellData ->
                new ReadOnlyObjectWrapper<>(fileview.getItems().indexOf(cellData.getValue()) + 1)
        );

        colFilename.setCellValueFactory(cellData ->
                new ReadOnlyStringWrapper(cellData.getValue().getFileName())
        );
    }



    @FXML
    public void moveUp() {
        int selectedIndex = fileview.getSelectionModel().getSelectedIndex();
        if (selectedIndex > 0) {
            FileItem item = uploadedFiles.remove(selectedIndex);
            uploadedFiles.add(selectedIndex - 1, item);
            fileview.getSelectionModel().clearAndSelect(selectedIndex - 1);
        }
    }

    @FXML
    public void moveDown() {
        int selectedIndex = fileview.getSelectionModel().getSelectedIndex();
        if (selectedIndex < uploadedFiles.size() - 1 && selectedIndex >= 0) {
            FileItem item = uploadedFiles.remove(selectedIndex);
            uploadedFiles.add(selectedIndex + 1, item);
            fileview.getSelectionModel().clearAndSelect(selectedIndex + 1);
        }
    }



@FXML
public void uploadFile() {
    FileChooser fileChooser = new FileChooser();
    fileChooser.setTitle("PDF-Datei/-en auswählen");
    fileChooser.getExtensionFilters().add(
            new FileChooser.ExtensionFilter("PDF Dateien (*.pdf)", "*.pdf")
    );
    List<File> selectedFiles = fileChooser.showOpenMultipleDialog(null);
    if (selectedFiles != null) {
        for (File file : selectedFiles) {
            uploadedFiles.add(new FileItem(file));
        }
    }
}

public void mergePDF() {

        lblError.setText("");
        FileChooser fileChooser = new FileChooser();


        // Benutzer wählt mehrere Dateien aus
        Window stage = null;


        if (uploadedFiles == null || uploadedFiles.size() <= 1) {
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

            for (FileItem file : uploadedFiles) {
                merger.addSource(file.getFile());
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
    public void SplitAll() throws IOException {
        if (uploadedFiles.size() !=1){

        }else {

            PDDocument document = Loader.loadPDF(uploadedFiles.get(0).getFile());
            Splitter splitter = new Splitter();
            List<PDDocument>splittedPdf= splitter.split(document);
            Window stage = null;
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("PDF-Datei speichern");
            fileChooser.setInitialFileName("splitted.pdf");
            File saveFile = fileChooser.showSaveDialog(stage);
            int num =0;
            for(PDDocument tosaveFile: splittedPdf){
                num++;
                tosaveFile.save(saveFile+String.valueOf(num)+".pdf");
            }
            document.close();
        }
    }

    public void SplitSpecific() throws IOException {
        if (uploadedFiles.size() != 1) {

        } else {
            PDDocument originalDoc = Loader.loadPDF(uploadedFiles.get(0).getFile());


            int startPage = Integer.parseInt(tlblStart.getText());
            int endPage = Integer.parseInt(tlblEnd.getText());

            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("PDF-Datei speichern");
            fileChooser.setInitialFileName("auswahl.pdf");
            File saveFile = fileChooser.showSaveDialog(null);

            if (saveFile != null) {

                if (!saveFile.getName().toLowerCase().endsWith(".pdf")) {
                    saveFile = new File(saveFile.getAbsolutePath() + ".pdf");
                }

                PDDocument newDoc = new PDDocument();


                for (int i = startPage - 1; i < endPage; i++) {
                    newDoc.addPage(originalDoc.getPage(i));
                }

                newDoc.save(saveFile);
                newDoc.close();
                originalDoc.close();
                System.out.println("PDF erfolgreich gespeichert!");
            }
        }
    }

    public void DeleteSelectedFileItem(){
        int selectedIndex = fileview.getSelectionModel().getSelectedIndex();
        if (selectedIndex < uploadedFiles.size()  && selectedIndex >= 0) {
            FileItem item = uploadedFiles.remove(selectedIndex);
            fileview.refresh();
        }
    }
}



