package com.nfortics.mfinanceV2.Fragments.RootSettingsPages;

import android.app.Activity;
import android.content.DialogInterface;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.nfortics.mfinanceV2.Application.Application;
import com.nfortics.mfinanceV2.Models.AppInstanceSettings;
import com.nfortics.mfinanceV2.R;
import com.nfortics.mfinanceV2.Typefacer;
import com.nfortics.mfinanceV2.Utilities.ToastUtil;
import com.nfortics.mfinanceV2.Utilities.Utils;

import java.io.IOException;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * to handle interaction events.
 * Use the {@link PageOne#newInstance} factory method to
 * create an instance of this fragment.
 */


public class PageOne extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    RelativeLayout serverLayout,devicelayout;
    TextView serverName,deviceType;

    Typefacer typefacer;
    private String mParam1;
    private String mParam2;

    private PageOneInteractionListener mListener;
    AppInstanceSettings appInstanceSettings ;

    // TODO: Rename and change types and number of parameters
    public static PageOne newInstance(String param1, String param2) {
        PageOne fragment = new PageOne();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public PageOne() {
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

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_page_one, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setViews(view);
    }

    void setViews(View view){

        devicelayout=(RelativeLayout)view.findViewById(R.id.devicelayout);
        devicelayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertSingleChoiceDevice();
            }
        });
        deviceType=(TextView)view.findViewById(R.id.deviceType);
        deviceType.setTypeface(typefacer.squareLight());
        deviceType.setText(Application.deviceType);




        serverLayout=(RelativeLayout)view.findViewById(R.id.severlayout);
        serverLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertSingleChoiceItems();
            }
        });

        serverName=(TextView)view.findViewById(R.id.serverName);
        serverName.setTypeface(typefacer.squareLight());
        serverName.setText(Application.ServerMode);



    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.PageOneInteraction(uri);
        }
    }



    public void alertSingleChoiceDevice(){
        final String[] mTestArray;

        mTestArray = getResources().getStringArray(R.array.deviceChoices);
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

                        // Set the dialog title
                        builder.setTitle("Select Device")

                                // specify the list array, the items to be selected by default (null for none),
                                // and the listener through which to receive call backs when items are selected
                                // again, R.array.choices were set in the resources res/values/strings.xml
                                .setSingleChoiceItems(mTestArray, -1, new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface arg0, int arg1) {
                                        // ToastUtil.snackbar(coordinatorLayout, "" + arg1);
                                        Utils.log("" + mTestArray[arg1]);
                    }

                })

                        // Set the action buttons
               .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                           @Override
                           public void onClick(DialogInterface dialog, int id) {
                               // user clicked OK, so save the mSelectedItems results somewhere
                               // or return them to the component that opened the dialog
                               try {

                                   int selectedPosition = ((AlertDialog) dialog).getListView().getCheckedItemPosition();
                                   Utils.log("" + mTestArray[selectedPosition]);


                                   try {
                                       appInstanceSettings = Utils.ApplicationSettings();
                                   } catch (ClassNotFoundException e) {
                                       e.printStackTrace();
                                   } catch (IOException e) {
                                       e.printStackTrace();
                                   }
                                   appInstanceSettings.setDeviceType(mTestArray[selectedPosition]);

                                   try {

                                       Utils.insertAppInstanceSettings(appInstanceSettings);

                                   } catch (Exception e) {
                                       e.printStackTrace();

                                   } finally {
                                       Application.deviceType = mTestArray[selectedPosition];
                                       deviceType.setText(Application.deviceType);
                                   }

                               } catch (Exception e) {


                               }
                               // ToastUtil.snackbar(coordinatorLayout, "" + selectedPosition);

                           }
                       }

               )

                           .

                                   setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                               @Override
                                               public void onClick(DialogInterface dialog, int id) {
                                                   // removes the dialog from the screen

                                               }
                                           }

                                   )

                           .

                                   show();

               }
    public void alertSingleChoiceItems(){
        final String[] mTestArray;
        mTestArray = getResources().getStringArray(R.array.ServerChoices);
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        // Set the dialog title
        builder.setTitle("Select Server")

                // specify the list array, the items to be selected by default (null for none),
                // and the listener through which to receive call backs when items are selected
                // again, R.array.choices were set in the resources res/values/strings.xml
                .setSingleChoiceItems(mTestArray, -1, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {
                        // ToastUtil.snackbar(coordinatorLayout, "" + arg1);
                        Utils.log("" + mTestArray[arg1]);
                    }

                })

                        // Set the action buttons
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int id) {
                                // user clicked OK, so save the mSelectedItems results somewhere
                                // or return them to the component that opened the dialog

                                int selectedPosition = ((AlertDialog) dialog).getListView().getCheckedItemPosition();
                                Utils.log("" + mTestArray[selectedPosition]);

                                AppInstanceSettings appInstanceSettings = new AppInstanceSettings();
                                appInstanceSettings.setServerMode(mTestArray[selectedPosition]);

                                try {

                                    Utils.insertAppInstanceSettings(appInstanceSettings);

                                } catch (Exception e) {
                                    e.printStackTrace();

                                } finally {
                                    Application.ServerMode= mTestArray[selectedPosition];
                                    serverName.setText(   Application.ServerMode);
                                }


                                // ToastUtil.snackbar(coordinatorLayout, "" + selectedPosition);

                            }
                        }

                )

                .

                        setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int id) {
                                        // removes the dialog from the screen

                                    }
                                }

                        )

                .

                        show();

    }

        @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (PageOneInteractionListener) activity;
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
    public interface PageOneInteractionListener {
        // TODO: Update argument type and name
        public void PageOneInteraction(Uri uri);
    }

}
