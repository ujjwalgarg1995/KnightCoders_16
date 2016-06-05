package com.osgrip.iclean.Dialog;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.osgrip.iclean.Activity.CitizenHomeActivity;
import com.osgrip.iclean.Activity.PendingActivity;
import com.osgrip.iclean.Modals.Complains;
import com.osgrip.iclean.R;

/**
 * Created by Pranjal on 12-Jan-16.
 */
public class ShowDetailDialog extends Dialog {
    private Activity activity;
    private Complains complains;
    private Button doneButton,pendingButton;
    private TextView  complainIDText, dialogHeading,dialogText;
    ImageView dialogImage;
    private int code;
    private int data[];

    public ShowDetailDialog(Activity activity,Complains complains,int code, int[] data) {
        super(activity);
        this.activity = activity;
        this.complains = complains;
        this.code = code;
        this.data = data;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        activity.finish();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.show_detail_dialog);
        complainIDText = (TextView) findViewById(R.id.complainIdText);
        dialogHeading = (TextView) findViewById(R.id.dialogHeading);
        dialogText = (TextView) findViewById(R.id.dialogText);
        dialogImage = (ImageView) findViewById(R.id.dialogImage);
        pendingButton = (Button) findViewById(R.id.pendingButton);
        doneButton = (Button) findViewById(R.id.doneButton);
        switch (code){
            case 1:
                dialogImage.setImageResource(R.drawable.greentick);
                dialogText.setText("Your complaint has been successfully posted");
                dialogHeading.setText("THANK YOU");
                complainIDText.setText("Complain : " + complains.getComplain_id());
                break;
            case 2:
                complainIDText.setVisibility(View.GONE);
                dialogImage.setImageResource(R.drawable.redcross);
                dialogHeading.setText("YOU ARE NOT CONNECTED TO THE INTERNET NOW.");
                dialogText.setText("Your complaint has been saved but not submitted. You can submit it once you are connected to the internet. Click Pending Uploads button on Home Screen to post the complaints");
                pendingButton.setVisibility(View.VISIBLE);
                pendingButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dismiss();
                        Intent i = new Intent(getContext(), PendingActivity.class);
                        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        getContext().startActivity(i);
                        activity.finish();
                    }
                });
                break;
            case 3:
                complainIDText.setVisibility(View.GONE);
                if(data[0]==0) {
                    dialogImage.setImageResource(R.drawable.redcross);
                    dialogText.setText("Nothing Uploaded Please try after sometime");
                    dialogHeading.setText("Nothing Uploaded");
                    dialogText.setText("Somthing went wrong, try again later.");
                } else {
                    dialogImage.setImageResource(R.drawable.greentick);
                    dialogText.setText(data[0] + " complaints out of "+data[1]+" has been successfully posted");
                    dialogHeading.setText("THANK YOU");
                }
        }
        doneButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
                Intent i = new Intent(getContext(), CitizenHomeActivity.class);
                i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                getContext().startActivity(i);
                activity.finish();
            }
        });


    }
}
