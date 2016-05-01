package com.example.masha.photoalbumapp52;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.content.ContentUris;
import android.content.Context;
import android.content.CursorLoader;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.FileDescriptor;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by stephen.dacayanan on 4/27/2016.
 */
public class ViewAlbum extends AppCompatActivity {
    public static final String ALBUM_NAME = "albumName";
    public static final String ALBUM_ID = "albumID";
    public static final int GET_FROM_GALLERY = 1;
    public static final int PERMISSION_REQUEST = 2;
    private static final int KITKAT_INTENT = 3;


    private TextView albumName;
    private Album album;
    private GridView gv;
    private int albumID;
    private static int pos;
    public static int imgpos;

    private AlbumList albumList;
    Context c;
    public  static ArrayList<Bitmap> bitmaps = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_album);

        Toolbar toolbar = (Toolbar)findViewById(R.id.my_toolbar);
        setSupportActionBar(toolbar);

        c = this;
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
        pos = (int) bundle.get("pos");
        if(!PhotoAlbum.albums.get(pos).getPhotos().isEmpty()){
            showImg(PhotoAlbum.albums.get(pos).getPhotos());
        }
        //gv.setAdapter(new ImageAdapter(this, album.getPhotos()));

        gv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                imgpos = position;
                openViewImage(imgpos);



                //gview.itemClick(position, context);
                //Toast.makeText(Photos.this, "Deep", Toast.LENGTH_SHORT).show();


                //a.add(1, a.get(position));
                //gridView.setAdapter(new GridAdapter(Photos.this, a,width));
                //startActivity(new Intent(Photos.this, ViewImage.class).putExtra("Bitmapimg", a.get(position)));
            }
        });

        /*gv.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {

            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                //gview.itemLongClick(position);
                imgpos = position;
                return false;
            }
        });*/

        registerForContextMenu(gv);

    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        getMenuInflater().inflate(R.menu.context_menu_gridview, menu);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.copy:
                Toast.makeText(ViewAlbum.this, "Copy was clicked", Toast.LENGTH_SHORT).show();
                break;
            case R.id.delete:
                Toast.makeText(ViewAlbum.this, "Delete was clicked", Toast.LENGTH_SHORT).show();

                AlertDialog.Builder b = new AlertDialog.Builder(c);
                b.setMessage("Are you sure you want to delete?");
                b.setCancelable(true);

                b.setPositiveButton(
                        "Yes",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                PhotoAlbum.albums.get(pos).getPhotos().remove(imgpos);
                                showImg(PhotoAlbum.albums.get(pos).getPhotos());
                                try {
                                    Album.make(PhotoAlbum.albums, c);
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                                dialog.cancel();
                            }
                        });

                b.setNegativeButton(
                        "No",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });

                AlertDialog a = b.create();
                a.show();
                break;

            case R.id.move:
                final ArrayAdapter<Album> al = new ArrayAdapter<>(ViewAlbum.this, android.R.layout.simple_list_item_1, PhotoAlbum.albums);
                AlertDialog.Builder m = new AlertDialog.Builder(this);
                m.setNegativeButton(
                        "cancel",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });
                m.setAdapter(al, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Album a = al.getItem(which);
                        move(which, PhotoAlbum.pos);
                        showImg(PhotoAlbum.albums.get(pos).getPhotos());
                        try {
                            Album.make(PhotoAlbum.albums, c);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        dialog.cancel();
                        Toast.makeText(ViewAlbum.this, a.toString() + " (" + which + ") " + " selected", Toast.LENGTH_SHORT).show();
                    }
                });
                m.setTitle("Select which album to move to");
                AlertDialog n = m.create();
                n.show();
                Toast.makeText(ViewAlbum.this, "move was clicked", Toast.LENGTH_SHORT).show();
                break;
        }
        return super.onContextItemSelected(item);
    }

    public void addPhoto(View view) {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, PERMISSION_REQUEST);
            } else {
                Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
                intent.addCategory(Intent.CATEGORY_OPENABLE);
                intent.setType("image/*");
                startActivityForResult(intent, KITKAT_INTENT);
            }


        } else {
            Intent intent = new Intent(
                    Intent.ACTION_PICK,
                    android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            intent.setType("image/*");
            startActivityForResult(
                    Intent.createChooser(intent, "Select a gallery"),
                    GET_FROM_GALLERY);
        }
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case PERMISSION_REQUEST: {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
                    intent.addCategory(Intent.CATEGORY_OPENABLE);
                    intent.setType("image/*");
                    startActivityForResult(intent, KITKAT_INTENT);

                } else {
//denial?>
                }
                return;
            }
        }
    }

    protected void onActivityResult(int requestCode, int resultCode,
                                    Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==GET_FROM_GALLERY && resultCode == RESULT_OK) {
            Uri selectedImageUri = data.getData();
            String[] ps = {MediaStore.MediaColumns.DATA};
            CursorLoader cursorLoader = new CursorLoader(this, selectedImageUri, ps, null, null,
                    null);
            Cursor cursor = cursorLoader.loadInBackground();
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.DATA);
            cursor.moveToFirst();

            String selectedImagePath = cursor.getString(column_index);
            Photo p = new Photo(selectedImagePath);
            if(!PhotoAlbum.albums.get(pos).getPhotos().contains(p)) {
                PhotoAlbum.albums.get(pos).addPhoto(p);
                showImg(PhotoAlbum.albums.get(pos).getPhotos());
                System.out.println(PhotoAlbum.albums.size());
                Drawable d = Drawable.createFromPath(p.getFileURL());

                try {
                    Album.make(PhotoAlbum.albums, this);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            // Album album = albumList.getAlbums().get(albumID);
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


        } else if (requestCode == KITKAT_INTENT){
            final Uri uri = data.getData();
            String s = getPath(this, uri);
            Photo p = new Photo(s);
            if(!PhotoAlbum.albums.get(pos).getPhotos().contains(p)) {
                PhotoAlbum.albums.get(pos).addPhoto(p);
                showImg(PhotoAlbum.albums.get(pos).getPhotos());
                Drawable d = Drawable.createFromPath(p.getFileURL());
                try {
                    Album.make(PhotoAlbum.albums, this);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
    public void showImg(List<Photo> photos) {
    bitmaps.clear();
        for(Photo p : photos) {
            System.out.println(p.getFileURL());

            Bitmap bm;
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inJustDecodeBounds = true;
            BitmapFactory.decodeFile(p.getFileURL(), options);
            final int REQUIRED_SIZE = 1000;
            int scale = 1;
            while (options.outWidth / scale / 2 >= REQUIRED_SIZE
                    && options.outHeight / scale / 2 >= REQUIRED_SIZE)
                scale *= 2;
            options.inSampleSize = scale;
            options.inJustDecodeBounds = false;
            bm = BitmapFactory.decodeFile(p.getFileURL(), options);
            bitmaps.add(bm);
            gv.setAdapter(new ImageAdapter(this, bitmaps));

        }
    }


    @TargetApi(Build.VERSION_CODES.KITKAT)
    public static String getPath(final Context context, final Uri uri) {
        final boolean isKitKat = Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT;
        if (isKitKat && DocumentsContract.isDocumentUri(context, uri)) {
            if (isExternalStorageDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];
                if ("primary".equalsIgnoreCase(type)) {
                    return Environment.getExternalStorageDirectory() + "/" + split[1];
                }
                // TODO handle non-primary volumes
            }
            else if (isDownloadsDocument(uri)) {

                final String id = DocumentsContract.getDocumentId(uri);
                final Uri contentUri = ContentUris.withAppendedId(
                        Uri.parse("content://downloads/public_downloads"), Long.valueOf(id));

                return getDataColumn(context, contentUri, null, null);
            }
            else if (isMediaDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];

                Uri contentUri = null;
                if ("image".equals(type)) {
                    contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                } else if ("video".equals(type)) {
                    contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                } else if ("audio".equals(type)) {
                    contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                }
                final String selection = "_id=?";
                final String[] selectionArgs = new String[] {
                        split[1]
                };

                return getDataColumn(context, contentUri, selection, selectionArgs);
            }
        }
        else if ("content".equalsIgnoreCase(uri.getScheme())) {
            return getDataColumn(context, uri, null, null);
        }
        else if ("file".equalsIgnoreCase(uri.getScheme())) {
            return uri.getPath();
        }
        return null;
    }
    public static boolean isExternalStorageDocument(Uri uri) {
        return "com.android.externalstorage.documents".equals(uri.getAuthority());
    }

    public static boolean isDownloadsDocument(Uri uri) {
        return "com.android.providers.downloads.documents".equals(uri.getAuthority());
    }

    public static boolean isMediaDocument(Uri uri) {
        return "com.android.providers.media.documents".equals(uri.getAuthority());
    }
    public static String getDataColumn(Context context, Uri uri, String selection,
                                       String[] selectionArgs) {

        Cursor cursor = null;
        final String column = "_data";
        final String[] projection = {
                column
        };
        try {
            cursor = context.getContentResolver().query(uri, projection, selection, selectionArgs,
                    null);
            if (cursor != null && cursor.moveToFirst()) {
                final int column_index = cursor.getColumnIndexOrThrow(column);
                return cursor.getString(column_index);
            }
        } finally {
            if (cursor != null)
                cursor.close();
        }
        return null;
    }

    public void openViewImage(int pos) {
        Intent intent = new Intent(this, ViewImage.class);
        startActivity(intent);
    }

    public void move(int alin, int phin) {
        //alin is album you want to move to
        // phin is photo you want to move
        Photo move = PhotoAlbum.albums.get(pos).getPhotos().get(phin);
        boolean should = PhotoAlbum.albums.get(alin).getPhotos().contains(move);
        if(!should){
            PhotoAlbum.albums.get(pos).getPhotos().remove(phin);
            PhotoAlbum.albums.get(alin).addPhoto(move);
        }
    }

}


