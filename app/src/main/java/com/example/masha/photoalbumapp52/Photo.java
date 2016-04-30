package com.example.masha.photoalbumapp52;

import java.io.Serializable;

/**
 * Created by Mariam on 4/24/2016.
 */
public class Photo implements Serializable {
    private String photoName;
    private String fileURL;
    private int id;


     public Photo(String fileURL){
        this.fileURL = fileURL;
    }

    public String getPhotoName() {
        return photoName;
    }

    public void setPhotoName(String photoName) {

        this.photoName = photoName;
    }

    public String getFileURL() {
        return fileURL;
    }

    public void setFileURL(String fileURL) {
        this.fileURL = fileURL;
    }
}
