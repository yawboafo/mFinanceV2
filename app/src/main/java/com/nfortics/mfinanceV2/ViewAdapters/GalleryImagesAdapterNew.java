package com.nfortics.mfinanceV2.ViewAdapters;

import android.content.Context;
import android.graphics.Bitmap;
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
import java.util.Map;

/**
 * Created by bigfire on 12/9/2015.
 */
public class GalleryImagesAdapterNew extends RecyclerView.Adapter<GalleryImagesViewHolderNew> {

    private Context context;
    private LayoutInflater inflater;
     Map<String,Bitmap>data= Collections.emptyMap();
    Typefacer typefacer;
    private String[] mKeys;

    public GalleryImagesAdapterNew(Context context, Map<String,Bitmap> data) {
        inflater= LayoutInflater.from(context);
        this.data=data;
        mKeys = data.keySet().toArray(new String[data.size()]);
        this.context=context;
    }


    @Override
    public GalleryImagesViewHolderNew onCreateViewHolder(ViewGroup parent, int viewType) {
        View view= inflater.inflate(R.layout.grid_item_layout,parent,false);

        GalleryImagesViewHolderNew holder = new GalleryImagesViewHolderNew(view);

        return holder;
    }

    @Override
    public void onBindViewHolder(GalleryImagesViewHolderNew holder, int position) {
        typefacer=new Typefacer();
        Bitmap mp =data.get(mKeys[position]);
        String key=mKeys[position];
        holder.textView.setTypeface(typefacer.squareLight());
        holder.textView.setText(key);

       // holder.imageView.setImageBitmap(entry.getValue());
        holder.imageView.setImageBitmap(mp);


    }

    @Override
    public int getItemCount() {
        return data.size();
    }
}
class GalleryImagesViewHolderNew extends RecyclerView.ViewHolder {
    ImageView imageView;
    TextView textView;

    public GalleryImagesViewHolderNew(View itemView) {
        super(itemView);
        textView = (TextView) itemView.findViewById(R.id.text);
        imageView = (ImageView) itemView.findViewById(R.id.image);
    }
}