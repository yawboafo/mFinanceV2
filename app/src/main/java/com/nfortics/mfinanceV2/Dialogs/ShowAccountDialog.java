package com.nfortics.mfinanceV2.Dialogs;

import android.app.Activity;
import android.app.Dialog;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;

import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.PopupMenu;
import android.widget.TextView;

import com.nfortics.mfinanceV2.Models.Customer;
import com.nfortics.mfinanceV2.R;
import com.nfortics.mfinanceV2.Typefacer;
import com.nfortics.mfinanceV2.Utilities.Utils;
import com.nfortics.mfinanceV2.ViewAdapters.CustomerAccountsListAdapter;

import de.hdodenhof.circleimageview.CircleImageView;


public class ShowAccountDialog extends DialogFragment {

    private static final String ARG_PARAM1 = "param1";


    // TODO: Rename and change types of parameters
    private String mParam1;


    private ShowAccountDialogInteractionListener mListener;
    CircleImageView profileImage;
    TextView customerName,customerMsisdn,customerID,customerDetailsTitle;


    Customer customer;
    ListView listMenu;

    Typefacer typefacer=new Typefacer();


    public static ShowAccountDialog newInstance(String param1) {
        ShowAccountDialog fragment = new ShowAccountDialog();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);

        fragment.setArguments(args);
        return fragment;
    }

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
    public ShowAccountDialog() {
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
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        setCancelable(true);
        return inflater.inflate(R.layout.fragment_show_account_dialog, container, false);
    }




    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        customerDetailsTitle=(TextView)view.findViewById(R.id.customerDetailsTitle);
        customerDetailsTitle.setTypeface(typefacer.getRoboCondensedRegular(getActivity().getAssets()));
        profileImage=(CircleImageView)view.findViewById(R.id.profile_image);

        customerName=(TextView)view.findViewById(R.id.customerName);
        customerName.setTypeface(typefacer.getRoboRegular(getActivity().getAssets()));






        createlistMenu(view);

        setValues();
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (ShowAccountDialogInteractionListener) activity;
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
    private ListView createlistMenu(View view){

        listMenu =(ListView)view.findViewById(R.id.customerAccountListView);
        CustomerAccountsListAdapter adapter=new CustomerAccountsListAdapter(getActivity(), Customer.getAccounts(mParam1));
        listMenu.setAdapter(adapter);
        listMenu.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                TextView textView=(TextView)view.findViewById(R.id.accountNumber);
                String accountNumber=textView.getText().toString();
                mListener.ShowAccountDialogInteraction(accountNumber);
                dismiss();
            }
        });



        return listMenu;
    }
    void setValues(){

        if(customer!=null){
            customerName.setText(customer.getFirst_name());



            Utils.setImage(customer.getPhotoUrl(), profileImage);





        }
    }

    public interface ShowAccountDialogInteractionListener {
        // TODO: Update argument type and name
        public void ShowAccountDialogInteraction(String value);
    }

}
