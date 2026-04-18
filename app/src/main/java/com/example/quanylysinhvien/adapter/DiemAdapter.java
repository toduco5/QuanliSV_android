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

// Adapter dùng để hiển thị danh sách điểm lên ListView
public class DiemAdapter extends BaseAdapter {

    // màn hình hiện tại
    Activity activity;

    // danh sách điểm
    ArrayList<Diem> list;

    // constructor nhận activity và list điểm
    public DiemAdapter(Activity activity, ArrayList<Diem> list) {
        this.activity = activity;
        this.list = list;
    }

    // số dòng hiển thị
    @Override
    public int getCount() {
        return list.size();
    }

    // lấy phần tử theo vị trí
    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    // id của dòng
    @Override
    public long getItemId(int position) {
        return position;
    }

    // giữ control của 1 dòng
    static class ViewHolder {
        TextView tvIdDiem;
        TextView tvMaSvDiem;
        TextView tvMaMonDiem;
        TextView tvDiem;
    }

    // tạo từng dòng cho ListView
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder holder;

        // nếu chưa có dòng thì tạo mới
        if (convertView == null) {

            holder = new ViewHolder();

            // nạp layout dòng điểm
            convertView = LayoutInflater.from(activity)
                    .inflate(R.layout.dong_xem_diem, parent, false);

            // ánh xạ control
            holder.tvIdDiem = convertView.findViewById(R.id.tvIdDiem);
            holder.tvMaSvDiem = convertView.findViewById(R.id.tvMaSvDiem);
            holder.tvMaMonDiem = convertView.findViewById(R.id.tvMaMonDiem);
            holder.tvDiem = convertView.findViewById(R.id.tvDiem);

            convertView.setTag(holder);

        } else {
            // lấy lại dòng cũ
            holder = (ViewHolder) convertView.getTag();
        }

        // lấy điểm tại vị trí đang hiển thị
        Diem d = list.get(position);

        // hiển thị dữ liệu
        holder.tvIdDiem.setText("ID: " + d.getId());
        holder.tvMaSvDiem.setText("Mã SV: " + d.getMaSv());
        holder.tvMaMonDiem.setText("Mã môn: " + d.getMaMon());

        float diem = d.getDiem();

        // hiện điểm 1 số lẻ
        holder.tvDiem.setText(
                "Điểm: " +
                        String.format(Locale.getDefault(), "%.1f", diem)
        );

        // đổi màu theo điểm
        if (diem < 5) {

            // rớt môn
            holder.tvDiem.setTextColor(Color.RED);

        } else if (diem < 8) {

            // khá
            holder.tvDiem.setTextColor(Color.parseColor("#FF9800"));

        } else {

            // giỏi
            holder.tvDiem.setTextColor(Color.parseColor("#1E88E5"));
        }

        return convertView;
    }

    // cập nhật lại dữ liệu mới
    public void updateData(ArrayList<Diem> newList) {

        list.clear();
        list.addAll(newList);

        // load lại ListView
        notifyDataSetChanged();
    }
}