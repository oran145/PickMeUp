package com.example.liron.finalproject.Dialogs;

import android.app.ProgressDialog;
import android.content.Context;

import com.example.liron.finalproject.R;

/**
 * Created by liron on 25-Jul-17.
 */

public class MyProgressBar {
    private ProgressDialog progressDialog;
    private Context context;

    public MyProgressBar(Context context) {
        this.context=context;
        progressDialog = new ProgressDialog(context);
        progressDialog.setMessage(context.getString(R.string.loading));
        progressDialog.setIndeterminate(true);
    }

    public void showProgressDialog() {
//        if (progressDialog == null) {
//            progressDialog = new ProgressDialog(context);
//            progressDialog.setMessage(""+R.string.loading);
//            progressDialog.setIndeterminate(true);
//        }

        progressDialog.show();
    }

    public void hideProgressDialog() {
        if (progressDialog != null && progressDialog.isShowing()) {
            progressDialog.dismiss();
        }
    }
}
