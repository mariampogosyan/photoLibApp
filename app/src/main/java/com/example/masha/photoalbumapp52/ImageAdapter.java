package com.example.masha.photoalbumapp52;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

import java.util.ArrayList;

/**
 * Created by stephen.dacayanan on 4/28/2016.
 */
public class ImageAdapter extends BaseAdapter {
    private Context mContext;
    private ArrayList<Bitmap> photos;


//    public ImageAdapter(Context c) {
//        mContext = c;
//    }

    public ImageAdapter(Context c, ArrayList<Bitmap> arrayList) {

        mContext = c;
        photos = arrayList;
    }
    public int getCount() {
        return 1;
    }

    public Object getItem(int position) {
        return null;
    }

    public long getItemId(int position) {
        return 0;
    }

    // create a new ImageView for each item referenced by the Adapter
    public View getView(int position, View convertView, ViewGroup parent) {
//        ImageView imageView;
//        imageView = (ImageView) convertView;
//        return imageView;
        ImageView imageView;
        if (convertView == null) {  // if it's not recycled, initialize some attributes
            imageView = new ImageView(mContext);
            imageView.setLayoutParams(new GridView.LayoutParams(150, 150));

            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            imageView.setPadding(8, 8, 8, 8);
        } else {
            imageView = (ImageView) convertView;
        }
        //Bitmap b2 = b.createBitmap(b);
        //b = b.createScaledBitmap(b, 300, 300, true);
        imageView.setImageBitmap(photos.get(position));

        return imageView;
    }
}
