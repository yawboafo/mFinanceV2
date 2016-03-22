package com.nfortics.mfinanceV2.FormWidgetGenerator;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.Base64;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.nfortics.mfinanceV2.Activities.OnBoardCustomerActivity;
import com.nfortics.mfinanceV2.R;
import com.nfortics.mfinanceV2.Typefacer;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;

/**
 * Created by bigfire on 10/30/2015.
 */
public class FormImageCapture extends FormWidget {



    protected TextView _label;
    protected ImageView _imageview;
    Context context;
    Typefacer typefacer;
    LayoutInflater inflator;
    RelativeLayout relativeLayout;
    public FormImageCapture( Context context,
                             String property,
                             String tag ) {
        super( context, property,tag );
        this.context=context;


        inflator = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);


        typefacer=new Typefacer();



   /**     _label = new TextView( context );
        _label.setText(getDisplayText());

        defaultLayoutParams.setMargins(2, 2, 2, 2);
        _label.setLayoutParams(defaultLayoutParams);
        _layout.setGravity(Gravity.START);
        _layout.setWeightSum(1);


        _label.setTypeface(typefacer.getRoboCondensedBold
                (context.getAssets()));
        _imageview = new ImageView( context );
        _imageview.setTag(tag);***/




        relativeLayout=(RelativeLayout)inflator.inflate(R.layout.capture_image_layout, null);

        _imageview=(ImageView)relativeLayout.findViewById(R.id.anImageview);
        _imageview.setTag(tag);
        TextView tc=(TextView)relativeLayout.findViewById(R.id.PhototextView);
        tc.setTypeface(typefacer.squareLight());








      //  _layout.addView(_label);
        _layout.addView(relativeLayout);
    }

    @Override
    public String getValue(){
        Bitmap bitmap = ((BitmapDrawable)_imageview.getDrawable()).getBitmap();

        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
        byte[] byteArray = byteArrayOutputStream .toByteArray();
        String encoded = Base64.encodeToString(byteArray, Base64.DEFAULT);

        return encoded;
    }
    @Override
    public void setValue( String value ) {

      //  _imageview.requestLayout();
      //  _imageview
       // _imageview.setImageResource(R.drawable.camera);
       // defaultLayoutParams.height=200;
       // defaultLayoutParams.weight=150;

    }

    @Override
    public void setHint( String value ){

    }


    public ByteArrayInputStream getDrawable(Drawable map){

        BitmapDrawable bitmapDrawable = ((BitmapDrawable) map);
        Bitmap bitmap = bitmapDrawable .getBitmap();
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
        byte[] imageInByte = stream.toByteArray();
        ByteArrayInputStream bis = new ByteArrayInputStream(imageInByte);



        return bis;
    }

    @Override
    public void setImageClickListener(OnBoardCustomerActivity.ImageClickListener handler) {
        super.setImageClickListener(handler);
        _imageview.setOnClickListener(handler);


    }

    @Override
    public void setImage(Bitmap map) {
        super.setImage(map);
        _imageview. setImageBitmap(map);
    }
}
