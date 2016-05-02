package com.example.masha.photoalbumapp52;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by Mariam on 4/24/2016.
 */
public class Photo implements Serializable {
        private String photoName;
        private String fileURL;
        private int id;
        ArrayList<String> personTag, placeTag;



        public Photo(String fileURL){
            this.fileURL = fileURL;
            this.personTag = new ArrayList<String>();
            this.placeTag = new ArrayList<String>();

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
         public void addPersonTag(String person){
        personTag.add(person);
         }

    public void addPlaceTag(String place){
        placeTag.add(place);
    }

    public ArrayList<String> getPersonTags(){
        return personTag;
    }

    public ArrayList<String> getPlaceTags(){
        return placeTag;
    }

    public boolean equals(Object o) {
        if(o == null || !(o instanceof Photo))
            return false;
        Photo p = (Photo) o;
        return getFileURL().equals(p.getFileURL());
    }

    }
