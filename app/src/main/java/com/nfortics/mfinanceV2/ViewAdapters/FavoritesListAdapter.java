package com.nfortics.mfinanceV2.ViewAdapters;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.nfortics.mfinanceV2.Models.Favorites;
import com.nfortics.mfinanceV2.NavigationDrawer.NavigationItems;
import com.nfortics.mfinanceV2.R;
import com.nfortics.mfinanceV2.Typefacer;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * Created by bigfire on 11/24/2015.
 */
public class FavoritesListAdapter extends ArrayAdapter<Favorites> {

    private  Activity context;

    private  List<Favorites> itemname= Collections.emptyList();


    public FavoritesListAdapter(Activity context, List<Favorites> itemname) {
        super(context, R.layout.row_menu_items1, itemname);
        // TODO Auto-generated constructor stub

        this.context = context;
        this.itemname = itemname;

    }

    public View getView(int position, View view, ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        Typefacer typefacer = new Typefacer();

        Favorites items = itemname.get(position);
        View rowView = inflater.inflate(R.layout.row_menu_items1, null, true);



        TextView txtTitle = (TextView) rowView.findViewById(R.id.tvTitle);
        ImageView icon = (ImageView) rowView.findViewById(R.id.icon);
        ImageView imageview2 = (ImageView) rowView.findViewById(R.id.imageView2);
        txtTitle.setTypeface(typefacer.squareRegular());
        txtTitle.setTextColor(context.getResources().getColor(R.color.primary));

        if(this.itemname.isEmpty()){

            txtTitle.setText("Long Click On Sub Menus to Add as Favorites");
            icon.setVisibility(View.GONE);
            imageview2.setVisibility(View.GONE);
        }else{


            txtTitle.setText(items.getClassname());


            int resId=0;
            switch (items.getClassname()){

                case "Collect Cash":

                    resId=R.drawable.cashcollect;
                    break;
                case "Collect Customer Feedback":

                    resId=R.drawable.feedback;
                    break;

                case "Balance Enquiry":

                    resId= R.drawable.balance;
                    break;

                case "Field Surveys":

                    resId=R.drawable.survey;
                    break;

                case "Withdrawal":

                    resId=R.drawable.withdrawing;
                    break;
                case "Statement":

                    resId=R.drawable.statement;
                    break;

                case "Loan Application":

                    resId=R.drawable.loanapp;
                    break;

                case "Internal Transfer":

                    resId=R.drawable.internaltran;
                    break;
                case "Interbank Transfer":

                    resId=R.drawable.moneytrans;
                    break;
                case "Money Transfer":

                    resId=R.drawable.interbank;
                    break;
                case "Mobile Money":

                    resId=R.drawable.mobilemoney;
                    break;

                case "Bill Payment":
                    resId=R.drawable.billpay;
                    break;

                case "Airtime":
                    resId=R.drawable.airtime;
                    break;

                case "Prepaid Cards":
                    resId=R.drawable.prepaid;
                    break;

                case "Identity Checks":
                    resId=  R.drawable.identitycheck;
                    break;

                case "Credit Checks":
                    resId=R.drawable.verify;
                    break;





            }
            icon.setVisibility(View.VISIBLE);
            imageview2.setVisibility(View.VISIBLE);
            icon.setImageResource(resId);


        }




        return rowView;

    }
}