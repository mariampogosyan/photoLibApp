package com.example.masha.photoalbumapp52;
import java.util.ArrayList;

/**
 * Created by Mariam on 4/24/2016.
 */
public class Album {
    public String albumName;
    private ArrayList<Photo> photos;
    public int id;

    public Album(int id, String albumName) {
        this.id = id;
        this.albumName = albumName;
        this.photos = new ArrayList<Photo>();

    }
    public Album(){ }
    public int compareTo(Album other){
        return albumName.compareToIgnoreCase(other.albumName);
    }
    public String getAlbumName() {
        return albumName;
    }

    @Override
    public String toString() {
        return this.albumName;
    }
    public static Album parseAlbum(String albumInfo){
        Album album = new Album();
        album.id = -1;
        album.albumName = albumInfo;
        return album;
    }
}
