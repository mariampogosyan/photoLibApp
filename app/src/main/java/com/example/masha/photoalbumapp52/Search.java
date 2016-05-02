package com.example.masha.photoalbumapp52;

import android.app.ActionBar;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.LinearLayout;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by stephen.dacayanan on 5/1/2016.
 */
public class Search extends ActionBarActivity {

    EditText editText;
    Context c;
    LinearLayout linear;
    GridView gv;
    public  static ArrayList<Bitmap> bitmaps = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.search);
        c=this;
        linear = (LinearLayout) findViewById(R.id.linear);

        editText = (EditText)findViewById(R.id.editText);

        gv = (GridView) findViewById(R.id.gridView2);

        Toolbar toolbar = (Toolbar)findViewById(R.id.my_toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        editText.addTextChangedListener(new TextWatcher() {

            public void afterTextChanged(Editable s) {

            }
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String text = editText.getText().toString();
                String[] pq = s.toString().split(",");
                List<String> st = Arrays.asList(pq);
                List<Photo> photo = SearchUtil.getAllResults(PhotoAlbum.albums, st);
                Album tmp = new Album("tmp");
                tmp.setallp(photo);

                System.out.println(text);
                if(photo.size()>0 && !text.matches("")){
                    showImg(photo);
                } else {
                    gv.setAdapter(null);
                }
                editText.setFocusable(true);
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_search, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == android.R.id.home) {
            NavUtils.navigateUpFromSameTask(this);
            return true;
        }

        return super.onOptionsItemSelected(item);
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
}
