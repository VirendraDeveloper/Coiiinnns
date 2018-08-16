package com.coldstoragecoins;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.coldstoragecoins.db.DatabaseHelper;
import com.coldstoragecoins.network.ApiCallback;
import com.coldstoragecoins.network.ApiHelper;
import com.coldstoragecoins.utils.AppConstants;
import com.coldstoragecoins.utils.NetworkRecognizer;
import com.coldstoragecoins.utils.SharedPreferenceUtility;
import com.google.gson.JsonObject;

public class AccountActivity extends BaseActivity implements View.OnClickListener {
    private TextView tvDone, tvLogout, tvUpdate, tvDelete, tvEmail, tvDefCurrency;
    private EditText edtPassword, edtConfirm, edtFName, edtLName;
    private DatabaseHelper db;
    private Intent intent;
    private int DEFAULT_CUR = 101;
    private String strEmail, strSID,fName,lName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account);
        strSID = SharedPreferenceUtility.getInstance(this).getString(AppConstants._SID);
        //db = new DatabaseHelper(this);
        //userInfo = db.getUserInfo(id);
        initUI();
        if (!strSID.equalsIgnoreCase("")) {
            // userInfo = db.getUserInfo(id);
            strEmail = SharedPreferenceUtility.getInstance(this).getString(AppConstants._EMAIL);
            fName = SharedPreferenceUtility.getInstance(this).getString(AppConstants._F_NAME);
            lName = SharedPreferenceUtility.getInstance(this).getString(AppConstants._L_NAME);
            tvEmail.setText(strEmail);
            edtLName.setText(lName);
            edtFName.setText(fName);
            tvDefCurrency.setText(HomeActivity.defCurrency);
        }
    }

    private void initUI() {
        tvDone = (TextView) findViewById(R.id.tv_done);
        tvLogout = (TextView) findViewById(R.id.tv_logout);
        tvUpdate = (TextView) findViewById(R.id.tv_update);
        tvDelete = (TextView) findViewById(R.id.tv_delete);
        tvEmail = (TextView) findViewById(R.id.tv_email);
        tvDefCurrency = (TextView) findViewById(R.id.tv_def_currency);
        edtFName = (EditText) findViewById(R.id.edt_f_name);
        edtLName = (EditText) findViewById(R.id.edt_l_name);
        edtPassword = (EditText) findViewById(R.id.edt_password);
        edtConfirm = (EditText) findViewById(R.id.edt_confirm);
        tvDefCurrency.setOnClickListener(this);
        tvDelete.setOnClickListener(this);
        tvDone.setOnClickListener(this);
        tvLogout.setOnClickListener(this);
        tvUpdate.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_done:
                finish();
                break;
            case R.id.tv_def_currency:
                intent = new Intent(this, CurrencyActivity.class);
                startActivityForResult(intent, DEFAULT_CUR);
                break;
            case R.id.tv_update:
                String strPass = edtPassword.getText().toString().trim();
                String strConfirm = edtConfirm.getText().toString().trim();
                String strFName = edtFName.getText().toString().trim();
                String strLName = edtLName.getText().toString().trim();
                if (strPass.equalsIgnoreCase("")) {
                    setInfoMessage("Info", getResources().getString(R.string.emp_password));
                    showMessage();
                } else if (!strPass.equalsIgnoreCase(strConfirm)) {
                    setInfoMessage("Info", getResources().getString(R.string.match_password));
                    showMessage();
                } else if (!NetworkRecognizer.isNetworkAvailable(this)) {
                    setInfoMessage("Info", getResources().getString(R.string.network_no));
                    showMessage();
                } else {
                    updatePassword(strFName, strLName, strConfirm);
                }
                break;
            case R.id.tv_logout:
                showLoading();
                ApiHelper.getInstance(this).logoutUser(strSID, new ApiCallback.Listener() {
                    @Override
                    public void onSuccess(String result) {
                        SharedPreferenceUtility.getInstance(AccountActivity.this).putString(AppConstants._SID, "");
                        SharedPreferenceUtility.getInstance(AccountActivity.this).putString(AppConstants._EMAIL, "");
                        SharedPreferenceUtility.getInstance(AccountActivity.this).putString(AppConstants._ID, "");
                        finish();
                        hideLoading();
                        Log.d("", "");
                    }

                    @Override
                    public void onFailure(String error) {
                        Log.d("", "");
                        hideLoading();

                    }
                });
                break;
            case R.id.tv_delete:
                showLoading();
                ApiHelper.getInstance(this).deleteUser(strSID, new ApiCallback.Listener() {
                    @Override
                    public void onSuccess(String result) {
                        Log.d("", "");
                        hideLoading();
                        SharedPreferenceUtility.getInstance(AccountActivity.this).putString(AppConstants._SID, "");
                        SharedPreferenceUtility.getInstance(AccountActivity.this).putString(AppConstants._EMAIL, "");
                        showToast(result);
                        finish();
                    }

                    @Override
                    public void onFailure(String error) {
                        Log.d("", "");
                        hideLoading();
                    }
                });
                break;
        }
    }

    private void updatePassword(String strFName, String strLName, String strConfirm) {
        JsonObject body = new JsonObject();
        body.addProperty("email", strEmail);
        body.addProperty("password", strConfirm);
        body.addProperty("lastname", strLName);
        body.addProperty("firstname", strFName);
        body.addProperty("default_currency", HomeActivity.defCurrency);

        showLoading();
        ApiHelper.getInstance(this).updateDetails(strSID, body, new ApiCallback.Listener() {
            @Override
            public void onSuccess(String result) {
                hideLoading();
                edtConfirm.setText("");
                edtPassword.setText("");
                showToast("Password change successfully");
                Log.d("", "");
            }

            @Override
            public void onFailure(String error) {
                showToast(error);
                Log.d("", "");
                hideLoading();

            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        tvDefCurrency.setText(HomeActivity.defCurrency);

    }
}
