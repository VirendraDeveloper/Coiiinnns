package com.coldstoragecoins;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.coldstoragecoins.models.DefCurrency;
import com.coldstoragecoins.network.ApiCallback;
import com.coldstoragecoins.network.ApiHelper;
import com.coldstoragecoins.utils.CurrencyAdapter;

import java.util.List;

public class CurrencyActivity extends BaseActivity implements View.OnClickListener, AdapterView.OnItemClickListener {
    private TextView tvDone, tvRefresh;
    private ListView lvCurrency;
    private CurrencyAdapter mAdapter;
    private TextView tvUSD, tvEUR, tvGBP;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_currency);
        tvDone = (TextView) findViewById(R.id.tv_done);
        tvRefresh = (TextView) findViewById(R.id.tv_refresh);
        tvUSD = (TextView) findViewById(R.id.tv_usd);
        tvEUR = (TextView) findViewById(R.id.tv_eur);
        tvGBP = (TextView) findViewById(R.id.tv_gbp);
        lvCurrency = (ListView) findViewById(R.id.lv_currency);
        tvRefresh.setOnClickListener(this);
        tvDone.setOnClickListener(this);
        lvCurrency.setOnItemClickListener(this);
        tvUSD.setOnClickListener(this);
        tvEUR.setOnClickListener(this);
        tvGBP.setOnClickListener(this);
        getCurrencyList();
    }

    private void getCurrencyList() {
        showLoading();
        ApiHelper.getInstance(this).getCurrencyDetails(new ApiCallback.CurrencyListener() {
            @Override
            public void onSuccess(List<DefCurrency> result) {
                hideLoading();
                mAdapter = new CurrencyAdapter(CurrencyActivity.this, result);
                lvCurrency.setAdapter(mAdapter);
            }

            @Override
            public void onFailure(String error) {
                hideLoading();
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_done:
                finish();
                break;
            case R.id.tv_usd:
                HomeActivity.defCurrency = "USD";
                finish();
                break;
            case R.id.tv_eur:
                HomeActivity.defCurrency ="EUR";
                finish();
                break;
            case R.id.tv_gbp:
                HomeActivity.defCurrency ="GBP";
                finish();
                break;
            case R.id.tv_refresh:
                getCurrencyList();
                break;
        }
    }


    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        DefCurrency defaultCurrency = (DefCurrency) mAdapter.getItem(position);
        HomeActivity.defCurrency = defaultCurrency.getCode();
        finish();
    }
}
