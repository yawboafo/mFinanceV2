package com.nfortics.mfinanceV2.Dialogs;

import android.app.Activity;
import android.app.ActivityOptions;
import android.app.Dialog;
import android.content.Intent;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import com.nfortics.mfinanceV2.Activities.BalancenquiryActivity;
import com.nfortics.mfinanceV2.Activities.FieldCollectionActivity;
import com.nfortics.mfinanceV2.Activities.MenuActivities.FieldCollectionMenuActivity;
import com.nfortics.mfinanceV2.Activities.OnBoardCustomerActivity;
import com.nfortics.mfinanceV2.Models.Customer;
import com.nfortics.mfinanceV2.R;
import com.nfortics.mfinanceV2.Typefacer;
import com.nfortics.mfinanceV2.Utilities.Utils;
import com.nfortics.mfinanceV2.ViewAdapters.CustomerAccountsListAdapter;
import com.nfortics.mfinanceV2.ViewAdapters.MenuListAdapters;

import de.hdodenhof.circleimageview.CircleImageView;


public class CustomerDetailDialog extends DialogFragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;


    Customer customer;

    private CustomerDetailDialogInteractionListener mListener;



   // Button buttonUpdatePin

    CircleImageView profileImage;
    TextView customerName,customerMsisdn,customerID,customerDetailsTitle;

    ListView listMenu;

    Typefacer typefacer=new Typefacer();



    @Override
    public void onStart()
    {
        super.onStart();
        Dialog dialog = getDialog();
        if (dialog != null)
        {
           // int width = ViewGroup.LayoutParams.MATCH_PARENT;
           // int height = ViewGroup.LayoutParams.WRAP_CONTENT;
           // dialog.getWindow().setLayout(width, height);
        }
    }

    public static CustomerDetailDialog newInstance(String param1) {
        CustomerDetailDialog fragment = new CustomerDetailDialog();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);

        fragment.setArguments(args);
        return fragment;
    }

    public CustomerDetailDialog() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            customer=Customer.getCustomer(mParam1);
        }
    }

    @Override
    public void show(FragmentManager manager, String tag) {
        if(manager.findFragmentByTag(tag)==null){
            super.show(manager, tag);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        setCancelable(true);
        return inflater.inflate(R.layout.fragment_customer_detail_dialog, container, false);
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.CustomerDetailDialogInteraction();
        }
    }


    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        customerDetailsTitle=(TextView)view.findViewById(R.id.customerDetailsTitle);
        customerDetailsTitle.setTypeface(typefacer.getRoboCondensedRegular(getActivity().getAssets()));
        profileImage=(CircleImageView)view.findViewById(R.id.profile_image);

        customerName=(TextView)view.findViewById(R.id.customerName);
        customerName.setTypeface(typefacer.squareBold());

        customerMsisdn=(TextView)view.findViewById(R.id.customerMsisdn);
        customerMsisdn.setTypeface(typefacer.squareLight());


        customerID=(TextView)view.findViewById(R.id.customerAccount);
        customerID.setTypeface(typefacer.squareLight());

        createlistMenu(view);

        setValues();

        //customerName,customerMsisdn,customerAccount;
    }

    private ListView createlistMenu(View view){

        listMenu =(ListView)view.findViewById(R.id.customerAccountListView);
        CustomerAccountsListAdapter adapter=new CustomerAccountsListAdapter(getActivity(), Customer.getAccounts(mParam1));
        listMenu.setAdapter(adapter);
        listMenu.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                final TextView accountNumber = (TextView) view.findViewById(R.id.accountNumber);
                PopupMenu popupMenu = new PopupMenu(getActivity(), view);
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.item_Deposit:
                                dismiss();
                                Intent intent = new Intent(getActivity(), FieldCollectionActivity.class);
                                intent.putExtra("account_number", accountNumber.getText().toString());
                                startActivity(intent);
                                getActivity().overridePendingTransition(R.anim.activity_animation, R.anim.activity_animation2);


                                return true;
                            case R.id.item_Withdrawal:

                                return true;
                            case R.id.item_Balance:
                                dismiss();
                                Intent intents=null;
                                intents = new Intent(getActivity(), BalancenquiryActivity.class);
                                intents.putExtra("account_number", accountNumber.getText().toString());
                                startActivity(intents);
                                getActivity().overridePendingTransition( R.anim.activity_animation, R.anim.activity_animation2);
                                return true;

                            case R.id.item_Statement:

                                return true;

                        }
                        return true;
                    }

                });
                popupMenu.inflate(R.menu.customer_quick_actions);
                popupMenu.show();
            }
        });



        return listMenu;
    }

    public void showDialog(DialogFragment dialogFragment,String tag){

        FragmentManager manager = getActivity().getSupportFragmentManager();
        // CashPaymentDialog dialog= new CashPaymentDialog();
        DialogFragment dialog = dialogFragment;
        dialog.show(manager, tag);

    }
    void setValues(){

        if(customer!=null){
            customerName.setText(customer.getFirst_name());

            if(Customer.getMsisdns(customer.getCustomer_id()).size()>0){
                customerMsisdn.setText(Customer.getMsisdns(customer.getCustomer_id()).get(0).getNumber());
            }

            Utils.setImage(customer.getPhotoUrl(), profileImage);

            customerID.setText(customer.getCustomer_id());



        }
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (CustomerDetailDialogInteractionListener) activity;
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
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface CustomerDetailDialogInteractionListener {
        // TODO: Update argument type and name
        public void CustomerDetailDialogInteraction();
    }

}
