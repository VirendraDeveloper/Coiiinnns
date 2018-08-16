package com.coldstoragecoins;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.TextView;

import com.coldstoragecoins.db.DatabaseHelper;
import com.coldstoragecoins.models.UserInfo;
import com.coldstoragecoins.models.Wallet;
import com.coldstoragecoins.network.ApiCallback;
import com.coldstoragecoins.network.ApiHelper;
import com.coldstoragecoins.utils.AppConstants;
import com.coldstoragecoins.utils.NetworkRecognizer;
import com.coldstoragecoins.utils.SharedPreferenceUtility;
import com.google.gson.JsonObject;

import java.util.List;

public class LoginActivity extends BaseActivity implements View.OnClickListener {
    private EditText edtEmail, edtPassword;
    private TextView tvForgot, tvCreate, tvLogin, tvBack;
    private Dialog fDialog;
    private DatabaseHelper db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initDialogs();
        db = new DatabaseHelper(this);
        setContentView(R.layout.activity_login);
        initUI();
    }

    private void initUI() {
        tvCreate = (TextView) findViewById(R.id.tv_create);
        tvForgot = (TextView) findViewById(R.id.tv_forgot);
        tvLogin = (TextView) findViewById(R.id.tv_login);
        edtEmail = (EditText) findViewById(R.id.edt_email);
        edtPassword = (EditText) findViewById(R.id.edt_password);
        tvBack = (TextView) findViewById(R.id.tv_back);
        tvBack.setOnClickListener(this);
        tvLogin.setOnClickListener(this);
        tvForgot.setOnClickListener(this);
        tvCreate.setOnClickListener(this);
        edtEmail.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (checkEmailPassword()) {
                    tvLogin.setEnabled(true);
                    tvLogin.setTextColor(getResources().getColor(R.color.colorBlue));
                } else {
                    tvLogin.setEnabled(false);
                    tvLogin.setTextColor(getResources().getColor(R.color.colorGray));
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        edtPassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                if (checkEmailPassword()) {
                    tvLogin.setEnabled(true);
                    tvLogin.setTextColor(getResources().getColor(R.color.colorBlue));
                } else {
                    tvLogin.setEnabled(false);
                    tvLogin.setTextColor(getResources().getColor(R.color.colorGray));
                }
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    private boolean checkEmailPassword() {
        String strEmail = edtEmail.getText().toString().trim();
        String strPassword = edtPassword.getText().toString().trim();
        if (strEmail.equalsIgnoreCase("")) {
            return false;
        } else if (!isEmailValid(strEmail)) {
            return false;
        } else if (strPassword.equalsIgnoreCase("")) {
            return false;
        } else {
            return true;
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_create:
                finish();
                break;
            case R.id.tv_login:
                String strEmail = edtEmail.getText().toString().trim();
                String strPassword = edtPassword.getText().toString().trim();
                if (strEmail.equalsIgnoreCase("")) {
                    setInfoMessage("Info", getResources().getString(R.string.emp_email));
                    showMessage();
                } else if (!isEmailValid(strEmail)) {
                    setInfoMessage("Info", getResources().getString(R.string.invalid_email));
                    showMessage();
                } else if (strPassword.equalsIgnoreCase("")) {
                    setInfoMessage("Info", getResources().getString(R.string.emp_password));
                    showMessage();
                } else {
                    if (NetworkRecognizer.isNetworkAvailable(this)) {
                        callLoginApi(strEmail, strPassword);
                    } else {
                        setInfoMessage("Info", getResources().getString(R.string.network_no));
                        showMessage();
                    }
                }
                break;
            case R.id.tv_forgot:
                fDialog.show();
                break;
            case R.id.tv_back:
                finish();
                break;
        }
    }

    private static String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";

    public static boolean isEmailValid(String email) {
        return email.matches(emailPattern);
    }

    private void initDialogs() {
        fDialog = new Dialog(this);
        fDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        fDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        fDialog.setContentView(R.layout.forgot_dialog);
        TextView tvSend = (TextView) fDialog.findViewById(R.id.tv_send);
        TextView tvCancel = (TextView) fDialog.findViewById(R.id.tv_cancel);
        final EditText edtEmail1 = (EditText) fDialog.findViewById(R.id.edt_email);
        tvCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fDialog.dismiss();
            }
        });
        tvSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String strEmail = edtEmail1.getText().toString().trim();
                if (strEmail.equalsIgnoreCase("")) {
                    showToast(getResources().getString(R.string.emp_email));
                } else if (!isEmailValid(strEmail)) {
                    showToast(getResources().getString(R.string.invalid_email));
                } else {
                    if (NetworkRecognizer.isNetworkAvailable(LoginActivity.this)) {
                        sendMail(strEmail);
                    } else {
                        setInfoMessage("Info", getResources().getString(R.string.network_no));
                        showMessage();
                    }
                }
            }

        });
    }

    private void sendMail(String strEmail) {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("email", strEmail);
        showLoading();
        ApiHelper.getInstance(this).passwordForgot(jsonObject, new ApiCallback.Listener() {
            @Override
            public void onSuccess(String result) {
                hideLoading();
            }

            @Override
            public void onFailure(String error) {
                setInfoMessage("Info", "Something went wrong");
                showMessage();
                hideLoading();

            }
        });


    }

    private void callLoginApi(String strEmail, String strPassword) {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("email", strEmail);
        jsonObject.addProperty("password", strPassword);
        showLoading();
        ApiHelper.getInstance(this).userLogin(jsonObject, new ApiCallback.LoginListener() {
            @Override
            public void onSuccess(UserInfo result) {
                //db.insert(result);
                hideLoading();
                SharedPreferenceUtility.getInstance(LoginActivity.this).putString(AppConstants._SID, result.getSid() + "");
                SharedPreferenceUtility.getInstance(LoginActivity.this).putString(AppConstants._ID, result.getId() + "");
                SharedPreferenceUtility.getInstance(LoginActivity.this).putString(AppConstants._EMAIL, result.getEmail());
                SharedPreferenceUtility.getInstance(LoginActivity.this).putString(AppConstants._F_NAME, result.getFirstname() + "");
                SharedPreferenceUtility.getInstance(LoginActivity.this).putString(AppConstants._L_NAME, result.getLastname());
                HomeActivity.defCurrency = result.getDefault_currency();
                try {
                    db.addWalletsInfo(result.getWallets());
                } catch (Exception e) {
                    e.printStackTrace();
                }
                List<Wallet> coins = db.getUserWallets(result.getId() + "");
                Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                finishAffinity();
                hideLoading();
            }

            @Override
            public void onFailure(String error) {
                setInfoMessage("Info", "Something went wrong");
                showMessage();
                hideLoading();
            }
        });

    }
}
