package com.nfortics.mfinanceV2.FormWidgetGenerator;

import android.app.Activity;
import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.nfortics.mfinanceV2.R;
import com.nfortics.mfinanceV2.Typefacer;
import com.rengwuxian.materialedittext.MaterialEditText;

/**
 * Created by bigfire on 10/29/2015.
 */
public class FormEditText extends FormWidget
{
    protected TextView _label;
    protected MaterialEditText _input;
    protected LinearLayout linearLayout;
    LayoutInflater inflator;
    Context context;
    public FormEditText( Context context, String property ,String tag)
    {
        super( context, property,tag );

         inflator = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            Typefacer typefacer=new Typefacer();

        linearLayout=(LinearLayout)inflator.inflate(R.layout.linear_layout_style, null);
        _input = (MaterialEditText)inflator.inflate(R.layout.edit_text_style, null);
        _label = (TextView)inflator.inflate(R.layout.textview_style_layout, null);

        _input.setHint("An editText");
        _input.setTag(tag);
       // _input.addTextChangedListener(new floatingButton(_input));
       // _layout.(_label);
      //  _layout.addView( _input );
       // linearLayout.addView(_label);
        linearLayout.addView(_input);

        _layout.addView( linearLayout );

       // _layout.setLayoutParams(defaultLayoutParams);
    }

    @Override
    public String getValue(){
        return _input.getText().toString();
    }

    @Override
    public void setValue( String value ) {
        _input.setText(value );
    }

    @Override
    public void setHint( String value ){
        _input.setHint(value.toLowerCase());
    }


    private boolean validateName() {
        if (_input.getText().toString().trim().isEmpty()) {
            _label.setText("validate information");
          requestFocus(_input);
            return false;
        } else {
            _label.setText("");
        }

        return true;
    }
    private void requestFocus(View view) {
        if (view.requestFocus()) {
            ((Activity) context).getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        }
    }
    class floatingButton implements TextWatcher{


        private View view;

        private floatingButton(View view) {
            this.view = view;
        }
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {

            _input.post(new Runnable() {
                @Override
                public void run() {
//                    validateName();
                }
            });

        }
    }
}

