package com.nfortics.mfinanceV2.ViewAdapters;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.nfortics.mfinanceV2.Models.PersonalDetailsList;
import com.nfortics.mfinanceV2.R;
import com.nfortics.mfinanceV2.Typefacer;

import java.util.List;

/**
 * Created by bigfire on 2/13/2016.
 */
public class PersonalDetailsListAdapter  extends ArrayAdapter<PersonalDetailsList> {

    private final Activity context;
    private final List<PersonalDetailsList> settingsName;
    private   boolean value;

    public PersonalDetailsListAdapter(Activity context, List<PersonalDetailsList> settingsName) {
        super(context, R.layout.personaldetails_row, settingsName);
        this.context = context;
        this.settingsName = settingsName;


    }

    public View getView(int position, View view, ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        Typefacer typefacer = new Typefacer();

        PersonalDetailsList items=settingsName.get(position);
        View rowView = inflater.inflate(R.layout.personaldetails_row, null, true);

        TextView txtTitle = (TextView) rowView.findViewById(R.id.settingsTitle);

        TextView  menudiscrip= (TextView) rowView.findViewById(R.id.settingsDetails);
        //txtTitle.setTextColor(0x0000FF);
        //txtTitle.setTypeface(typefacer.getRoboRegular(context.getAssets()));


        txtTitle.setTypeface(typefacer.squareRegular());
        txtTitle.setTextColor(context.getResources().getColor(R.color.primary));
        menudiscrip.setTypeface(typefacer.squareLight());
        // txtTitle.setTextColor(0x0000FF);
        txtTitle.setText(items.getTitle());


        menudiscrip.setText(items.getDescription());
        return rowView;

    }

}
