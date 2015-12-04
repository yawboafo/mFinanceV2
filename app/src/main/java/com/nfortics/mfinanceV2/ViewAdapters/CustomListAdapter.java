package com.nfortics.mfinanceV2.ViewAdapters;

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
 * Created by bigfire on 10/29/2015.
 */
public class CustomListAdapter extends ArrayAdapter<String> {

    private final Activity context;
    private final String[] descrip;
    private final String[] itemname;
    private final Integer[] imgid;

    public CustomListAdapter(Activity context, String[] itemname, String[] descrip,Integer[] imgid) {
        super(context, R.layout.row_item_menu, itemname);
        // TODO Auto-generated constructor stub

        this.context=context;
        this.itemname=itemname;
        this.descrip=descrip;
        this.imgid=imgid;
    }

    public View getView(int position,View view,ViewGroup parent) {
        LayoutInflater inflater=context.getLayoutInflater();
        Typefacer typefacer=new Typefacer();
        View rowView=inflater.inflate(R.layout.row_item_menu, null, true);

        TextView txtTitle = (TextView) rowView.findViewById(R.id.tvTitle);
        txtTitle.setTypeface(typefacer.getRoboRegular(context.getAssets()));
        TextView txtTitleDesc = (TextView) rowView.findViewById(R.id.txtmenudesc);
        txtTitleDesc.setTypeface(typefacer.getRoboCondensedLight(context.getAssets()));
        ImageView imageView = (ImageView) rowView.findViewById(R.id.menuIcon);
        // TextView extratxt = (TextView) rowView.findViewById(R.id.textView1);

        txtTitle.setText(itemname[position]);
        txtTitleDesc.setText(descrip[position]);
        imageView.setImageResource(imgid[position]);
        //extratxt.setText("Description "+itemname[position]);
        return rowView;

    }
}
