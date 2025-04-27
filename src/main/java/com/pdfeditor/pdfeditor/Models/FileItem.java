package com.pdfeditor.pdfeditor.Models;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

import java.io.File;

public class FileItem {
    private final SimpleStringProperty fileName;
    private final File file;

    public FileItem(File file) {
        this.file = file;
        this.fileName = new SimpleStringProperty(file.getName());
    }

    public String getFileName() {
        return fileName.get();
    }

    public File getFile() {
        return file;
    }

    public StringProperty fileNameProperty() {
        return fileName;
    }
}
