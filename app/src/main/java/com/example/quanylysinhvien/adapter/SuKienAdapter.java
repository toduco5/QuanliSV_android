package com.example.quanylysinhvien.adapter;

import android.app.Activity;
import android.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.quanylysinhvien.R;
import com.example.quanylysinhvien.dao.SuKienDao;
import com.example.quanylysinhvien.model.SuKien;

import java.util.ArrayList;

public class SuKienAdapter extends BaseAdapter {

    Activity activity;
    ArrayList<SuKien> list;
    SuKienDao suKienDao;

    public SuKienAdapter(Activity activity, ArrayList<SuKien> list) {
        this.activity = activity;
        this.list = list;
        this.suKienDao = new SuKienDao(activity);
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
        TextView tvMaSk, tvTenSk, tvNgaySk, tvNoiDung;
        Button btnSuaSk, btnXoaSk;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;

        if (convertView == null) {
            holder = new ViewHolder();
            convertView = LayoutInflater.from(activity).inflate(R.layout.item_sukien, parent, false);

            holder.tvMaSk = convertView.findViewById(R.id.tvMaSk);
            holder.tvTenSk = convertView.findViewById(R.id.tvTenSk);
            holder.tvNgaySk = convertView.findViewById(R.id.tvNgaySk);
            holder.tvNoiDung = convertView.findViewById(R.id.tvNoiDung);
            holder.btnSuaSk = convertView.findViewById(R.id.btnSuaSk);
            holder.btnXoaSk = convertView.findViewById(R.id.btnXoaSk);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        SuKien sk = list.get(position);

        holder.tvMaSk.setText("Mã SK: " + sk.getMaSk());
        holder.tvTenSk.setText("Tên: " + sk.getTenSk());
        holder.tvNgaySk.setText("Ngày: " + sk.getNgaySk());
        holder.tvNoiDung.setText("Nội dung: " + sk.getNoiDung());

        holder.btnXoaSk.setOnClickListener(v -> {
            new AlertDialog.Builder(activity)
                    .setTitle("Xóa sự kiện")
                    .setMessage("Bạn có chắc muốn xóa không?")
                    .setPositiveButton("Xóa", (dialog, which) -> {
                        boolean kq = suKienDao.delete(sk.getMaSk());
                        if (kq) {
                            list.remove(position);
                            notifyDataSetChanged();
                            Toast.makeText(activity, "Xóa thành công", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(activity, "Xóa thất bại", Toast.LENGTH_SHORT).show();
                        }
                    })
                    .setNegativeButton("Hủy", null)
                    .show();
        });

        holder.btnSuaSk.setOnClickListener(v -> showDialogSua(sk));

        return convertView;
    }

    private void showDialogSua(SuKien sk) {
        View view = LayoutInflater.from(activity).inflate(R.layout.dialog_sukien, null);

        EditText edtMaSk = view.findViewById(R.id.edtMaSk);
        EditText edtTenSk = view.findViewById(R.id.edtTenSk);
        EditText edtNgaySk = view.findViewById(R.id.edtNgaySk);
        EditText edtNoiDungSk = view.findViewById(R.id.edtNoiDungSk);

        edtMaSk.setText(sk.getMaSk());
        edtMaSk.setEnabled(false);
        edtTenSk.setText(sk.getTenSk());
        edtNgaySk.setText(sk.getNgaySk());
        edtNoiDungSk.setText(sk.getNoiDung());

        new AlertDialog.Builder(activity)
                .setTitle("Sửa sự kiện")
                .setView(view)
                .setPositiveButton("Lưu", (dialog, which) -> {
                    String ten = edtTenSk.getText().toString().trim();
                    String ngay = edtNgaySk.getText().toString().trim();
                    String noiDung = edtNoiDungSk.getText().toString().trim();

                    if (ten.isEmpty() || ngay.isEmpty() || noiDung.isEmpty()) {
                        Toast.makeText(activity, "Nhập đầy đủ thông tin", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    SuKien skMoi = new SuKien(sk.getMaSk(), ten, ngay, noiDung);
                    boolean kq = suKienDao.update(skMoi);

                    if (kq) {
                        sk.setTenSk(ten);
                        sk.setNgaySk(ngay);
                        sk.setNoiDung(noiDung);
                        notifyDataSetChanged();
                        Toast.makeText(activity, "Cập nhật thành công", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(activity, "Cập nhật thất bại", Toast.LENGTH_SHORT).show();
                    }
                })
                .setNegativeButton("Hủy", null)
                .show();
    }
}