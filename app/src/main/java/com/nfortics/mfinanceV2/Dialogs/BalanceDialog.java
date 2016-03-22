package com.nfortics.mfinanceV2.Dialogs;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.grabba.Grabba;
import com.grabba.GrabbaConnectionListener;
import com.grabba.GrabbaMagstripeListener;
import com.grabba.GrabbaSmartcardListener;
import com.nfortics.mfinanceV2.R;
import com.nfortics.mfinanceV2.Typefacer;
import com.nfortics.mfinanceV2.Utilities.Utils;


public class BalanceDialog extends DialogFragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";


    TextView checkby,customerDetailsTitle;

    Button btnSummary,smsbutton;

    EditText balanceAccountValue;

    Spinner checkbySpinner;

    private String mParam1;
    private String mParam2;

    private BalanceDialogInteractionListener mListener;


    public static BalanceDialog newInstance(String param1, String param2) {
        BalanceDialog fragment = new BalanceDialog();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public BalanceDialog() {
        // Required empty public constructor
    }


    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        SetViews(view);
    }

    void SetViews(View view){


        Typefacer typefacer=new Typefacer();


        checkby=(TextView)view.findViewById(R.id.checkby);
        checkby.setTypeface(typefacer.getRoboCondensedLight(getActivity().getAssets()));

        customerDetailsTitle=(TextView)view.findViewById(R.id.checkby);
        customerDetailsTitle.setTypeface(typefacer.getRoboCondensedRegular(getActivity().getAssets()));


        btnSummary=(Button)view.findViewById(R.id.printButton);

        smsbutton=(Button)view.findViewById(R.id.smsbutton);



        balanceAccountValue=(EditText)view.findViewById(R.id.balanceAccountValue);


        checkbySpinner=(Spinner)view.findViewById(R.id.checkbySpinner);

        Utils.populateSpinner(getActivity(), checkbySpinner, getResources().getStringArray(R.array.collection_categories));

    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        setCancelable(true);
        return inflater.inflate(R.layout.fragment_balance_dialog, container, false);
    }


    public void onButtonPressed(String uri) {
        if (mListener != null) {
            mListener.BalanceDialogInteraction(uri);
        }
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (BalanceDialogInteractionListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnFragmentInteractionListener");
        }

        try {




        }catch (Exception e){


        }
    }


    @Override
    public void onResume() {
        super.onResume();

        if (Utils.deviceIsGrabba()) {
            Grabba.getInstance().addConnectionListener(grabbaConnectionListner);
            Grabba.getInstance().acquireGrabba();
        }

    }
    private void toastOnUiThread(final String msg) {

    }


    private final GrabbaMagstripeListener grabbaMagstripeListener = new GrabbaMagstripeListener() {

        @Override
        public void magstripeReadEvent(byte[] track1, byte[] track2, byte[] track3) {
            //
            // byte[][] tracks = new byte[][] { track1, track2, track3 };
            // for (int i = 0; i < 3; i++) {
            if (track2 == null) {
                toastOnUiThread("Swipe again");
            } else if (track2.length == 0) {
                toastOnUiThread("Swipe again");
            } else {
                toastOnUiThread("Card Read Succesfully...");
              //updateViewGrabba(new String(track2));

            }

        }

        @Override
        public void magstripeRawReadEvent(byte[] track1raw, byte[] track2raw, byte[] track3raw) {
            // TODO Auto-generated method stub

        }
    };
    private GrabbaSmartcardListener grabbaSmartcardListener = new GrabbaSmartcardListener() {

        @Override
        public void smartcardRemovedEvent() {
            // TODO Auto-generated method stub

        }

        @Override
        public void smartcardInsertedEvent() {
            // TODO Auto-generated method stub

        }
    };

    private final GrabbaConnectionListener grabbaConnectionListner = new GrabbaConnectionListener() {

        @Override
        public void grabbaDisconnectedEvent() {
            toastOnUiThread("Device disconnected");

        }

        @Override
        public void grabbaConnectedEvent() {

            toastOnUiThread("Device Connected");
        }

    };

    @Override
    public void onPause() {
        super.onPause();

        if (Utils.deviceIsGrabba()) {
            Grabba.getInstance().addConnectionListener(grabbaConnectionListner);
            Grabba.getInstance().acquireGrabba();
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface BalanceDialogInteractionListener {

        public void BalanceDialogInteraction(String uri);
    }

}
