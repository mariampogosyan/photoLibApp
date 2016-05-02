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
        private ArrayList<Tag> tags;



        public Photo(String fileURL){
            this.fileURL = fileURL;
            this.tags = new ArrayList<Tag>();

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
        public ArrayList<Tag> getTags(){
            return this.tags;
        }
        public boolean hasTag(String tagType, String tagValue) {
            for (Tag tag : tags) {
                if (tag.equals(new Tag(tagType, tagValue))) {
                    return true;
                }
            } return false;
        }

        public void addTag(String tagType, String tagValue) {
            this.tags.add(new Tag(tagType, tagValue));
        }

        public void deleteTag(String tagType, String tagValue) {
            this.tags.remove(new Tag(tagType, tagValue));
        }

        public int getSizeOfTags() {
            return this.tags.size();
        }

    }
