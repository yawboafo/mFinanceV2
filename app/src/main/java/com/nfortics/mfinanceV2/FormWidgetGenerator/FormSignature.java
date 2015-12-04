package com.nfortics.mfinanceV2.FormWidgetGenerator;

import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.nfortics.mfinanceV2.Activities.OnBoardCustomerActivity;
import com.nfortics.mfinanceV2.R;
import com.nfortics.mfinanceV2.Typefacer;

/**
 * Created by bigfire on 11/9/2015.
 */
public class FormSignature extends FormWidget
{
    protected TextView _label;
    protected ImageView _imageview;
    Context context;
    Typefacer typefacer;
    protected RelativeLayout linearLayout;
    LayoutInflater inflator;



    public FormSignature( Context context, String property ,String tag)
    {

        super( context, property,tag );
        this.context=context;
        typefacer=new Typefacer();

        inflator = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);


        linearLayout=(RelativeLayout)inflator.inflate(R.layout.signature_layout_style, null);
        TextView tv=(TextView)linearLayout.findViewById(R.id.signatureTv);
        tv.setTypeface(typefacer.squareLight());
        tv.setText("No Signature");


        //_label = new TextView( context );
        //tv.setText(getDisplayText());





        _imageview = (ImageView)linearLayout.findViewById(R.id.imageView4);
        _imageview.setTag(tag);


        _layout.addView(linearLayout);
    }
    @Override
    public void setValue( String value ) {

       // _imageview.requestLayout();
        //  _imageview
        //_imageview.setImageResource(R.drawable.signature);

    }
    @Override
    public void setSignatureButtonClick(OnBoardCustomerActivity.SignatureClickListen handler) {
        super.setSignatureButtonClick(handler);
        _imageview.setOnClickListener(handler);

    }
}
