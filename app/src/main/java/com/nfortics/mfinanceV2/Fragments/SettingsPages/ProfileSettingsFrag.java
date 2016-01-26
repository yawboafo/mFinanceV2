package com.nfortics.mfinanceV2.Fragments.SettingsPages;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.nfortics.mfinanceV2.Application.Application;
import com.nfortics.mfinanceV2.DataBase.DaoAccess;
import com.nfortics.mfinanceV2.Models.Agent;
import com.nfortics.mfinanceV2.Models.Branch;
import com.nfortics.mfinanceV2.Models.Merchant;
import com.nfortics.mfinanceV2.R;
import com.nfortics.mfinanceV2.Typefacer;
import com.nfortics.mfinanceV2.Utilities.Utils;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * to handle interaction events.
 * Use the {@link ProfileSettingsFrag#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ProfileSettingsFrag extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private ProfileSettingsFragInteractionListener mListener;

    TextView agentName,
            agentMsisdn,
            branchesTitle,
            branchesValue,
            merchantTitle,
            merchantValue,
            transactionTitle,
            transactionValue;

    Button  updateprofileButton,
            resetPinButton,
            checkUpdateButton;


    CircleImageView profile_image;


    Merchant merchant;
    private static final int CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE = 1888;
    Typefacer typefacer;

    public static ProfileSettingsFrag newInstance(String param1, String param2) {
        ProfileSettingsFrag fragment = new ProfileSettingsFrag();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public ProfileSettingsFrag() {
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
        try{
            merchant= Merchant.getActiveMerchant("true");
            Log.d("oxinbo", "active merchant details " + merchant.getCode() + merchant.getName() + merchant.getAmount_limit());

        }catch (Exception e){

            e.printStackTrace();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_profile_settings, container, false);
    }


    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        SetViews(view);
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.ProfileSettingsFragInteraction(uri);
        }
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (ProfileSettingsFragInteractionListener) activity;
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




    void SetViews(View view){

                agentName=(TextView)view.findViewById(R.id.agentName);
                agentName.setTypeface(typefacer.squareLight());

                agentMsisdn=(TextView)view.findViewById(R.id.agentMsisdn);
                agentMsisdn.setTypeface(typefacer.squareLight());

                branchesTitle=(TextView)view.findViewById(R.id.branchesTitle);
                 branchesTitle.setOnClickListener(new View.OnClickListener() {
                @Override
                 public void onClick(View v) {
                showBrachesDialog();
                 }
                  });
                branchesTitle.setTypeface(typefacer.squareLight());

                branchesValue=(TextView)view.findViewById(R.id.branchesValue);
                branchesValue.setTypeface(typefacer.squareLight());
                branchesValue.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        showBrachesDialog();
                    }
                });

                merchantTitle=(TextView)view.findViewById(R.id.merchantTitle);
                merchantTitle.setTypeface(typefacer.squareLight());
                merchantTitle.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        showMerchantsDialog();
                    }
                });

                merchantValue=(TextView)view.findViewById(R.id.merchantValue);
                merchantValue.setTypeface(typefacer.squareLight());
                 merchantValue.setOnClickListener(new View.OnClickListener() {
                     @Override
                     public void onClick(View v) {

                         showMerchantsDialog();
                     }
                 });


                transactionTitle=(TextView)view.findViewById(R.id.transactionTitle);
                transactionTitle.setTypeface(typefacer.squareLight());

                transactionValue=(TextView)view.findViewById(R.id.transactionValue);
                transactionValue.setTypeface(typefacer.squareLight());

                 updateprofileButton=(Button)view.findViewById(R.id.updateprofileButton);
                 updateprofileButton.setTypeface(typefacer.squareLight());

                resetPinButton=(Button)view.findViewById(R.id.resetPinButton);
                resetPinButton .setTypeface(typefacer.squareLight());

                profile_image=(CircleImageView)view.findViewById(R.id.profile_image);

        profile_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(intent,
                        CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE);

            }
        });



        profile_image.post(new Runnable() {
            @Override
            public void run() {

                try {
                    Agent agent = Utils.AgentData();

                    if (agent.getProfile_pics() != null) {


                        final Bitmap bitmap = BitmapFactory.decodeByteArray(agent.getProfile_pics(), 0,
                                agent.getProfile_pics().length);

                        profile_image.setImageBitmap(bitmap);
                    }


                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });

                //checkUpdateButton=(Button)view.findViewById(R.id.checkUpdateButton);
               // checkUpdateButton .setTypeface(typefacer.squareLight());



        ///////////////////////
     setViewValues();
    }
    void setViewValues(){


        try{
            agentMsisdn.setText(Application.getActiveAgent().getMsisdn());
            agentName.setText(Application.getActiveAgent().getFirstName());
            merchantValue.setText(DaoAccess.AllMerchantStringify());
            branchesValue.setText(DaoAccess.AllBranchStringify());
            transactionValue.setText(" Cash-In = "+merchant.getCurrency()+" . "+merchant.getAmount_limit()+"\n Cash-Out = "+merchant.getCurrency() +" 0.0");
        }catch (Exception e){

            e.printStackTrace();


        }

    }





    public  String[] getMerchantsAsArray() {

        final List<Merchant> merchantList = Merchant.getAll();
        String[] object = new String[merchantList.size()];
        for (int i = 0; i < merchantList.size(); i++) {
            object[i] = merchantList.get(i).getName();
            Log.d("oxinbo","object "+ object[i]);
        }

        return object;
    }

    public  String[] getBranchesAsArray() {

      Merchant merchant=  Merchant.getActiveMerchant("true");
        final List<Branch> branchestList = Branch.getAllMerchantBranch(merchant.getCode());
        String[] object = new String[branchestList.size()];
        for (int i = 0; i < branchestList.size(); i++) {
            object[i] = branchestList.get(i).getName();
            Log.d("oxinbo","object "+ object[i]);
        }

        return object;
    }
    public  void showMerchantsDialog() {
        final CharSequence[] items = getMerchantsAsArray();
        final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Set Merchant");
        builder.setItems(items, new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                // userInfoSettings.setCurrentMerchant(userInfoSettings.getMerchants().get(which));

                // String selectedItem= ((AlertDialog)dialog).getListView().getSelectedItem().toString();
                // Log.d("oxinbo","selected merchant "+items[which]);

                try{


                    String selectedMerchant=items[which].toString();

                    DaoAccess.setActiveMerchant(selectedMerchant);

                }catch(Exception e){



                }

                // setMerchant();
                // saveSettingsInstance(userInfoSettings, USerInfoSettings.class);
                builder.create().dismiss();
            }
        });
        AlertDialog createOptions = builder.create();
        createOptions.show();
    }
    public  void showBrachesDialog() {
        final CharSequence[] items = getBranchesAsArray();
        final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Merchant Branches");
        builder.setItems(items, new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                // userInfoSettings.setCurrentMerchant(userInfoSettings.getMerchants().get(which));

                // String selectedItem= ((AlertDialog)dialog).getListView().getSelectedItem().toString();
                // Log.d("oxinbo","selected merchant "+items[which]);

                try{


                    String selectedMerchant=items[which].toString();

                   // DaoAccess.setActiveMerchant(selectedMerchant);

                }catch(Exception e){



                }

                // setMerchant();
                // saveSettingsInstance(userInfoSettings, USerInfoSettings.class);
                builder.create().dismiss();
            }
        });
        AlertDialog createOptions = builder.create();
        createOptions.show();
    }

    void SetOnClickListener(){



    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE) {
            if (resultCode == Activity.RESULT_OK) {

                Bitmap bmp = (Bitmap) data.getExtras().get("data");
                ByteArrayOutputStream stream = new ByteArrayOutputStream();

                bmp.compress(Bitmap.CompressFormat.PNG, 100, stream);
                byte[] byteArray = stream.toByteArray();

                Agent agent = new Agent();
                agent.setProfile_pics(byteArray);

                try {
                    Utils.insertAgentData(agent);
                } catch (IOException e) {
                    e.printStackTrace();
                }


                // convert byte array to Bitmap

                final Bitmap bitmap = BitmapFactory.decodeByteArray(byteArray, 0,
                        byteArray.length);


                profile_image.post(new Runnable() {
                    @Override
                    public void run() {
                        profile_image.setImageBitmap(bitmap);

                    }
                });

            }
        }
    }
        public interface ProfileSettingsFragInteractionListener {
        // TODO: Update argument type and name
        public void ProfileSettingsFragInteraction(Uri uri);
    }

}
