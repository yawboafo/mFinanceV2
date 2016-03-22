package com.nfortics.mfinanceV2.DataBase;

import com.nfortics.mfinanceV2.Application.Application;
import com.nfortics.mfinanceV2.Models.Branch;
import com.nfortics.mfinanceV2.Models.Merchant;

/**
 * Created by bigfire on 11/26/2015.
 */
public class DaoAccess {


    public static String AllMerchantStringify(){

        StringBuffer sBuffer = new StringBuffer();

        for(Merchant merchant : Merchant.getAll()){

            sBuffer.append(merchant.getName());
           sBuffer.append("\n");
        }

        return sBuffer.toString();
    }


    public static String AllBranchStringify(){

        StringBuffer sBuffer = new StringBuffer();
        Merchant m=Merchant.getActiveMerchant("true");
        for(Branch branch : Branch.getAllMerchantBranch(m.getCode())){

            sBuffer.append(branch.getName());
            sBuffer.append("\n");
        }

        return sBuffer.toString();
    }

    public static  void setActiveMerchant(String merchant){

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

}
