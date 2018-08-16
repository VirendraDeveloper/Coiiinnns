package com.coldstoragecoins;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.coldstoragecoins.db.DatabaseHelper;
import com.coldstoragecoins.models.Wallet;
import com.coldstoragecoins.network.ApiCallback;
import com.coldstoragecoins.network.ApiHelper;
import com.coldstoragecoins.utils.AppConstants;
import com.coldstoragecoins.utils.NetworkRecognizer;
import com.coldstoragecoins.utils.SharedPreferenceUtility;
import com.coldstoragecoins.utils.Util;
import com.coldstoragecoins.utils.WalletAdapter;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class HomeActivity extends BaseActivity implements View.OnClickListener, AdapterView.OnItemClickListener {
    private ImageView ivProfile, ivRefresh, ivPlus, ivUserProfile;
    private TextView tvEmail, tvCoins, tvAmount, tvChangeCurrency, tvQuickCheck;
    private boolean isLogin;
    private DatabaseHelper db;
    private String strEmail, strSID, userID;
    private ListView lvWallet;
    public static String defCurrency = "USD";
    private WalletAdapter mAdapter;
    private static final int CAMERA = 1002;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        db = new DatabaseHelper(this);
        initUI();
        LayoutInflater inflater = getLayoutInflater();
        ViewGroup header = (ViewGroup) inflater.inflate(R.layout.bottom_layout, lvWallet, false);
        tvAmount = (TextView) header.findViewById(R.id.tv_amount);
        tvChangeCurrency = (TextView) header.findViewById(R.id.tv_change_currency);
        tvQuickCheck = (TextView) header.findViewById(R.id.tv_quick_bal);
        tvChangeCurrency.setOnClickListener(this);
        tvQuickCheck.setOnClickListener(this);
        mAdapter = new WalletAdapter(this);
        lvWallet.setAdapter(mAdapter);
        lvWallet.addFooterView(header, null, false);
    }

    /**
     * check camera permission granted or not
     *
     * @return
     */
    private boolean isReadCameraAllowed() {
        //Getting the permission status
        int result = ContextCompat.checkSelfPermission(HomeActivity.this, Manifest.permission.CAMERA);
        //If permission is granted returning true
        if (result == PackageManager.PERMISSION_GRANTED) {
            return true;
        }
        //If permission is not granted returning false
        return false;
    }

    /**
     * request to camera permission
     */
    private void requestCameraPermission() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(HomeActivity.this, Manifest.permission.CAMERA)) {
            //If the user has denied the permission previously your code will come to this block
            //Here you can explain why you need this permission
            //Explain here why you need this permission
        }
        //And finally ask for the permission
        ActivityCompat.requestPermissions(HomeActivity.this, new String[]{Manifest.permission.CAMERA}, CAMERA);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case CAMERA:
                try {
                    if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    } else if (grantResults[0] == PackageManager.PERMISSION_DENIED) {
                        requestCameraPermission();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
        }
    }


    private void initSetting() {
        strSID = SharedPreferenceUtility.getInstance(this).getString(AppConstants._SID);
        userID = SharedPreferenceUtility.getInstance(this).getString(AppConstants._ID);
        lvWallet.setOnItemClickListener(this);
        if (!strSID.equalsIgnoreCase("")) {
            isLogin = true;
            strEmail = SharedPreferenceUtility.getInstance(this).getString(AppConstants._EMAIL);
            tvEmail.setText(strEmail);
            ApiHelper.getInstance(this).getUserInfo(strSID, new ApiCallback.Listener() {
                @Override
                public void onSuccess(String result) {
                    Log.d("", "");
                }

                @Override
                public void onFailure(String error) {
                    Log.d("", "");

                }
            });
            String email = SharedPreferenceUtility.getInstance(this).getString(AppConstants._EMAIL);
            String imageUrl = Util.getProfileImage(email);
            Picasso.with(this)
                    .load(imageUrl)
                    .into(ivUserProfile);
        } else {
            isLogin = false;
            tvEmail.setText(getResources().getString(R.string.login_create));
            tvCoins.setText("0  " + getResources().getString(R.string.locally_coin));
            tvAmount.setText(Util.currencySymbol(defCurrency) + " 0.00");
        }
        List<Wallet> coins;
        String filter;
        if (strSID.equalsIgnoreCase("")) {
            filter = AppConstants._LOCAL;
        } else {
            filter = userID;
        }
        coins = db.getUserWallets(filter);
        showAdapter(coins);
        if (coins.size() > 0) {
            if (NetworkRecognizer.isNetworkAvailable(this)) {
                updateWallets(coins);
            }
        }
    }

    private void updateWallets(List<Wallet> wallets) {
        JsonObject bodyBal = new JsonObject();
        JsonArray jsonArray = new JsonArray();
        for (int i = 0; i < wallets.size(); i++) {
            Wallet wallet = wallets.get(i);
            jsonArray.add(wallet.getAddress());
        }
        bodyBal.add("wallet", jsonArray);
        ApiHelper.getInstance(this).checkBalance(strSID, bodyBal, new ApiCallback.WalletListener() {
            @Override
            public void onSuccess(List<Wallet> result) {
                Log.d("Coin ", "");
                String filter;
                if (strSID.equalsIgnoreCase("")) {
                    filter = AppConstants._LOCAL;
                } else {
                    filter = userID;
                }
                db.addWalletsBalance(result);

                List<Wallet> wallets = db.getUserWallets(filter);
                showAdapter(wallets);
                Log.d("Coin ", "");
            }

            @Override
            public void onFailure(String error) {
                Log.d("Coin ", "");
            }
        });

        ApiHelper.getInstance(this).getMyWallets(strSID, bodyBal, new ApiCallback.WalletListener() {
            @Override
            public void onSuccess(List<Wallet> wallets1) {
                Log.d("Coin ", "");
                String filter;
                if (strSID.equalsIgnoreCase("")) {
                    filter = AppConstants._LOCAL;
                } else {
                    filter = userID;
                }
                db.addWalletsInfo(wallets1);
                List<Wallet> wallets = db.getUserWallets(filter);
                Log.d("Coin ", "");
                showAdapter(wallets);
                Log.d("Coin ", "");
            }

            @Override
            public void onFailure(String error) {
                Log.d("", "");
            }
        });

    }

    private void showAdapter(List<Wallet> wallets) {
        List<Wallet> wallets1 = new ArrayList<>();
        float ammount = 0.00f;
        for (Wallet wallet : wallets) {
            if (strSID.equalsIgnoreCase("")) {
                if (wallet.getUserId().equalsIgnoreCase("coins")) {
                    wallets1.add(wallet);
                    try {
                        ammount = ammount + Float.parseFloat(wallet.getPrice());
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            } else {
                if (wallet.getUserId().equalsIgnoreCase(userID)) {
                    wallets1.add(wallet);
                    try {
                        ammount = ammount + Float.parseFloat(wallet.getPrice());
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }

        }
        mAdapter.addWallets(wallets1);
        tvAmount.setText(Util.currencySymbol(defCurrency) + " " + ammount);
        if (strSID.equalsIgnoreCase("")) {
            tvCoins.setText(mAdapter.getCount() + "  " + getResources().getString(R.string.locally_coin));
        } else {
            tvCoins.setText(mAdapter.getCount() + " " + getResources().getString(R.string.coin_available));
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        initSetting();
    }

    private void initUI() {
        ivProfile = (ImageView) findViewById(R.id.iv_profile);
        ivRefresh = (ImageView) findViewById(R.id.iv_refresh);
        ivPlus = (ImageView) findViewById(R.id.iv_plus);
        ivUserProfile = (ImageView) findViewById(R.id.iv_user_profile);
        tvEmail = (TextView) findViewById(R.id.tv_email);
        tvCoins = (TextView) findViewById(R.id.tv_coins);
        lvWallet = (ListView) findViewById(R.id.lv_wallet);

        ivPlus.setOnClickListener(this);
        ivRefresh.setOnClickListener(this);
        ivProfile.setOnClickListener(this);
        tvEmail.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        Intent intent;
        switch (v.getId()) {
            case R.id.tv_change_currency:
                intent = new Intent(this, CurrencyActivity.class);
                startActivity(intent);
                break;
            case R.id.tv_quick_bal:
                if (isReadCameraAllowed()) {
                    QRActivity.isScanned = true;
                    intent = new Intent(this, QRActivity.class);
                    startActivity(intent);
                } else {
                    requestCameraPermission();
                }
                break;
            case R.id.tv_email:
                if (!isLogin) {
                    intent = new Intent(this, RegisterActivity.class);
                    startActivity(intent);
                }
                break;
            case R.id.iv_plus:
                if (isReadCameraAllowed()) {
                    QRActivity.isScanned = true;
                    intent = new Intent(this, QRActivity.class);
                    startActivity(intent);
                } else {
                    requestCameraPermission();
                }
                break;
            case R.id.iv_profile:
                if (isLogin) {
                    intent = new Intent(this, AccountActivity.class);
                    startActivity(intent);
                } else {
                    intent = new Intent(this, RegisterActivity.class);
                    startActivity(intent);
                }
                break;
            case R.id.iv_refresh:
                String filter;
                if (strSID.equalsIgnoreCase("")) {
                    filter = AppConstants._LOCAL;
                } else {
                    filter = userID;
                }
                List<Wallet> coins = db.getUserWallets(filter);
                mAdapter.addWallets(coins);
                if (coins.size() > 0) {
                    updateWallets(coins);
                }
                break;
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Wallet coin = (Wallet) mAdapter.getItem(position);
        Intent intent = new Intent(HomeActivity.this, WalletActivity.class);
        intent.putExtra("Wallet", coin);
        startActivity(intent);
    }
}
