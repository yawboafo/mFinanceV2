package com.nfortics.mfinanceV2.FormWidgetGenerator;

import android.content.Context;
import android.view.LayoutInflater;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.nfortics.mfinanceV2.Activities.OnBoardCustomerActivity;
import com.nfortics.mfinanceV2.R;
import com.nfortics.mfinanceV2.Typefacer;

/**
 * Created by bigfire on 12/4/2015.
 */
public class FormPictures  extends FormWidget {


    protected RelativeLayout relativeLayout;
    LayoutInflater inflator;
    protected Button FingerPrintButton,ImagesButton,SignatureButton;
    protected LinearLayout linearLayout;

    public FormPictures(Context context, String name, String tag) {
        super(context, name, tag);

        inflator = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        Typefacer typefacer=new Typefacer();

        relativeLayout=(RelativeLayout)inflator.inflate(R.layout.image_taking_bitmap, null);
        relativeLayout.setTag("picturesLayout");
        FingerPrintButton=(Button)relativeLayout.findViewById(R.id.buttonFinger);
        ImagesButton=(Button)relativeLayout.findViewById(R.id.buttonImage);
        SignatureButton=(Button)relativeLayout.findViewById(R.id.buttonSugnature);


        _layout.addView(relativeLayout);
    }





    @Override
    public void setPicsSignatureClickListener(OnBoardCustomerActivity.PictruesSignatureClickListen handler) {
        super.setPicsSignatureClickListener(handler);
        SignatureButton.setOnClickListener(handler);
    }

    @Override
    public void setPicturesImageClickListener(OnBoardCustomerActivity.PicturesImageClickListener handler) {
        super.setPicturesImageClickListener(handler);
        ImagesButton.setOnClickListener(handler);
    }





    @Override
    public void setFingerPrinntClickListner(OnBoardCustomerActivity.FingerPrintRequestClickListener handler) {
        super.setFingerPrinntClickListner(handler);

        FingerPrintButton.setOnClickListener(handler);
    }
}
