package com.nfortics.mfinanceV2.Activities;

import android.app.Activity;
import android.app.ActivityOptions;
import android.app.AlertDialog;
import android.app.Application;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.GestureDetector;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewFlipper;

import com.cengalabs.flatui.FlatUI;
import com.nfortics.mfinanceV2.Activities.MenuActivities.CustomersMenuActivity;
import com.nfortics.mfinanceV2.Activities.MenuActivities.FieldBankingMenuActivity;
import com.nfortics.mfinanceV2.Activities.MenuActivities.FieldCollectionMenuActivity;
import com.nfortics.mfinanceV2.Activities.MenuActivities.MobileVasActivityMenu;
import com.nfortics.mfinanceV2.Activities.MenuActivities.RiskIQActivityMenu;
import com.nfortics.mfinanceV2.Dialogs.BalanceDialog;
import com.nfortics.mfinanceV2.Dialogs.BalanceDialog.BalanceDialogInteractionListener;
import com.nfortics.mfinanceV2.Models.Favorites;
import com.nfortics.mfinanceV2.Models.Merchant;
import com.nfortics.mfinanceV2.Models.User;
import com.nfortics.mfinanceV2.NavigationDrawer.NavigationDrawerFragment;
import com.nfortics.mfinanceV2.NavigationDrawer.NavigationItems;
import com.nfortics.mfinanceV2.R;
import com.nfortics.mfinanceV2.Settings.GeneralSettings;
import com.nfortics.mfinanceV2.Settings.Settings;
import com.nfortics.mfinanceV2.Typefacer;
import com.nfortics.mfinanceV2.Utilities.ToastUtil;
import com.nfortics.mfinanceV2.ViewAdapters.ActivityMenusAdapter;
import com.nfortics.mfinanceV2.ViewAdapters.FavoritesListAdapter;
import com.nfortics.mfinanceV2.ViewAdapters.MenuListAdapters;
import com.nfortics.mfinanceV2.ViewWidgets.SlidingTabLayout;
import com.oguzdev.circularfloatingactionmenu.library.FloatingActionButton;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by bigfire on 10/22/2015.
 */
