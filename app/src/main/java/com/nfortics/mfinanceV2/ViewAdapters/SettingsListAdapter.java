package com.nfortics.mfinanceV2.ViewAdapters;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.nfortics.mfinanceV2.Models.ImageItem;
import com.nfortics.mfinanceV2.Models.SettingsList;
import com.nfortics.mfinanceV2.R;
import com.nfortics.mfinanceV2.Typefacer;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by bigfire on 2/3/2016.
 */
public class SettingsListAdapter extends ArrayAdapter<SettingsList> {

    private final Activity context;
    private final List<SettingsList> settingsName;

    public SettingsListAdapter(Activity context, List<SettingsList> settingsName) {
        super(context, R.layout.appsettings_row, settingsName);
        this.context = context;
        this.settingsName = settingsName;

    }

    public View getView(int position, View view, ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        Typefacer typefacer = new Typefacer();

        SettingsList items=settingsName.get(position);
        View rowView = inflater.inflate(R.layout.appsettings_row, null, true);

        TextView txtTitle = (TextView) rowView.findViewById(R.id.settingsTitle);
        ImageView icon=(ImageView)rowView.findViewById(R.id.imageView5);

        TextView  menudiscrip= (TextView) rowView.findViewById(R.id.settingsDetails);
        //txtTitle.setTextColor(0x0000FF);
        //txtTitle.setTypeface(typefacer.getRoboRegular(context.getAssets()));


        txtTitle.setTypeface(typefacer.squareRegular());
        txtTitle.setTextColor(context.getResources().getColor(R.color.primary));
        menudiscrip.setTypeface(typefacer.squareLight());
        // txtTitle.setTextColor(0x0000FF);
        txtTitle.setText(items.getTitle());
        icon.setImageResource(items.getResourceID());
        menudiscrip.setText(items.getDescription());
        return rowView;

    }


}
