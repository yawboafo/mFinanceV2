package com.nfortics.mfinanceV2.Activities.MenuActivities;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.nfortics.mfinanceV2.Activities.BalancenquiryActivity;
import com.nfortics.mfinanceV2.Activities.BaseActivity;
import com.nfortics.mfinanceV2.Activities.MainActivity;
import com.nfortics.mfinanceV2.Models.Favorites;
import com.nfortics.mfinanceV2.NavigationDrawer.NavigationItems;
import com.nfortics.mfinanceV2.R;
import com.nfortics.mfinanceV2.Typefacer;
import com.nfortics.mfinanceV2.Utilities.ToastUtil;
import com.nfortics.mfinanceV2.ViewAdapters.ActivityMenusAdapter;

import java.util.ArrayList;
import java.util.List;

public class RiskIQActivityMenu extends BaseActivity{


    View view;
    ListView listMenu;
    RelativeLayout relativeLayout;
    Typefacer typefacer;
    private TextView txtTitleField,txtDiscripFieldC;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        typefacer=new Typefacer();

        relativeLayout=(RelativeLayout)findViewById(R.id.compound);


        view=generateActivityView();
        createlistMenu(view);
        SetLabels(view);
        relativeLayout.removeAllViews();
        relativeLayout.addView(view);
    }

    private View generateActivityView(){

        LayoutInflater layoutInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View activityView = layoutInflater.inflate(R.layout.activity_risk_iqactivity_menu, null, false);


        return activityView;
    }
    private ListView createlistMenu(View view){
        getSupportActionBar().setTitle(" ");
        listMenu =(ListView)view.findViewById(R.id.menuList);
        ActivityMenusAdapter menusAdapter=new ActivityMenusAdapter(this,getData());
        listMenu.setAdapter(menusAdapter);
        listMenu.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent;
                switch (position) {
                    case 0:
                        Intent intents = null;
                        intents = new Intent(RiskIQActivityMenu.this, BalancenquiryActivity.class);

                        startActivity(intents);
                        overridePendingTransition(R.anim.activity_animation, R.anim.activity_animation2);


                        break;
                    case 3:

                        break;

                }
            }
        });
        listMenu.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {


                try {
                    TextView txtTitle = (TextView) view.findViewById(R.id.tvTitle);

                    String txtTitleString = txtTitle.getText().toString();
                    Favorites favorites;
                    favorites = Favorites.getFavorites(txtTitleString);

                    if (favorites == null) {
                        favorites = new Favorites();
                        favorites.setClassname(txtTitleString);
                        favorites.save();


                        ToastUtil.showToast("" + txtTitleString + " Added to Favorites ",true);


                    } else {

                        Long intt = favorites.getId();
                        Favorites.delete(Favorites.class, intt);


                        ToastUtil.showToast("" + txtTitleString + " Removed from Favorites ",false);


                    }


                } catch (Exception e) {

                    e.printStackTrace();

                }

                return true;
            }
        });


        return listMenu;
    }
    private void SetLabels(View view){
        txtTitleField=(TextView)view.findViewById(R.id.txtTitleField);
        txtTitleField.setTypeface(typefacer.squareRegular());
        txtTitleField.setText("Risk IQ");
        txtDiscripFieldC=(TextView)view.findViewById(R.id.txtDiscripFieldC);
        txtDiscripFieldC.setTypeface(typefacer.squareLight());
        txtDiscripFieldC.setText("Consumer Risk Intelligence");

    }



    private String[]menuTitles(){

        String[] menuTitles = new String[]{
                "Balance Enquiry",
                "Loan Application",
                "Funds Transfer",
                "Balance Enquiry",
                "Statement Request",
                "Credit Check"

        };

        return menuTitles;
    }

    public static List<NavigationItems> getData() {

        String[] Titles={
                "Identity Checks",
                "Credit Checks"};

        Integer[] menuIcons = {
                R.drawable.identitycheck,
                R.drawable.verify

        };

        List<NavigationItems> data = new ArrayList<>();

        for(int t=0;t<Titles.length;t++){

            NavigationItems current = new NavigationItems();
            current.setTitle(Titles[t]);
            current.setIconid(menuIcons[t]);
            data.add(current);
        }

        return data;

    }


}
