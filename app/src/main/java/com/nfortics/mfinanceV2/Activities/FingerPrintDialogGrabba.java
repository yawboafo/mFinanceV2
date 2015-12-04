package com.nfortics.mfinanceV2.Activities;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Looper;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
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
import com.nfortics.mfinanceV2.MessageAlert;
import com.nfortics.mfinanceV2.R;
import com.nfortics.mfinanceV2.Settings.GeneralSettings;
import com.nfortics.mfinanceV2.Utilities.ToastUtil;

/**
 * Created by bigfire on 11/2/2015.
 */
public class FingerPrintDialogGrabba extends Dialog {

    private Button saveButton;
    private Button cancelButton;
    private static ImageView fingerPrintImage;

    private final SubmitSuccessCallback callback;
    private GeneralSettings generalSettings;
    private Context context;
    Application application;
    Activity activity;
    private GrabbaFingerprintListener grabbaFingerprintListener;
    private GrabbaConnectionListener grabbaConnectionListner;

    public FingerPrintDialogGrabba(final Context context, Activity activity, SubmitSuccessCallback callback) {
        super(context);
        this.context = context;
        this.callback = callback;
        this.activity = activity;
        this.setContentView(R.layout.finger_print_dialog_layout);
        setTitle("Read Finger Print");
        generalSettings = GeneralSettings.getInstance();

        application = (Application) context.getApplicationContext();
        application.setActivity((Activity) context);
      generalSettings.setFingerPrintImageData("");



        initialiseWidgets();
        setLabels();
        eventsListeners();
        try {
            openGrabba();
            initGrabbaConnectionListener();
            Grabba.getInstance().addConnectionListener(grabbaConnectionListner);
            Grabba.getInstance().acquireGrabba();

            new InitFingerPrinter().execute();
            show();
        } catch (Exception e) {
            MessageAlert.showMessage("Connection to Grabba device failed. Please check to make sure grabba device is on and try again.", context);
        }
    }
    private void initialiseWidgets() {
        saveButton = (Button) findViewById(R.id.loginButton);
        cancelButton = (Button) findViewById(R.id.resetButton);
        // mainLayout = (LinearLayout) findViewById(R.id.main_layout);
        fingerPrintImage = (ImageView) findViewById(R.id.finger_print_image);
    }

    private void eventsListeners() {
        onSaveButtonClickListener();
        onCancelButtonClickListener();
    }

