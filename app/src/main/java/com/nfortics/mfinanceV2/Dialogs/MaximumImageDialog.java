package com.nfortics.mfinanceV2.Dialogs;

import android.app.Activity;

import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.nfortics.mfinanceV2.Application.Application;
import com.nfortics.mfinanceV2.R;
import com.nfortics.mfinanceV2.Typefacer;

import java.io.ByteArrayOutputStream;
import java.util.Map;


public class MaximumImageDialog extends DialogFragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private static final String ARG_PARAM3="param3";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private byte[] mParam3;
    private MaximumDialogInteractionListener mListener;
    ImageView imageView;
     ProgressBar progressBar;
Button retryButton,clearButton,OkButton;
Typefacer typefacer;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment MaximumImageDialog.
     */
    // TODO: Rename and change types and number of parameters
    public static MaximumImageDialog newInstance(String param1, String param2) {
        MaximumImageDialog fragment = new MaximumImageDialog();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);


        fragment.setArguments(args);
        return fragment;
    }

    public static MaximumImageDialog newInstance(String mParam1) {
        MaximumImageDialog fragment = new MaximumImageDialog();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, mParam1);
      //  args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }
    public static MaximumImageDialog newInstance(byte[] mParam3) {
        MaximumImageDialog fragment = new MaximumImageDialog();
        Bundle args = new Bundle();
        args.putByteArray(ARG_PARAM3, mParam3);
        fragment.setArguments(args);
        return fragment;
    }
    public MaximumImageDialog() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        typefacer=new Typefacer();
        if (getArguments() != null) {
            mParam2 = getArguments().getString(ARG_PARAM2);
            mParam1 = getArguments().getString(ARG_PARAM1);

            new ImageLoader(mParam1).execute();
        }



    }



    public class ImageLoader extends AsyncTask<Void,Void,Void>{


        private String key;
        private Bitmap bitmap;

        public ImageLoader(String key){

            this.key=key;



        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... params) {
            bitmap= getKeyByValue(key);


            return null;
        }


        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);

            progressBar.setVisibility(View.GONE);
            imageView.setImageBitmap(bitmap);
        }
    }
    private Bitmap getKeyByValue(String value ) {

        for (Map.Entry<String, Bitmap> entry : Application.getGalleryImages().entrySet()) {


            if(entry.getKey().equals(value)){

                Bitmap bimap=(Bitmap)entry.getValue();
                   return  bimap;
            }
        }


        return null;
    }
    private void InitViews(View view) {

        imageView=(ImageView)view.findViewById(R.id.imageholder);
        imageView.post(new Runnable() {
            @Override
            public void run() {


                if (mParam3 != null) {

                    //TODO  get Image from byteArray here
                   // Bitmap bmp = BitmapFactory.decodeByteArray(mParam3, 0, mParam3.length);

                    imageView.setImageBitmap(null);

                }
            }
        });

        progressBar=(ProgressBar)view.findViewById(R.id.progressBar);
        retryButton=(Button)view.findViewById(R.id.retryButton);
        retryButton.setTypeface(typefacer.squareLight());
        retryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.maximumInteraction(mParam2);
            }
        });
        clearButton=(Button)view.findViewById(R.id.clearButton);
        clearButton.setTypeface(typefacer.squareLight());
        clearButton.setText("Clear");
        clearButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                try {
                    Application.getGalleryImages().remove(mParam1);
                }
                catch (Exception e)
                {}

                try {
                    Application. basse64Images.remove(mParam1);
                }catch (Exception e){}




                try {
                    Application.listOfKeysChecked.remove(mParam1);
                }catch (Exception e)
                {}





                dismiss();
            }
        });

        OkButton=(Button)view.findViewById(R.id.OkButton);
        OkButton.setTypeface(typefacer.squareLight());
        OkButton.setText("Ok");
        OkButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
    }

    @Override
    public void onDismiss(DialogInterface dialog) {
        super.onDismiss(dialog);
        mListener.maximumInteraction(null);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        setCancelable(true);
        return inflater.inflate(R.layout.fragment_maximum_image_dialog, container, false);
    }


    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        InitViews(view);
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(String uri) {
        if (mListener != null) {
            mListener.maximumInteraction(uri);
        }
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (MaximumDialogInteractionListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface MaximumDialogInteractionListener {
        // TODO: Update argument type and name
        public void maximumInteraction(String uri);
    }

}
