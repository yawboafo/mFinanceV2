package com.nfortics.mfinanceV2.FormWidgetGenerator;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.graphics.Bitmap;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.OvershootInterpolator;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.nfortics.mfinanceV2.Activities.OnBoardCustomerActivity;
import com.nfortics.mfinanceV2.Application.Application;
import com.nfortics.mfinanceV2.Dialogs.MaximumImageDialog;
import com.nfortics.mfinanceV2.R;
import com.nfortics.mfinanceV2.Typefacer;
import com.nfortics.mfinanceV2.ViewAdapters.GalleryImagesAdapterNew;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * Created by bigfire on 12/10/2015.
 */
public class FormMasterPictures extends FormWidget  {

    protected RelativeLayout relativeLayout;
    LayoutInflater inflator;
    RecyclerView recyclerView;
    com.github.clans.fab.FloatingActionMenu menu1;
    private com.github.clans.fab.FloatingActionButton fab1;
    private com.github.clans.fab.FloatingActionButton fab2;
    private com.github.clans.fab.FloatingActionButton fab3;
    protected Button FingerPrintButton,ImagesButton,SignatureButton;
    protected LinearLayout linearLayout;
    private int mScrollOffset = 4;
    private int mMaxProgress = 100;
    GalleryImagesAdapterNew galleryImagesAdapterNew;
    Typefacer typefacer;
    String type;
    String tagg;
    JSONArray namesInner;
    String objectName;
    JSONObject objectInnerName;
    JSONObject innerObject;

