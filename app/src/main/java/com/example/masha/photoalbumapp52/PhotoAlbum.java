package com.example.masha.photoalbumapp52;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.view.ContextMenu;
import android.view.View;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Mariam on 4/24/2016.
 */
public class PhotoAlbum extends AppCompatActivity {
    Context context;
    String userResponse;
    public static List<Album> albums = new ArrayList<>();
    private ListView lv;
    private Album selectedItem;
    public static int pos;

    public static final int VIEW_ALBUM_CODE=1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.album_list);
        try {
            albums = Album.remake(this);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        context = this;

       /* if(albums.size() != 0)
            prgmImages = Drawable.createFromPath(albums.get(0).getPhotos().get(0).getAddress());*/
        Toolbar toolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(toolbar);

        lv = (ListView) findViewById(R.id.lv);
        registerForContextMenu(lv);




        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                pos = position;
                openPhotoView(pos);

            }
        });

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        assert fab != null;
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setTitle("Enter album name:");

                final EditText input = new EditText(context);
                input.setInputType(InputType.TYPE_CLASS_TEXT);
                builder.setView(input);

                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                      //  userResponse = input.getText().toString();
                        //Album tmp = new Album();
                        String name =input.getText().toString();
                       // tmp.setAlbumName(name);
                        if(!duplicate(name)){
                            Toast.makeText(context, "Album already exists.", Toast.LENGTH_LONG).show();
                            dialog.cancel();
                        } else {

                        Album a = new Album(name);
                        if(!albums.contains(a)) {
                            albums.add(a);
                            try {
                                Album.make(albums, context);
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                      //  arrayAdapter = new ArrayAdapter<Album>(context,R.layout.album,albums);
                       // albumNames.setAdapter(arrayAdapter);
                    }}
                });

                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });

                builder.show();

            }
        });

        lv.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {

            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                //gview.itemLongClick(position);
                pos = position;
                return false;
            }
        });

        lv.setAdapter(
                new ArrayAdapter<Album>(PhotoAlbum.this, android.R.layout.simple_list_item_1, albums));
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.context_menu_listview, menu);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.rename:
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setTitle("Enter new name:");

                final EditText input = new EditText(context);
                input.setInputType(InputType.TYPE_CLASS_TEXT);
                builder.setView(input);

                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        String name =input.getText().toString();
                        if(!duplicate(name)){
                            Toast.makeText(context, "Album already exists.", Toast.LENGTH_LONG).show();
                            dialog.cancel();
                        } else {

                            Album a = albums.get(pos);
                            a.setName(name);
                            try {
                                Album.make(albums, context);
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }}
                });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });

                builder.show();

                break;
            case R.id.delete:
                //Toast.makeText(Home.this, "Delete was clicked", Toast.LENGTH_SHORT).show();

                android.app.AlertDialog.Builder b = new android.app.AlertDialog.Builder(context);
                b.setMessage("Are you sure you want to delete?");
                b.setCancelable(true);

                b.setPositiveButton(
                        "Yes",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                albums.remove(pos);
                                try {
                                    Album.make(albums, context);
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                                dialog.cancel();
                                lv.setAdapter(
                                        new ArrayAdapter<Album>(PhotoAlbum.this, android.R.layout.simple_list_item_1, albums));
                            }
                        });

                b.setNegativeButton(
                        "No",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });

                android.app.AlertDialog a = b.create();
                a.show();
                break;
        }
        return super.onContextItemSelected(item);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }
    public void openPhotoView(int pos){
        Intent intent = new Intent(this, ViewAlbum.class);
        intent.putExtra("pos", pos);
        startActivity(intent);
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

}
