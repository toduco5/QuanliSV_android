package com.example.quanylysinhvien.admin;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.example.quanylysinhvien.R;
import com.example.quanylysinhvien.database.DBHelper;

import java.util.ArrayList;
import java.util.HashMap;

public class QuanLyTaiKhoanActivity extends AppCompatActivity {

    TextView tvTitleTaiKhoan, tvThongBaoTaiKhoan;
    ListView lvTaiKhoan;

    DBHelper dbHelper;

    ArrayList<HashMap<String, String>> list;
    SimpleAdapter adapter;

    String tenTaiKhoanChon = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quan_ly_tai_khoan);

        anhXa();

        dbHelper = new DBHelper(this);

        loadTaiKhoan();

        lvTaiKhoan.setOnItemClickListener((parent, view, position, id) -> {
            tenTaiKhoanChon = list.get(position).get("tkRaw");
            xuLyChonTaiKhoan();
        });
    }

    private void anhXa() {
        tvTitleTaiKhoan = findViewById(R.id.tvTitleTaiKhoan);
        tvThongBaoTaiKhoan = findViewById(R.id.tvThongBaoTaiKhoan);
        lvTaiKhoan = findViewById(R.id.lvTaiKhoan);
    }

    private void loadTaiKhoan() {

        list = new ArrayList<>();

        SQLiteDatabase db = dbHelper.getReadableDatabase();

        Cursor c = db.rawQuery("SELECT tenTaiKhoan, matKhau FROM taiKhoan", null);

        if (c.moveToFirst()) {
            do {
                HashMap<String, String> map = new HashMap<>();

                String tk = c.getString(0);
                String mk = c.getString(1);

                map.put("tkRaw", tk);
                map.put("tenTaiKhoan", "Tài khoản: " + tk);
                map.put("matKhau", "Mật khẩu: " + mk);

                if (tk.equalsIgnoreCase("admin")) {
                    map.put("vaiTro", "Vai trò: Quản trị viên");
                } else {
                    map.put("vaiTro", "Vai trò: Người dùng");
                }

                list.add(map);

            } while (c.moveToNext());
        }

        c.close();
        db.close();

        tvThongBaoTaiKhoan.setText("Tổng tài khoản: " + list.size());

        adapter = new SimpleAdapter(
                this,
                list,
                R.layout.dong_tai_khoan,
                new String[]{"tenTaiKhoan", "matKhau", "vaiTro"},
                new int[]{R.id.tvTenTaiKhoan, R.id.tvMatKhau, R.id.tvVaiTro}
        );

        lvTaiKhoan.setAdapter(adapter);
    }

    private void xuLyChonTaiKhoan() {

        if (tenTaiKhoanChon.equalsIgnoreCase("admin")) {
            Toast.makeText(this, "Không thể xóa tài khoản admin", Toast.LENGTH_SHORT).show();
            return;
        }

        String[] chucNang = {"Xóa tài khoản", "Đặt lại mật khẩu = 123"};

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Chọn chức năng");

        builder.setItems(chucNang, (dialog, which) -> {

            if (which == 0) {
                xoaTaiKhoan();
            } else {
                resetMatKhau();
            }

        });

        builder.show();
    }

    private void xoaTaiKhoan() {

        SQLiteDatabase db = dbHelper.getWritableDatabase();

        int kq = db.delete(
                "taiKhoan",
                "tenTaiKhoan=?",
                new String[]{tenTaiKhoanChon}
        );

        db.close();

        if (kq > 0) {
            Toast.makeText(this, "Xóa thành công", Toast.LENGTH_SHORT).show();
            loadTaiKhoan();
        } else {
            Toast.makeText(this, "Xóa thất bại", Toast.LENGTH_SHORT).show();
        }
    }

    private void resetMatKhau() {

        SQLiteDatabase db = dbHelper.getWritableDatabase();

        android.content.ContentValues values = new android.content.ContentValues();
        values.put("matKhau", "123");

        int kq = db.update(
                "taiKhoan",
                values,
                "tenTaiKhoan=?",
                new String[]{tenTaiKhoanChon}
        );

        db.close();

        if (kq > 0) {
            Toast.makeText(this, "Đặt lại mật khẩu thành 123", Toast.LENGTH_SHORT).show();
            loadTaiKhoan();
        } else {
            Toast.makeText(this, "Thất bại", Toast.LENGTH_SHORT).show();
        }
    }
}