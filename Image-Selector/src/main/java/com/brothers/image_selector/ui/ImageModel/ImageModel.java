package com.brothers.image_selector.ui.ImageModel;


import android.net.Uri;

public class ImageModel {

    String image;
    private Uri imageUri;
    String title;
    int resImg;
    public boolean isSelected;

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public void setImageUri(Uri uri) {
        this.imageUri = uri;
    }

    public Uri getImageUri() {
        return imageUri;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getResImg() {
        return resImg;
    }

    public void setResImg(int resImg) {
        this.resImg = resImg;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean isSelected) {
        this.isSelected = isSelected;
    }


}
