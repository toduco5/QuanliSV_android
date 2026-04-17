package com.example.quanylysinhvien.adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.quanylysinhvien.R;
import com.example.quanylysinhvien.model.MonHoc;

import java.util.ArrayList;

public class MonHocAdapter extends BaseAdapter {
    Activity activity;
    ArrayList<MonHoc> list;

    public MonHocAdapter(Activity activity, ArrayList<MonHoc> list) {
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
        TextView tvMaMon, tvTenMon, tvSoTinChi;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;

        if (convertView == null) {
            holder = new ViewHolder();
            convertView = LayoutInflater.from(activity).inflate(R.layout.dong_xem_mon_hoc, parent, false);

            holder.tvMaMon = convertView.findViewById(R.id.tvMaMon);
            holder.tvTenMon = convertView.findViewById(R.id.tvTenMon);
            holder.tvSoTinChi = convertView.findViewById(R.id.tvSoTinChi);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        MonHoc mh = list.get(position);
        holder.tvMaMon.setText("Mã môn: " + mh.getMaMon());
        holder.tvTenMon.setText("Tên môn: " + mh.getTenMon());
        holder.tvSoTinChi.setText("Số tín chỉ: " + mh.getSoTinChi());

        return convertView;
    }
}