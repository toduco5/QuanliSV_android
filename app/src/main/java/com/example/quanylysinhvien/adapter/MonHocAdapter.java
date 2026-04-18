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

// Adapter dùng để đưa danh sách môn học lên ListView
public class MonHocAdapter extends BaseAdapter {

    // Màn hình hiện tại
    Activity activity;

    // Danh sách môn học cần hiển thị
    ArrayList<MonHoc> list;

    // Constructor nhận Activity và danh sách môn học
    public MonHocAdapter(Activity activity, ArrayList<MonHoc> list) {
        this.activity = activity;
        this.list = list;
    }

    // Trả về số lượng môn học
    // Nếu có 5 môn học thì ListView hiện 5 dòng
    @Override
    public int getCount() {
        return list.size();
    }

    // Lấy 1 môn học theo vị trí
    // position = 0 là dòng đầu tiên
    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    // Trả về id của dòng
    // Ở đây lấy luôn vị trí làm id
    @Override
    public long getItemId(int position) {
        return position;
    }

    // ViewHolder dùng để giữ các TextView của 1 dòng
    // Giúp ListView chạy mượt hơn
    static class ViewHolder {
        TextView tvMaMon;
        TextView tvTenMon;
        TextView tvSoTinChi;
    }

    // Hàm quan trọng nhất
    // Dùng để tạo từng dòng trong ListView
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder holder;

        // Nếu dòng chưa có giao diện thì tạo mới
        if (convertView == null) {

            holder = new ViewHolder();

            // Nạp file XML của 1 dòng môn học
            convertView = LayoutInflater.from(activity)
                    .inflate(R.layout.dong_xem_mon_hoc, parent, false);

            // Ánh xạ TextView trong XML
            holder.tvMaMon = convertView.findViewById(R.id.tvMaMon);
            holder.tvTenMon = convertView.findViewById(R.id.tvTenMon);
            holder.tvSoTinChi = convertView.findViewById(R.id.tvSoTinChi);

            // Gắn holder vào dòng để dùng lại lần sau
            convertView.setTag(holder);

        } else {

            // Nếu đã có dòng cũ thì lấy lại holder
            holder = (ViewHolder) convertView.getTag();
        }

        // Lấy môn học tại vị trí đang hiển thị
        MonHoc mh = list.get(position);

        // Hiển thị mã môn lên dòng
        holder.tvMaMon.setText("Mã môn: " + mh.getMaMon());

        // Hiển thị tên môn
        holder.tvTenMon.setText("Tên môn: " + mh.getTenMon());

        // Hiển thị số tín chỉ
        holder.tvSoTinChi.setText("Số tín chỉ: " + mh.getSoTinChi());

        // Trả về dòng hoàn chỉnh cho ListView
        return convertView;
    }
}