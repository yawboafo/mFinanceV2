package com.nfortics.mfinanceV2.NavigationDrawer;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.nfortics.mfinanceV2.R;
import com.nfortics.mfinanceV2.Typefacer;

/**
 * Created by bigfire on 11/12/2015.
 */
public class DrawerListAdapter extends ArrayAdapter<String> {

    private final Activity context;
    private final String[] itemname;
    private final Integer[] imgid;


    public DrawerListAdapter(Activity context, String[] itemname,Integer[] imgid) {
        super(context, R.layout.navigation_drawer_row, itemname);
        // TODO Auto-generated constructor stub

        this.context=context;
        this.itemname=itemname;

        this.imgid=imgid;
    }


    public View getView(int position,View view,ViewGroup parent) {
        LayoutInflater inflater=context.getLayoutInflater();
        Typefacer typefacer=new Typefacer();
        View rowView=inflater.inflate(R.layout.navigation_drawer_row, null, true);



        TextView txtTitle = (TextView) rowView.findViewById(R.id.listText);
        txtTitle.setTypeface(typefacer.getRoboRegular(context.getAssets()));
        txtTitle.setTextColor(0x0000FF);
       // TextView txtTitleDesc = (TextView) rowView.findViewById(R.id.txtmenudesc);
       // txtTitleDesc.setTypeface(typefacer.getRoboCondensedLight(context.getAssets()));
        ImageView imageView = (ImageView) rowView.findViewById(R.id.listicon);
        // TextView extratxt = (TextView) rowView.findViewById(R.id.textView1);

        txtTitle.setText(itemname[position]);

        imageView.setImageResource(imgid[position]);

        return rowView;

    }


}
