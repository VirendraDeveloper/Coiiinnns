package com.coldstoragecoins;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.coldstoragecoins.db.DatabaseHelper;
import com.coldstoragecoins.models.Wallet;
import com.coldstoragecoins.network.ApiCallback;
import com.coldstoragecoins.network.ApiHelper;
import com.coldstoragecoins.network.SelfSigningClientBuilder;
import com.coldstoragecoins.utils.AppConstants;
import com.coldstoragecoins.utils.NetworkRecognizer;
import com.coldstoragecoins.utils.SharedPreferenceUtility;
import com.coldstoragecoins.utils.Util;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.jakewharton.picasso.OkHttp3Downloader;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class WalletActivity extends BaseActivity implements View.OnClickListener {
    private TextView tvAdd, tvClose, tvBalance, tvRemove, tvChange, tvCoin, tvWallet;
    private ImageView ivQr;
    private Bitmap qrBitmap;
    private Wallet wallet;
    private DatabaseHelper db;
    private String strSID, userID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        db = new DatabaseHelper(this);
        wallet = (Wallet) getIntent().getSerializableExtra("Wallet");
        strSID = SharedPreferenceUtility.getInstance(this).getString(AppConstants._SID);
        userID = SharedPreferenceUtility.getInstance(this).getString(AppConstants._ID);
        setContentView(R.layout.activity_wallet);
        initUI();
        if (NetworkRecognizer.isNetworkAvailable(this)) {
            List<Wallet> wallets = new ArrayList<>();
            wallets.add(wallet);
            updateWallets(wallets);
        }
        setWalletSetting();
    }

    private void setWalletSetting() {
        if (wallet != null) {
            if (wallet.getSlug().equalsIgnoreCase("")) {
                tvWallet.setText(wallet.getAddress());
            } else {
                tvWallet.setText(wallet.getSlug());
            }
            try {
                if (!wallet.getCurrency().equalsIgnoreCase("")) {
                    tvCoin.setText(wallet.getBalance() + " " + wallet.getCurrency());
                    wallet.setCurrency(wallet.getCurrency());
                    wallet.setBalance(wallet.getBalance());
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            try {
                if (!wallet.getImg().equalsIgnoreCase("")) {
                    Picasso picasso = new Picasso.Builder(this)
                            .downloader(new OkHttp3Downloader(SelfSigningClientBuilder.createClient(this)))
                            .build();
                    picasso.load(wallet.getImg()).into(ivQr);

                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        String filter;
        if (strSID.equalsIgnoreCase("")) {
            filter = AppConstants._LOCAL;
        } else {
            filter = userID;
            updateDetails(wallet);
        }
        List<Wallet> wallets = db.getUserWallets(filter);
        boolean isAdded = false;
        for (Wallet wallet1 : wallets) {
            if (wallet.getAddress().equalsIgnoreCase(wallet1.getAddress())) {
                isAdded = true;
                break;
            }
        }
        if (isAdded) {
            tvRemove.setVisibility(View.VISIBLE);
            tvAdd.setEnabled(false);
            tvAdd.setTextColor(getResources().getColor(R.color.colorDialog));
            tvAdd.setAlpha(0.5f);
        } else {
            tvRemove.setVisibility(View.GONE);
            tvAdd.setEnabled(true);
            tvAdd.setAlpha(1);
            tvAdd.setTextColor(getResources().getColor(R.color.colorGray));


        }
    }

    private void updateDetails(Wallet coin) {
        try {
            if (!coin.getSlug().equalsIgnoreCase("")) {
                tvWallet.setText(coin.getSlug());
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            String cc = Util.currencySymbol(HomeActivity.defCurrency);
            if (coin.getPrice().equalsIgnoreCase("") || coin.getPrice().equalsIgnoreCase("false")) {
                tvBalance.setText(cc + " " + "0.00");
            } else {
                tvBalance.setText(cc + " " + coin.getPrice());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            tvCoin.setText(coin.getBalance() + " " + coin.getCurrency());
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            if (!coin.getImg().equalsIgnoreCase("")) {
                Picasso picasso = new Picasso.Builder(this)
                        .downloader(new OkHttp3Downloader(SelfSigningClientBuilder.createClient(this)))
                        .build();
                picasso.load(coin.getImg()).into(ivQr);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    protected void onResume() {
        super.onResume();
        try {
            String cc = Util.currencySymbol(HomeActivity.defCurrency);
            if (wallet.getPrice().equalsIgnoreCase("") || wallet.getPrice().equalsIgnoreCase("false")) {
                tvBalance.setText(cc + " " + "0.00");
            } else {
                tvBalance.setText(cc + " " + wallet.getPrice());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void initUI() {
        tvAdd = (TextView) findViewById(R.id.tv_add);
        tvClose = (TextView) findViewById(R.id.tv_close);
        tvBalance = (TextView) findViewById(R.id.tv_balance);
        ivQr = (ImageView) findViewById(R.id.iv_qr);
        tvRemove = (TextView) findViewById(R.id.tv_remove);
        tvChange = (TextView) findViewById(R.id.tv_change_currency);
        tvCoin = (TextView) findViewById(R.id.tv_coin);
        tvWallet = (TextView) findViewById(R.id.tv_wallet);
        tvChange.setOnClickListener(this);
        tvRemove.setOnClickListener(this);
        tvClose.setOnClickListener(this);
        tvAdd.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_add:
                if (NetworkRecognizer.isNetworkAvailable(this) && !strSID.equalsIgnoreCase("")) {
                    showLoading();
                    addWalletApiCall(strSID, wallet);
                    Log.d("", "");
                } else {
                    wallet.setIsLocal("true");
                    wallet.setUserId("coins");
                    db.addWallet(wallet);
                    List<Wallet> wallets = db.getUserWallets("coins");
                    finish();
                    setWalletSetting();
                    Log.d("", "");
                }
                break;
            case R.id.tv_close:
                finish();
                break;
            case R.id.tv_remove:
                removeAlertDialog();
                break;
            case R.id.tv_change_currency:
                Intent intent = new Intent(this, CurrencyActivity.class);
                startActivity(intent);
                break;
        }
    }

    private void removeAlertDialog() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);

        // Setting Dialog Title
        alertDialog.setTitle("Remove wallet");

        // Setting Dialog Message
        alertDialog.setMessage("Are you sure you want to remove this wallet?");

        // Setting Icon to Dialog

        // Setting Positive "Yes" Button
        alertDialog.setPositiveButton(getString(R.string.ok), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                if (strSID.equalsIgnoreCase("")) {
                    db.removeWallet(wallet);
                    List<Wallet> wallets = db.getUserWallets("coins");
                    setWalletSetting();
                    finish();
                } else {
                    removeWalletApi();
                }

            }
        });

        // Setting Negative "NO" Button
        alertDialog.setNegativeButton(getString(R.string.cancel), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        // Showing Alert Message
        alertDialog.show();
    }


    private void addWalletApiCall(String sid, final Wallet wallet) {
        JsonObject body = new JsonObject();
        body.addProperty("wallet", wallet.getAddress());
        ApiHelper.getInstance(this).addCoin(sid, body, new ApiCallback.Listener() {
            @Override
            public void onSuccess(String result) {
                Log.d("", "");
                wallet.setIsLocal("false");
                wallet.setUserId(userID);
                //db.updateWalletMark(wallet);
                db.addWallet(wallet);
                setWalletSetting();
                updateDetails(wallet);
                finish();
                List<Wallet> wallets = db.getUserWallets(userID);
                hideLoading();
            }

            @Override
            public void onFailure(String error) {
                Log.d("", "");
                setWalletSetting();
                hideLoading();
            }
        });

    }

    private void updateWallets(final List<Wallet> wallets) {
        JsonObject bodyBal = new JsonObject();
        JsonArray jsonArray = new JsonArray();
        for (int i = 0; i < wallets.size(); i++) {
            Wallet wallet = wallets.get(i);
            jsonArray.add(wallet.getAddress());
        }
        bodyBal.add("wallet", jsonArray);
        bodyBal.addProperty("currency", HomeActivity.defCurrency);
        ApiHelper.getInstance(this).checkBalance(strSID, bodyBal, new ApiCallback.WalletListener() {
            @Override
            public void onSuccess(List<Wallet> result) {
                String filter;
                if (strSID.equalsIgnoreCase("")) {
                    filter = AppConstants._LOCAL;
                } else {
                    filter = userID;
                }
                wallet.setUserId(filter);
                db.addWalletsBalance(result);
                Wallet w = result.get(0);
                wallet.setBalance(w.getBalance());
                wallet.setCurrency(w.getCurrency());
                wallet.setPrice(w.getPrice());
                updateDetails(wallet);
                setWalletSetting();
                List<Wallet> wallets = db.getUserWallets(filter);
                Log.d("Coin ", "");
            }

            @Override
            public void onFailure(String error) {
                Log.d("Coin ", "");
            }
        });
        ApiHelper.getInstance(this).getMyWallets(strSID, bodyBal, new ApiCallback.WalletListener() {
            @Override
            public void onSuccess(List<Wallet> result) {
                String filter;
                if (strSID.equalsIgnoreCase("")) {
                    filter = AppConstants._LOCAL;
                    wallet.setUserId(filter);
                } else {
                    filter = userID;
                    wallet.setUserId(userID);
                }

                db.addWalletsInfo(result);
                Wallet w = result.get(0);
                wallet.setImg(w.getImg());
                wallet.setSlug(w.getSlug());
                updateDetails(wallet);
                setWalletSetting();
                List<Wallet> wallets = db.getUserWallets(filter);
                Log.d("Coin ", "");
            }

            @Override
            public void onFailure(String error) {
                Log.d("", "");
            }
        });
    }

    private void addWallet(String wallet) {
        showLoading();
        String sid = SharedPreferenceUtility.getInstance(this).getString(AppConstants._SID);
        JsonObject body = new JsonObject();
        body.addProperty("wallet", wallet);
        ApiHelper.getInstance(this).addCoin(sid, body, new ApiCallback.Listener() {
            @Override
            public void onSuccess(String result) {
                Log.d("", "");
                hideLoading();
            }

            @Override
            public void onFailure(String error) {
                Log.d("", "");
                hideLoading();
            }
        });
    }

    private void removeWalletApi() {
        showLoading();
        JsonObject object = new JsonObject();
        object.addProperty("wallet", wallet.getAddress());
        String sid = SharedPreferenceUtility.getInstance(this).getString(AppConstants._SID);
        ApiHelper.getInstance(this).removeCoins(sid, object, new ApiCallback.Listener() {
            @Override
            public void onSuccess(String result) {
                Log.d("", "");
                hideLoading();
                db.removeWallet(wallet);
                List<Wallet> wallets = db.getUserWallets(userID);
                setWalletSetting();
                Toast.makeText(WalletActivity.this, "Removed success", Toast.LENGTH_SHORT).show();
                finish();

            }

            @Override
            public void onFailure(String error) {
                Log.d("", "");
                hideLoading();
            }
        });
    }
}
