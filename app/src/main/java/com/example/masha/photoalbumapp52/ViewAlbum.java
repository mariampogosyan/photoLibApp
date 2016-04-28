package com.example.masha.photoalbumapp52;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.GridView;
import android.widget.TextView;

import java.io.FileNotFoundException;
import java.io.IOException;

/**
 * Created by stephen.dacayanan on 4/27/2016.
 */
public class ViewAlbum extends AppCompatActivity {
    public static final String ALBUM_NAME = "albumName";
    public static final String ALBUM_ID = "albumID";
    public static final int GET_FROM_GALLERY=1;

    TextView albumName;
    private Album album;
    GridView gv;
    int albumID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_album);

        Toolbar toolbar = (Toolbar)findViewById(R.id.my_toolbar);
        setSupportActionBar(toolbar);

        gv = (GridView) findViewById(R.id.gridView);
        //gv.setAdapter(new ImageAdapter(this));

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
        startActivityForResult(new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.INTERNAL_CONTENT_URI), GET_FROM_GALLERY);
    }

    protected void onActivityResult(int requestCode, int resultCode,
                                    Intent data) {
        if(requestCode==GET_FROM_GALLERY && resultCode == RESULT_OK) {
            Uri selectedImage = data.getData();
            Bitmap bitmap = null;
            try {
                bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), selectedImage);
            } catch (FileNotFoundException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }
}
