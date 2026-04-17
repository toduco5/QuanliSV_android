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
import com.example.quanylysinhvien.dao.DaoTaiKhoan;
import com.example.quanylysinhvien.database.DBHelper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;

public class XemDiemActivity extends AppCompatActivity {

    TextView tvTitle, tvThongBao;
    ListView lvDiem;

    DBHelper dbHelper;
    DaoTaiKhoan daoTaiKhoan;

    String tenDangNhap, maSv;

    ArrayList<HashMap<String, String>> list;
    SimpleAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_xem_diem);

        anhXa();

        dbHelper = new DBHelper(this);
        daoTaiKhoan = new DaoTaiKhoan(this);

        tenDangNhap = getIntent().getStringExtra("tenDangNhap");
        maSv = getIntent().getStringExtra("maSv");

        if (tenDangNhap == null) tenDangNhap = "";

        if (maSv == null || maSv.trim().isEmpty()) {
            maSv = daoTaiKhoan.getMaSvTheoTaiKhoan(tenDangNhap);
        }

        if (maSv == null || maSv.trim().isEmpty()) {
            tvTitle.setText("Bảng điểm");
            tvThongBao.setText("Tài khoản này chưa được gán sinh viên.");
            Toast.makeText(this,
                    "Admin chưa gán sinh viên cho tài khoản này",
                    Toast.LENGTH_SHORT).show();
            return;
        }

        loadDiemTheoMaSV();
    }

    private void anhXa() {
        tvTitle = findViewById(R.id.tvTitleXemDiem);
        tvThongBao = findViewById(R.id.tvThongBaoDiem);
        lvDiem = findViewById(R.id.lvXemDiem);
    }

    private void loadDiemTheoMaSV() {

        list = new ArrayList<>();

        SQLiteDatabase db = dbHelper.getReadableDatabase();

        String tenSV = "";
        String lop = "";
        String nganh = "";

        Cursor cSinhVien = db.rawQuery(
                "SELECT tenSV, maLop, maNganh FROM SINHVIEN WHERE maSv=?",
                new String[]{maSv}
        );

        if (cSinhVien.moveToFirst()) {
            tenSV = cSinhVien.getString(0);
            lop = cSinhVien.getString(1);
            nganh = cSinhVien.getString(2);
        }

        cSinhVien.close();

        if (tenSV.isEmpty()) {
            tvTitle.setText("Bảng điểm");
            tvThongBao.setText("Không tìm thấy sinh viên.");
            db.close();
            return;
        }

        tvTitle.setText("Bảng điểm của " + tenSV);

        Cursor cDiem = db.rawQuery(
                "SELECT DIEM.maSv, SINHVIEN.tenSV, " +
                        "MONHOC.maMon, MONHOC.tenMon, MONHOC.soTinChi, DIEM.diem " +
                        "FROM DIEM " +
                        "INNER JOIN SINHVIEN ON DIEM.maSv = SINHVIEN.maSv " +
                        "INNER JOIN MONHOC ON DIEM.maMon = MONHOC.maMon " +
                        "WHERE DIEM.maSv=? " +
                        "ORDER BY MONHOC.maMon ASC",
                new String[]{maSv}
        );

        float tongDiem = 0;
        int tongTinChi = 0;

        if (cDiem.moveToFirst()) {

            do {
                String maSinhVien = cDiem.getString(0);
                String tenSinhVien = cDiem.getString(1);
                String maMon = cDiem.getString(2);
                String tenMon = cDiem.getString(3);
                int soTinChi = cDiem.getInt(4);
                float diem = cDiem.getFloat(5);

                HashMap<String, String> map = new HashMap<>();
                map.put("maSv", "Mã SV: " + maSinhVien);
                map.put("tenSv", "Tên SV: " + tenSinhVien);
                map.put("maMon", "Mã môn: " + maMon);
                map.put("tenMon", "Tên môn: " + tenMon);
                map.put("soTinChi", "Số tín chỉ: " + soTinChi);
                map.put("diem", "Điểm: " + diem);

                list.add(map);

                tongDiem += diem * soTinChi;
                tongTinChi += soTinChi;

            } while (cDiem.moveToNext());

            float gpa = 0;
            if (tongTinChi > 0) {
                gpa = tongDiem / tongTinChi;
            }

            tvThongBao.setText(
                    "MSSV: " + maSv +
                            " | Lớp: " + lop +
                            " | Ngành: " + nganh +
                            "\nTổng môn: " + list.size() +
                            " | GPA: " + String.format(Locale.getDefault(), "%.2f", gpa)
            );

        } else {
            tvThongBao.setText(
                    "MSSV: " + maSv +
                            "\nSinh viên chưa có điểm."
            );
        }

        cDiem.close();
        db.close();

        adapter = new SimpleAdapter(
                this,
                list,
                R.layout.dong_xem_diem_user,
                new String[]{"maSv", "tenSv", "maMon", "tenMon", "soTinChi", "diem"},
                new int[]{
                        R.id.tvMaSvXem,
                        R.id.tvTenSvXem,
                        R.id.tvMaMonXem,
                        R.id.tvTenMonXem,
                        R.id.tvSoTinChiXem,
                        R.id.tvDiemXem
                }
        );

        lvDiem.setAdapter(adapter);
    }
}