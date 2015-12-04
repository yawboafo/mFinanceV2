package com.nfortics.mfinanceV2.ViewAdapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.activeandroid.util.Log;
import com.nfortics.mfinanceV2.Application.Application;
import com.nfortics.mfinanceV2.Models.Customer;
import com.nfortics.mfinanceV2.R;
import com.nfortics.mfinanceV2.Typefacer;
import com.nfortics.mfinanceV2.Utilities.Utils;

import java.util.Collections;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by bigfire on 11/17/2015.
 */
public class CustomerViewAdapter  extends RecyclerView.Adapter<CustomersViewHolder>{

    private Context context;
    private LayoutInflater inflater;
    public List<Customer> data= Collections.emptyList();

    public CustomerViewAdapter(Context context, List<Customer> data) {
        inflater= LayoutInflater.from(context);
        this.data=data;
        this.context=context;
    }


    @Override
    public CustomersViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view= inflater.inflate(R.layout.customerdisplay_row,parent,false);

        CustomersViewHolder holder = new CustomersViewHolder(view);

        return holder;
    }

    @Override
    public void onBindViewHolder(CustomersViewHolder holder, int position) {
        Typefacer typeface= new Typefacer();
        Customer customer =data.get(position);

        holder.txtcustomerid.setText(customer.getCustomer_id());


        int tmp =customer.getFirst_name().length();

        if(tmp>20){

        String nowString=customer.getFirst_name().substring(0,10);
            holder.txtCutomerName.setText(nowString+".....");
        }else{
            holder.txtCutomerName.setText(customer.getFirst_name());

        }

        holder.txtCutomerName.setTypeface(typeface.squareBold());

       if(Customer.getMsisdns(customer.getCustomer_id()).size()>0){
           holder.txtCustomerPhonevalue.setText(Customer.getMsisdns(customer.getCustomer_id()).get(0).getNumber());
       }

        holder.txtCustomerPhonevalue.setTypeface(typeface.squareLight());


        holder.txtCustomerPhonelabel.setTypeface(typeface.squareLight());

        int idSize=customer.getCustomer_id().length();
        if(idSize>8){

            String nowString=customer.getCustomer_id().substring(0,7);
            holder.txtCustomerPhonelabel.setText(nowString + " | ");
        }else {
            holder.txtCustomerPhonelabel.setText(customer.getCustomer_id() + " | ");

        }


        holder.profilepics.setImageDrawable(Application.getAppContext().getResources().getDrawable(R.drawable.smile));
        holder.profilepics.setTag(customer.getCustomer_id());
        Utils.setImage(customer.getPhotoUrl(), holder.profilepics);

        Log.d("oxinbo","account number "+customer.getAccount_number());
    }

    @Override
    public int getItemCount() {
        return data.size();
    }
}
     class CustomersViewHolder extends RecyclerView.ViewHolder{
    //ImageView productIcon;
       TextView txtCutomerName,txtCustomerPhonevalue,txtCustomerPhonelabel,txtcustomerid;
       CircleImageView profilepics;RelativeLayout mini_parent_layout;

         public CustomersViewHolder(View itemView) {
        super(itemView);
        txtcustomerid =(TextView)itemView.findViewById(R.id.txtcustomerid);
        txtCutomerName =(TextView)itemView.findViewById(R.id.txtCutomerName);
       // txtCutomerName.set
        txtCustomerPhonevalue =(TextView)itemView.findViewById(R.id.txtCustomerPhonevalue);
        profilepics =(CircleImageView)itemView.findViewById(R.id.profile_image);
       txtCustomerPhonelabel=(TextView)itemView.findViewById(R.id.txtCustomerPhonelabel);
    }
}