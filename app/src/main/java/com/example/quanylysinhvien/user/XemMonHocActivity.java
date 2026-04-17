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

public class XemMonHocActivity extends AppCompatActivity {

    TextView tvTitleMonHoc, tvThongBaoMonHoc;
    ListView lvMonHoc;

    DBHelper dbHelper;
    DaoTaiKhoan daoTaiKhoan;

    String tenDangNhap, maSv;

    ArrayList<HashMap<String, String>> list;
    SimpleAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_xem_mon_hoc);

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
            tvTitleMonHoc.setText("Danh sách môn học");
            tvThongBaoMonHoc.setText("Tài khoản này chưa được gán sinh viên.");
            Toast.makeText(this, "Admin chưa gán sinh viên cho tài khoản này", Toast.LENGTH_SHORT).show();
            return;
        }

        loadMonHocTheoLop();
    }

    private void anhXa() {
        tvTitleMonHoc = findViewById(R.id.tvTitleMonHoc);
        tvThongBaoMonHoc = findViewById(R.id.tvThongBaoMonHoc);
        lvMonHoc = findViewById(R.id.lvMonHocUser);
    }

    private void loadMonHocTheoLop() {
        list = new ArrayList<>();

        SQLiteDatabase db = dbHelper.getReadableDatabase();

        String tenSV = "";
        String maLop = "";
        String tenLop = "";

        Cursor cSinhVien = db.rawQuery(
                "SELECT SINHVIEN.tenSV, SINHVIEN.maLop, LOP.tenLop " +
                        "FROM SINHVIEN " +
                        "INNER JOIN LOP ON SINHVIEN.maLop = LOP.maLop " +
                        "WHERE SINHVIEN.maSv=?",
                new String[]{maSv}
        );

        if (cSinhVien.moveToFirst()) {
            tenSV = cSinhVien.getString(0);
            maLop = cSinhVien.getString(1);
            tenLop = cSinhVien.getString(2);
        }

        cSinhVien.close();

        if (maLop.isEmpty()) {
            tvTitleMonHoc.setText("Danh sách môn học");
            tvThongBaoMonHoc.setText("Không tìm thấy lớp của sinh viên.");
            db.close();
            return;
        }

        Cursor c = db.rawQuery(
                "SELECT MONHOC.maMon, MONHOC.tenMon, MONHOC.soTinChi, NGANH.tenNganh, " +
                        "LOP_MONHOC.hocKy, LOP_MONHOC.namHoc " +
                        "FROM LOP_MONHOC " +
                        "INNER JOIN MONHOC ON LOP_MONHOC.maMon = MONHOC.maMon " +
                        "INNER JOIN NGANH ON MONHOC.maNganh = NGANH.maNganh " +
                        "WHERE LOP_MONHOC.maLop=? " +
                        "ORDER BY MONHOC.maMon ASC",
                new String[]{maLop}
        );

        if (c.moveToFirst()) {
            do {
                HashMap<String, String> map = new HashMap<>();
                map.put("maMon", "Mã môn: " + c.getString(0));
                map.put("tenMon", "Tên môn: " + c.getString(1));
                map.put("soTinChi", "Số tín chỉ: " + c.getInt(2));
                map.put("tenNganh", "Ngành: " + c.getString(3));
                map.put("hocKy", "Học kỳ: " + c.getInt(4));
                map.put("namHoc", "Năm học: " + c.getString(5));

                list.add(map);
            } while (c.moveToNext());

            tvTitleMonHoc.setText("Môn học của " + tenSV);
            tvThongBaoMonHoc.setText(
                    "Lớp: " + tenLop + " (" + maLop + ")" +
                            "\nTổng số môn học: " + list.size()
            );

        } else {
            tvTitleMonHoc.setText("Danh sách môn học");
            tvThongBaoMonHoc.setText("Lớp này chưa được gán môn học.");
            Toast.makeText(this, "Không tìm thấy môn học theo lớp", Toast.LENGTH_SHORT).show();
        }

        c.close();
        db.close();

        adapter = new SimpleAdapter(
                this,
                list,
                R.layout.dong_xem_mon_hoc,
                new String[]{"maMon", "tenMon", "soTinChi", "tenNganh", "hocKy", "namHoc"},
                new int[]{R.id.tvMaMon, R.id.tvTenMon, R.id.tvSoTinChi, R.id.tvTenNganhMon, R.id.tvHocKyMon, R.id.tvNamHocMon}
        );

        lvMonHoc.setAdapter(adapter);
    }
}