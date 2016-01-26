package com.nfortics.mfinanceV2.NavigationDrawer;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.app.ActivityOptions;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.OvershootInterpolator;
import android.view.animation.RotateAnimation;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.nfortics.mfinanceV2.Activities.CustomerActivity;
import com.nfortics.mfinanceV2.Activities.LoginActivity;
import com.nfortics.mfinanceV2.Activities.MenuActivities.FieldCollectionMenuActivity;
import com.nfortics.mfinanceV2.Activities.SettingsActivity;
import com.nfortics.mfinanceV2.Application.Application;
import com.nfortics.mfinanceV2.Models.User;
import com.nfortics.mfinanceV2.R;
import com.nfortics.mfinanceV2.Typefacer;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by bigfire on 10/15/2015.
 */
public class NavigationDrawerFragment   extends Fragment {

    private RecyclerView recyclerView,recyclerView1;

    ImageView settingButton;
    public static final String PREF_FILE_NAME="testpref";
    public static final String KEY_USER_LEARNED_DRAWER="user_learned_drawer";
    private ActionBarDrawerToggle mDrawerToggle;
    private DrawerLayout mDrawerLayout;
    private View containerview;
    private boolean mUserLearnedDrawer;
    private boolean mFromSavedInstanceState;
    private NavigationItemsAdapter adapter,adapter1;
    Bundle extras;
    Typefacer typefacer;
    getNavigationDrawerClicks getnavigationDrawerClicks;
    TextView
            logoutTxtBox,
            listOneTitle,
            listTwoTitle,
            listSettingsTtitle,
            TxtUsername,
            TxtUserid;

        User user;
    CircleImageView profileimg;
    Handler handler = new Handler();
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        getnavigationDrawerClicks=(getNavigationDrawerClicks)context;

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
         typefacer =new Typefacer();
        mUserLearnedDrawer=Boolean.valueOf(readFromPreferences(getActivity(),KEY_USER_LEARNED_DRAWER,"false"));

        if(savedInstanceState!=null){

            mFromSavedInstanceState=true;
        }