    private void onCancelButtonClickListener() {
        cancelButton.setOnClickListener(new android.view.View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                fingerPrintImage.setImageResource(context.getResources().getColor(R.color.white));
                new InitFingerPrinter().execute();
            }
        });
    }

    protected static Runnable setImageRunnable(final Bitmap fingerPrintBitmap) {
        Runnable runnable = new Runnable() {

            @Override
            public void run() {
                fingerPrintImage.setImageBitmap(fingerPrintBitmap);

            }
        };
        return runnable;
    }

    public static void setImage(final Bitmap fingerPrintBitmap) {
        fingerPrintImage.post(setImageRunnable(fingerPrintBitmap));
    }

    private void onSaveButtonClickListener() {
        saveButton.setOnClickListener(new android.view.View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                // Thread thread = new Thread("saving") {
                // @Override
                // public void run() {
                // // TODO Auto-generated method stub
                // super.run();
                if (generalSettings.getFingerPrintImageData().trim().length() > 4) {
                    abortFingerPrint();
                    // new Init             FingerPrinter().cancel(true);
                    callback.execute();
                    dismiss();
                } else {
                    // dismiss();
                    toastOnUiThread("Finger Print can not be empty.");
                    // callback.execute();
                }
                // }
                // };
                // thread.setPriority(Thread.MAX_PRIORITY);
                // thread.start();

            }
        });
    }

    private void setLabels() {
        // saveButton.setText("Yes");
        // cancelButton.setText("No");
    }

    private void initGrabbaConnectionListener() {
        grabbaConnectionListner = new GrabbaConnectionListener() {

            @Override
            public void grabbaDisconnectedEvent() {
                toastOnUiThread("Device disconnected");

            }

            @Override
            public void grabbaConnectedEvent() {

                toastOnUiThread("Device connected");
            }

        };
    }

    private void toastOnUiThread(final String msg) {
        activity.runOnUiThread(new Runnable() {

            @Override
            public void run() {
                // TODO Auto-generated method stub
                Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private class InitFingerPrinter extends AsyncTask<Void, Integer, Void> {

        @Override
        protected void onPreExecute() {

            grabbaFingerprintListener = new GrabbaFingerprintListener() {

                @Override
                public void imageDataEvent(byte[] data, int imageType, int numRows, int numColumns) {
                    if (data != null) {
                        // generalSettings.setFingerPrintImageData(new String(data));

                    }
                    if (imageType == GrabbaFingerprint.ImageType.IMG_PREVIEW) {
                        final Bitmap drawBitmap = GrabbaFingerprint.getBlackAndWhiteBitmapFromData(numColumns, numRows, data);
                        generalSettings.setFingerPrintBitmap(drawBitmap);
                        setImage(drawBitmap);
                        generalSettings.setFingerPrintImageData(new String(data));
                        generalSettings.setFingerPrintBitmap(drawBitmap);
                    } else if (imageType == GrabbaFingerprint.ImageType.IMG_NO_COMPRESSION) {
                        final Bitmap drawBitmap = GrabbaFingerprint.getGreyscaleBitmapFromData(numColumns, numRows, data);
                        generalSettings.setFingerPrintBitmap(drawBitmap);
                        setImage(drawBitmap);
                        generalSettings.setFingerPrintBitmap(drawBitmap);
                    } else if (imageType == GrabbaFingerprint.ImageType.IMG_WSQ_COMPRESSION) {
                        Bitmap decodedWSQ;
                        try {
                            decodedWSQ = GrabbaFingerprint.getInstance().decodeWSQFile(data);
                            generalSettings.setFingerPrintBitmap(decodedWSQ);
                            setImage(decodedWSQ);
                            generalSettings.setFingerPrintBitmap(decodedWSQ);
                        } catch (Exception e) {
                            toastOnUiThread("crash");
                        }
                    }

                }

                @Override
                public void templateDataEvent(byte[] data, int templateType) {

                }

                @Override
                public void userMessageEvent(int messageId, int number, int total, GrabbaFingerprintUserRecord userRecord) {
                    // TODO Auto-generated method stub

                }
            };

        }

        @Override
        protected Void doInBackground(Void... params) {

            try {
                Looper.prepare();
            } catch (Exception e) {
                e.printStackTrace();
            }

            GrabbaFingerprint.getInstance().addEventListener(grabbaFingerprintListener);
            try {
                // GrabbaFingerprint.getInstance().enrolFingerprint_v2(GrabbaFingerprint.TemplateType.NO_TEMPLATE,
                // GrabbaFingerprint.ImageType.IMG_PREVIEW, 1, 1);
                // GrabbaFingerprint.getInstance().enrolFingerprint_v2(GrabbaFingerprint.TemplateType.ISO_PK_DATA_FMC_CS,
                // GrabbaFingerprint.ImageType.IMG_WSQ_COMPRESSION, 1, 1);

                GrabbaFingerprint.getInstance().enrolFingerprint_v2(GrabbaFingerprint.TemplateType.ISO_PK_DATA_FMC_CS, GrabbaFingerprint.ImageType.IMG_NO_COMPRESSION, 1, 1);

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

        @Override
        protected void onProgressUpdate(Integer... values) {
        }

        @Override
        protected void onPostExecute(Void result) {

        }
    }

    private void abortFingerPrint() {
        new Thread() {
            public void run() {
                try {
                    GrabbaFingerprint.getInstance().abort();
                } catch (GrabbaFunctionNotSupportedException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                } catch (GrabbaNotConnectedException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                } catch (GrabbaBusyException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                } catch (GrabbaNoExclusiveAccessException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            };
        }.start();
    }

    private void openGrabba() {
        try {

            Grabba.open(context, context.getResources().getString(R.string.app_name));

        } catch (GrabbaDriverNotInstalledException e) {
            // TODO display message that the Grabba driver is not installed
            e.printStackTrace();
            // logger.debug("Driver not installed");
            ToastUtil.showToast(context, "Install device driver");

        }

    }

    public interface SubmitSuccessCallback {
        public void execute();

        public void reset();
    }
}
