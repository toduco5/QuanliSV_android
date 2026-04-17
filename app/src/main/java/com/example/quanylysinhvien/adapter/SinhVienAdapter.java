package com.example.quanylysinhvien.adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.quanylysinhvien.R;
import com.example.quanylysinhvien.database.DBHelper;
import com.example.quanylysinhvien.model.SinhVien;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class SinhVienAdapter extends BaseAdapter {

    Context context;
    ArrayList<SinhVien> ds;
    DBHelper dbHelper;
    OnSinhVienActionListener listener;

    public interface OnSinhVienActionListener {
        void onSua(SinhVien sv);
        void onReload();
    }

    public SinhVienAdapter(Context context, ArrayList<SinhVien> ds, OnSinhVienActionListener listener) {
        this.context = context;
        this.ds = ds;
        this.listener = listener;
        dbHelper = new DBHelper(context);
    }

    @Override
    public int getCount() {
        return ds.size();
    }

    @Override
    public Object getItem(int position) {
        return ds.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    static class ViewHolder {
        CircleImageView imageViewHinh;
        TextView tvMaSV, tvTenSV, tvEmailSV, tvLopSV;
        ImageView imageViewDelete;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;

        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.dong_sinhvien, parent, false);
            holder = new ViewHolder();

            holder.imageViewHinh = convertView.findViewById(R.id.imageViewHinh);
            holder.tvMaSV = convertView.findViewById(R.id.tvMaSV);
            holder.tvTenSV = convertView.findViewById(R.id.tvTenSV);
            holder.tvEmailSV = convertView.findViewById(R.id.tvEmailSV);
            holder.tvLopSV = convertView.findViewById(R.id.tvLopSV);
            holder.imageViewDelete = convertView.findViewById(R.id.imageViewDelete);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        SinhVien sv = ds.get(position);

        holder.tvMaSV.setText("MSSV: " + sv.getMaSv());
        holder.tvTenSV.setText("Tên: " + sv.getTenSv());
        holder.tvEmailSV.setText("Email: " + sv.getEmail());
        holder.tvLopSV.setText("Lớp: " + sv.getMaLop());

        setAvatar(holder.imageViewHinh, sv.getHinh());

        convertView.setOnClickListener(v -> {
            if (listener != null) {
                listener.onSua(sv);
            }
        });

        holder.imageViewDelete.setOnClickListener(v -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            builder.setTitle("Xóa sinh viên");
            builder.setMessage("Bạn có chắc muốn xóa sinh viên " + sv.getMaSv() + " không?");

            builder.setPositiveButton("Có", (dialog, which) -> {
                SQLiteDatabase db = dbHelper.getWritableDatabase();
                int kq = db.delete("SINHVIEN", "maSv=?", new String[]{sv.getMaSv()});
                db.close();

                if (kq > 0) {
                    Toast.makeText(context, "Xóa thành công", Toast.LENGTH_SHORT).show();
                    if (listener != null) {
                        listener.onReload();
                    }
                } else {
                    Toast.makeText(context, "Xóa thất bại", Toast.LENGTH_SHORT).show();
                }
            });

            builder.setNegativeButton("Không", null);
            builder.show();
        });

        return convertView;
    }

    private void setAvatar(CircleImageView imageView, String tenHinh) {
        String fileName = normalizeImageName(tenHinh);
        int resId = context.getResources().getIdentifier(fileName, "drawable", context.getPackageName());

        if (resId != 0) {
            imageView.setImageResource(resId);
        } else {
            imageView.setImageResource(R.drawable.avatamacdinh);
        }
    }

    private String normalizeImageName(String input) {
        if (input == null) return "";

        String s = input.trim().toLowerCase();

        if (s.endsWith(".png")) {
            s = s.substring(0, s.length() - 4);
        } else if (s.endsWith(".jpg")) {
            s = s.substring(0, s.length() - 4);
        } else if (s.endsWith(".jpeg")) {
            s = s.substring(0, s.length() - 5);
        }

        s = s.replace(" ", "_");
        return s;
    }
}