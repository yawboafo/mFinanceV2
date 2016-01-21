package com.nfortics.mfinanceV2.Activities;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.BitmapDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.grabba.Grabba;
import com.grabba.GrabbaBusyException;
import com.grabba.GrabbaConnectionListener;
import com.grabba.GrabbaDriverNotInstalledException;
import com.grabba.GrabbaFingerprint;
import com.grabba.GrabbaFingerprintListener;
import com.grabba.GrabbaFingerprintUserRecord;
import com.grabba.GrabbaFunctionNotSupportedException;
import com.grabba.GrabbaNoExclusiveAccessException;
import com.grabba.GrabbaNotConnectedException;
import com.nfortics.mfinanceV2.Application.Application;
import com.nfortics.mfinanceV2.Models.FingerPrintInfo;
import com.nfortics.mfinanceV2.R;
import com.nfortics.mfinanceV2.Settings.GeneralSettings;
import com.nfortics.mfinanceV2.Typefacer;
import com.nfortics.mfinanceV2.Utilities.ToastUtil;
import com.nfortics.mfinanceV2.Utilities.Utils;

import org.mobile2i.api.Fingerprint;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class FingerPrintCaptureActivity extends Activity {
private ImageView imageview;
    private Button finishButton,cancelButton;
    private   TextView   LT,LIF,LMF,LRF,LBF,RT,RIF,RMF,RRF,RBF;
    private static TextView memoryTextView;
    GeneralSettings generalSettings;

   public static String selectedFinger;

    Map<String,Bitmap> fingerGallery=new HashMap<>();

    private List<FingerPrintInfo> fingerPrints = new ArrayList<FingerPrintInfo>(10);
    private Fingerprint mFingerprint;
    Typefacer typefacer;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_finger_print);
        //LT,LIF,LMF,LRF,LBF,RT,RIF,RMF,RRF,RBF;

         typefacer=new Typefacer();
        generalSettings=GeneralSettings.getInstance();

        SetLabels();

        if (Utils.deviceIsGrabba()) {
            openGrabba();
        }


        FingerClickLabels();
        EventListeners();
       try{

    //   KeySets();
       preInitializeFingerLabels();
      }catch (Exception e){


}

    }


    private void preInitializeFingerLabels(){

        for (String x :Application.listOfKeysChecked) {

            strikeLabels(x);
        }

    }



    void strikeLabels(String x){


        switch (x){

            case "left_thumb":


                LT.setPaintFlags(LT.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
                LT.setTextColor(Color.parseColor("#000000"));
                LT.setEnabled(false);
                break;
            case "left_index":


                LIF.setPaintFlags(LIF.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
                LIF.setTextColor(Color.parseColor("#000000"));
                LIF.setEnabled(false);
                break;
            case "left_middle":


                LMF.setPaintFlags(LMF.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
                LMF.setTextColor(Color.parseColor("#000000"));
                LMF.setEnabled(false);
                break;
            case "left_ring":


                LRF.setPaintFlags(LRF.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
                LRF.setTextColor(Color.parseColor("#000000"));
                LRF.setEnabled(false);
                break;

            case "left_little":


                LBF.setPaintFlags(LBF.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
                LBF.setTextColor(Color.parseColor("#000000"));
                LBF.setEnabled(false);
                break;

            case "right_thumb":


                RT.setPaintFlags(RT.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
                RT.setTextColor(Color.parseColor("#000000"));
                RT.setEnabled(false);
                break;
            case "right_index":


                RIF.setPaintFlags(RIF.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
                RIF.setTextColor(Color.parseColor("#000000"));
                RIF.setEnabled(false);
                break;
            case "right_middle":


                RMF.setPaintFlags(RMF.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
                RMF.setTextColor(Color.parseColor("#000000"));
                RMF.setEnabled(false);
                break;
            case "right_ring":


                RRF.setPaintFlags(RRF.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
                RRF.setTextColor(Color.parseColor("#000000"));
                RRF.setEnabled(false);
                break;

            case "right_little":


                RBF.setPaintFlags(RBF.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
                RBF.setTextColor(Color.parseColor("#000000"));
                RBF.setEnabled(false);
                break;
        }
    }



    List<String> mapKeys=new ArrayList<>();
    private void KeySets(){

        for ( String key :  Application.basse64Images.keySet() ) {

            mapKeys.add(key);
         Log.d("oxinbo","key = >"+key);
        }
    }

    private void SetLabels(){

        LT=(TextView)findViewById(R.id.R_thumbFinger);
       // LT.setPaintFlags(LT.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
        LT.setTypeface(typefacer.squareRegular());


        LIF=(TextView)findViewById(R.id.R_indexfingerLabel);
        LIF.setTypeface(typefacer.squareRegular());

        LMF=(TextView)findViewById(R.id.R_middlefingerLabel);
        LMF.setTypeface(typefacer.squareRegular());

        LRF=(TextView)findViewById(R.id.R_ringfingerLabel);
        LRF.setTypeface(typefacer.squareRegular());


        LBF=(TextView)findViewById(R.id.R_babyfingerLabel);
        LBF.setTypeface(typefacer.squareRegular());


        RT=(TextView)findViewById(R.id.L_thumbFinger);
        RT.setTypeface(typefacer.squareRegular());

        RIF=(TextView)findViewById(R.id.L_indexfinger);
        RIF.setTypeface(typefacer.squareRegular());

        RMF=(TextView)findViewById(R.id.L_middlefinger);
        RMF.setTypeface(typefacer.squareRegular());

        RRF=(TextView)findViewById(R.id.L_ringfinger);
        RRF.setTypeface(typefacer.squareRegular());

        RBF=(TextView)findViewById(R.id.L_babyfinger);
        RBF.setTypeface(typefacer.squareRegular());


        imageview=(ImageView)findViewById(R.id.imageView);
        finishButton=(Button)findViewById(R.id.finishButton);
        finishButton.setTypeface(typefacer.squareLight());
        cancelButton=(Button)findViewById(R.id.butCancel);
        cancelButton.setTypeface(typefacer.squareLight());
    }
    private void EventListeners(){
        finishButtonClickListener();
        cancelButtonClickListener();
    }
    private void finishButtonClickListener(){

        finishButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

              //  Application.getGalleryImageList().add(fingerGallery);
                setResult(RESULT_OK);
                finish();


            }
        });
    }
    private void cancelButtonClickListener(){

        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

              //  generalSettings.setFingerPrints(fingerPrints);
               // setResult(RESULT_OK);
                finish();


            }
        });
    }
    Bitmap bitmap;
    private void LT_clicked(){

        LT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                memoryTextView=LT;
                LT.setTextColor(Color.parseColor("#FC0F0F"));
                selectedFinger="left_thumb";
                restartGrabbaFingerPrintService();






            }
        });
    }
    private void LIF_clicked(){

        LIF.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                memoryTextView=LIF;
                LIF.setTextColor(Color.parseColor("#FC0F0F"));
                selectedFinger="left_index";
                restartGrabbaFingerPrintService();


            }
        });
    }
    private void LMF_clicked(){

        LMF.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                memoryTextView=LMF;
                LMF.setTextColor(Color.parseColor("#FC0F0F"));
                selectedFinger="left_middle";
                restartGrabbaFingerPrintService();


            }
        });
    }
    private void LRF_clicked(){

        LRF.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                memoryTextView=LRF;
                LRF.setTextColor(Color.parseColor("#FC0F0F"));
                selectedFinger="left_ring";
                restartGrabbaFingerPrintService();

            }
        });
    }
    private void LBF_clicked(){

        LBF.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                memoryTextView=LBF;
                LBF.setTextColor(Color.parseColor("#FC0F0F"));
                selectedFinger="left_little";
                restartGrabbaFingerPrintService();


            }
        });
    }

    private void RT_clicked(){

        RT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                memoryTextView=RT;
                RT.setTextColor(Color.parseColor("#FC0F0F"));

                selectedFinger="right_thumb";
                restartGrabbaFingerPrintService();

            }
        });
    }
    private void RIF_clicked(){

        RIF.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                memoryTextView=RIF;
                RIF.setTextColor(Color.parseColor("#FC0F0F"));
                selectedFinger="right_index";
                restartGrabbaFingerPrintService();


            }
        });
    }
    private void RMF_clicked(){

        RMF.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                memoryTextView=RMF;
                RMF.setTextColor(Color.parseColor("#FC0F0F"));


                selectedFinger="right_middle";
                restartGrabbaFingerPrintService();

            }
        });
    }
    private void RRF_clicked(){

        RRF.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                memoryTextView=RRF;
                RRF.setTextColor(Color.parseColor("#FC0F0F"));

                selectedFinger="right_ring";
                restartGrabbaFingerPrintService();


            }
        });
    }
    private void RBF_clicked(){

        RBF.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                memoryTextView=RBF;
                RBF.setTextColor(Color.parseColor("#FC0F0F"));

                selectedFinger="right_little";
                restartGrabbaFingerPrintService();

            }
        });
    }



    private void FingerClickLabels(){

        LT_clicked();
        LIF_clicked();
        LMF_clicked();
        LRF_clicked();
        LBF_clicked();


        RT_clicked();
        RIF_clicked();
        RMF_clicked();
        RRF_clicked();
        RBF_clicked();
    }






    private void addFingerPrintInfoByIndex(int index, FingerPrintInfo f) {

        int i = 0;
        for (FingerPrintInfo fgi : fingerPrints) {
            if (fgi.getIndex() == index) {
                fingerPrints.set(i, f);
                return;
            }
            i++;
        }

        fingerPrints.add(f);
    }


    private FingerPrintInfo getFingerPrintInfoByIndex(int index) {

        for (FingerPrintInfo fgi : fingerPrints) {
            if (fgi.getIndex() == index)
                return fgi;
        }
        return new FingerPrintInfo();
    }


    private void restartFingerPrintService() {
        if (Utils.deviceIsM50()) {
           // restartM50FingerPrintService();
        } else if (Utils.deviceIsC180()) {
          //  restartC180FingerPrintService();
        } else if (Utils.deviceIsGrabba()) {
            restartGrabbaFingerPrintService();
        } else if (Utils.deviceIsSecugen()) {
           // restartSecugenFingerPrintService();
        }
    }


    /**************All grabba methods and classed plus variables go inn here ************/
    private void openGrabba() {
        try {
            Grabba.open(this, getResources().getString(R.string.app_name));
        } catch (GrabbaDriverNotInstalledException e) {

            e.printStackTrace();
            ToastUtil.showToast(this, "Install device driver");
        }

    }
    private final GrabbaConnectionListener grabbaConnectionListner = new GrabbaConnectionListener() {

        @Override
        public void grabbaDisconnectedEvent() {
            // Implementation is not thread safe so using posting the UI thread queuq
            runOnUiThread(new Runnable() {

                @Override
                public void run() {
                    // TODO Auto-generated method stub
                    ToastUtil.showToast(FingerPrintCaptureActivity.this, "Device Disconnected");

                }
            });

        }

        @Override
        public void grabbaConnectedEvent() {
            // Implementation is not thread safe so using posting the UI thread queue
            runOnUiThread(new Runnable() {

                @Override
                public void run() {
                    // TODO Auto-generated method stub
                    ToastUtil.showToast(FingerPrintCaptureActivity.this, "Device Connected");

                }
            });

        }
    };
    private final GrabbaFingerprintListener grabbaFingerprintListener = new GrabbaFingerprintListener() {

        @Override
        public void imageDataEvent(byte[] data, int imageType, int numRows, int numColumns) {
            if (data != null) {
                generalSettings.setFingerPrintImageData(new String(data));

            }
            if (imageType == GrabbaFingerprint.ImageType.IMG_PREVIEW) {
                final Bitmap drawBitmap = GrabbaFingerprint.getBlackAndWhiteBitmapFromData(numColumns, numRows, data);

                setFingerPrintImage(drawBitmap);
            } else if (imageType == GrabbaFingerprint.ImageType.IMG_NO_COMPRESSION) {
                final Bitmap drawBitmap = GrabbaFingerprint.getGreyscaleBitmapFromData(numColumns, numRows, data);
                setFingerPrintImage2(drawBitmap);

            } else if (imageType == GrabbaFingerprint.ImageType.IMG_WSQ_COMPRESSION) {
                Bitmap decodedWSQ;
                try {
                    decodedWSQ = GrabbaFingerprint.getInstance().decodeWSQFile(data);

                    setFingerPrintImage(decodedWSQ);


                } catch (Exception e) {
                }
            }

        }

        @Override
        public void templateDataEvent(byte[] data, int templateType) {
            // TODO Auto-generated method stub

        }

        @Override
        public void userMessageEvent(int messageId, int number, int total, GrabbaFingerprintUserRecord userRecord) {
            // TODO Auto-generated method stub

        }
    };
    private void restartGrabbaFingerPrintService() {

        imageview.setImageResource(android.R.color.white);
        initGrabbaFingerPrint();
        Toast.makeText(this, "Please put your finger on the sensor", Toast.LENGTH_LONG).show();

    }
    private void initGrabbaFingerPrint() {
        GrabbaFingerprint.getInstance().addEventListener(grabbaFingerprintListener);
        new initFingerPrinter().execute();

    }

    List<String>stringList=new ArrayList<>();
    private void setFingerPrintImage(final Bitmap bm) {
        imageview.post(new Runnable() {

            @Override
            public void run() {


                if (selectedFinger != null && bm != null) {
                    imageview.setImageBitmap(bm);
                }

            }
        });
    }
    private void setFingerPrintImage2(final Bitmap bm) {
        imageview.post(new Runnable() {

            @Override
            public void run() {


                if (selectedFinger != null && bm != null) {
                    imageview.setImageBitmap(bm);
                   // fingerGallery.put(selectedFinger, bm);


                  // Application.getFingerPrintImages().put(selectedFinger,getBase64Bytes(bm));
                    Application.getGalleryImages().put(selectedFinger, bm);
                    Application. basse64Images.put(selectedFinger, getBase64Bytes(bm));
                    String afisID=selectedFinger+"_id";
                    Application.afistemplateList.put(afisID, "nothing or now");
                    Application.listOfKeysChecked.add(selectedFinger);




                    if(memoryTextView!=null){
                        memoryTextView.setPaintFlags(memoryTextView.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
                        memoryTextView.setTextColor(Color.parseColor("#000000"));
                        memoryTextView.setEnabled(false);

                    }



                }

            }
        });
    }
    private String getBase64Bytes(Bitmap bitmap) {
        ByteArrayOutputStream bao = new ByteArrayOutputStream();

        //  bitmap = Bitmap.createScaledBitmap(bitmap, 350, 350, true);
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bao);

        byte[] ba = bao.toByteArray();

        return com.nfortics.mfinanceV2.Utilities.Base64.encodeBytes(ba);
    }

    public static void printMap(HashMap<String, Bitmap> map,String selectedFinger){

        Set<String> keys = map.keySet();
        for(String p:keys){

            if(map.get(p).equals(selectedFinger)){


            }

        }





    }



    void addFingerPrintToListGallery( Map<String,Bitmap> fGallery){




                Application.getGalleryImageList().add(fingerGallery);
                  fingerGallery=new HashMap<String, Bitmap>();










    }
    private void toastOnUiThread(final String msg) {
        FingerPrintCaptureActivity.this.runOnUiThread(new Runnable() {

            @Override
            public void run() {
                // TODO Auto-generated method stub
                Toast.makeText(FingerPrintCaptureActivity.this, msg, Toast.LENGTH_SHORT).show();
            }
        });
    }
    boolean addFingerPrint2ListOfMap( String entryKey){

        for( Map<String,Bitmap> map:Application.getGalleryImageList()){

           for(Map.Entry<String,Bitmap> entry:map.entrySet()){

               String key=entry.getKey();
               toastOnUiThread(key);
               if(key.equalsIgnoreCase(entryKey)) {return true;

               }else{  return false;}
           }



        }

        return false;
    }


    void FingerPrint2ListOfMap( String entryKey,final  Bitmap bm) {

        for (Map<String, Bitmap> map : Application.getGalleryImageList()) {


            for(Map.Entry<String,Bitmap> entry   :map.entrySet()){

                String key=entry.getKey();


                if(key.equalsIgnoreCase(entryKey)){


                }
            }

        }

    }












    private class initFingerPrinter extends AsyncTask<Void, String, Void> {

        @Override
        protected Void doInBackground(Void... params) {
            GrabbaFingerprint.getInstance().addEventListener(grabbaFingerprintListener);
            try {
                GrabbaFingerprint.getInstance().enrolFingerprint_v2(GrabbaFingerprint.TemplateType.ISO_PK_DATA_FMC_NS, GrabbaFingerprint.ImageType.IMG_NO_COMPRESSION, 1, 1);
            } catch (GrabbaFunctionNotSupportedException e) {

                e.printStackTrace();
            } catch (GrabbaNotConnectedException e) {

                e.printStackTrace();
            } catch (GrabbaBusyException e) {
                e.printStackTrace();
            } catch (GrabbaNoExclusiveAccessException e) {
                e.printStackTrace();
            }
            return null;
        }
    }
    /***************************All Grabba Ends here**************/

    @Override
    protected void onResume() {
        super.onResume();
        if (Utils.deviceIsGrabba()) {
            Grabba.getInstance().addConnectionListener(grabbaConnectionListner);
            Grabba.getInstance().acquireGrabba();
        }


    }

    @Override
    protected void onPause() {
        super.onPause();
        if (Utils.deviceIsGrabba()) {
            Grabba.getInstance().removeConnectionListener(grabbaConnectionListner);
            Grabba.getInstance().releaseGrabba();
        }
    }


}