        try{

            user=Application.getActiveAgent();

            if(user==null){

                user=User.load(User.class,1);
            }


        }catch (Exception c){


            c.printStackTrace();

        }
    }


    void ObjectAnimator(){
        RotateAnimation rotate = new RotateAnimation(0, 180, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);

//Setup anim with desired properties
        rotate.setInterpolator(new LinearInterpolator());
        //anim.setRepeatCount(Animation.ZORDER_BOTTOM); //Repeat animation indefinitely
        rotate.setDuration(700); //Put desired duration per anim cycle here, in milliseconds


        profileimg.startAnimation(rotate);





    }


    void timer(){

        final Handler handler = new Handler();
        final Runnable r = new Runnable()
        {
            public void run()
            {
                SignOut();
            }
        };
        handler.postDelayed(r, 600);
    }

    public void SignOut()
    {

        getActivity().finish();



        Intent intent = new Intent(getActivity(), LoginActivity.class);

        intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
        startActivity(intent);


    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View layout=inflater.inflate(R.layout.navigation_drawer1, container, false);


        try {

            recyclerView = (RecyclerView) layout.findViewById(R.id.drawerlistOne);


            //logoutTxtBox=(TextView)layout.findViewById(R.id.txtLogout);
            //logoutTxtBox.setTypeface(typefacer.getRoboRealThin(getActivity().getAssets()));


            /**  settingButton=(ImageView)layout.findViewById(R.id.settingButton);
             settingButton.setOnClickListener(new View.OnClickListener() {
            @Override
                public void onClick(View v) {

            //  Bundle bndlanimation = ActivityOptions.makeCustomAnimation(getActivity(), R.anim.activity_animation, R.anim.activity_animation2).toBundle();
                    Intent  intent = new Intent(getActivity(), SettingsActivity.class);
                    startActivity(intent);
                }
            });**/

            profileimg=(CircleImageView)layout.findViewById(R.id.profileimg);
            profileimg.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    timer();
                  //  RotateAnimation rotate = new RotateAnimation(0, 180, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
                  //  rotate.setDuration(700);
                   // rotate.setInterpolator(new LinearInterpolator());
                    //profileimg.startAnimation(rotate);
                    ObjectAnimator();
                }
            });
            TxtUsername=(TextView)layout.findViewById(R.id.txtUsername);
            TxtUsername.setTypeface(typefacer.getRoboCondensedLight(getActivity().getAssets()));
            TxtUserid=(TextView)layout.findViewById(R.id.txtUserid);
            TxtUserid.setTypeface(typefacer.getRoboRegular(getActivity().getAssets()));
            TxtUsername.setText(user.getFirstName());
            adapter=new NavigationItemsAdapter(getActivity(),getData());


            recyclerView.setAdapter(adapter);


            recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));



            recyclerView.addOnItemTouchListener(new RecyclerTouchListener(getActivity(), recyclerView, new ClickListener() {
                @Override
                public void onClick(View view, int position) {

                    getnavigationDrawerClicks.navigate(position);
                    mDrawerLayout.closeDrawer(containerview);
                }

                @Override
                public void onLongClick(View view, int position) {

                }
            }));

        }catch(Exception e){

            e.printStackTrace();
        }


        return layout;
    }

    private static String readFromPreferences(Context context,String preferenceName,String defaultValue){

        SharedPreferences sharedpreference=context.getSharedPreferences(PREF_FILE_NAME, Context.MODE_PRIVATE);
        return sharedpreference.getString(preferenceName,defaultValue);

    }
    public void setUp(int fag,DrawerLayout drawerlayout, final Toolbar toolbar) {

    containerview=getActivity().findViewById(fag);
    mDrawerLayout=drawerlayout;
    mDrawerToggle= new ActionBarDrawerToggle(getActivity(),drawerlayout,toolbar, R.string.drawer_open,R.string.drawer_close){

        @Override
        public void onDrawerOpened(View drawerView) {
            super.onDrawerOpened(drawerView);
            getActivity().invalidateOptionsMenu();


            /**    if(!mUserLearnedDrawer){

             mUserLearnedDrawer=true;
             saveToPreferences(getActivity(),KEY_USER_LEARNED_DRAWER,mUserLearnedDrawer+"Close");

             }**/

        }

        @Override
        public void onDrawerClosed(View drawerView) {
            super.onDrawerClosed(drawerView);
            getActivity().invalidateOptionsMenu();
        }

        @Override
        public void onDrawerSlide(View drawerView, float slideOffset) {
            super.onDrawerSlide(drawerView, slideOffset);
            // if(slideOffset<0.6){

            toolbar.setAlpha(1-slideOffset/2);

            // }

            // toolbar.setAlpha(slideOffset);
        }
    };
    //  if(!mUserLearnedDrawer && !mFromSavedInstanceState){

    //  mDrawerLayout.openDrawer(containerview);
    // }


    mDrawerLayout.setDrawerListener(mDrawerToggle);
    mDrawerLayout.post(new Runnable() {
        @Override
        public void run() {

            mDrawerToggle.syncState();
        }
    });

}




    public static List<NavigationItems> getData(){

        List<NavigationItems> data=new ArrayList<>();

        String[] Titles={
                "CUSTOMERS",
                "COLLECTION",
                "BANKING",
                "MOBILE VAS",
                "RISK IQ",
                "CUSTOM APPS",
                "SETTINGS"};

       /** Integer[] Icons={
                R.drawable.customer,  R.drawable.collect,  R.drawable.bank,  R.drawable.mobilevas,  R.drawable.customapps,  R.drawable.settins};**/

        Integer[] Icons={
                R.drawable.customer,
                R.drawable.collect,
                R.drawable.bank,
                R.drawable.mobilevas,
                R.drawable.riskiq,
                R.drawable.customapps,
                R.drawable.settins};

        for(int t=0;t<Titles.length;t++){

            NavigationItems current = new NavigationItems();
            current.setTitle(Titles[t]);
            current.setIconid(Icons[t]);
            data.add(current);
        }


        return data;
    }


    //InterFaces Methods
    public static interface getNavigationDrawerClicks{

        public void navigate(int position);
        public void navigate1(int position);
    }
    public static interface ClickListener{

        public void onClick(View view,int position);
        public void onLongClick(View view,int position);

    }



    //Inner Classes
    static class RecyclerTouchListener implements RecyclerView.OnItemTouchListener {
        private GestureDetector gestureDetector;
        private  ClickListener clickListener;

        public RecyclerTouchListener(Context context, final RecyclerView recyclerView, final ClickListener clickListener){
            this.clickListener=clickListener;
            gestureDetector=new GestureDetector(context,new GestureDetector.SimpleOnGestureListener(){

                @Override
                public boolean onSingleTapUp(MotionEvent e) {
                    return true;
                }

                @Override
                public void onLongPress(MotionEvent e) {
                    View child= recyclerView.findChildViewUnder(e.getX(),e.getY());
                    if(child!=null && clickListener!=null){


                        clickListener.onLongClick(child,recyclerView.getChildPosition(child));
                    }

                }
            });
        }
        @Override
        public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {
            View child= rv.findChildViewUnder(e.getX(),e.getY());
            if(child!=null && clickListener!=null && gestureDetector.onTouchEvent(e)){


                clickListener.onClick(child, rv.getChildPosition(child));
            }
            return false;
        }

        @Override
        public void onTouchEvent(RecyclerView rv, MotionEvent e) {


            return;


        }

        @Override
        public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {

        }
    }

}
