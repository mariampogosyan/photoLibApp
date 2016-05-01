package com.example.masha.photoalbumapp52;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by stephen.dacayanan on 5/1/2016.
 */
public class ViewImage extends ActionBarActivity {
    ViewAlbum va;
    ImageView imageView;
    Context context;
    Toolbar toolbar;
    boolean isclicked = false;
    private int min_distance = 100;
    private float downX, downY, upX, upY;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_image);
        context = this;
        imageView = (ImageView)findViewById(R.id.imageView);
        toolbar = (Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
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
    }

    public void LeftToRightSwipe(){
        toolbar.setVisibility(View.GONE);
        isclicked = false;

        va.imgpos--;
        if (va.imgpos>=0){
            Bitmap bitmap = va.bitmaps.get(va.imgpos);
            imageView.setImageBitmap(bitmap);
        } else {
            va.imgpos = 0;
        }
    }

    public void RightToLeftSwipe() {
        toolbar.setVisibility(View.GONE);
        isclicked = false;

        va.imgpos++;
        if(!(GridViewImg.pos>=va.bitmaps.size())) {
            Bitmap bitmap = va.bitmaps.get(va.imgpos);
            imageView.setImageBitmap(bitmap);
        } else {
            va.imgpos = va.bitmaps.size();
        }
    }
}
