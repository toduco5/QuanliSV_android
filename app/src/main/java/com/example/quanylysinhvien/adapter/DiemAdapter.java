package com.example.quanylysinhvien.adapter;

import android.app.Activity;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.quanylysinhvien.R;
import com.example.quanylysinhvien.model.Diem;

import java.util.ArrayList;
import java.util.Locale;

public class DiemAdapter extends BaseAdapter {

    Activity activity;
    ArrayList<Diem> list;

    public DiemAdapter(Activity activity, ArrayList<Diem> list) {
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
        TextView tvIdDiem;
        TextView tvMaSvDiem;
        TextView tvMaMonDiem;
        TextView tvDiem;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder holder;

        if (convertView == null) {

            holder = new ViewHolder();

            convertView = LayoutInflater.from(activity)
                    .inflate(R.layout.dong_xem_diem, parent, false);

            holder.tvIdDiem = convertView.findViewById(R.id.tvIdDiem);
            holder.tvMaSvDiem = convertView.findViewById(R.id.tvMaSvDiem);
            holder.tvMaMonDiem = convertView.findViewById(R.id.tvMaMonDiem);
            holder.tvDiem = convertView.findViewById(R.id.tvDiem);

            convertView.setTag(holder);

        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        Diem d = list.get(position);

        holder.tvIdDiem.setText("ID: " + d.getId());
        holder.tvMaSvDiem.setText("Mã SV: " + d.getMaSv());
        holder.tvMaMonDiem.setText("Mã môn: " + d.getMaMon());

        float diem = d.getDiem();

        holder.tvDiem.setText(
                "Điểm: " + String.format(Locale.getDefault(), "%.1f", diem)
        );

        // Màu theo điểm
        if (diem < 5) {
            holder.tvDiem.setTextColor(Color.RED);
        } else if (diem < 8) {
            holder.tvDiem.setTextColor(Color.parseColor("#FF9800"));
        } else {
            holder.tvDiem.setTextColor(Color.parseColor("#1E88E5"));
        }

        return convertView;
    }

    // refresh dữ liệu
    public void updateData(ArrayList<Diem> newList) {
        list.clear();
        list.addAll(newList);
        notifyDataSetChanged();
    }
}