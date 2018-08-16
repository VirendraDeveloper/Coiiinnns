package com.coldstoragecoins;

import android.app.Dialog;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.widget.TextView;
import android.widget.Toast;

/**
 * This class is used as
 *
 * @author DreamWorksSoftwares
 * @version 1.0
 * @since 05 Aug,2018
 */
public class BaseActivity extends AppCompatActivity {
    private Dialog pDialog, iDialog;
    private TextView tvTitle, tvMessage;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        pDialog = new Dialog(this);
        pDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        pDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        pDialog.setContentView(R.layout.progress_dialog_layout);
        pDialog.setCancelable(false);

        iDialog = new Dialog(this);
        iDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        iDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        iDialog.setContentView(R.layout.message_dialog_layout);
        tvTitle = (TextView) iDialog.findViewById(R.id.tv_title);
        tvMessage = (TextView) iDialog.findViewById(R.id.tv_message);
        TextView tvOk = (TextView) iDialog.findViewById(R.id.tv_ok);
        iDialog.setCancelable(false);
        tvOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                iDialog.dismiss();
            }
        });
    }


    public void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    public void showLoading() {
        pDialog.show();
    }

    public void hideLoading() {
        pDialog.dismiss();
    }

    public void showMessage() {
        iDialog.show();
    }

    public void hideMessage() {
        iDialog.dismiss();
    }

    public void setInfoMessage(String title, String msg) {
        tvMessage.setText(msg);
        tvTitle.setText(title);
    }


}