public abstract class
        BaseActivity extends
        ActionBarActivity
        implements
        NavigationDrawerFragment.
                getNavigationDrawerClicks,
        BalanceDialogInteractionListener{
    View view;
    ListView listMenu;
    RelativeLayout relativeLayout;
     public static int count=0;
    ViewPager mPager;
    SlidingTabLayout mTabs;
    private Toolbar toolbar;
    TextView toolbarTitle;
    private NavigationDrawerFragment drawerFragment;
   GeneralSettings generalSettings=GeneralSettings.getInstance();
    Typefacer typefacer=new Typefacer();
    Merchant merchant;
    FavoritesListAdapter menusAdapter;
    User user;

    Button button;
    private ViewFlipper viewFlipper;
    private float lastX;
    Menu Alphamenu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

       try{

      user=User.load(User.class, 1);
       if(user==null){

        user= com.nfortics.mfinanceV2.Application.Application.getActiveAgent();

        if(user==null){

            user=User.load(User.class,1);
        }
    }



    merchant=Merchant.getActiveMerchant("true");
    if(merchant==null){
        showMerchantsDialog();

    }








    Log.d("oxinbo", "Basactivity called ");
    setToolBar();
    setNavigationDrawer();
           createlistMenu();
    //setViewFlipper();
           //floatingbutton();
    }catch (Exception e){

    e.printStackTrace();

    }

       // BroadCastReciever();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        Alphamenu=menu;
        MenuItem menuItem = menu.findItem(R.id.testAction);
        menuItem.setIcon(buildCounterDrawable(count, R.drawable.emailback));


        if(count<=0)
        {
            menuItem.setVisible(false);

        }else{
            menuItem.setVisible(true);
        }
        return true;
    }


    void NotifyMenuItem(){

        MenuItem menuItem = Alphamenu.findItem(R.id.testAction);
        menuItem.setIcon(buildCounterDrawable(count, R.drawable.emailback));


        if(count<=0)
        {
            menuItem.setVisible(false);

        }else{
            menuItem.setVisible(true);
        }
    }

    private Drawable buildCounterDrawable(int count, int backgroundImageId) {
        LayoutInflater inflater = LayoutInflater.from(this);
        View view = inflater.inflate(R.layout.counter_menuitem_layout, null);
        view.setBackgroundResource(backgroundImageId);

        if (count == 0) {
            View counterTextPanel = view.findViewById(R.id.counterValuePanel);
            counterTextPanel.setVisibility(View.GONE);
        } else {
            TextView textView = (TextView) view.findViewById(R.id.count);
            textView.setText("" + count);
        }

        view.measure(
                View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED),
                View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));
        view.layout(0, 0, view.getMeasuredWidth(), view.getMeasuredHeight());

        view.setDrawingCacheEnabled(true);
        view.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);
        Bitmap bitmap = Bitmap.createBitmap(view.getDrawingCache());
        view.setDrawingCacheEnabled(false);

        return new BitmapDrawable(getResources(), bitmap);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.testAction) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
    void setActiveMerchant(String merchant){

     try{
    for(Merchant merc:Merchant.getAll()){

        if(merc.getName().equalsIgnoreCase(merchant)){

            merc.setIsActive("true");
            merc.save();
        }else{

            merc.setIsActive("false");
            merc.save();
        }
    }
   }
     catch (Exception e){

    e.printStackTrace();
       }


       // Merchant merchant1=Merchant.getMerchantByName(merchant);

    }
    private void setToolBar(){
        toolbar=(Toolbar)findViewById(R.id.app_bar);
        toolbarTitle= (TextView) findViewById(R.id.toolbarTitle);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("");

        if(merchant!=null){


            toolbarTitle.setText(merchant.getName());

            toolbarTitle.setTypeface(typefacer.squareLight());


        }

        toolbarTitle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(com.nfortics.mfinanceV2.Application.Application.getCurrentActivityState().equalsIgnoreCase("Application")){

                    showMerchantsDialog();
                }else{

                    Intent myIntent=new Intent(BaseActivity.this,MainActivity.class);

                    startActivity(myIntent);
                    overridePendingTransition(R.anim.activity_animation, R.anim.activity_animation2);
                    com.nfortics.mfinanceV2.Application.Application.setCurrentActivityState("Application");
                }

            Log.d("oxinbo", "current Activity " + com.nfortics.mfinanceV2.Application.Application.getCurrentActivityState());
                //


            }
        });

    }
    public String[] getMerchantsAsArray() {

        final List<Merchant> merchantList = Merchant.getAll();
        String[] object = new String[merchantList.size()];
        for (int i = 0; i < merchantList.size(); i++) {
            object[i] = merchantList.get(i).getName();
            Log.d("oxinbo","object "+ object[i]);
        }

        return object;
    }
    public void saveSettingsInstance(Settings settingsInstance, Class clazz) {
        try {
            settingsInstance.saveInstance(openFileOutput(clazz.getName(), 0));
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

   // 233207711598




    void BroadCastReciever(){

        BroadcastReceiver receiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                //String someValue = intent.getStringExtra("value");
                count++;
               invalidateOptionsMenu();
               /// new LoadRecycleView(view).execute();
                // ... do something ...
            }
        };
        LocalBroadcastManager.getInstance(this)
                .registerReceiver(receiver, new IntentFilter("myBroadcastIntent"));
    }


    private ListView createlistMenu(){
        getSupportActionBar().setTitle(" ");
        listMenu =(ListView)findViewById(R.id.favItemsList);
         menusAdapter=new FavoritesListAdapter(this,_getData());
        listMenu.setAdapter(menusAdapter);

        listMenu.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {

                try{
                    TextView txtTitle = (TextView) view.findViewById(R.id.tvTitle);

                    String txtTitleString=txtTitle.getText().toString();
                    Favorites  favorites;
                    favorites= Favorites.getFavorites(txtTitleString);

                    if(favorites!=null){
                        Long intt = favorites.getId();
                        Favorites.delete(Favorites.class, intt);


                        ToastUtil.showToast("" + txtTitleString + " Removed from Favorites ",false);


                        menusAdapter=new FavoritesListAdapter(BaseActivity.this,_getData());
                        listMenu.setAdapter(menusAdapter);


                    }


                }catch (Exception e){

                    e.printStackTrace();
                }



                return false;
            }
        });

        return listMenu;
    }

    private String[]menuTitles(){

        String[] menuTitles = new String[]{
                "New Customer",
                "Search Customer",
                "New Customer",
                "Search Customer",
                "New Customer",
                "Search Customer"
        };


        List<Favorites> fav=Favorites.getAllFavorites();
        String[] stockArr = new String[fav.size()];
        stockArr = fav.toArray(stockArr);

        return stockArr;
    }

    public static List<Favorites> _getData() {


        List<Favorites> fabulous=new ArrayList<>();

        for(Favorites fav:Favorites.getAllFavorites()){
            Favorites fas=new Favorites();
            fas.setClassname(fav.getClassname());

            Log.d("oxinbo", "fab name =" + fav.getClassname());
            fabulous.add(fas);
        }

        return fabulous;

    }


    void floatingbutton(){


        ImageView imageView=new ImageView(this);
        imageView.setImageResource(R.drawable.plus);
        FloatingActionButton actionButton =new FloatingActionButton.Builder(this)
                .setContentView(imageView).build();

    }

    private void showMerchantsDialog() {
        final CharSequence[] items = getMerchantsAsArray();
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Set Merchant");
        builder.setItems(items, new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
               // userInfoSettings.setCurrentMerchant(userInfoSettings.getMerchants().get(which));

              // String selectedItem= ((AlertDialog)dialog).getListView().getSelectedItem().toString();
                Log.d("oxinbo","selected merchant "+items[which]);

                try{


                    String selectedMerchant=items[which].toString();

                    setActiveMerchant(selectedMerchant);

                    generalSettings.setActivemerchant(Merchant.getMerchantByName(selectedMerchant));
                    toolbarTitle.setText(generalSettings.getActivemerchant().getName());
                    saveSettingsInstance(generalSettings,GeneralSettings.class);
                }catch(Exception e){



                }

               // setMerchant();
               // saveSettingsInstance(userInfoSettings, USerInfoSettings.class);
                builder.create().dismiss();
            }
        });
        AlertDialog createOptions = builder.create();
        createOptions.show();
    }
    private void setNavigationDrawer(){


        drawerFragment = (NavigationDrawerFragment)
                getSupportFragmentManager().findFragmentById(R.id.fragment_navigation_drawer);

        drawerFragment.setUp(R.id.fragment_navigation_drawer, (DrawerLayout) findViewById(R.id.drawer_layout), toolbar);
        // drawerFragment.setDrawerListener(this);


    }

    @Override
    public void navigate(int position) {
        Intent intent;
        Bundle bndlanimation =
                ActivityOptions.makeCustomAnimation(getApplicationContext(), R.anim.activity_animation, R.anim.activity_animation2).toBundle();
        switch (position){

            case 0:


                com.nfortics.mfinanceV2.Application.Application.setCurrentActivityState("CustomerActivity");
                intent = new Intent(this, CustomerActivity.class);

                intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                startActivity(intent,bndlanimation);


                break;
            case 1:
                com.nfortics.mfinanceV2.Application.Application.setCurrentActivityState("FieldCollectionMenuActivity");
                intent = new Intent(this, FieldCollectionMenuActivity.class);

                intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                startActivity(intent,bndlanimation);
                // finish();



              //  finish();
                break;
            case 2:
                com.nfortics.mfinanceV2.Application.Application.setCurrentActivityState("FieldBankingMenuActivity");
                intent = new Intent(this, FieldBankingMenuActivity.class);

                intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                startActivity(intent,bndlanimation);
              //  finish();
                break;
            case 3:
                com.nfortics.mfinanceV2.Application.Application.setCurrentActivityState("MobileVasActivityMenu");
                intent = new Intent(this, MobileVasActivityMenu.class);

                intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                startActivity(intent,bndlanimation);
                //  finish();
                break;
            case 4:
                com.nfortics.mfinanceV2.Application.Application.setCurrentActivityState("RiskIQActivityMenu");
                intent = new Intent(this, RiskIQActivityMenu.class);

                intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                startActivity(intent,bndlanimation);
                //  finish();
                break;

            case 6:
                com.nfortics.mfinanceV2.Application.Application.setCurrentActivityState("ActivitySetting");
                intent = new Intent(this, ActivitySetting.class);

                intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                startActivity(intent,bndlanimation);
                //  finish();
                break;
        }
    }

    @Override
    public void navigate1(int position) {

    }


    @Override
    public void BalanceDialogInteraction(String uri) {

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
