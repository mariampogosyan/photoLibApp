package com.example.masha.photoalbumapp52;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
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
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
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
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent,
                "Select Picture"), GET_FROM_GALLERY);
    }

    protected void onActivityResult(int requestCode, int resultCode,
                                    Intent data) {
        if(requestCode==GET_FROM_GALLERY && resultCode == RESULT_OK) {
            String imagePath;
            Uri selectedImage = data.getData();
            Bitmap bitmap = null;
            try {
                bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), selectedImage);
                imagePath = getRealPathFromURI(getApplicationContext(), selectedImage);
                Toast.makeText(this, imagePath, Toast.LENGTH_LONG)
                        .show();
                //Photo p = new Photo(imagePath);
                //album.addPhoto(p);
            } catch (FileNotFoundException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }

    public static String getRealPathFromURI(Context context, Uri contentUri) {

        if (contentUri != null) {

            Cursor cursor = null;
            try {

                String[] proj = {MediaStore.Images.Media.DATA};
                cursor = context.getContentResolver().query(contentUri, proj,
                        null, null, null);
                int column_index = cursor
                        .getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
                cursor.moveToFirst();
                return cursor.getString(column_index);
            } finally {

                if (cursor != null) {
                    cursor.close();
                }
            }
        }

        return "WHAT";
    }

}
