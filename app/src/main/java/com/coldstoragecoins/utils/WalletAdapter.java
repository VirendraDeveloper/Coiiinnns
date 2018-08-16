package com.coldstoragecoins.utils;

import android.app.Activity;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.coldstoragecoins.HomeActivity;
import com.coldstoragecoins.R;
import com.coldstoragecoins.WalletActivity;
import com.coldstoragecoins.models.Wallet;

import java.util.ArrayList;
import java.util.List;

/**
 * This class is used as
 *
 * @author DreamWorksSoftwares
 * @version 1.0
 * @since 09 Aug,2018
 */
public class WalletAdapter extends BaseAdapter {
    private final LayoutInflater inflater;
    private Activity mContext;
    private List<Wallet> coins = new ArrayList<>();

    public WalletAdapter(Activity mContext) {
        this.mContext = mContext;
        inflater = LayoutInflater.from(this.mContext);
    }

    public void addWallets(List<Wallet> chapterInfos) {
        coins.clear();
        coins.addAll(chapterInfos);
        notifyDataSetChanged();
    }

    public List<Wallet> getCoins() {
        return coins;
    }

    @Override
    public int getCount() {
        return coins.size();
    }

    @Override
    public Object getItem(int i) {
        return coins.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View convertView, ViewGroup parent) {
        ViewHolder mViewHolder;
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.wallet_item, parent, false);
            mViewHolder = new ViewHolder(convertView);
            convertView.setTag(mViewHolder);
        } else {
            mViewHolder = (ViewHolder) convertView.getTag();
        }
        final Wallet coin = coins.get(i);

        try {
            if (coin.getSlug().equalsIgnoreCase("")) {
                mViewHolder.tvWallet.setText(coin.getAddress());
            } else {
                mViewHolder.tvWallet.setText(coin.getSlug());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            if (coin.getPrice().equalsIgnoreCase("") || coin.getPrice().equalsIgnoreCase("false")) {
                mViewHolder.tvBalance.setText(Util.currencySymbol(HomeActivity.defCurrency) + " " + "0.00");
            } else {
                mViewHolder.tvBalance.setText(Util.currencySymbol(HomeActivity.defCurrency) + " " + coin.getPrice());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            mViewHolder.tvCoin.setText(coin.getBalance() + " " + coin.getCurrency());
        } catch (Exception e) {
            e.printStackTrace();
        }

        mViewHolder.ivInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, WalletActivity.class);
                intent.putExtra("Wallet", coin);
                mContext.startActivity(intent);
            }
        });
        return convertView;
    }

    private class ViewHolder {
        private TextView tvCoin, tvWallet, tvBalance;
        private ImageView ivInfo;

        public ViewHolder(View item) {
            ivInfo = (ImageView) item.findViewById(R.id.iv_info);
            tvCoin = (TextView) item.findViewById(R.id.tv_coin);
            tvWallet = (TextView) item.findViewById(R.id.tv_wallet);
            tvBalance = (TextView) item.findViewById(R.id.tv_balance);
        }
    }
}
