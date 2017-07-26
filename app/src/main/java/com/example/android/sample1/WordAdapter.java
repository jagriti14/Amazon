package com.example.android.sample1;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by Dell on 23-07-2017.
 */

public class WordAdapter extends ArrayAdapter<AmazonXmlParser.Entry> {
    private static final String LOG_TAG = WordAdapter.class.getSimpleName();


    public WordAdapter(Activity context, ArrayList<AmazonXmlParser.Entry> androidFlavors) {
        super(context, 0, androidFlavors);
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View listItemView = convertView;
        if (listItemView == null) {
            listItemView = LayoutInflater.from(getContext()).inflate(R.layout.list_item, parent, false);
        }
        AmazonXmlParser.Entry currentAndroidFlavor = getItem(position);

        ImageView ProductImage = (ImageView) listItemView.findViewById(R.id.image);

        Picasso.with(getContext()).load(currentAndroidFlavor.getUrl()).into(ProductImage);

        TextView name = (TextView) listItemView.findViewById(R.id.name);
        name.setText(currentAndroidFlavor.getTitle());

        return listItemView;
    }



}
