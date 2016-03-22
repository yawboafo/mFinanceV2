package com.nfortics.mfinanceV2.FormWidgetGenerator;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.OvershootInterpolator;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.nfortics.mfinanceV2.Activities.OnBoardCustomerActivity;
import com.nfortics.mfinanceV2.Application.Application;
import com.nfortics.mfinanceV2.R;
import com.nfortics.mfinanceV2.Typefacer;
import com.nfortics.mfinanceV2.Utilities.ToastUtil;
import com.nfortics.mfinanceV2.ViewAdapters.GalleryImagesAdapterNew;

/**
 * Created by bigfire on 12/4/2015.
 */
public class FormPictures  extends FormWidget {


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
    public FormPictures(Context context, String name, String tag) {
        super(context, name, tag);

        inflator = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        linearLayout=(LinearLayout)inflator.inflate(R.layout.linear_layout_style, null);
        Typefacer typefacer=new Typefacer();

        relativeLayout=(RelativeLayout)inflator.inflate(R.layout.image_taking_bitmap, null);
        relativeLayout.setTag("picturesLayout");
        recyclerView=(RecyclerView)relativeLayout.findViewById(R.id.viewRecycle);

        RecyclerView.LayoutManager mlyout2=new GridLayoutManager(context,3,GridLayoutManager.VERTICAL,false);
        galleryImagesAdapterNew=new GalleryImagesAdapterNew(context, Application.getGalleryImages());
        recyclerView.setAdapter(galleryImagesAdapterNew);
        recyclerView.setLayoutManager(mlyout2);

       // createFloatinButton(relativeLayout);



        recyclerView.setOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (Math.abs(dy) > mScrollOffset) {
                    if (dy > 0) {
                        menu1.hideMenu(true);
                    } else {
                        menu1.showMenu(true);
                    }
                }
            }
        });

      //  linearLayout.addView(relativeLayout);

        _layout.addView(relativeLayout);
    }


    private void createFloatinButton(View view) {
        // final com.github.clans.fab.FloatingActionMenu menu3 = (com.github.clans.fab.FloatingActionMenu) findViewById(R.id.menu3);
         //menu1 = (com.github.clans.fab.FloatingActionMenu)view. findViewById(R.id.menu3);

        //  menu1.getMenuIconView().setImageResource(R.drawable.ic_menu);
        fab1 = (com.github.clans.fab.FloatingActionButton)view. findViewById(R.id.fab1);
        fab1.setLabelText("Signature");

        fab2 = (com.github.clans.fab.FloatingActionButton)view. findViewById(R.id.fab2);
        fab2.setLabelText("Pictures");
        fab3 = (com.github.clans.fab.FloatingActionButton)view. findViewById(R.id.fab3);
        fab3.setLabelText("FingerPrint");

        AnimatorSet set = new AnimatorSet();

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
}
