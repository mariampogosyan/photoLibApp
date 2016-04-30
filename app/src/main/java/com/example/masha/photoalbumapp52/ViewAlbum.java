package com.example.masha.photoalbumapp52;

import android.content.Context;
import android.content.CursorLoader;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by stephen.dacayanan on 4/27/2016.
 */
public class ViewAlbum extends AppCompatActivity {
    public static final String ALBUM_NAME = "albumName";
    public static final String ALBUM_ID = "albumID";
    public static final int GET_FROM_GALLERY = 1;
    private TextView albumName;
    private Album album;
    private GridView gv;
    private int albumID;
    private AlbumList albumList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_album);

        Toolbar toolbar = (Toolbar)findViewById(R.id.my_toolbar);
        setSupportActionBar(toolbar);

        gv = (GridView) findViewById(R.id.gridView);
        try {
            albumList = AlbumList.getInstance(this);
        } catch (IOException e) {
            Toast.makeText(this, "Error loading albums", Toast.LENGTH_LONG)
                    .show();
        }
        albumName = (TextView)findViewById(R.id.album_name);
        // check if Bundle was passed, and populate fields
        Bundle bundle = getIntent().getExtras();
        albumID = -1;
        if (bundle != null) {
            albumID = bundle.getInt(ALBUM_ID);
            albumName.setText(bundle.getString(ALBUM_NAME));
        }

    }

    public void addPhoto(View view) {
        Intent intent = new Intent(Intent.ACTION_PICK,
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        intent.setType("image/*");
        startActivityForResult(Intent.createChooser(intent, "Select a gallery"),
                GET_FROM_GALLERY);

    }

    protected void onActivityResult(int requestCode, int resultCode,
                                    Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==GET_FROM_GALLERY && resultCode == RESULT_OK) {
            Uri selectedImageUri = data.getData();
            String[] projection = {MediaStore.MediaColumns.DATA};
            CursorLoader cursorLoader = new CursorLoader(this, selectedImageUri, projection, null, null,
                    null);
            Cursor cursor = cursorLoader.loadInBackground();
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.DATA);
            cursor.moveToFirst();
            String selectedImage = cursor.getString(column_index);
            Photo p = new Photo(selectedImage);

          //  Album album = albumList.getAlbums().get(albumID);
           // album.addPhoto(p);
      //      showImg(album.getPhotos());
//            try {
//                albumList.addPic(albumID, p);
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
           // String size = Integer.toString(albumList.sizeCurrent(albumID));
            //  album.addPhoto(p);
           // album.getAlbumName();
           // String size =Integer.toString(album.getPhotos().size());
          //Toast.makeText(this, size, Toast.LENGTH_SHORT).show();
       // String id = Integer.toString(albumID);
          //  Toast.makeText(this, id, Toast.LENGTH_SHORT).show();


        }
    }
}


