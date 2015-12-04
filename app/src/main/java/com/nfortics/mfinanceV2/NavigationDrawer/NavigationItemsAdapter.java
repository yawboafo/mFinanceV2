package com.nfortics.mfinanceV2.NavigationDrawer;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;


import com.nfortics.mfinanceV2.R;
import com.nfortics.mfinanceV2.Typefacer;

import java.util.Collections;
import java.util.List;

/**
 * Created by bigifre on 7/6/2015.
 */
public class NavigationItemsAdapter  extends RecyclerView.Adapter<NavigationItemsAdapter.NavigationItemViewHolder> {
    private LayoutInflater inflater;
    private Context context;
    List<NavigationItems> data = Collections.emptyList();

    public NavigationItemsAdapter(Context context, List<NavigationItems> data) {
        this.context = context;
        inflater = LayoutInflater.from(context);
        this.data = data;
    }

    @Override
    public NavigationItemViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view= inflater.inflate(R.layout.navigation_drawer_row,viewGroup,false);
        NavigationItemViewHolder holder=new NavigationItemViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(NavigationItemViewHolder holder, int position) {

        //Get CustonFonts Classes

        NavigationItems current=data.get(position);
        holder.title.setText(current.getTitle());
        //holder.title.setTypeface(typefacer.getRoboRegular(context.getAssets()));
        //holder.title.setTypeface(typefacer.getRoboCondensedBold(context.getAssets()));
      holder.icon.setImageResource(current.getIconid());
    }

    @Override
    public int getItemCount() {
        return data.size();
    }


    class NavigationItemViewHolder extends RecyclerView.ViewHolder {

        TextView title;
       ImageView icon;


        public NavigationItemViewHolder(View itemView) {
            super(itemView);


// TxtUsername.setTypeface(typefacer.getRoboCondensedRegular(getActivity().getAssets()));
            Typefacer typefacer = new Typefacer();
            title = (TextView) itemView.findViewById(R.id.listText);
            title.setTypeface(typefacer.squareLight());
           // title.setTextColor(context.getResources().getColor(R.color.primary));
              icon = (ImageView) itemView.findViewById(R.id.listicon);
            //icon.setOnClickListener(this);
            //numberPicker =(NumberPicker)itemView.findViewById(R.id.numberPicker);


        }
    }
}