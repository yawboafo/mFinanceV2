package com.nfortics.mfinanceV2.Activities;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Paint;
import android.graphics.Path;
import android.os.AsyncTask;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.nfortics.mfinanceV2.R;
import com.nfortics.mfinanceV2.Signature.DoodlingPath;
import com.nfortics.mfinanceV2.Widgets.DoodlingSurface;

import java.io.File;
import java.io.FileOutputStream;

public class SignatureActivity extends Activity implements View.OnTouchListener {
    private DoodlingSurface doodlingSurface;
    private DoodlingPath currentDoodlingPath;
    private Paint currentPaint;

    private Button sendButton;
    private Button undoBtn;
    private ImageView colorPicker;

    // @InjectView(R.id.banner)
    // private TextView header;

    private static File APP_FILE_PATH = new File(Environment.getExternalStorageDirectory().getPath() + "/Ozinbo/Media/Ozinbo Images");
    private static Intent intent;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signature);

        intent = new Intent();
        // headerLabel.setText("Saya");//
        // getIntent().getExtras().getString("label"));
        // TextView headerTitle = (TextView) findViewById(R.id.activity_title);
        // headerTitle.setText("Doodle");
        doodlingSurface = (DoodlingSurface) findViewById(R.id.drawingSurface);
        sendButton = (Button) findViewById(R.id.sendButton);
        sendButton.setText("Save");
        // colorPicker = (ImageView) findViewById(R.id.color_picker);
        // colorPicker.setBackgroundDrawable(getResources().getDrawable(R.drawable.color_picker));
        // header.setText("Signature");
        setCurrentPaint();
        eventListerners();
        // headerItemsLayout.setVisibility(View.GONE);


    }
    private void colorClickListerner(int item) {
        switch (item) {
            case 0:
                setCurrentPaint(0xFFFFFFFF);
                break;
            case 1:
                setCurrentPaint(0xFFFF0000);
                break;
            case 2:
                setCurrentPaint(0xFF00FF00);
                break;
            default:
                setCurrentPaint(0x0276FD);
                break;
        }

    }

    private void setCurrentPaint() {
        currentPaint = new Paint();
        currentPaint.setDither(true);
        currentPaint.setColor(0xFFFFFFFF);
        currentPaint.setStyle(Paint.Style.STROKE);
        currentPaint.setStrokeJoin(Paint.Join.ROUND);
        currentPaint.setStrokeCap(Paint.Cap.ROUND);
        currentPaint.setStrokeWidth(3);

    }

    /**
     * Sets eventListerners to the widgets in DoodlingActivity
     */
    private void eventListerners() {
        doodlingSurface.setOnTouchListener(this);
        onSendButtonClickListerner();
        // onColorPickerClicklistener();
    }

    public boolean onTouch(View view, MotionEvent motionEvent) {
        if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
            currentDoodlingPath = new DoodlingPath();
            currentDoodlingPath.paint = currentPaint;
            currentDoodlingPath.path = new Path();
            currentDoodlingPath.path.moveTo(motionEvent.getX(),
                    motionEvent.getY());
            currentDoodlingPath.path.lineTo(motionEvent.getX(),
                    motionEvent.getY());

        } else if (motionEvent.getAction() == MotionEvent.ACTION_MOVE) {
            currentDoodlingPath.path.lineTo(motionEvent.getX(),
                    motionEvent.getY());
            doodlingSurface.addDrawingPath(currentDoodlingPath);

        } else if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
            currentDoodlingPath.path.lineTo(motionEvent.getX(),
                    motionEvent.getY());
            doodlingSurface.addDrawingPath(currentDoodlingPath);

        }

        return true;
    }

    /**
     * Listens for click events on the send Button. onClick doodle is saved as a
     * png and then sent to chat partner via http
     */
    private void onSendButtonClickListerner() {
        sendButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                final Activity currentActivity = SignatureActivity.this;
                Handler saveHandler = new Handler() {
                    @Override
                    public void handleMessage(Message msg) {
                        // Toast.makeText(DoodlingActivity.this,
                        // "Sending Doodle...", Toast.LENGTH_LONG).show();
                    }
                };
             // Toast.makeText(SignatureActivity.this, "Attaching signature...", Toast.LENGTH_LONG).show();
                new ExportBitmapToFile(currentActivity, saveHandler, doodlingSurface.getBitmap()).execute();

                // sending png via http
            }
        });
    }

    /**
     * Listens for click events on the color picker image. onclick color options
     * are displayed to pick a color
     */
    private void onColorPickerClicklistener() {
        colorPicker.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // colorsDialog().show();

            }
        });
    }

    /**
     * Set the fore color of the canvas.
     *
     * @param color
     *            the current color to be set
     */
    private void setCurrentPaint(int color) {
        currentPaint = new Paint();
        currentPaint.setDither(true);
        currentPaint.setColor(color);
        currentPaint.setStyle(Paint.Style.STROKE);
        currentPaint.setStrokeJoin(Paint.Join.ROUND);
        currentPaint.setStrokeCap(Paint.Cap.ROUND);
        currentPaint.setStrokeWidth(1);
    }



    private class ExportBitmapToFile extends AsyncTask<Intent, Void, Boolean> {
        private Context mContext;
        private Handler mHandler;
        private Bitmap nBitmap;

        public ExportBitmapToFile(Context context, Handler handler,
                                  Bitmap bitmap) {
            mContext = context;
            nBitmap = bitmap;
            mHandler = handler;
        }

        @Override
        protected Boolean doInBackground(Intent... arg0) {
            try {
                if (!APP_FILE_PATH.exists()) {
                    // Log.i("DoodlingActivity",
                    // ">>>>>>>>>>>>>>FILE DOES NOT EXIST<<<<<<<<<<<<<<<<<<<<");
                    boolean created = APP_FILE_PATH.mkdirs();
                    // if (created)
                    // Log.i("DoodlingActivity",
                    // ">>>>>>>>>>>>>>FILE created<<<<<<<<<<<<<<<<<<<<");
                    // else
                    // Log.i("DoodlingActivity",
                    // ">>>>>>>>>>>>>>FILE NOT created<<<<<<<<<<<<<<<<<<<<");
                }
                final FileOutputStream out = new FileOutputStream(new File(
                        APP_FILE_PATH + "/signature.jpeg"));
                nBitmap = Bitmap.createScaledBitmap(nBitmap, 150, 200, true);
                nBitmap.compress(Bitmap.CompressFormat.JPEG, 90, out);
                intent.putExtra("filePath", APP_FILE_PATH + "/signature.jpeg");
                out.flush();
                out.close();
                return true;
            } catch (Exception e) {
                intent.putExtra("filePath", "exception");
                e.printStackTrace();
            }
            // mHandler.post(completeRunnable);
            return false;
        }

        @Override
        protected void onPostExecute(Boolean bool) {
            super.onPostExecute(bool);
            if (bool) {
                mHandler.sendEmptyMessage(1);
            }
            setResult(RESULT_OK, intent);
            finish();
        }
    }





}
