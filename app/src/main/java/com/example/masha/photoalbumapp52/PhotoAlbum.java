package com.example.masha.photoalbumapp52;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.util.SparseBooleanArray;
import android.view.ActionMode;
import android.view.View;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by Mariam on 4/24/2016.
 */
public class PhotoAlbum extends AppCompatActivity {
    final Context context = this;
    String userResponse;
    private AlbumList albumList;
    private ListView lv;
    private Album selectedItem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.album_list);
        Toolbar toolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(toolbar);

        try {
            albumList = AlbumList.getInstance(this);
            albumList.load();
      } catch (IOException e) {
          Toast.makeText(this, "Error loading albums", Toast.LENGTH_LONG)
                    .show();
        }
        lv = (ListView) findViewById(R.id.lv);

        lv.setAdapter(
                new ArrayAdapter<Album>(this, android.R.layout.simple_list_item_1,albumList.getAlbums()));

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                selectedItem = (Album)parent.getItemAtPosition(position);
            }
        });


//        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
//            }
//        });

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
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
                       albumList.add(name);
                      //  arrayAdapter = new ArrayAdapter<Album>(context,R.layout.album,albums);
                       // albumNames.setAdapter(arrayAdapter);
                    }
                });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });

                builder.show();

            }
        });


        // for contextual action mode
        lv.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE_MODAL);
        lv.setMultiChoiceModeListener(
                new AbsListView.MultiChoiceModeListener() {
                    @Override
                    public boolean onActionItemClicked(ActionMode actionMode, MenuItem menuItem) {
                        // Respond to clicks on the actions in the CAB
                        switch (menuItem.getItemId()) {
                            case R.id.action_delete:
                                deleteSelectedItems();
                                actionMode.finish(); // Action picked, so close the CAB
                                return true;
                            default:
                                return false;
                        }

                    }

                    @Override
                    public boolean onCreateActionMode(ActionMode mode, Menu menu) {
                        // Inflate the menu for the CAB
                        MenuInflater inflater = mode.getMenuInflater();
                        inflater.inflate(R.menu.delete_menu, menu);
                        return true;
                    }

                    @Override
                    public void onItemCheckedStateChanged(ActionMode mode, int position,
                                                          long id, boolean checked) {
                        // Here you can do something when items are selected/de-selected,
                        // such as update the title in the CAB
                        mode.setTitle(lv.getCheckedItemCount() +
                                " selected");
                    }

                    @Override
                    public void onDestroyActionMode(ActionMode mode) {
                        // Here you can make any necessary updates to the activity when
                        // the CAB is removed. By default, selected items are deselected/unchecked.
                    }

                    @Override
                    public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
                        // Here you can perform updates to the CAB due to
                        // an invalidate() request
                        return false;
                    }

                });
    }
    private void deleteSelectedItems() {
        SparseBooleanArray arr = lv.getCheckedItemPositions();
        //String str="";
        // gather songs in a to-delete list
        ArrayList<Album> deleteAlbums = new ArrayList<Album>();
        for (int i=0; i < arr.size(); i++) {
            if (arr.valueAt(i)) {
                Album album = (Album)lv.getItemAtPosition(
                        arr.keyAt(i));
                //str += song.id + ";";
                deleteAlbums.add(album);
            }
        }
        for (Album album: deleteAlbums) {
            albumList.remove(album);
        }
        //Toast.makeText(SongLib.this,str,Toast.LENGTH_LONG).show();
      lv.setAdapter(new ArrayAdapter<Album>(this, android.R.layout.simple_list_item_1,albumList.getAlbums()));

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
