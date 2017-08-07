package com.bahwell.inoncharge.other;

import android.graphics.Bitmap;

/**
 * Created by NgocTri on 10/22/2016.
 */

public class DataListGrid {
    private int imageId;
    private Bitmap image;
    private String title;
    private String description;
    private byte[] imageByte;
    private String url;

    public DataListGrid(int imageId, String title, String description) {
        this.imageId = imageId;
        this.title = title;
        this.description = description;
    }

    public DataListGrid(int imageId, String title) {
        this.imageId = imageId;
        this.title = title;
    }
    public DataListGrid(Bitmap image, String title) {
        this.image = image;
        this.title = title;
    }
    public DataListGrid(byte[] image, String title) {
        this.imageByte = image;
        this.title = title;
    }

    public DataListGrid(String url, String title) {
        this.url = url;
        this.title = title;
    }

    public DataListGrid(int imageId) {
        this.imageId = imageId;
    }

    public int getImageId() {
        return imageId;
    }
    public Bitmap getImage() {
        return image;
    }
    public byte[] getImageByte() {
        return imageByte;
    }
    public String getTitle() {
        return title;
    }
    public String getUrl() {
        return url;
    }
    public String getDescription() {
        return description;
    }
    public void setImageId(int imageId) {
        this.imageId = imageId;
    }
    public void setImage(Bitmap image) {
        this.image = image;
    }
    public void setImageByte(byte[] image) {
        this.imageByte = image;
    }
    public void setTitle(String title) {
        this.title = title;
    }
    public void setUrl(String url) {
        this.url = url;
    }
    public void setDescription(String description) {
        this.description = description;
    }
}
