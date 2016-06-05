package com.osgrip.iclean.Dialog;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.View;

/**
 * Created by Pranjal on 29-Jan-16.
 */
public class ErrorDialog {
    public static void displayError(Context context, String title, String errorMSG, final View v) {
        new AlertDialog.Builder(context)
                    .setMessage(errorMSG)
                    .setTitle(title)
                    .setPositiveButton("Okay", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            if (v != null) {
                                v.requestFocus();
                            }
                        }
                    })
                    .setCancelable(true)
                    .show();
    }
}
