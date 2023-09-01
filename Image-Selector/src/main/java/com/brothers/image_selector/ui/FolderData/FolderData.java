package com.brothers.image_selector.ui.FolderData;

public class FolderData {
    private String folderName;
    private String folderPath;
    private int count;

    public FolderData(String folderName, String folderPath, int count) {
        this.folderName = folderName;
        this.folderPath = folderPath;
        this.count = count;
    }

    public String getFolderName() {
        return folderName;
    }

    public String getFolderPath() {
        return folderPath;
    }

    public int getCount() {
        return count;
    }
}
