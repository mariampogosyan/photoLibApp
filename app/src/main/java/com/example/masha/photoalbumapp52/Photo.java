package com.example.masha.photoalbumapp52;

/**
 * Created by Mariam on 4/24/2016.
 */
public class Photo {
    private String photoName;
    private String fileURL;
    private int id;

    public Photo(){

        this(null,null);
    }

    public Photo(String photoName, String fileURL){
        this.photoName = photoName;
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
