package com.nfortics.mfinanceV2.ViewAdapters;


import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.nfortics.mfinanceV2.Models.Account;
import com.nfortics.mfinanceV2.R;
import com.nfortics.mfinanceV2.Typefacer;

import java.util.List;

/**
 * Created by bigfire on 11/17/2015.
 */
public class CustomerAccountsListAdapter extends ArrayAdapter<Account> {

    private final Activity context;

    private final List<Account> itemname;


    public CustomerAccountsListAdapter(Activity context, List<Account> itemname) {
        super(context, R.layout.customers_accounts_row, itemname);
        // TODO Auto-generated constructor stub

        this.context = context;
        this.itemname = itemname;

    }



    public View getView(int position, View view, ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        Typefacer typefacer = new Typefacer();
        View rowView = inflater.inflate(R.layout.customers_accounts_row, null, true);


        Account account=itemname.get(position);
        TextView accountDesc = (TextView) rowView.findViewById(R.id.accountDesc);
        TextView accountNumber = (TextView) rowView.findViewById(R.id.accountNumber);

        accountDesc.setText(account.getDescription());
        accountNumber.setText(account.getAccount_number());
        accountDesc.setTextColor(context.getResources().getColor(R.color.primary));
        accountDesc.setTypeface(typefacer.squareRegular());


        accountNumber.setTypeface(typefacer.squareLight());

        // txtTitle.setTextColor(0x0000FF);





        return rowView;

    }

}
