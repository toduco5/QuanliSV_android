package com.example.quanylysinhvien.user;

import androidx.appcompat.app.AppCompatActivity;

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

public class XemDiemActivity extends AppCompatActivity {

    TextView tvTitle, tvThongBao;
    ListView lvDiem;

    DBHelper dbHelper;
    String tenDangNhap;

    ArrayList<HashMap<String, String>> list;
    SimpleAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_xem_diem);

        anhXa();

        dbHelper = new DBHelper(this);

        tenDangNhap = getIntent().getStringExtra("tenDangNhap");
        if (tenDangNhap == null) {
            tenDangNhap = "";
        }

        loadDiem();
    }

    private void anhXa() {
        tvTitle = findViewById(R.id.tvTitleXemDiem);
        tvThongBao = findViewById(R.id.tvThongBaoDiem);
        lvDiem = findViewById(R.id.lvXemDiem);
    }

    private void loadDiem() {
        list = new ArrayList<>();
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        Cursor cSinhVien = db.rawQuery(
                "SELECT maSv, tenSV FROM SINHVIEN WHERE email LIKE ? OR maSv = ?",
                new String[]{"%" + tenDangNhap + "%", tenDangNhap}
        );

        if (!cSinhVien.moveToFirst()) {
            tvThongBao.setText("Không tìm thấy thông tin sinh viên.");
            Toast.makeText(this, "Không tìm thấy sinh viên phù hợp", Toast.LENGTH_SHORT).show();
            cSinhVien.close();
            db.close();
            return;
        }

        String maSv = cSinhVien.getString(0);
        String tenSV = cSinhVien.getString(1);
        cSinhVien.close();

        tvTitle.setText("Bảng điểm của " + tenSV);

        Cursor cDiem = db.rawQuery(
                "SELECT MONHOC.maMon, MONHOC.tenMon, MONHOC.soTinChi, DIEM.diem " +
                        "FROM DIEM INNER JOIN MONHOC ON DIEM.maMon = MONHOC.maMon " +
                        "WHERE DIEM.maSv = ?",
                new String[]{maSv}
        );

        if (cDiem.moveToFirst()) {
            do {
                HashMap<String, String> map = new HashMap<>();
                map.put("maMon", "Mã môn: " + cDiem.getString(0));
                map.put("tenMon", "Tên môn: " + cDiem.getString(1));
                map.put("soTinChi", "Số tín chỉ: " + cDiem.getInt(2));
                map.put("diem", "Điểm: " + cDiem.getFloat(3));
                list.add(map);
            } while (cDiem.moveToNext());

            tvThongBao.setText("Tổng số môn: " + list.size());
        } else {
            tvThongBao.setText("Sinh viên này chưa có điểm.");
        }

        cDiem.close();
        db.close();

        adapter = new SimpleAdapter(
                this,
                list,
                R.layout.dong_xem_diem_user,
                new String[]{"maMon", "tenMon", "soTinChi", "diem"},
                new int[]{R.id.tvMaMonXem, R.id.tvTenMonXem, R.id.tvSoTinChiXem, R.id.tvDiemXem}
        );

        lvDiem.setAdapter(adapter);
    }
}