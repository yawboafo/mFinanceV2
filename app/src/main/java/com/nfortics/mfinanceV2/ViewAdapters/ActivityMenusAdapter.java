package com.nfortics.mfinanceV2.ViewAdapters;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.nfortics.mfinanceV2.NavigationDrawer.NavigationItems;
import com.nfortics.mfinanceV2.R;
import com.nfortics.mfinanceV2.Typefacer;

import java.util.List;

/**
 * Created by bigfire on 11/23/2015.
 */
public class ActivityMenusAdapter extends ArrayAdapter<NavigationItems> {

    private final Activity context;

    private final List<NavigationItems> itemname;


    public ActivityMenusAdapter(Activity context, List<NavigationItems> itemname) {
        super(context, R.layout.row_menu_items1, itemname);
        // TODO Auto-generated constructor stub

        this.context = context;
        this.itemname = itemname;

    }

    public View getView(int position, View view, ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        Typefacer typefacer = new Typefacer();

        NavigationItems items=itemname.get(position);
        View rowView = inflater.inflate(R.layout.row_menu_items1, null, true);

        TextView txtTitle = (TextView) rowView.findViewById(R.id.tvTitle);
        ImageView icon=(ImageView)rowView.findViewById(R.id.icon);

        TextView  menudiscrip= (TextView) rowView.findViewById(R.id.menudiscrip);
        //txtTitle.setTextColor(0x0000FF);
        //txtTitle.setTypeface(typefacer.getRoboRegular(context.getAssets()));


        txtTitle.setTypeface(typefacer.squareRegular());
        txtTitle.setTextColor(context.getResources().getColor(R.color.primary));
        menudiscrip.setTypeface(typefacer.squareLight());
        // txtTitle.setTextColor(0x0000FF);
        txtTitle.setText(items.getTitle());
        icon.setImageResource(items.getIconid());

        return rowView;

    }

}