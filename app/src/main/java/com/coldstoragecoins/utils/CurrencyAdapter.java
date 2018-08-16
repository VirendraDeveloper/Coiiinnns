package com.coldstoragecoins.utils;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.coldstoragecoins.R;
import com.coldstoragecoins.models.DefCurrency;

import java.util.List;

/**
 * This class is used as
 *
 * @author DreamWorksSoftwares
 * @version 1.0
 * @since 07 Aug,2018
 */
public class CurrencyAdapter extends BaseAdapter {
    private final LayoutInflater inflater;
    private Activity mContext;
    private List<DefCurrency> chapterInfos;

    public CurrencyAdapter(Activity mContext, List<DefCurrency> chapterInfos) {
        this.mContext = mContext;
        this.chapterInfos = chapterInfos;
        inflater = LayoutInflater.from(this.mContext);
    }

    @Override
    public int getCount() {
        return chapterInfos.size();
    }

    @Override
    public Object getItem(int i) {
        return chapterInfos.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View convertView, ViewGroup parent) {
        ViewHolder mViewHolder;
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.def_item, parent, false);
            mViewHolder = new ViewHolder(convertView);
            convertView.setTag(mViewHolder);
        } else {
            mViewHolder = (ViewHolder) convertView.getTag();
        }
        DefCurrency defCurrency = chapterInfos.get(i);
        mViewHolder.tvChapterName.setText(defCurrency.getName()+" ("+ defCurrency.getCode()+")");

        return convertView;
    }

    private class ViewHolder {
        private TextView tvChapterName;

        public ViewHolder(View item) {
            tvChapterName = (TextView) item.findViewById(R.id.tv_def);
        }
    }
}
