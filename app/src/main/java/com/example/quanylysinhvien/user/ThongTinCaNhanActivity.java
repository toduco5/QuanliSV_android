package com.example.quanylysinhvien.user;

import androidx.appcompat.app.AppCompatActivity;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import com.example.quanylysinhvien.R;
import com.example.quanylysinhvien.database.DBHelper;

public class ThongTinCaNhanActivity extends AppCompatActivity {

    TextView tvTaiKhoan, tvMaSV, tvHoTen, tvEmail, tvLop, tvNganh;

    DBHelper dbHelper;
    String tenDangNhap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_thong_tin_ca_nhan);

        anhXa();

        dbHelper = new DBHelper(this);

        tenDangNhap = getIntent().getStringExtra("tenDangNhap");
        if (tenDangNhap == null) {
            tenDangNhap = "";
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

        SQLiteDatabase db = dbHelper.getReadableDatabase();

        Cursor c = db.rawQuery(
                "SELECT maSv, tenSV, email, maLop, maNganh FROM SINHVIEN WHERE email LIKE ? OR maSv = ?",
                new String[]{"%" + tenDangNhap + "%", tenDangNhap}
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
            tvMaSV.setText("Mã sinh viên: Chưa có dữ liệu");
            tvHoTen.setText("Họ tên: Chưa có dữ liệu");
            tvEmail.setText("Email: Chưa có dữ liệu");
            tvLop.setText("Lớp: Chưa có dữ liệu");
            tvNganh.setText("Ngành: Chưa có dữ liệu");

            Toast.makeText(this, "Chưa tìm thấy thông tin cá nhân", Toast.LENGTH_SHORT).show();
        }

        c.close();
        db.close();
    }
}