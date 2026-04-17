package com.example.quanylysinhvien.admin;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.example.quanylysinhvien.R;
import com.example.quanylysinhvien.database.DBHelper;

import java.util.ArrayList;
import java.util.HashMap;

public class DanhSachSinhVienActivity extends AppCompatActivity {

    TextView tvTitleSinhVien, tvThongBaoSinhVien;
    ListView lvSinhVien;

    DBHelper dbHelper;

    ArrayList<HashMap<String, String>> list;
    SimpleAdapter adapter;

    String maSvChon = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_danh_sach_sinh_vien);

        anhXa();

        dbHelper = new DBHelper(this);

        loadSinhVien();

        lvSinhVien.setOnItemClickListener((parent, view, position, id) -> {
            maSvChon = list.get(position).get("maSvRaw");
            menuSinhVien();
        });
    }

    private void anhXa() {
        tvTitleSinhVien = findViewById(R.id.tvTitleSinhVien);
        tvThongBaoSinhVien = findViewById(R.id.tvThongBaoSinhVien);
        lvSinhVien = findViewById(R.id.lvSinhVien);
    }

    private void loadSinhVien() {
        list = new ArrayList<>();

        SQLiteDatabase db = dbHelper.getReadableDatabase();

        Cursor c = db.rawQuery(
                "SELECT maSv, tenSV, email, maLop FROM SINHVIEN",
                null
        );

        if (c.moveToFirst()) {
            do {
                HashMap<String, String> map = new HashMap<>();

                String maSv = c.getString(0);
                String tenSv = c.getString(1);
                String email = c.getString(2);
                String lop = c.getString(3);

                map.put("maSvRaw", maSv);
                map.put("maSv", "MSSV: " + maSv);
                map.put("tenSv", "Họ tên: " + tenSv);
                map.put("email", "Email: " + email);
                map.put("lop", "Lớp: " + lop);

                list.add(map);

            } while (c.moveToNext());
        }

        c.close();
        db.close();

        tvThongBaoSinhVien.setText("Tổng sinh viên: " + list.size());

        adapter = new SimpleAdapter(
                this,
                list,
                R.layout.dong_sinhvien,
                new String[]{"maSv", "tenSv", "email", "lop"},
                new int[]{
                        R.id.tvMaSV,
                        R.id.tvTenSV,
                        R.id.tvEmailSV,
                        R.id.tvLopSV
                }
        );

        lvSinhVien.setAdapter(adapter);
    }

    private void menuSinhVien() {
        String[] chucNang = {
                "Sửa sinh viên",
                "Xóa sinh viên"
        };

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Chọn chức năng");

        builder.setItems(chucNang, (dialog, which) -> {
            if (which == 0) {
                suaSinhVien();
            } else {
                xoaSinhVien();
            }
        });

        builder.show();
    }

    private void suaSinhVien() {
        Intent intent = new Intent(this, ThemSinhVienActivity.class);
        intent.putExtra("maSv", maSvChon);
        intent.putExtra("cheDo", "sua");
        startActivity(intent);
    }

    private void xoaSinhVien() {
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        int kq = db.delete(
                "SINHVIEN",
                "maSv=?",
                new String[]{maSvChon}
        );

        db.close();

        if (kq > 0) {
            Toast.makeText(this, "Xóa thành công", Toast.LENGTH_SHORT).show();
            loadSinhVien();
        } else {
            Toast.makeText(this, "Xóa thất bại", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadSinhVien();
    }
}