package com.nfortics.mfinanceV2.ViewAdapters;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.nfortics.mfinanceV2.R;
import com.nfortics.mfinanceV2.Typefacer;

/**
 * Created by bigfire on 11/12/2015.
 */
public class MenuListAdapters extends ArrayAdapter<String> {

    private final Activity context;

    private final String[] itemname;


    public MenuListAdapters(Activity context, String[] itemname) {
        super(context, R.layout.menu_row_items, itemname);
        // TODO Auto-generated constructor stub

        this.context = context;
        this.itemname = itemname;

    }

    public View getView(int position, View view, ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        Typefacer typefacer = new Typefacer();
        View rowView = inflater.inflate(R.layout.menu_row_items, null, true);

        TextView txtTitle = (TextView) rowView.findViewById(R.id.tvTitle);
        //txtTitle.setTextColor(0x0000FF);
        //txtTitle.setTypeface(typefacer.getRoboRegular(context.getAssets()));


        txtTitle.setTypeface(typefacer.squareLight());
        txtTitle.setTextColor(context.getResources().getColor(R.color.primary));
       // txtTitle.setTextColor(0x0000FF);
        txtTitle.setText(itemname[position]);


        return rowView;

    }

}