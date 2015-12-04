package com.nfortics.mfinanceV2.Activities;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.nfortics.mfinanceV2.Application.Application;
import com.nfortics.mfinanceV2.DataBase.DaoAccess;
import com.nfortics.mfinanceV2.Models.Merchant;
import com.nfortics.mfinanceV2.Models.User;
import com.nfortics.mfinanceV2.R;
import com.nfortics.mfinanceV2.Typefacer;
import com.nfortics.mfinanceV2.Utilities.Utils;

public class SettingActive extends BaseActivity {


    TextView profileTitle,
            agentName,
            agentMsisdn,
            branchesTitle,
            branchesValue,
            merchantTitle,
            merchantValue,
            transactionTitle,
            transactionValue,
            SettingsapplicationTitle,
            verionLabel,
            versionValue;

    ImageView imageViewGeneral,imageViewApp,imageViewProfile;

    RelativeLayout profileCon,AppCon,GeneralCon;

    TextView generalTitle,deviceID,deviceIDvalue,printerSetup,prntername;
    Button updateprofileButton,
            resetPinButton,
            checkUpdateButton,
            checkupdateButton,
    printerConnectButton;

    RelativeLayout relativeLayout;
    Typefacer typefacer;

    Utils utils=new Utils();
    View view;

    Merchant merchant;
    User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        typefacer=new Typefacer();
        getSupportActionBar().setTitle("");
        relativeLayout=(RelativeLayout)findViewById(R.id.compound);

        view=generateActivityView();



        relativeLayout.removeAllViews();
        setViews(view);
        relativeLayout.addView(view);

        setViewValues();



