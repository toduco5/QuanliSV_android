package com.example.quanylysinhvien.adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.quanylysinhvien.R;
import com.example.quanylysinhvien.model.Nganh;

import java.util.ArrayList;

public class NganhAdapter extends BaseAdapter {
    Activity activity;
    ArrayList<Nganh> list;

    public NganhAdapter(Activity activity, ArrayList<Nganh> list) {
        this.activity = activity;
        this.list = list;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    static class ViewHolder {
        TextView tvMaNganh, tvTenNganh;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;

        if (convertView == null) {
            holder = new ViewHolder();
            convertView = LayoutInflater.from(activity).inflate(R.layout.dong_nganh, parent, false);

            holder.tvMaNganh = convertView.findViewById(R.id.tvMaNganh);
            holder.tvTenNganh = convertView.findViewById(R.id.tvTenNganh);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        Nganh n = list.get(position);
        holder.tvMaNganh.setText("Mã ngành: " + n.getMaNganh());
        holder.tvTenNganh.setText("Tên ngành: " + n.getTenNganh());

        return convertView;
    }
}