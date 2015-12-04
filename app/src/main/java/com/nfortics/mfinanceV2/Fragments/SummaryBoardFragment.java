package com.nfortics.mfinanceV2.Fragments;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.nfortics.mfinanceV2.Models.Collection;
import com.nfortics.mfinanceV2.R;
import com.nfortics.mfinanceV2.Typefacer;


public class SummaryBoardFragment extends Fragment {


    private TextView  txtTitle,txtDiscription,txtCashLabel,txtCashAmount;
    private Button buttViewActivities,butPrintActivity;

    Typefacer typefacer;



    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;


    // TODO: Rename and change types and number of parameters
    public static SummaryBoardFragment newInstance(String param1, String param2) {
        SummaryBoardFragment fragment = new SummaryBoardFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public SummaryBoardFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
        typefacer=new Typefacer();
    }


    void SetViewsBasedOn(String value){

        switch (value){

            case "Deposits" :


               // Collection.

                break;
        }

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_summary_board, container, false);
    }


    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setLabels( view);
    }



    private void setLabels(View view){

        txtTitle=(TextView)view.findViewById(R.id.txtTitle);
        txtTitle.setTypeface(typefacer.squareRegular());
        txtDiscription=(TextView)view.findViewById(R.id.txtSummarydetails);
        txtDiscription.setTypeface(typefacer.squareLight());

        txtCashLabel=(TextView)view.findViewById(R.id.txtCashLabel);
        txtCashLabel.setTypeface(typefacer.squareRegular());
        txtCashAmount=(TextView)view.findViewById(R.id.txtCashAmount);
        txtCashAmount.setTypeface(typefacer.squareLight());
        txtTitle.setText(mParam1);


     /**  buttViewActivities=(Button)view.findViewById(R.id.buttViewActivities);
        buttViewActivities.setTypeface(typefacer.squareLight());
        buttViewActivities.setText("View Activities");


        butPrintActivity=(Button)view.findViewById(R.id.butPrintActivity);
        butPrintActivity.setTypeface(typefacer.squareLight());
        butPrintActivity.setText("View Commissions");**/
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (OnFragmentInteractionListener) activity;
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


    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        public void onFragmentInteraction(Uri uri);
    }

}
