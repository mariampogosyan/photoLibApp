package com.example.masha.photoalbumapp52;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by stephen.dacayanan on 5/2/2016.
 */
public class SearchUtil {
    /**
     * list of hashes
     */
    //  public static List<Hash> hash = new ArrayList<>();

    /**
     * private constructor for search
     */
    private SearchUtil() {
    }

    public static List<Photo> searchByPerson(List<Album> album, List<String> tags) {
        List<Photo> photo = new ArrayList<>();
        for (int i = 0; i < tags.size(); i++) {
            for (int j = 0; j < album.size(); j++) {
                for (int k = 0; k < album.get(j).getPhotos().size(); k++) {
                    String g = tags.get(i);
                    Object[] tag;
                    for (String in : album.get(j).getPhotos().get(k).getPersonTags())
                        if(in.contains(g) && !photo.contains(album.get(j).getPhotos().get(k)))
                            photo.add(album.get(j).getPhotos().get(k));
                }
            }
        }
        return photo;
    }

    public static List<Photo> searchByPlace(List<Album> album, List<String> tags) {
        List<Photo> photo = new ArrayList<>();
        for (int i = 0; i < tags.size(); i++) {
            for (int j = 0; j < album.size(); j++) {
                for (int k = 0; k < album.get(j).getPhotos().size(); k++) {
                    String g = tags.get(i);
                    Object[] tag;
                    for (String in : album.get(j).getPhotos().get(k).getPlaceTags())
                        if(in.contains(g) && !photo.contains(album.get(j).getPhotos().get(k)))
                            photo.add(album.get(j).getPhotos().get(k));
                }
            }
        }
        return photo;
    }

    /** main search method
     * @param album list of albums
     * @param tags list of tags
     * @return list of photos
     */
    public static List<Photo> getAllResults(List<Album> album, List<String> tags) {
        List<Photo> photo = searchByPerson(album, tags);
        List<Photo> photos = searchByPlace(album, tags);
        photo.addAll(photos);
        return photo;
    }
}