        try{
            merchant=Merchant.getActiveMerchant("true");
            Log.d("oxinbo","active merchant details "+merchant.getCode()+merchant.getName()+merchant.getAmount_limit());

        }catch (Exception e){

            e.printStackTrace();
        }
    }



    private View generateActivityView(){

        LayoutInflater layoutInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View activityView = layoutInflater.inflate(R.layout.activity_setting_active, null, false);

        return activityView;
    }


   void setViews(View view){

       imageViewGeneral=(ImageView)view.findViewById(R.id.imageViewGeneral);
       imageViewApp=(ImageView)view.findViewById(R.id.imageViewApp);
       imageViewProfile=(ImageView)view.findViewById(R.id.imageViewProfile);

                 profileCon=(RelativeLayout)view.findViewById(R.id.profileCon);
               profileCon.setVisibility(View.GONE);
                 AppCon=(RelativeLayout)view.findViewById(R.id.AppCon);
                  AppCon.setVisibility(View.GONE);
                 GeneralCon=(RelativeLayout)view.findViewById(R.id.GeneralCon);
                 GeneralCon.setVisibility(View.GONE);



                profileTitle=(TextView)view.findViewById(R.id.profileTitle);
                profileTitle.setTypeface(typefacer.squareMedium());
             imageViewProfile.setOnClickListener(new View.OnClickListener() {
                     @Override
                     public void onClick(View v) {



                         if (profileCon.getVisibility() == View.VISIBLE) {

                             profileCon.setVisibility(View.GONE);



                             imageViewProfile.post(new Runnable() {
                                 @Override
                                 public void run() {
                                     imageViewProfile.setImageResource(R.drawable.adda);
                                 }
                             });

                         } else {

                             profileCon.setVisibility(View.VISIBLE);

                             imageViewProfile.post(new Runnable() {
                                 @Override
                                 public void run() {
                                     imageViewProfile.setImageResource(R.drawable.minus);
                                 }
                             });
                         }
                     }
                 });





               agentName=(TextView)view.findViewById(R.id.agentName);
               agentMsisdn=(TextView)view.findViewById(R.id.agentMsisdn);

               branchesTitle=(TextView)view.findViewById(R.id.branchesTitle);
               branchesTitle.setTypeface(typefacer.squareLight());

               branchesValue=(TextView)view.findViewById(R.id.branchesValue);


                branchesValue.setTypeface(typefacer.squareLight());

               merchantTitle=(TextView)view.findViewById(R.id.merchantTitle);
                merchantTitle.setTypeface(typefacer.squareLight());

               merchantValue=(TextView)view.findViewById(R.id.merchantValue);
                merchantValue.setTypeface(typefacer.squareLight());


               transactionTitle=(TextView)view.findViewById(R.id.transactionTitle);
                transactionTitle.setTypeface(typefacer.squareLight());

               transactionValue=(TextView)view.findViewById(R.id.transactionValue);
               transactionValue.setTypeface(typefacer.squareLight());


               SettingsapplicationTitle=(TextView)view.findViewById(R.id.SettingsapplicationTitle);
               SettingsapplicationTitle.setTypeface(typefacer.squareMedium());

       imageViewApp.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               if (AppCon.getVisibility() == View.VISIBLE) {

                   AppCon.setVisibility(View.GONE);


                   imageViewApp.post(new Runnable() {
                       @Override
                       public void run() {
                           imageViewApp.setImageResource(R.drawable.adda);
                       }
                   });

               } else {


                   AppCon.setVisibility(View.VISIBLE);

                   imageViewApp.post(new Runnable() {
                       @Override
                       public void run() {
                           imageViewApp.setImageResource(R.drawable.minus);
                       }
                   });

               }
           }
       });


              verionLabel=(TextView)view.findViewById(R.id.verionLabel);
               verionLabel.setTypeface(typefacer.squareLight());

               versionValue=(TextView)view.findViewById(R.id.versionValue);
                versionValue.setTypeface(typefacer.squareLight());




               generalTitle=(TextView)view.findViewById(R.id.generalTitle);
               generalTitle.setTypeface(typefacer.squareMedium());

             imageViewGeneral.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               if (GeneralCon.getVisibility() == View.VISIBLE) {

                   GeneralCon.setVisibility(View.GONE);
                   imageViewGeneral.post(new Runnable() {
                       @Override
                       public void run() {
                           imageViewGeneral.setImageResource(R.drawable.adda);
                       }
                   });


               } else {

                   GeneralCon.setVisibility(View.VISIBLE);

                   imageViewGeneral.post(new Runnable() {
                       @Override
                       public void run() {
                           imageViewGeneral.setImageResource(R.drawable.minus);
                       }
                   });

               }
           }
       });



               deviceID=(TextView)view.findViewById(R.id.deviceID);
               deviceIDvalue=(TextView)view.findViewById(R.id.deviceIDvalue);
                deviceIDvalue.setTypeface(typefacer.squareLight());

               printerSetup=(TextView)view.findViewById(R.id.printerSetup);
               printerSetup.setTypeface(typefacer.squareLight());
                 prntername=(TextView)view.findViewById(R.id.prntername);
                 prntername.setTypeface(typefacer.squareLight());

               printerConnectButton=(Button)view.findViewById(R.id.printerConnectButton);
                printerConnectButton.setTypeface(typefacer.squareLight());


               updateprofileButton=(Button)view.findViewById(R.id.updateprofileButton);
               updateprofileButton.setTypeface(typefacer.squareLight());

               resetPinButton=(Button)view.findViewById(R.id.resetPinButton);
               resetPinButton.setTypeface(typefacer.squareLight());
               checkUpdateButton=(Button)view.findViewById(R.id.checkUpdateButton);
               checkUpdateButton.setTypeface(typefacer.squareLight());
               checkupdateButton=(Button)view.findViewById(R.id.checkupdateButton);
               checkupdateButton.setTypeface(typefacer.squareLight());
    }
   void setViewValues(){


       try{
           agentMsisdn.setText(Application.getActiveAgent().getMsisdn());
           agentName.setText(Application.getActiveAgent().getFirstName());
           merchantValue.setText(DaoAccess.AllMerchantStringify());
           branchesValue.setText(DaoAccess.AllBranchStringify());
           transactionValue.setText(""+merchant.getAmount_limit());
       }catch (Exception e){

           e.printStackTrace();
       }

   }


}
