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

public class XemLopActivity extends AppCompatActivity {

    TextView tvTitleLop, tvThongBaoLop;
    ListView lvLopUser;

    DBHelper dbHelper;
    DaoTaiKhoan daoTaiKhoan;

    String tenDangNhap, maSv;

    ArrayList<HashMap<String, String>> list;
    SimpleAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_xem_lop);

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
            tvTitleLop.setText("Thông tin lớp");
            tvThongBaoLop.setText("Tài khoản này chưa được gán sinh viên.");
            Toast.makeText(this, "Admin chưa gán sinh viên cho tài khoản này", Toast.LENGTH_SHORT).show();
            return;
        }

        loadLop();
    }

    private void anhXa() {
        tvTitleLop = findViewById(R.id.tvTitleLop);
        tvThongBaoLop = findViewById(R.id.tvThongBaoLop);
        lvLopUser = findViewById(R.id.lvLopUser);
    }

    private void loadLop() {
        list = new ArrayList<>();
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        Cursor c = db.rawQuery(
                "SELECT LOP.maLop, LOP.tenLop, NGANH.tenNganh " +
                        "FROM SINHVIEN " +
                        "INNER JOIN LOP ON SINHVIEN.maLop = LOP.maLop " +
                        "INNER JOIN NGANH ON SINHVIEN.maNganh = NGANH.maNganh " +
                        "WHERE SINHVIEN.maSv = ?",
                new String[]{maSv}
        );

        if (c.moveToFirst()) {
            do {
                HashMap<String, String> map = new HashMap<>();
                map.put("maLop", "Mã lớp: " + c.getString(0));
                map.put("tenLop", "Tên lớp: " + c.getString(1));
                map.put("tenNganh", "Ngành: " + c.getString(2));
                list.add(map);
            } while (c.moveToNext());

            tvTitleLop.setText("Lớp của bạn");
            tvThongBaoLop.setText("Thông tin lớp hiện tại");
        } else {
            tvTitleLop.setText("Thông tin lớp");
            tvThongBaoLop.setText("Chưa có dữ liệu lớp.");
            Toast.makeText(this, "Không tìm thấy lớp của sinh viên này", Toast.LENGTH_SHORT).show();
        }

        c.close();
        db.close();

        adapter = new SimpleAdapter(
                this,
                list,
                R.layout.dong_xem_lop_user,
                new String[]{"maLop", "tenLop", "tenNganh"},
                new int[]{R.id.tvMaLopUser, R.id.tvTenLopUser, R.id.tvTenNganhUser}
        );

        lvLopUser.setAdapter(adapter);
    }
}