package com.nfortics.mfinanceV2.Activities.MenuActivities;

import android.app.ActivityOptions;
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
import android.widget.Toast;

import com.nfortics.mfinanceV2.Activities.BaseActivity;
import com.nfortics.mfinanceV2.Activities.CustomerActivity;
import com.nfortics.mfinanceV2.Activities.MainActivity;
import com.nfortics.mfinanceV2.Activities.OnBoardCustomerActivity;
import com.nfortics.mfinanceV2.FormWidgetGenerator.FormWidgetModel;
import com.nfortics.mfinanceV2.R;
import com.nfortics.mfinanceV2.ViewAdapters.CustomListAdapter;

public class CustomersMenuActivity extends BaseActivity {

        View view;
        ListView listMenu;
        RelativeLayout relativeLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
       super.onCreate(savedInstanceState);


        relativeLayout=(RelativeLayout)findViewById(R.id.compound);


        view=generateActivityView();
        createlistMenu(view);
        relativeLayout.removeAllViews();
        relativeLayout.addView(view);
    }



    private View generateActivityView(){

        LayoutInflater layoutInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View activityView = layoutInflater.inflate(R.layout.activity_customers_menu, null, false);


        return activityView;
    }
    private ListView createlistMenu(View view){
        getSupportActionBar().setTitle("Customer ");
        listMenu =(ListView)view.findViewById(R.id.menuList);
        CustomListAdapter adapter=new CustomListAdapter(this, menuTitles(),menuTitlesDescription(), ImageIDS());
        listMenu.setAdapter(adapter);
        listMenu.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent;
                Bundle bndlanimation =
                        ActivityOptions.makeCustomAnimation(getApplicationContext(), R.anim.activity_animation, R.anim.activity_animation2).toBundle();
                switch (position) {
                    case 0:
                        intent = new Intent(CustomersMenuActivity.this, OnBoardCustomerActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                        startActivity(intent, bndlanimation);

                        break;
                    case 1:

                        break;

                }
            }
        });


        listMenu.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {

                TextView txtTitle = (TextView) view.findViewById(R.id.tvTitle);


                Toast.makeText(CustomersMenuActivity.this,"hello world = "+txtTitle.getText().toString()
                        ,Toast.LENGTH_LONG).show();
                return true;
            }
        });

        return listMenu;
    }

    private Integer[]ImageIDS(){

        Integer[] imgid={

                R.drawable.search

        };

        return imgid;
    }

    private String[]menuTitles(){

        String[] menuTitles = new String[]{
                "New Customer",
                "Search Customer"
        };

        return menuTitles;
    }
    private String[]menuTitlesDescription(){

        String[] menuTitles = new String[]{
                "Register new customers",
                "Search for Existing Customers"
        };

        return menuTitles;
    }



}
