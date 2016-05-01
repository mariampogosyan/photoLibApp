package com.example.masha.photoalbumapp52;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.DisplayMetrics;
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
    int height;
    int width;

//    public ImageAdapter(Context c) {
//        mContext = c;
//    }

    public ImageAdapter(Context c, ArrayList<Bitmap> arrayList) {

        mContext = c;
        photos = arrayList;
        DisplayMetrics metrics = c.getResources().getDisplayMetrics();
        width = metrics.widthPixels;
        height = metrics.heightPixels;
    }
    public int getCount() {
        return photos.size();
    }

    public Object getItem(int position) {
        return position;
    }

    public long getItemId(int position) {
        return position;
    }

    // create a new ImageView for each item referenced by the Adapter
    public View getView(int position, View convertView, ViewGroup parent) {


        ImageView imageView;
        if (convertView == null) {  // if it's not recycled, initialize some attributes
            imageView = new ImageView(mContext);
            imageView.setLayoutParams(new GridView.LayoutParams(width/3, width/3));

            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            imageView.setPadding(15, 15, 15, 15);
        } else {
            imageView = (ImageView) convertView;
        }
        //Bitmap b2 = b.createBitmap(b);
        //b = b.createScaledBitmap(b, 300, 300, true);
        imageView.setImageBitmap(photos.get(position));

        return imageView;
    }
}
