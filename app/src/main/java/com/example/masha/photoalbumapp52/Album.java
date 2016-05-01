package com.example.masha.photoalbumapp52;
import android.content.Context;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Mariam on 4/24/2016.
 */
public class Album implements Serializable {
    public String albumName;
    private ArrayList<Photo> photos;
    public int id;

    public Album(String albumName) {
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
    public void setName(String albumName) {
        this.albumName = albumName;
    }
    public ArrayList<Photo> getPhotos() {
        return photos;
    }
    public void addPhoto(Photo p) {
        photos.add(p);
    }
    public int getSize() {
        return photos.size();
    }

    public static void make(List<Album> albums, Context c) throws IOException {

        FileOutputStream fos =
                c.openFileOutput("Album.dat", Context.MODE_PRIVATE);
        ObjectOutputStream oos = new ObjectOutputStream(fos);
        oos.writeObject(albums);
        oos.close();
    }

    public static List<Album> remake(Context context) throws IOException, ClassNotFoundException {
        List<Album> albums = new ArrayList<>();
        try {
            FileInputStream fis =
                    context.openFileInput("Album.dat");
            ObjectInputStream oi = new ObjectInputStream(fis);
            albums.addAll( (List<Album>) oi.readObject());
            oi.close();
            fis.close();


        } catch (FileNotFoundException e) { // default to initial se
        }
        return albums;
    }
}
