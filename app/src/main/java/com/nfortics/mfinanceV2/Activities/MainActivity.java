package com.nfortics.mfinanceV2.Activities;


import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Handler;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.OvershootInterpolator;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.ViewFlipper;

import com.nfortics.mfinanceV2.Fragments.HomeFragment;
import com.nfortics.mfinanceV2.Fragments.HomeFragment.OnFragmentInteractionListener;
import com.nfortics.mfinanceV2.Fragments.SummaryBoardFragment;
import com.nfortics.mfinanceV2.NavigationDrawer.NavigationDrawerFragment;
import com.nfortics.mfinanceV2.NavigationDrawer.NavigationItems;
import com.nfortics.mfinanceV2.R;
import com.nfortics.mfinanceV2.Typefacer;
import com.nfortics.mfinanceV2.Utilities.ToastUtil;
import com.nfortics.mfinanceV2.ViewAdapters.ActivityMenusAdapter;
import com.nfortics.mfinanceV2.ViewAdapters.SummaryRecycleAdapter;
import com.nfortics.mfinanceV2.ViewWidgets.SlidingTabLayout;
import com.oguzdev.circularfloatingactionmenu.library.FloatingActionButton;
import com.oguzdev.circularfloatingactionmenu.library.FloatingActionMenu;
import com.oguzdev.circularfloatingactionmenu.library.SubActionButton;

import java.util.ArrayList;
import java.util.List;


public class MainActivity
        extends
        BaseActivity
        implements
        OnFragmentInteractionListener,
        SummaryBoardFragment.
                OnFragmentInteractionListener
{

    private com.github.clans.fab.FloatingActionButton fab1;
    private com.github.clans.fab.FloatingActionButton fab2;
    private com.github.clans.fab.FloatingActionButton fab3;

    private List<com.github.clans.fab.FloatingActionMenu> menus = new ArrayList<>();
    private Handler mUiHandler = new Handler();

    View view;
    ListView listMenu;
    RelativeLayout relativeLayout;
    Typefacer typefacer;

    ViewPager mPager;
    SlidingTabLayout mTabs;
    RecyclerView recyle;
    private Toolbar toolbar;
    private NavigationDrawerFragment drawerFragment;

    SummaryRecycleAdapter summaryRecycleAdapter;


    Button button;
    private ViewFlipper viewFlipper;
    private float lastX;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        typefacer=new Typefacer();

       // relativeLayout=(RelativeLayout)findViewById(R.id.compound);

          InitializeViews(savedInstanceState);
        Log.d("oxinbo", "MainActivty called ");

        //setViewFlipper();
    }


    private View generateActivityView(){

        LayoutInflater layoutInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View activityView = layoutInflater.inflate(R.layout.activity_main, null, false);


        return activityView;
    }

    class ViewPagerAdapter extends FragmentPagerAdapter {
        String tabs[];
        Fragment fragment = null;

        public ViewPagerAdapter(FragmentManager fm) {
            super(fm);
            tabs = getResources().getStringArray(R.array.tabs);
            // Message.message(ctx, "view pager called");
            //fragment=new LastFragment();
        }

        @Override
        public Fragment getItem(int i) {

            SummaryBoardFragment summaryBoardFragment = SummaryBoardFragment.newInstance(getSummaryTitle(i),"");


            return summaryBoardFragment;
        }


        @Override
        public CharSequence getPageTitle(int position) {
            return tabs[position];
        }

        @Override
        public int getCount() {
            return 6;
        }



    }
    public boolean onTouchEvent(MotionEvent touchevent) {

        switch (touchevent.getAction()) {

            case MotionEvent.ACTION_DOWN:
                lastX = touchevent.getX();
                break;
            case MotionEvent.ACTION_UP:
                float currentX = touchevent.getX();

                // Handling left to right screen swap.
                if (lastX < currentX) {

                    // If there aren't any other children, just break.
                    if (viewFlipper.getDisplayedChild() == 0)
                        break;

                    // Next screen comes in from left.
                  viewFlipper.setInAnimation(this, R.anim.slide_in_from_left);
                    //  // Current screen goes out from right.
                  viewFlipper.setOutAnimation(this, R.anim.slide_out_to_right);

                    // Display next screen.
                    viewFlipper.showNext();
                }

                // Handling right to left screen swap.
                if (lastX > currentX) {

                    // If there is a child (to the left), kust break.
                    if (viewFlipper.getDisplayedChild() == 1)
                        break;

                    // Next screen comes in from right.
                   viewFlipper.setInAnimation(this, R.anim.slide_in_from_right);
                    // Current screen goes out from left.
                     viewFlipper.setOutAnimation(this, R.anim.slide_out_to_left);

                    // Display previous screen.
                    viewFlipper.showPrevious();
                }
                break;
        }
        return false;
    }
    ///Methods Regarding View///
    private void InitializeViews(Bundle savedInstanceState){
        setRecycleView();
        //setPagers();
       // createFloatinButton();
       // floatingbutton();
      //  view=generateActivityView();
      //  createlistMenu(view);
       // relativeLayout.addView(view);

    }
