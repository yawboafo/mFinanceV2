package com.nfortics.mfinanceV2.Models;

import android.graphics.Bitmap;
import android.util.Log;

import org.mobile2i.api.FingerprintUtil;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Created by bigfire on 11/3/2015.
 */
public class FingerPrintInfo {
    /**
     *
     */
    private static final long serialVersionUID = -6780176537825847910L;
    private int index;
    private String imageData = "";
    private String afisTemplate = "";
    private String description = "";
    transient private Bitmap bitmap;

    public FingerPrintInfo(int index, String data, Bitmap bitmap) {
        setIndex(index);
        setImageData(data);
        setBitmap(bitmap);
    }

    public FingerPrintInfo() {
        // TODO Auto-generated constructor stub
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public String getImageData() {
        return imageData;
    }

    public void setImageData(String imageData) {
        this.imageData = imageData;
    }

    public Bitmap getBitmap() {
        if (this.bitmap == null && this.imageData.trim().length() > 5)
            this.bitmap = FingerprintUtil.getImgBitmap(imageData.getBytes(),
                    false);
        return bitmap;
    }

    public void setBitmap(Bitmap bitmap) {
        this.bitmap = bitmap;
    }

    public String getMd5Hash() {
        return getMd5Hash(getImageData());
    }

    private String getMd5Hash(String input) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] messageDigest = md.digest(input.getBytes());
            BigInteger number = new BigInteger(1, messageDigest);
            String md5 = number.toString(16);

            while (md5.length() < 32)
                md5 = "0" + md5;

            return md5;
        } catch (NoSuchAlgorithmException e) {
            Log.e("MD5", e.getLocalizedMessage());
            return null;
        }
    }

    public String getAfisTemplate() {
        return afisTemplate;
    }

    public void setAfisTemplate(String afisTemplate) {
        this.afisTemplate = afisTemplate;
    }

    public String getDescription() {
        if (this.description == null)
            this.description = "";
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

}
