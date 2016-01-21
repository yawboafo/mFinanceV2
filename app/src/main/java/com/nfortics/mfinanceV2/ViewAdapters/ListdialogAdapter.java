package com.nfortics.mfinanceV2.ViewAdapters;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.nfortics.mfinanceV2.Application.Application;
import com.nfortics.mfinanceV2.Models.Favorites;
import com.nfortics.mfinanceV2.R;
import com.nfortics.mfinanceV2.Typefacer;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by bigfire on 12/14/2015.
 */
public class ListdialogAdapter extends ArrayAdapter<String> {

    private Activity context;

    private List<String> itemname= Collections.emptyList();

    List<String> mapKeys;
    public ListdialogAdapter(Activity context, List<String> itemname) {
        super(context, R.layout.list_dialog_item_layout, itemname);
        // TODO Auto-generated constructor stub

        this.context = context;
        this.itemname = itemname;
       // mapKeys=new ArrayList<>();
       // KeySets();

    }



    private void KeySets(){

        for ( String key :  Application.checkedImageLabels.keySet() ) {

            mapKeys.add(key);
            Log.d("oxinbo", "key = >" + key);
        }
    }
    public View getView(int position, View view, ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        Typefacer typefacer = new Typefacer();


       // LT.setPaintFlags(LT.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
      //  LT.setTextColor(Color.parseColor("#000000"));

        String items = itemname.get(position);
        View rowView = inflater.inflate(R.layout.list_dialog_item_layout, null, true);


        TextView txtTitle = (TextView) rowView.findViewById(R.id.item);
      //  ImageView icon = (ImageView) rowView.findViewById(R.id.icon);

        txtTitle.setTypeface(typefacer.squareLight());
        txtTitle.setText(items);
        for (String x :Application.listOfKeysChecked) {

            if (x.equals(items)) {
                txtTitle.setPaintFlags(txtTitle.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
                txtTitle.setTextColor(Color.parseColor("#000000"));


            }}


            return rowView;

        }
}