/***
    private void createFloatinButton() {
       // final com.github.clans.fab.FloatingActionMenu menu3 = (com.github.clans.fab.FloatingActionMenu) findViewById(R.id.menu3);
        final com.github.clans.fab.FloatingActionMenu menu1 = (com.github.clans.fab.FloatingActionMenu) findViewById(R.id.menu1);

        //  menu1.getMenuIconView().setImageResource(R.drawable.ic_menu);
        fab1 = (com.github.clans.fab.FloatingActionButton) findViewById(R.id.fab1);

        fab2 = (com.github.clans.fab.FloatingActionButton) findViewById(R.id.fab2);
        fab3 = (com.github.clans.fab.FloatingActionButton) findViewById(R.id.fab3);
        fab1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ToastUtil.showToast("I was clicked ",true);
            }
        });
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
                        ? R.drawable.ic_close : R.drawable.ic_menu);
            }
        });

        set.play(scaleOutX).with(scaleOutY);
        set.play(scaleInX).with(scaleInY).after(scaleOutX);
        set.setInterpolator(new OvershootInterpolator(2));

        menu1.setIconToggleAnimatorSet(set);
    }
***/
    private void setPagers(){
     //   mPager = (ViewPager) findViewById(R.id.pager);
        mPager.setAdapter(new ViewPagerAdapter(getSupportFragmentManager()));
        mTabs = (SlidingTabLayout) findViewById(R.id.tabs);

    }
    private void setRecycleView(){

        recyle=(RecyclerView)findViewById(R.id.recyle);
        //recyle.setHasFixedSize(true);

        List<String> values=new ArrayList<>();
        values.add("All ACTIVITIES");
        values.add("DEPOSITS");
        values.add("WITHDRAWALS");
        values.add("MOBILE MONEY");
        values.add("AIRTIME");
        values.add("BILL PAY");
        values.add("CUSTOMERS");
        summaryRecycleAdapter=new SummaryRecycleAdapter(MainActivity.this,values);
        RecyclerView.LayoutManager mlayout=new LinearLayoutManager(MainActivity.this,LinearLayoutManager.HORIZONTAL,false);
        recyle.setLayoutManager(mlayout);
        recyle.setAdapter(summaryRecycleAdapter);

        recyle.addOnItemTouchListener(new RecyclerTouchListener(MainActivity.this, recyle, new ClickListner() {
            @Override
            public void onClick(View view, int position) {
                TextView txtAllactive = (TextView) view.findViewById(R.id.txtAllactive);

                String value = txtAllactive.getText().toString();

                switch (value) {

                    case "CUSTOMERS":

                        Intent intent = new Intent(MainActivity.this, CustomerActivity.class);
                      //  intent.putExtra("name", nameHidden);
                        //intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                        startActivity(intent);

                        break;
                }
            }

            @Override
            public void onLongClick(View view, int position) {

            }
        }));
      //  recyle.addOnItemTouchListener(new RecyclerView.SimpleOnItemTouchListener());
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }

    String getSummaryTitle(int i){


        String title="ACTIVITY SUMMARY";

        switch (i){

            case 0 :
                title="ACTIVITY SUMMARY";
                break;
            case 1:
                title="DEPOSITS";
                break;
            case 2:
                title="WITHDRAWAL";
                break;
            case 3:
                title="CASH-IN";
                break;
            case 4:
                title="CASH-OUT";
                break;
            case 5:
                title="CUSTOMERS";
                break;
        }






        return title;

    }
    void floatingbutton(){


        ImageView imageView=new ImageView(this);
        imageView.setImageResource(R.drawable.add);
        FloatingActionButton actionButton =new FloatingActionButton.Builder(this)
                .setContentView(imageView).build();

        ImageView printActivities =new ImageView(this);
        printActivities.setImageResource(R.drawable.profile_avator);

        ImageView printcommision =new ImageView(this);
        printActivities.setImageResource(R.drawable.prepaid);


        SubActionButton.Builder itembuilder=new SubActionButton.Builder(this);

        SubActionButton printActivitiesButton=itembuilder.setContentView(printActivities).build();

        SubActionButton printcommisionButton=itembuilder.setContentView(printcommision).build();


        FloatingActionMenu actionMenu = new FloatingActionMenu.Builder(this)

                .addSubActionView(printActivitiesButton)
                .addSubActionView(printcommisionButton)
                .attachTo(actionButton)
                .build();


    }

    @Override
    public void onBackPressed() {
        finish();
    }
}
