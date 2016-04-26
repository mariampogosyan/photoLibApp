package com.example.masha.photoalbumapp52;

import android.content.Context;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.NoSuchElementException;

/**
 * Created by Mariam on 4/24/2016.
 */
public class AlbumList {
    private static AlbumList albumList = null;
    private ArrayList<Album> albums;
    private int maxId;
    Context context;
    public static final String ALBUMS_FILE = "albumlist.dat";

    private AlbumList() {
        albums = new ArrayList<Album>();
        maxId = -1;
    }

    public static AlbumList getInstance(Context ctx)
            throws IOException {
        if (albumList == null) {
            albumList = new AlbumList();
            albumList.context = ctx;
            albumList.load();
        }

        return albumList;
    }

    public Album add(String name) {
        if (name == null) {
            Toast.makeText(context, "Please, give a name for your album.", Toast.LENGTH_LONG).show();
            return null;
        }
        maxId++;

        if(!duplicate(name)){
            Toast.makeText(context, "Album already exists.", Toast.LENGTH_LONG).show();
            return null;
        }

        Album album = new Album(maxId, name);
        if (albums.size() == 0) {
            albums.add(album);
            try {
                store();
            } catch (IOException e) {
                Toast.makeText(context, "Could not store albums to file",
                        Toast.LENGTH_LONG).show();
            }
            return album;
        }

        // search in array list and add at correct spot
        int lo = 0, hi = albums.size() - 1, mid = -1, c = 0;
        while (lo <= hi) {
            mid = (lo + hi) / 2;
            c = album.compareTo(albums.get(mid));
            if (c == 0) {  // duplicate name
                break;
            }
            if (c < 0) {
                hi = mid - 1;
            } else {
                lo = mid + 1;
            }
        }
        int pos = c <= 0 ? mid : mid + 1;
        // insert at pos
        albums.add(pos, album);
        // write through
        try {
            store();
            return album;
        } catch (IOException e) {
            return null;
        }

    }
    public boolean duplicate(String name){
        if(albums.size()==0){
            return true;
        }

        for(Album album: albums){
            if(name.equals(album.getAlbumName())){
                return false;
            }
        }

        return true;

    }
    public void load() throws IOException {

        int maxId=-1;
        try {
            FileInputStream fis =
                    context.openFileInput(ALBUMS_FILE);
            BufferedReader br =
                    new BufferedReader(
                            new InputStreamReader(fis));
            String AlbumInfo;
            while ((AlbumInfo = br.readLine()) != null) {
                Album album = Album.parseAlbum(AlbumInfo);
                maxId++;
                album.id = maxId;
                albums.add(album);
            }
            this.maxId = maxId;
            fis.close();

        } catch (FileNotFoundException e) {  // default to initial set
            // load initial set of Albums

        }

    }
    public int getPos(Album album) {
        if (albums.size() == 0) {
            return -1;
        }

        // search in array list for name match, then id match
        int lo = 0, hi = albums.size() - 1;

        while (lo <= hi) {
            int mid = (lo + hi) / 2;
            Album lalbum = albums.get(mid);
            int c = album.compareTo(lalbum);
            if (c == 0) {  // need to compare id
                if (album.id == lalbum.id) {
                    return mid;
                }
                // check left
                int i = mid - 1;
                while (i >= 0) {
                    lalbum = albums.get(i);
                    if (album.compareTo(lalbum) == 0 && album.id == lalbum.id) {
                        return i;
                    }
                    i--;
                }
                // check right
                i = mid + 1;
                while (i < albums.size()) {
                    lalbum = albums.get(i);
                    if (album.compareTo(lalbum) == 0 && album.id == lalbum.id) {
                        return i;
                    }
                    i++;
                }
                return -1;
            }
            if (c < 0) {
                hi = mid - 1;
            } else {
                lo = mid + 1;
            }
        }
        return -1;
    }

    public void store() throws IOException {
        // TO BE FILLED IN
        FileOutputStream fos =
                context.openFileOutput(ALBUMS_FILE,Context.MODE_PRIVATE);
        PrintWriter pw = new PrintWriter(fos);
        for (Album album: albums) {
            pw.println(album.toString());
        }
        pw.close();

    }

    public ArrayList<Album> remove(Album album) throws NoSuchElementException {
        int pos = getPos(album);
        if (pos == -1) {
            throw new NoSuchElementException();
        }
        albums.remove(pos);

        try {
            store();
        } catch (IOException e) {
            Toast.makeText(context, "Could not store albums to file",
                    Toast.LENGTH_LONG).show();
        }

        return albums;
    }


    public void setContext(Context ctx) {
        // TO BE FILLED IN
        context = ctx;

    }



    public ArrayList<Album> update(Album album) throws NoSuchElementException {
        // since name could be updated, best to sequentially search on id
        for (int i = 0; i < albums.size(); i++) {
            if (albums.get(i).id == album.id) {
                albums.set(i, album);

                try {
                    store();
                } catch (IOException e) {
                    Toast.makeText(context, "Could not store albums to file",
                            Toast.LENGTH_LONG).show();
                }

                return albums;
            }
        }
        throw new NoSuchElementException();
    }
    public ArrayList<Album> getAlbums(){
        return this.albums;
    }

}
