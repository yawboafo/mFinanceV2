package com.nfortics.mfinanceV2.Signature;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.net.Uri;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.nfortics.mfinanceV2.Application.Application;
import com.nfortics.mfinanceV2.R;
import com.nfortics.mfinanceV2.Utilities.Base64;
import com.nfortics.mfinanceV2.Utilities.Utils;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;



public class SignaturePad extends Activity {


    private com.github.gcacace.signaturepad.views.SignaturePad mSignaturePad;
    private Button mClearButton;
    private Button mSaveButton;

    Intent intent;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signature_pad);

        intent = new Intent();
        mSignaturePad = (com.github.gcacace.signaturepad.views.SignaturePad) findViewById(R.id.signature_pad);
        mSignaturePad.setOnSignedListener(new com.github.gcacace.signaturepad.views.SignaturePad.OnSignedListener() {
            @Override
            public void onSigned() {
                mSaveButton.setEnabled(true);
                mClearButton.setEnabled(true);
            }

            @Override
            public void onClear() {
                mSaveButton.setEnabled(false);
                mClearButton.setEnabled(false);
            }
        });

        mClearButton = (Button) findViewById(R.id.clear_button);
        mSaveButton = (Button) findViewById(R.id.save_button);

        mClearButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mSignaturePad.clear();
            }
        });

        mSaveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                Bitmap signatureBitmap = mSignaturePad.getSignatureBitmap();



                 if(Application.activeSignatureLabel==null){

                     Application.getGalleryImages().put("Signature", signatureBitmap);
                     Application. basse64Images.put(Application.activeSignatureLabel, getBase64Bytes(signatureBitmap));
                     Application.listOfKeysChecked.add(Application.activeSignatureLabel);


                 }else{


                     Application.getGalleryImages().put(Application.activeSignatureLabel, signatureBitmap);
                     Application. basse64Images.put(Application.activeSignatureLabel, getBase64Bytes(signatureBitmap));
                     Application.listOfKeysChecked.add(Application.activeSignatureLabel);

                 }






                setResult(RESULT_OK, intent);
                finish();







            }
        });
    }


    private String getBase64Bytes(Bitmap bitmap) {
        ByteArrayOutputStream bao = new ByteArrayOutputStream();

        bitmap = Bitmap.createScaledBitmap(bitmap, 350, 350, true);
        bitmap.compress(Bitmap.CompressFormat.JPEG, 90, bao);

        byte[] ba = bao.toByteArray();

        return Base64.encodeBytes(ba);
    }
    public File getAlbumStorageDir(String albumName) {
        // Get the directory for the user's public pictures directory.
        File file = new File(Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES), albumName);
        if (!file.mkdirs()) {
            Log.e("SignaturePad", "Directory not created");
        }
        return file;
    }

    public void saveBitmapToJPG(Bitmap bitmap, File photo) throws IOException {
        Bitmap newBitmap = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(newBitmap);
        canvas.drawColor(Color.WHITE);
        canvas.drawBitmap(bitmap, 0, 0, null);
        OutputStream stream = new FileOutputStream(photo);
        newBitmap.compress(Bitmap.CompressFormat.JPEG, 80, stream);
        stream.close();
    }

    public boolean addSignatureToGallery(Bitmap signature) {
        boolean result = false;
        try {
            File photo = new File(getAlbumStorageDir("SignaturePad"), String.format("Signature_%d.jpg", System.currentTimeMillis()));
            saveBitmapToJPG(signature, photo);
            Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
            Uri contentUri = Uri.fromFile(photo);
            mediaScanIntent.setData(contentUri);
            SignaturePad.this.sendBroadcast(mediaScanIntent);
            result = true;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }
}
