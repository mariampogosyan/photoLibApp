package com.example.masha.photoalbumapp52;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.view.View.OnClickListener;
import android.widget.Toast;

import java.io.IOException;


/**
 * Created by stephen.dacayanan on 5/1/2016.
 */
public class ViewImage extends AppCompatActivity {
    ViewAlbum va;
    ImageView imageView;
    Context context;
    TextView place, people;
    Toolbar toolbar;
    boolean isclicked = false;
    private int min_distance = 100;
    private float downX, downY, upX, upY;
    Photo p = PhotoAlbum.albums.get(ViewAlbum.pos).getPhotos().get(ViewAlbum.imgpos);


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_image);
        context = this;
        imageView = (ImageView)findViewById(R.id.imageView);
        place = (TextView)findViewById(R.id.tagPlaces);
        people = (TextView)findViewById(R.id.tagPeople);
        place.setText("Places" + p.getPlaceTags());
        people.setText("People" + p.getPersonTags());

        toolbar = (Toolbar)findViewById(R.id.my_toolbar);
        toolbar.inflateMenu(R.menu.tag_options);
        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                switch (menuItem.getItemId()) {

                    case R.id.add_loc: {
                        Toast.makeText(context, "move was clicked", Toast.LENGTH_SHORT).show();

                        tag("place");
                        return true;
                    }
                    case R.id.add_per:
                        tag("person");
                        return true;

                    case R.id.delete_tag:
                        return false;
                }
                return true;
            }


        });
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        Bitmap bitmap = va.bitmaps.get(va.imgpos);
        imageView.setImageBitmap(bitmap);
        imageView.setOnTouchListener(
                new View.OnTouchListener() {
                    @Override
                    public boolean onTouch(View v, MotionEvent event) {
                        // TODO Auto-generated method stub
                        switch(event.getAction()) { // Check vertical and horizontal touches
                            case MotionEvent.ACTION_DOWN: {
                                downX = event.getX();
                                downY = event.getY();
                                if (!isclicked) {
                                    toolbar.setVisibility(v.VISIBLE);
                                    isclicked = true;
                                } else {
                                    toolbar.setVisibility(v.GONE);
                                    isclicked = false;
                                }

                                return true;
                            }
                            case MotionEvent.ACTION_UP: {
                                upX = event.getX();
                                upY = event.getY();

                                float deltaX = downX - upX;
                                float deltaY = downY - upY;

                                if (Math.abs(deltaX) > Math.abs(deltaY)) {
                                    if (Math.abs(deltaX) > min_distance) {
                                        // left or right
                                        if (deltaX < 0) {
                                            LeftToRightSwipe();
                                            return true;
                                        }
                                        if (deltaX > 0) {
                                            RightToLeftSwipe();
                                            return true;
                                        }
                                    } else {
                                        return false;
                                    }
                                }
                                return false;
                            }
                        }
                        return false;
                    }
                });
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        //add person tags
        TextView tagPeople = (TextView)findViewById(R.id.tagPeople);
        tagPeople.setOnClickListener(new OnClickListener(){
            @Override
            public void onClick(View v) {
                tag("person");
            }
        });

        //add place tags
        TextView tagPlaces = (TextView)findViewById(R.id.tagPlaces);
        tagPlaces.setOnClickListener(new OnClickListener(){
            @Override
            public void onClick(View v) {
                tag("place");
            }
        });

    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.tag_options, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == android.R.id.home) {
            NavUtils.navigateUpFromSameTask(this);
            return true;

        }
        return true;
    }

    public void LeftToRightSwipe(){
        toolbar.setVisibility(View.GONE);
        isclicked = false;

        va.imgpos--;
        if (va.imgpos>=0){
            Bitmap bitmap = va.bitmaps.get(va.imgpos);
            imageView.setImageBitmap(bitmap);
        } else {
            va.imgpos = va.bitmaps.size();
        }
    }

    public void RightToLeftSwipe() {
        toolbar.setVisibility(View.GONE);
        isclicked = false;

        va.imgpos++;
        if(!(va.imgpos>=va.bitmaps.size())) {
            Bitmap bitmap = va.bitmaps.get(va.imgpos);
            imageView.setImageBitmap(bitmap);
        } else {
            va.imgpos = 0;
        }
    }
    private void tag(final String type){

        AlertDialog.Builder tag = new AlertDialog.Builder(this);
        tag.setTitle("Enter "+type+" name:");
        final EditText input = new EditText(this);
        tag.setView(input);

        tag.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String value = input.getText().toString();

                if(type.equalsIgnoreCase("person")){
                    p.addPersonTag(value);
//                    try {
//                        AlbumList.albumList.store();
//                    } catch (IOException e) {
//                        // TODO Auto-generated catch block
//                        e.printStackTrace();
//                    }
                }
                else if(type.equalsIgnoreCase("place")){
                    p.addPlaceTag(value);
//                    try {
//                        AlbumList.albumList.store();
//                    } catch (IOException e) {
//                        // TODO Auto-generated catch block
//                        e.printStackTrace();
//                    }
                }
                showTagList();
            }
        });
        tag.setNegativeButton("Cancel",new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // Canceled
            }
        });
        tag.show();
    }
    protected void showTagList() {
        place.setText("Places" + p.getPlaceTags());
        people.setText("People" + p.getPersonTags());

    }


}
