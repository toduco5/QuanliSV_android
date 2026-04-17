package com.example.quanylysinhvien.user;

import androidx.appcompat.app.AppCompatActivity;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import com.example.quanylysinhvien.R;
import com.example.quanylysinhvien.dao.DaoTaiKhoan;
import com.example.quanylysinhvien.database.DBHelper;

public class ThongTinCaNhanActivity extends AppCompatActivity {

    TextView tvTaiKhoan, tvMaSV, tvHoTen, tvEmail, tvLop, tvNganh;

    DBHelper dbHelper;
    DaoTaiKhoan daoTaiKhoan;

    String tenDangNhap;
    String maSv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_thong_tin_ca_nhan);

        anhXa();

        dbHelper = new DBHelper(this);
        daoTaiKhoan = new DaoTaiKhoan(this);

        tenDangNhap = getIntent().getStringExtra("tenDangNhap");
        if (tenDangNhap == null) {
            tenDangNhap = "";
        }

        maSv = getIntent().getStringExtra("maSv");
        if (maSv == null || maSv.trim().isEmpty()) {
            maSv = daoTaiKhoan.getMaSvTheoTaiKhoan(tenDangNhap);
        }

        hienThiThongTin();
    }

    private void anhXa() {
        tvTaiKhoan = findViewById(R.id.tvTaiKhoan);
        tvMaSV = findViewById(R.id.tvMaSV);
        tvHoTen = findViewById(R.id.tvHoTen);
        tvEmail = findViewById(R.id.tvEmail);
        tvLop = findViewById(R.id.tvLop);
        tvNganh = findViewById(R.id.tvNganh);
    }

    private void hienThiThongTin() {
        tvTaiKhoan.setText("Tài khoản: " + tenDangNhap);

        if (maSv == null || maSv.trim().isEmpty()) {
            hienThiKhongCoDuLieu();
            Toast.makeText(this, "Tài khoản này chưa được gán sinh viên", Toast.LENGTH_SHORT).show();
            return;
        }

        SQLiteDatabase db = dbHelper.getReadableDatabase();

        Cursor c = db.rawQuery(
                "SELECT SINHVIEN.maSv, SINHVIEN.tenSV, SINHVIEN.email, LOP.tenLop, NGANH.tenNganh " +
                        "FROM SINHVIEN " +
                        "INNER JOIN LOP ON SINHVIEN.maLop = LOP.maLop " +
                        "INNER JOIN NGANH ON SINHVIEN.maNganh = NGANH.maNganh " +
                        "WHERE SINHVIEN.maSv = ?",
                new String[]{maSv}
        );

        if (c.moveToFirst()) {
            String maSV = c.getString(0);
            String hoTen = c.getString(1);
            String email = c.getString(2);
            String lop = c.getString(3);
            String nganh = c.getString(4);

            tvMaSV.setText("Mã sinh viên: " + maSV);
            tvHoTen.setText("Họ tên: " + hoTen);
            tvEmail.setText("Email: " + email);
            tvLop.setText("Lớp: " + lop);
            tvNganh.setText("Ngành: " + nganh);
        } else {
            hienThiKhongCoDuLieu();
            Toast.makeText(this, "Chưa tìm thấy thông tin cá nhân", Toast.LENGTH_SHORT).show();
        }

        c.close();
        db.close();
    }

    private void hienThiKhongCoDuLieu() {
        tvMaSV.setText("Mã sinh viên: Chưa có dữ liệu");
        tvHoTen.setText("Họ tên: Chưa có dữ liệu");
        tvEmail.setText("Email: Chưa có dữ liệu");
        tvLop.setText("Lớp: Chưa có dữ liệu");
        tvNganh.setText("Ngành: Chưa có dữ liệu");
    }
}