    public FormMasterPictures(Context context, String name, JSONObject obj,String tag) {
        super(context, name, tag);

        inflator = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        linearLayout=(LinearLayout)inflator.inflate(R.layout.linear_layout_style, null);
        typefacer=new Typefacer();
        relativeLayout=(RelativeLayout)inflator.inflate(R.layout.image_taking_bitmap, null);
        relativeLayout.setTag("picturesLayout");
        recyclerView=(RecyclerView)relativeLayout.findViewById(R.id.viewRecycle);


        RecyclerView.LayoutManager mlyout2=new GridLayoutManager(context,3,GridLayoutManager.VERTICAL,false);
        galleryImagesAdapterNew=new GalleryImagesAdapterNew(context, Application.getGalleryImages());
        recyclerView.setAdapter(galleryImagesAdapterNew);
        recyclerView.setLayoutManager(mlyout2);




        recyclerView.setOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (Math.abs(dy) > mScrollOffset) {
                    if (dy > 0) {

                        fab1.hide(true);
                        fab2.hide(true);
                        fab3.hide(true);
                       // menu1.hideMenu(true);
                    } else {
                        fab1.show(true);
                        fab2.show(true);
                        fab3.show(true);
                       // menu1.showMenu(true);
                    }
                }
            }
        });

        recyclerView.addOnItemTouchListener(new RecyclerTouchListener(Application.getAppContext(), recyclerView, new ClickListner() {
            @Override
            public void onClick(View view, int position) {

                TextView textView=(TextView)view.findViewById(R.id.text);
                String key=textView.getText().toString();
               // byte[] bmap=getKeyByValue(key);
                 if(key!=null){
                       MaximumImageDialog maxDialog=MaximumImageDialog.newInstance(key);
                  showDialog(maxDialog);
                 // maxDialog.show();
            }


            }

            @Override
            public void onLongClick(View view, int position) {
                TextView textView=(TextView)view.findViewById(R.id.text);
                String key=textView.getText().toString();


                try {
                    Application.FingerPrintbase64Images.remove(key);
                }
                catch (Exception e)
                {}

                try {
                    Application.getGalleryImages().remove(key);
                }
                catch (Exception e)
                {}

                try {
                    Application. basse64Images.remove(key);
                }catch (Exception e){}




                try {
                    Application.listOfKeysChecked.remove(key);
                }catch (Exception e)
                {}





                RecyclerView.LayoutManager mlyout2=new GridLayoutManager(Application.getAppContext(),3,GridLayoutManager.VERTICAL,false);
                galleryImagesAdapterNew=new GalleryImagesAdapterNew(Application.getAppContext(), Application.getGalleryImages());
                recyclerView.setAdapter(galleryImagesAdapterNew);
                recyclerView.setLayoutManager(mlyout2);


            }
        }));
        createFloatinButton(relativeLayout);
        decorateJson(obj);


        _layout.addView(relativeLayout);
    }


    public void showDialog(DialogFragment dialogFragment){
        FragmentManager manager =    Application.fragmentManager;
        // CashPaymentDialog dialog= new CashPaymentDialog();
        DialogFragment dialog = dialogFragment;
        dialog.show(manager, "");

    }
    private byte[] getKeyByValue(String value ) {

        for (Map.Entry<String, Bitmap> entry : Application.getGalleryImages().entrySet()) {


            if(entry.getKey().equals(value)){

                Bitmap bimap=(Bitmap)entry.getValue();

                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                bimap.compress(Bitmap.CompressFormat.PNG, 100, stream);
                byte[] byteArray = stream.toByteArray();

                return byteArray;
            }
        }


        return null;
    }


    void decorateJson(JSONObject obj){

        try {
             type=obj.getString("type");
             tagg=obj.getString("tag");
             namesInner=obj.names();

            for(int i=0;i<=namesInner.length();i++){


                objectName=namesInner.getString(i);


                if(objectName.equals("Pictures")){
                 //   Application.PictureLabelsList= Collections.EMPTY_LIST;
                    fab2.setVisibility(View.VISIBLE);
                    HashMap<String,String> picuresMap=new HashMap<>();
                    try{

                        innerObject=obj.getJSONObject(objectName);

                        JSONArray innerArray=innerObject.names();
                        Application.PictureLabelsList=new ArrayList<>();
                        for(int x=0;x<innerArray.length();x++){

                            String tmp=innerArray.getString(x);
                            objectInnerName=innerObject.getJSONObject(tmp);

                            Application.PictureLabelsList.add(objectInnerName.getString("label"));

                            picuresMap.put(objectInnerName.getString("label"), objectInnerName.getString("tag"));


                        }



                    }catch (Exception e){e.printStackTrace();}

                }


                if(objectName.equals("FingerPrint")){
                   // fab3.hide(false);
                    fab3.setVisibility(View.VISIBLE);
                    try{

                        innerObject=obj.getJSONObject(objectName);



                    }catch (Exception e){e.printStackTrace();}

                }

                if(objectName.equals("Signature")){
                  //  Application.SignatureLabelsList= Collections.EMPTY_LIST;
                    fab1.setVisibility(View.VISIBLE);
                    try{

                        HashMap<String,String> signatureMap=new HashMap<>();

                        try{

                            innerObject=obj.getJSONObject(objectName);

                            JSONArray innerArray=innerObject.names();
                            Application.SignatureLabelsList=new ArrayList<>();
                            for(int x=0;x<innerArray.length();x++){

                                String tmp=innerArray.getString(x);
                                objectInnerName=innerObject.getJSONObject(tmp);

                               Application.SignatureLabelsList.add(objectInnerName.getString("label"));

                                signatureMap.put(objectInnerName.getString("label"), objectInnerName.getString("tag"));


                            }


                        }catch (Exception e){e.printStackTrace();}

                    }catch (Exception e){e.printStackTrace();}

                }








            }

            //    Log.d("oxanbo","from picturesMsaster type = "+type+"tagg = "+tagg);
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }


    public static <T, E> T getKeyByValue(Map<T, E> map, E value) {
        for (Map.Entry<T, E> entry : map.entrySet()) {
            if (Objects.equals(value, entry.getValue())) {
                return entry.getKey();
            }
        }
        return null;
    }

    private void createFloatinButton(View view) {
        // final com.github.clans.fab.FloatingActionMenu menu3 = (com.github.clans.fab.FloatingActionMenu) findViewById(R.id.menu3);
       // menu1 = (com.github.clans.fab.FloatingActionMenu)view. findViewById(R.id.menu3);

        //  menu1.getMenuIconView().setImageResource(R.drawable.ic_menu);
        fab1 = (com.github.clans.fab.FloatingActionButton)view. findViewById(R.id.fab1);
        //fab1.setLabelText("Signature");
        fab1.setVisibility(View.GONE);

        fab2 = (com.github.clans.fab.FloatingActionButton)view. findViewById(R.id.fab2);
      //  fab2.setLabelText("Pictures");
        fab2.setVisibility(View.GONE);

        fab3 = (com.github.clans.fab.FloatingActionButton)view. findViewById(R.id.fab3);
       // fab3.setLabelText("FingerPrint");
        fab3.setVisibility(View.GONE);




     /**   AnimatorSet set = new AnimatorSet();

        ObjectAnimator scaleOutX = ObjectAnimator.ofFloat(menu1.getMenuIconView(), "scaleX", 1.0f, 0.2f);
        ObjectAnimator scaleOutY = ObjectAnimator.ofFloat(menu1.getMenuIconView(), "scaleY", 1.0f, 0.2f);

        ObjectAnimator scaleInX = ObjectAnimator.ofFloat(menu1.getMenuIconView(), "scaleX", 0.2f, 1.0f);
        ObjectAnimator scaleInY = ObjectAnimator.ofFloat(menu1.getMenuIconView(), "scaleY", 0.2f, 1.0f);

        scaleOutX.setDuration(50);
        scaleOutY.setDuration(50);

        scaleInX.setDuration(150);
        scaleInY.setDuration(150);

        scaleInX.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationStart(Animator animation) {
                menu1.getMenuIconView().setImageResource(menu1.isOpened()
                        ? R.drawable.ic_close : R.drawable.plus);
            }
        });

        set.play(scaleOutX).with(scaleOutY);
        set.play(scaleInX).with(scaleInY).after(scaleOutX);
        set.setInterpolator(new OvershootInterpolator(2));

        menu1.setIconToggleAnimatorSet(set);
        ***/
    }




    @Override
    public void setPicsSignatureClickListener(OnBoardCustomerActivity.PictruesSignatureClickListen handler) {
        super.setPicsSignatureClickListener(handler);
        fab1.setOnClickListener(handler);
    }

    @Override
    public void setPicturesImageClickListener(OnBoardCustomerActivity.PicturesImageClickListener handler) {
        super.setPicturesImageClickListener(handler);
        fab2.setOnClickListener(handler);
    }





    @Override
    public void setFingerPrinntClickListner(OnBoardCustomerActivity.FingerPrintRequestClickListener handler) {
        super.setFingerPrinntClickListner(handler);

        fab3.setOnClickListener(handler);
    }


    static class RecyclerTouchListener implements RecyclerView.OnItemTouchListener{

        private GestureDetector gestureDetector;
        private ClickListner clickListner;

        public RecyclerTouchListener(Context context, final RecyclerView recyclerView, final ClickListner clickListner){

            this.clickListner=clickListner;

            gestureDetector= new GestureDetector(context,new GestureDetector.SimpleOnGestureListener(){

                @Override
                public boolean onSingleTapUp(MotionEvent e) {

                    View child= recyclerView.findChildViewUnder(e.getX(),e.getY());

                    if(child !=null & clickListner !=null){


                        clickListner.onClick(child, recyclerView.getChildPosition(child));

                    }

                    return super.onSingleTapUp(e);
                }

                @Override
                public void onLongPress(MotionEvent e) {



                    View child= recyclerView.findChildViewUnder(e.getX(),e.getY());

                    if(child !=null & clickListner !=null){


                        clickListner.onLongClick(child,recyclerView.getChildPosition(child));

                    }
                    // super.onLongPress(e);
                }
            });



        }

        @Override
        public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {

            View child= rv.findChildViewUnder(e.getX(),e.getY());

            if(child !=null & clickListner !=null && gestureDetector.onTouchEvent(e) ){


                clickListner.onClick(child, rv.getChildPosition(child));

            }
            return false;
        }

        @Override
        public void onTouchEvent(RecyclerView rv, MotionEvent e) {

        }

        @Override
        public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {

        }
    }
    public static  interface ClickListner{
        public void onClick(View view,int position);
        public void onLongClick(View view,int position);
    }

}
