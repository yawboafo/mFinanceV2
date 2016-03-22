package com.nfortics.mfinanceV2.Activities;

import android.content.Context;
import android.net.Uri;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.RelativeLayout;

import com.nfortics.mfinanceV2.Fragments.SettingsPages.ApplicationSettingsFrag;
import com.nfortics.mfinanceV2.Fragments.SettingsPages.ApplicationSettingsFrag.ApplicationSettingsFragInteractionListener;
import com.nfortics.mfinanceV2.Fragments.SettingsPages.GeneralSettingsFrag;
import com.nfortics.mfinanceV2.Fragments.SettingsPages.GeneralSettingsFrag.GeneralSettingsFragInteractionListener;
import com.nfortics.mfinanceV2.Fragments.SettingsPages.ProfileSettingsFrag;
import com.nfortics.mfinanceV2.Fragments.SettingsPages.ProfileSettingsFrag.ProfileSettingsFragInteractionListener;
import com.nfortics.mfinanceV2.Fragments.SummaryBoardFragment;
import com.nfortics.mfinanceV2.Models.Merchant;
import com.nfortics.mfinanceV2.Models.User;
import com.nfortics.mfinanceV2.R;
import com.nfortics.mfinanceV2.Typefacer;
import com.nfortics.mfinanceV2.Utilities.Utils;
import com.nfortics.mfinanceV2.ViewWidgets.SlidingTabLayout;

public class ActivitySetting
        extends
        BaseActivity implements
        ProfileSettingsFragInteractionListener,
        GeneralSettingsFragInteractionListener,
        ApplicationSettingsFragInteractionListener {

    RelativeLayout relativeLayout;
    Typefacer typefacer;

    Utils utils=new Utils();
    View view;

    Merchant merchant;
    User user;
    ViewPager mPager;
    SlidingTabLayout mTabs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        typefacer=new Typefacer();
        getSupportActionBar().setTitle("");
        relativeLayout=(RelativeLayout)findViewById(R.id.compound);

        view=generateActivityView();


        relativeLayout.removeAllViews();
         setPagers(view);
        relativeLayout.addView(view);

        //setContentView(R.layout.settings_activity);
    }

    private View generateActivityView(){

        LayoutInflater layoutInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View activityView = layoutInflater.inflate(R.layout.settings_activity, null, false);

        return activityView;
    }

    private void setPagers(View view){
        mPager = (ViewPager)view.findViewById(R.id.Viewpager);
        mTabs = (SlidingTabLayout)view.findViewById(R.id.tabs);

         mPager.setAdapter(new ViewPagerAdapter(getSupportFragmentManager()));
        mTabs.setViewPager(mPager);
    }

    @Override
    public void ProfileSettingsFragInteraction(Uri uri) {

    }

    @Override
    public void GeneralSettingsFragInteraction(Uri uri) {

    }

    @Override
    public void ApplicationSettingsFragInteraction(Uri uri) {

    }


    /**  class ViewPagerAdapter extends FragmentPagerAdapter {
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

           SummaryBoardFragment summaryBoardFragment = SummaryBoardFragment.newInstance("1","");


            return summaryBoardFragment;
        }


        @Override
        public CharSequence getPageTitle(int position) {
            return tabs[position];
        }

        @Override
        public int getCount() {
            return 3;
        }



    }
***/
   class ViewPagerAdapter extends FragmentPagerAdapter {

        Fragment fragment=null;
      String tabs[];
        public ViewPagerAdapter(FragmentManager fm) {
            super(fm);
            tabs = getResources().getStringArray(R.array.Settingstabs);
        }

        @Override
        public Fragment getItem(int i) {

            if(i==0)
            {

                // actionButton.setVisibility(View.GONE);
              fragment=new ProfileSettingsFrag();

            }     if(i==1)
            {
                // actionButton.setVisibility(View.VISIBLE);
               fragment=new GeneralSettingsFrag();

            }if(i==2)
            {
                // actionButton.setVisibility(View.GONE);
                fragment=new ApplicationSettingsFrag();
                //  new connectOptician().execute();

            }

            return fragment;
        }

        @Override
        public int getCount() {
            return 3;
        }

      @Override
      public CharSequence getPageTitle(int position) {
          return tabs[position];
      }
    }

}
