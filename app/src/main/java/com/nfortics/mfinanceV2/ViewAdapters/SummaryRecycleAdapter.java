package com.nfortics.mfinanceV2.ViewAdapters;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.nfortics.mfinanceV2.Activities.CustomerActivity;
import com.nfortics.mfinanceV2.Application.Application;
import com.nfortics.mfinanceV2.Models.Customer;
import com.nfortics.mfinanceV2.R;
import com.nfortics.mfinanceV2.Typefacer;
import com.nfortics.mfinanceV2.Utilities.Utils;

import java.util.Collections;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by bigfire on 1/21/2016.
 */
public class SummaryRecycleAdapter extends RecyclerView.Adapter<SummaryRecycleViewHolder>{


    private Context context;
    private LayoutInflater inflater;
    public List<String> data= Collections.emptyList();

    public SummaryRecycleAdapter(Context context, List<String> data) {
        inflater= LayoutInflater.from(context);
        this.data=data;
        this.context=context;
    }

    @Override
    public SummaryRecycleViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view= inflater.inflate(R.layout.summary_recycleview_rows,parent,false);

        SummaryRecycleViewHolder holder = new SummaryRecycleViewHolder(view);

        return holder;
    }

    @Override
    public void onBindViewHolder(SummaryRecycleViewHolder holder, int position) {

        String value =data.get(position);


       // holder.txtAllactive.setText(value);
        SetViewsBasedOn(holder,value);
    }

    @Override
    public int getItemCount() {
         return data.size();
    }

    void SetViewsBasedOn(SummaryRecycleViewHolder holder,String value){

        switch (value){

            case "CUSTOMERS" :

                holder.txtAllactive.setText(value);
                holder.txtCustomerAmt.setText(""+Customer.getAllCustomers().size()+"");
                holder.txtcustomerLabel.setText("Customers");

                holder.txtAmount.setText("");
                holder.txtCashHand.setText("");


                int partial= Customer.getAllCustomers("partial").size();
                int none=Customer.getAllCustomers("none").size();
                int failed=Customer.getAllCustomers("failed").size();
                int unsyncedCustomers=partial+none+failed;
                Utils.log("unsynced customers = " + unsyncedCustomers);

                if(unsyncedCustomers>0) {
                    holder.unsyncedlayout.setVisibility(View.VISIBLE);
                    holder.unsyncedItems.setText("" + unsyncedCustomers);
                }  else{
                    holder.unsyncedlayout.setVisibility(View.INVISIBLE);}

             /***   holder.txtAllactive.setText(value);
                holder.txtCustomerAmt.setText(""+Customer.getAllCustomers().size()+"");
                holder.txtAmount.setText("");

                int partial= Customer.getAllCustomers("partial").size();
                int none=Customer.getAllCustomers("none").size();
                int failed=Customer.getAllCustomers("failed").size();
                int unsyncedCustomers=partial+none+failed;
                Utils.log("unsynced customers = " + unsyncedCustomers);***/

              //  holder. txtCashHand.setVisibility(View.INVISIBLE);
              //  holder.butSynced.setVisibility(View.GONE);
           //  if(unsyncedCustomers<1)
            //     holder.butUnsynced.setVisibility(View.INVISIBLE);

              //  holder.butUnsynced.setText("" + unsyncedCustomers);
//


                break;





            case "CASH-OUT" :
                holder.txtAllactive.setText(value);
                holder.txtCustomerAmt.setText("0");
                holder.txtcustomerLabel.setText("Customers");

                holder.txtAmount.setText("GHs 100,000");
                holder.txtCashHand.setText("Withdrawals");

                holder.unsyncedItems.setText("");

                holder.unsyncedlayout.setVisibility(View.INVISIBLE);

                break;



            case "CASH-IN" :
                holder.txtAllactive.setText(value);
                holder.txtCustomerAmt.setText("0");
                holder.txtcustomerLabel.setText("Customers");

                holder.txtAmount.setText("GHs 0.0");
                holder.txtCashHand.setText("Deposits");


                holder.unsyncedItems.setText("");

                holder.unsyncedlayout.setVisibility(View.INVISIBLE);

                break;

            case "All ACTIVITIES" :
                holder.txtAllactive.setText(value);
                holder.txtCustomerAmt.setText("0");
                holder.txtcustomerLabel.setText("Customers");

                holder.txtAmount.setText("GHs 0.0");
                holder.txtCashHand.setText("Cash at Hand");

                holder.unsyncedItems.setText("");

                holder.unsyncedlayout.setVisibility(View.INVISIBLE);

                break;


        }


    }

}




   class SummaryRecycleViewHolder extends RecyclerView.ViewHolder{
    //ImageView productIcon;
    Button butUnsynced;
    Button   butSynced;
    TextView txtAllactive,txtCustomerAmt,txtAmount,txtcustomerLabel,txtCashHand,unsyncedItems;
    CircleImageView profilepics;RelativeLayout mini_parent_layout;
       RelativeLayout unsyncedlayout;
    Typefacer typeface= new Typefacer();
    public SummaryRecycleViewHolder(View itemView) {
        super(itemView);
        txtAllactive =(TextView)itemView.findViewById(R.id.txtAllactive);
        txtAllactive.setTypeface(typeface.squareRegular());
        txtCustomerAmt =(TextView)itemView.findViewById(R.id.txtCustomerAmt);
        txtCustomerAmt.setTypeface(typeface.squareLight());
        txtAmount =(TextView)itemView.findViewById(R.id.txtAmount);
        txtAmount.setTypeface(typeface.squareLight());
        txtcustomerLabel =(TextView)itemView.findViewById(R.id.txtcustomerLabel);
        txtcustomerLabel.setTypeface(typeface.squareMedium());

        txtCashHand=(TextView)itemView.findViewById(R.id.txtCashHand);
        txtCashHand.setTypeface(typeface.squareMedium());

        unsyncedItems=(TextView)itemView.findViewById(R.id.unsyncedItems);
        unsyncedItems.setTypeface(typeface.squareLight());

        unsyncedlayout=(RelativeLayout)itemView.findViewById(R.id.unsyncedlayout);

    }
}