package com.nfortics.mfinanceV2.ViewAdapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.nfortics.mfinanceV2.Models.Customer;
import com.nfortics.mfinanceV2.R;
import com.nfortics.mfinanceV2.Typefacer;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by bigfire on 12/4/2015.
 */
public class GalleryImagesAdapter extends RecyclerView.Adapter<GalleryImagesViewHolder> {

    private Context context;
    private LayoutInflater inflater;
    public List<Map<String,Bitmap>> data= Collections.emptyList();
  Typefacer typefacer;



    public GalleryImagesAdapter(Context context, List<Map<String,Bitmap>> data) {
        inflater= LayoutInflater.from(context);
        this.data=data;
        this.context=context;
    }


    @Override
    public GalleryImagesViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view= inflater.inflate(R.layout.grid_item_layout,parent,false);

        GalleryImagesViewHolder holder = new GalleryImagesViewHolder(view);

        return holder;
    }

    @Override
    public void onBindViewHolder(GalleryImagesViewHolder holder, int position) {
        typefacer=new Typefacer();
         Map<String,Bitmap>  da =data.get(position);

         for(Map.Entry<String, Bitmap> entry : da.entrySet()){
             holder.textView.setTypeface(typefacer.squareLight());
             holder.textView.setText(entry.getKey());
             holder.imageView.setImageBitmap(entry.getValue());
         }

    }

    @Override
    public int getItemCount() {
        return data.size();
    }
}


class GalleryImagesViewHolder extends RecyclerView.ViewHolder {
    ImageView imageView;
    TextView textView;

    public GalleryImagesViewHolder(View itemView) {
        super(itemView);
        textView = (TextView) itemView.findViewById(R.id.text);
        imageView = (ImageView) itemView.findViewById(R.id.image);
    }
}