package com.nfortics.mfinanceV2.Dialogs;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.nfortics.mfinanceV2.Application.Application;
import com.nfortics.mfinanceV2.R;
import com.nfortics.mfinanceV2.Typefacer;

/**
 * Created by bigfire on 11/13/2015.
 */
public class QuestionDialog extends AlertDialog {
    private Button saveButton;
    private Button cancelButton;
    private LinearLayout mainLayout;
    private final SubmitSuccessCallback callback;
    String content, actionLabel1, actionLabel2 = "";
    Context context;
    private LayoutInflater layoutInflater;
    public static boolean status = false;
  Typefacer typefacer=new Typefacer();
    public QuestionDialog(final Context context, String content,
                          String actionLabel1, String actionLabel2,
                          SubmitSuccessCallback callback) {
        super(context);
        // requestWindowFeature(Window.FEATURE_NO_TITLE);
        layoutInflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = layoutInflater.inflate(R.layout.send_over_sms_layout, null);
        TextView description = (TextView) view.findViewById(R.id.esync_txt);
        description.setTypeface(typefacer.getRoboCondensedRegular(Application.getAppContext().getAssets()));
        description.setText(content);
        setView(view);
        // setContentView(R.layout.send_over_sms_layout);

        this.context = context;
        this.callback = callback;
        this.content = content;
        this.actionLabel1 = actionLabel1;
        this.actionLabel2 = actionLabel2;

        eventsListeners();
        populateContent();

    }



    private void eventsListeners() {
        onSaveButtonClickListener();
        onCancelButtonClickListener();
    }

    private void setWidgetSize() {
        int width = Application.screenWidth;
        int height = Application.screenheight;

        int emptySpaceWidth = (width - 300) / 4;

        saveButton.setWidth(width / 2 - 5);
        cancelButton.setWidth(width / 2 - 5);
    }

    private void populateContent() {

        // mainLayout.removeAllViews();
        // TextView contentView = new TextView(context);
        // contentView
        // .setTextColor(context.getResources().getColor(R.color.black));
        // contentView.setPadding(10, 10, 10, 10);
        //
        // contentView.setText(content);
        // Utils.setContentTypeFace(contentView);
        // mainLayout.addView(contentView);
    }
    private void onCancelButtonClickListener() {
        setButton(AlertDialog.BUTTON_NEGATIVE, actionLabel2,
                new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        status = false;
                        callback.execute();

                    }
                });
    }

    private void onSaveButtonClickListener() {

        setButton(AlertDialog.BUTTON_POSITIVE, actionLabel1,
                new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        status = true;
                        callback.execute();

                    }
                });
    }

    private void setLabels() {

        // saveButton.setText("Submit");
        // cancelButton.setText("Cancel");

    }

    public interface SubmitSuccessCallback {
        public void execute();
    }
}
