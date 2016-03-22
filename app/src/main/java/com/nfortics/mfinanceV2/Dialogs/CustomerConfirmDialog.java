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
import android.widget.ListView;
import android.widget.TextView;

import com.nfortics.mfinanceV2.Models.Customer;
import com.nfortics.mfinanceV2.R;
import com.nfortics.mfinanceV2.Typefacer;
import com.nfortics.mfinanceV2.Utilities.Utils;

import de.hdodenhof.circleimageview.CircleImageView;


public class CustomerConfirmDialog extends DialogFragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private CustomerConfirmDialogInteractionListener mListener;

    CircleImageView profileImage;
    TextView customerName,customerMsisdn,customerID,customerDetailsTitle;

    Button submitButton,cancelButton;

    ListView listMenu;

    Typefacer typefacer=new Typefacer();
    Customer customer;
    public static CustomerConfirmDialog newInstance(String param1, String param2) {
        CustomerConfirmDialog fragment = new CustomerConfirmDialog();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public CustomerConfirmDialog() {
        // Required empty public constructor
    }

    @Override
    public void show(FragmentManager manager, String tag) {
        if(manager.findFragmentByTag(tag)==null){
            super.show(manager, tag);
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2= getArguments().getString(ARG_PARAM2);
            try{

                if(mParam1.equalsIgnoreCase("incomplete")){

                    customer=new Customer();
                }else{

                    customer=Customer.getCustomer(mParam1);
                }



            }catch (Exception e) {

                e.printStackTrace();
            }


        }
    }
    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        customerDetailsTitle=(TextView)view.findViewById(R.id.customerDetailsTitle);
        customerDetailsTitle.setTypeface(typefacer.getRoboCondensedRegular(getActivity().getAssets()));
        profileImage=(CircleImageView)view.findViewById(R.id.profile_image);

        customerName=(TextView)view.findViewById(R.id.customerName);
        customerName.setTypeface(typefacer.getRoboCondensedLight(getActivity().getAssets()));


        submitButton=(Button)view.findViewById(R.id.butSubmit);
        cancelButton=(Button)view.findViewById(R.id.butCancel);



        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                dismiss();
                mListener.CustomerConfirmDialogInteraction("submit");
            }
        });

        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                dismiss();
                //mListener.CustomerConfirmDialogInteraction("cancel");
            }
        });

        setValues();
        //customerName,customerMsisdn,customerAccount;
    }

          void setValues(){


              try{

                  if(mParam1.equalsIgnoreCase("incomplete")){

                   customerName.setText("Unknown Customer "+"\n"+mParam2);


                      // Utils.setImage(customer.getPhotoUrl(), profileImage);

                  }else {

                      if(customer != null) {
                          customerName.setText(customer.getFirst_name()+"\n"+mParam2);


                          Utils.setImage(customer.getPhotoUrl(), profileImage);





                      }

                  }
              }catch (Exception e){
                  e.printStackTrace();

              }




    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        setCancelable(true);
        return inflater.inflate(R.layout.fragment_customer_confirm_dialog, container, false);
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(String uri) {
        if (mListener != null) {
            mListener.CustomerConfirmDialogInteraction(uri);
        }
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (CustomerConfirmDialogInteractionListener) activity;
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
    public interface CustomerConfirmDialogInteractionListener {
        // TODO: Update argument type and name
        public void CustomerConfirmDialogInteraction(String message);
    }

}
