package com.coldstoragecoins;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
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

import static com.coldstoragecoins.LoginActivity.isEmailValid;

public class RegisterActivity extends BaseActivity implements View.OnClickListener {
    private EditText edtEmail, edtPassword, edtFName, edtLName;
    private TextView tvCurrency, tvCreate, tvLogin;
    private ImageView ivBack;
    private Dialog fDialog;
    private String defaultCurrency = "USD";
    private int DEFAULT_CUR = 102;
    private DatabaseHelper db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        db = new DatabaseHelper(this);
        setContentView(R.layout.activity_register);
        initUI();
    }

    private void initUI() {
        tvCreate = (TextView) findViewById(R.id.tv_create);
        tvCurrency = (TextView) findViewById(R.id.tv_currency);
        tvLogin = (TextView) findViewById(R.id.tv_login);
        edtFName = (EditText) findViewById(R.id.edt_f_name);
        edtLName = (EditText) findViewById(R.id.edt_l_name);
        edtEmail = (EditText) findViewById(R.id.edt_email);
        edtPassword = (EditText) findViewById(R.id.edt_password);
        ivBack = (ImageView) findViewById(R.id.iv_back);
        tvLogin.setOnClickListener(this);
        ivBack.setOnClickListener(this);
        tvCurrency.setOnClickListener(this);
        tvCreate.setOnClickListener(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        tvCurrency.setText(HomeActivity.defCurrency);
    }

    @Override
    public void onClick(View v) {
        Intent intent;
        switch (v.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.tv_login:
                intent = new Intent(this, LoginActivity.class);
                startActivity(intent);
                break;
            case R.id.tv_create:
                String strEmail = edtEmail.getText().toString().trim();
                String strPassword = edtPassword.getText().toString().trim();
                String strFName = edtFName.getText().toString().trim();
                String strLName = edtLName.getText().toString().trim();

                if (strEmail.equalsIgnoreCase("")) {
                    setInfoMessage("Info", getResources().getString(R.string.emp_email));
                    showMessage();
                } else if (!isEmailValid(strEmail)) {
                    setInfoMessage("Info", getResources().getString(R.string.invalid_email));
                    showMessage();
                } else if (strPassword.equalsIgnoreCase("")) {
                    setInfoMessage("Info", getResources().getString(R.string.emp_password));
                    showMessage();
                } else if (strPassword.length() < 6) {
                    setInfoMessage("Info", getResources().getString(R.string.pass_len));
                    showMessage();
                } else if (!NetworkRecognizer.isNetworkAvailable(this)) {
                    setInfoMessage("Info", getResources().getString(R.string.network_no));
                    showMessage();
                } else {
                    callRegisterApi(strFName, strLName, strEmail, strPassword);
                }
                break;
            case R.id.tv_currency:
                intent = new Intent(this, CurrencyActivity.class);
                startActivityForResult(intent, DEFAULT_CUR);
                break;
        }
    }

    private void callRegisterApi(final String strFName, final String strLName, final String strEmail, final String strPassword) {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("firstname", strFName);
        jsonObject.addProperty("lastname", strLName);
        jsonObject.addProperty("email", strEmail);
        jsonObject.addProperty("password", strPassword);
        jsonObject.addProperty("default_currency", HomeActivity.defCurrency);
        showLoading();
        ApiHelper.getInstance(this).userRegister(jsonObject, new ApiCallback.Listener() {
            @Override
            public void onSuccess(String result) {
                JsonObject jsonObject = new JsonObject();
                jsonObject.addProperty("email", strEmail);
                jsonObject.addProperty("password", strPassword);
                ApiHelper.getInstance(RegisterActivity.this).userLogin(jsonObject, new ApiCallback.LoginListener() {
                    @Override
                    public void onSuccess(UserInfo result) {
                        hideLoading();
                        SharedPreferenceUtility.getInstance(RegisterActivity.this).putString(AppConstants._SID, result.getSid() + "");
                        SharedPreferenceUtility.getInstance(RegisterActivity.this).putString(AppConstants._ID, result.getId() + "");
                        SharedPreferenceUtility.getInstance(RegisterActivity.this).putString(AppConstants._EMAIL, result.getEmail());
                        SharedPreferenceUtility.getInstance(RegisterActivity.this).putString(AppConstants._F_NAME, result.getFirstname() + "");
                        SharedPreferenceUtility.getInstance(RegisterActivity.this).putString(AppConstants._L_NAME, result.getLastname());
                        HomeActivity.defCurrency = result.getDefault_currency();
                        try {
                            db.addWalletsInfo(result.getWallets());
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        List<Wallet> coins = db.getUserWallets(result.getId() + "");
                        Intent intent = new Intent(RegisterActivity.this, HomeActivity.class);
                        startActivity(intent);
                        finish();
                        hideLoading();
                        edtEmail.setText("");
                        edtPassword.setText("");
                        edtFName.setText("");
                        edtFName.setText("");
                    }

                    @Override
                    public void onFailure(String error) {
                        setInfoMessage("Info", "Something went wrong");
                        showMessage();
                        hideLoading();
                    }
                });
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
