package com.nfortics.mfinanceV2.FormWidgetGenerator;

import android.app.Activity;
import android.content.Context;
import android.support.design.widget.TextInputLayout;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.nfortics.mfinanceV2.Application.Application;
import com.nfortics.mfinanceV2.R;
import com.nfortics.mfinanceV2.Typefacer;

/**
 * Created by bigfire on 12/14/2015.
 */
public class FormEditTextInputLayout extends FormWidget {

    private EditText inputName;
    private TextInputLayout inputLayoutName;
    protected LinearLayout linearLayout;
    LayoutInflater inflator;
    Context context;
    private String hint;
    public FormEditTextInputLayout(Context context, String name, String tag) {
        super(context, name, tag);
        inflator = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        Typefacer typefacer=new Typefacer();
       this.context=context;
        linearLayout=(LinearLayout)inflator.inflate(R.layout.linear_layout_style, null);
        inputLayoutName = (TextInputLayout)inflator.inflate(R.layout.floating_labels_layout, null);

        inputName = (EditText)inputLayoutName.findViewById(R.id.input_name);
        inputName.setTypeface(typefacer.squareLight());
        inputName.requestFocus();

        inputName.setTag(tag);
        inputName.addTextChangedListener(new MyTextWatcher(inputName));
        hint= name;

        linearLayout.addView(inputLayoutName);
       // inputLayoutName.requestFocus();
        inputLayoutName.setError(inputName.getHint());
        _layout.addView(linearLayout);

        InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
       // ((Activity) context).getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
    }

    @Override
    public String getValue(){
        return inputName.getText().toString();
    }

    @Override
    public void setValue( String value ) {
        inputName.setText(value );
    }

    @Override
    public void setHint( String value ){
        inputLayoutName.setHint(value);
    }

    private boolean validateName() {
        if (inputName.getText().toString().trim().isEmpty()) {
           inputLayoutName.setError(inputName.getHint());
           requestFocus(inputName);
            return false;
        } else {
           inputLayoutName.setHint(inputName.getHint());
            inputLayoutName.setErrorEnabled(false);
        }

        return true;
    }
    private void requestFocus(View view) {
        if (view.requestFocus()) {

        }else {

            InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
        }
    }

    private class MyTextWatcher implements TextWatcher {

        private View view;

        private MyTextWatcher(View view) {
            this.view = view;
        }

        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        }

        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        }

        public void afterTextChanged(Editable editable) {

                    validateName();




        }
    }
}
