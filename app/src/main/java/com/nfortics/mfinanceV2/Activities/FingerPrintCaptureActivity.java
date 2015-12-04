package com.nfortics.mfinanceV2.Activities;

import android.app.Activity;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
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
import com.nfortics.mfinanceV2.Models.FingerPrintInfo;
import com.nfortics.mfinanceV2.R;
import com.nfortics.mfinanceV2.Settings.GeneralSettings;
import com.nfortics.mfinanceV2.Typefacer;
import com.nfortics.mfinanceV2.Utilities.ToastUtil;
import com.nfortics.mfinanceV2.Utilities.Utils;

import org.mobile2i.api.Fingerprint;

import java.util.ArrayList;
import java.util.List;

public class FingerPrintCaptureActivity extends Activity {
private ImageView imageview;
    private Button finishButton,cancelButton;
    private   TextView   LT,LIF,LMF,LRF,LBF,RT,RIF,RMF,RRF,RBF;

    GeneralSettings generalSettings;

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
    }

    private void SetLabels(){

        LT=(TextView)findViewById(R.id.R_thumbFinger);
        LT.setTypeface(typefacer.getRoboCondensedRegular(getAssets()));


        LIF=(TextView)findViewById(R.id.R_indexfingerLabel);
        LIF.setTypeface(typefacer.getRoboCondensedRegular(getAssets()));

        LMF=(TextView)findViewById(R.id.R_middlefingerLabel);
        LMF.setTypeface(typefacer.getRoboCondensedRegular(getAssets()));

        LRF=(TextView)findViewById(R.id.R_ringfingerLabel);
        LRF.setTypeface(typefacer.getRoboCondensedRegular(getAssets()));


        LBF=(TextView)findViewById(R.id.R_babyfingerLabel);
        LBF.setTypeface(typefacer.getRoboCondensedRegular(getAssets()));


        RT=(TextView)findViewById(R.id.L_thumbFinger);
        RT.setTypeface(typefacer.getRoboCondensedRegular(getAssets()));

        RIF=(TextView)findViewById(R.id.L_indexfinger);
        RIF.setTypeface(typefacer.getRoboCondensedRegular(getAssets()));

        RMF=(TextView)findViewById(R.id.L_middlefinger);
        RMF.setTypeface(typefacer.getRoboCondensedRegular(getAssets()));

        RRF=(TextView)findViewById(R.id.L_ringfinger);
        RRF.setTypeface(typefacer.getRoboCondensedRegular(getAssets()));

        RBF=(TextView)findViewById(R.id.L_babyfinger);
        RBF.setTypeface(typefacer.getRoboCondensedRegular(getAssets()));


        imageview=(ImageView)findViewById(R.id.imageView);
        finishButton=(Button)findViewById(R.id.finishButton);

        cancelButton=(Button)findViewById(R.id.butCancel);
    }
    private void EventListeners(){
        finishButtonClickListener();
        cancelButtonClickListener();
    }
    private void finishButtonClickListener(){

        finishButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                generalSettings.setFingerPrints(fingerPrints);
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
    private void LT_clicked(){

        LT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                FingerPrintInfo f = getFingerPrintInfoByIndex(0);
                if (f.getImageData().trim().length() > 0) {
                    imageview.setImageBitmap(f.getBitmap());

                    generalSettings.setFingerPrintImageData(f.getImageData());
                    generalSettings.setFingerPrintBitmap(f.getBitmap());
                    restartFingerPrintService();

                }else {
                    restartFingerPrintService();
                }

                if (generalSettings.getFingerPrintImageData().length() > 1) {

                    FingerPrintInfo fgi = new FingerPrintInfo(0, generalSettings.getFingerPrintImageData(), generalSettings.getFingerPrintBitmap());
                    addFingerPrintInfoByIndex(0, fgi);

                }
                LT.setTextColor(0xff000000);


            }
        });
    }
    private void LIF_clicked(){

        LIF.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                FingerPrintInfo f = getFingerPrintInfoByIndex(1);
                if (f.getImageData().trim().length() > 0) {
                    imageview.setImageBitmap(f.getBitmap());

                    generalSettings.setFingerPrintImageData(f.getImageData());
                    generalSettings.setFingerPrintBitmap(f.getBitmap());
                    restartFingerPrintService();

                } else {
                    restartFingerPrintService();
                }

                if (generalSettings.getFingerPrintImageData().length() > 1) {

                    FingerPrintInfo fgi = new FingerPrintInfo(1, generalSettings.getFingerPrintImageData(), generalSettings.getFingerPrintBitmap());
                    addFingerPrintInfoByIndex(1, fgi);

                }
              LIF.setTextColor(0xff000000);


            }
        });
    }
    private void LMF_clicked(){

        LMF.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                FingerPrintInfo f = getFingerPrintInfoByIndex(2);
                if (f.getImageData().trim().length() > 0) {
                    imageview.setImageBitmap(f.getBitmap());

                    generalSettings.setFingerPrintImageData(f.getImageData());
                    generalSettings.setFingerPrintBitmap(f.getBitmap());
                    restartFingerPrintService();

                }else {
                    restartFingerPrintService();
                }

                if (generalSettings.getFingerPrintImageData().length() > 1) {

                    FingerPrintInfo fgi = new FingerPrintInfo(2, generalSettings.getFingerPrintImageData(), generalSettings.getFingerPrintBitmap());
                    addFingerPrintInfoByIndex(2, fgi);

                }
                LMF.setTextColor(0xff000000);


            }
        });
    }
    private void LRF_clicked(){

        LRF.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                FingerPrintInfo f = getFingerPrintInfoByIndex(3);
                if (f.getImageData().trim().length() > 0) {
                    imageview.setImageBitmap(f.getBitmap());

                    generalSettings.setFingerPrintImageData(f.getImageData());
                    generalSettings.setFingerPrintBitmap(f.getBitmap());
                    restartFingerPrintService();

                } else {
                    restartFingerPrintService();
                }

                if (generalSettings.getFingerPrintImageData().length() > 1) {

                    FingerPrintInfo fgi = new FingerPrintInfo(3, generalSettings.getFingerPrintImageData(), generalSettings.getFingerPrintBitmap());
                    addFingerPrintInfoByIndex(3, fgi);

                }
               LRF.setTextColor(0xff000000);


            }
        });
    }
    private void LBF_clicked(){

        LBF.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                FingerPrintInfo f = getFingerPrintInfoByIndex(4);
                if (f.getImageData().trim().length() > 0) {
                    imageview.setImageBitmap(f.getBitmap());

                    generalSettings.setFingerPrintImageData(f.getImageData());
                    generalSettings.setFingerPrintBitmap(f.getBitmap());
                    restartFingerPrintService();

                }else {
                    restartFingerPrintService();
                }

                if (generalSettings.getFingerPrintImageData().length() > 1) {

                    FingerPrintInfo fgi = new FingerPrintInfo(4, generalSettings.getFingerPrintImageData(), generalSettings.getFingerPrintBitmap());
                    addFingerPrintInfoByIndex(4, fgi);

                }
                LBF.setTextColor(0xff000000);


            }
        });
    }

    private void RT_clicked(){

        RT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                FingerPrintInfo f = getFingerPrintInfoByIndex(5);
                if (f.getImageData().trim().length() > 0) {
                    imageview.setImageBitmap(f.getBitmap());

                    generalSettings.setFingerPrintImageData(f.getImageData());
                    generalSettings.setFingerPrintBitmap(f.getBitmap());
                    restartFingerPrintService();

                }else {
                    restartFingerPrintService();
                }

                if (generalSettings.getFingerPrintImageData().length() > 1) {

                    FingerPrintInfo fgi = new FingerPrintInfo(5, generalSettings.getFingerPrintImageData(), generalSettings.getFingerPrintBitmap());
                    addFingerPrintInfoByIndex(5, fgi);

                }
                RT.setTextColor(0xff000000);


            }
        });
    }
    private void RIF_clicked(){

        RIF.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                FingerPrintInfo f = getFingerPrintInfoByIndex(6);
                if (f.getImageData().trim().length() > 0) {
                    imageview.setImageBitmap(f.getBitmap());

                    generalSettings.setFingerPrintImageData(f.getImageData());
                    generalSettings.setFingerPrintBitmap(f.getBitmap());
                    restartFingerPrintService();

                }else {
                    restartFingerPrintService();
                }

                if (generalSettings.getFingerPrintImageData().length() > 1) {

                    FingerPrintInfo fgi = new FingerPrintInfo(6, generalSettings.getFingerPrintImageData(), generalSettings.getFingerPrintBitmap());
                    addFingerPrintInfoByIndex(6, fgi);

                }
                RIF.setTextColor(0xff000000);


            }
        });
    }
    private void RMF_clicked(){

        RMF.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                FingerPrintInfo f = getFingerPrintInfoByIndex(7);
                if (f.getImageData().trim().length() > 0) {
                    imageview.setImageBitmap(f.getBitmap());

                    generalSettings.setFingerPrintImageData(f.getImageData());
                    generalSettings.setFingerPrintBitmap(f.getBitmap());
                    restartFingerPrintService();

                }else {
                    restartFingerPrintService();
                }

                if (generalSettings.getFingerPrintImageData().length() > 1) {

                    FingerPrintInfo fgi = new FingerPrintInfo(7, generalSettings.getFingerPrintImageData(), generalSettings.getFingerPrintBitmap());
                    addFingerPrintInfoByIndex(7, fgi);

                }
                //LMF.setTextColor(android.R.color.black);
                RMF.setTextColor(0xff000000);

            }
        });
    }
    private void RRF_clicked(){

        RRF.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                FingerPrintInfo f = getFingerPrintInfoByIndex(8);
                if (f.getImageData().trim().length() > 0) {
                    imageview.setImageBitmap(f.getBitmap());

                    generalSettings.setFingerPrintImageData(f.getImageData());
                    generalSettings.setFingerPrintBitmap(f.getBitmap());
                    restartFingerPrintService();

                }else {
                    restartFingerPrintService();
                }

                if (generalSettings.getFingerPrintImageData().length() > 1) {

                    FingerPrintInfo fgi = new FingerPrintInfo(8, generalSettings.getFingerPrintImageData(), generalSettings.getFingerPrintBitmap());
                    addFingerPrintInfoByIndex(8, fgi);

                }
                RRF.setTextColor(0xff000000);


            }
        });
    }
    private void RBF_clicked(){

        RBF.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                FingerPrintInfo f = getFingerPrintInfoByIndex(9);
                if (f.getImageData().trim().length() > 0) {
                    imageview.setImageBitmap(f.getBitmap());

                    generalSettings.setFingerPrintImageData(f.getImageData());
                    generalSettings.setFingerPrintBitmap(f.getBitmap());
                    restartFingerPrintService();

                }else {
                    restartFingerPrintService();
                }

                if (generalSettings.getFingerPrintImageData().length() > 1) {

                    FingerPrintInfo fgi = new FingerPrintInfo(9, generalSettings.getFingerPrintImageData(), generalSettings.getFingerPrintBitmap());
                    addFingerPrintInfoByIndex(9, fgi);

                }
                RBF.setTextColor(0xff000000);


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
                generalSettings.setFingerPrintBitmap(drawBitmap);
                setFingerPrintImage(drawBitmap);
            } else if (imageType == GrabbaFingerprint.ImageType.IMG_NO_COMPRESSION) {
                final Bitmap drawBitmap = GrabbaFingerprint.getGreyscaleBitmapFromData(numColumns, numRows, data);
                generalSettings.setFingerPrintBitmap(drawBitmap);
                setFingerPrintImage(drawBitmap);
            } else if (imageType == GrabbaFingerprint.ImageType.IMG_WSQ_COMPRESSION) {
                Bitmap decodedWSQ;
                try {
                    decodedWSQ = GrabbaFingerprint.getInstance().decodeWSQFile(data);
                    generalSettings.setFingerPrintBitmap(decodedWSQ);
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
    private void setFingerPrintImage(final Bitmap bm) {
        imageview.post(new Runnable() {

            @Override
            public void run() {
                imageview.setImageBitmap(bm);

            }
        });
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
