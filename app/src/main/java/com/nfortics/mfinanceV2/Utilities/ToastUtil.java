package com.nfortics.mfinanceV2.Utilities;

import android.content.Context;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.nfortics.mfinanceV2.Application.Application;
import com.nfortics.mfinanceV2.R;
import com.nfortics.mfinanceV2.Typefacer;

public class ToastUtil {
	public static void showToast(Context context,String msg) {
         Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
	}
	
	public static void showToast(Context context,int resId) {
        Toast.makeText(context, resId, Toast.LENGTH_SHORT).show();
	}

	public static void showToast(String message,boolean correct ) {
		Context context=Application.getAppContext();
		LayoutInflater layoutInflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

		View customToastroot =layoutInflater.inflate(R.layout.toast_layout, null);

		Typefacer typefacer=new Typefacer();

		TextView textView=(TextView) customToastroot.findViewById(R.id.toastMessage);
		ImageView imageView1=(ImageView)customToastroot.findViewById(R.id.imageView1);
		if(correct){

			imageView1.setImageResource(R.drawable.symbol);
		}else{
			imageView1.setImageResource(R.drawable.wrong);
		}


		textView.setText(message);
		textView.setTypeface(typefacer.squareLight());
		Toast customtoast=new Toast(context);

		customtoast.setView(customToastroot);
		customtoast.setGravity(Gravity.TOP | Gravity.CENTER, 0, 0);
		customtoast.setDuration(Toast.LENGTH_SHORT);
		customtoast.show();
	}

	public static void showMessageToast(String message,boolean correct ) {
		Context context=Application.getAppContext();
		LayoutInflater layoutInflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

		View customToastroot =layoutInflater.inflate(R.layout.toast_layout, null);

		Typefacer typefacer=new Typefacer();

		TextView textView=(TextView) customToastroot.findViewById(R.id.toastMessage);
		ImageView imageView1=(ImageView)customToastroot.findViewById(R.id.imageView1);
		if(correct){

			imageView1.setImageResource(R.drawable.symbol);
		}else{
			imageView1.setImageResource(R.drawable.wrong);
		}


		textView.setText(message);
		textView.setTypeface(typefacer.squareLight());
		Toast customtoast=new Toast(context);

		customtoast.setView(customToastroot);
		customtoast.setGravity(Gravity.TOP | Gravity.TOP, 0, 0);
		customtoast.setDuration(Toast.LENGTH_SHORT);
		customtoast.show();
	}

	/**public static  void ShowCustomerToast(String mess){


		Context context=Application.getAppContext();
		LayoutInflater layoutInflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

		View customToastroot =layoutInflater.inflate(R.layout.toast_layout, null);

		Typefacer typefacer=new Typefacer();

	    TextView textView=(TextView) customToastroot.findViewById(R.id.toastMessage);
		textView.setText(mess);
		textView.setTypeface(typefacer.squareLight());
		Toast customtoast=new Toast(context);

		customtoast.setView(customToastroot);
		customtoast.setGravity(Gravity.TOP | Gravity.CENTER_HORIZONTAL, 50, 0);
		customtoast.setDuration(Toast.LENGTH_SHORT);
		customtoast.show();
	}
	**/


	public static void snackbar(CoordinatorLayout coordinatorLayout,String value){

		Snackbar snackbar = Snackbar
				.make(coordinatorLayout, value, Snackbar.LENGTH_SHORT);
	}


}
