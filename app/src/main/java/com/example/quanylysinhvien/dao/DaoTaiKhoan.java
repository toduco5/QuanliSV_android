package com.example.quanylysinhvien.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.quanylysinhvien.database.DBHelper;
import com.example.quanylysinhvien.model.TaikhoanMatKhau;

import java.util.ArrayList;

public class DaoTaiKhoan {

    private DBHelper dbHelper;

    public DaoTaiKhoan(Context context) {
        dbHelper = new DBHelper(context);
    }

    // Lấy toàn bộ tài khoản
    public ArrayList<TaikhoanMatKhau> getAll() {
        ArrayList<TaikhoanMatKhau> list = new ArrayList<>();

        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor c = db.rawQuery("SELECT * FROM taiKhoan", null);

        while (c.moveToNext()) {
            String tenTaiKhoan = c.getString(0);
            String matKhau = c.getString(1);
            list.add(new TaikhoanMatKhau(tenTaiKhoan, matKhau));
        }

        c.close();
        db.close();
        return list;
    }

    // Thêm tài khoản
    public boolean them(TaikhoanMatKhau tk) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put("tenTaiKhoan", tk.getTenTaiKhoan());
        values.put("matKhau", tk.getMatKhau());

        long kq = db.insert("taiKhoan", null, values);

        db.close();
        return kq != -1;
    }

    // Giữ tên hàm cũ nếu project cũ đang gọi
    public boolean Them(TaikhoanMatKhau tk) {
        return them(tk);
    }

    // Kiểm tra đăng nhập
    public boolean kiemTraDangNhap(String tenTaiKhoan, String matKhau) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        Cursor c = db.rawQuery(
                "SELECT * FROM taiKhoan WHERE tenTaiKhoan=? AND matKhau=?",
                new String[]{tenTaiKhoan, matKhau}
        );

        boolean check = c.moveToFirst();

        c.close();
        db.close();
        return check;
    }

    // Kiểm tra tài khoản đã tồn tại chưa
    public boolean kiemTraTonTai(String tenTaiKhoan) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        Cursor c = db.rawQuery(
                "SELECT * FROM taiKhoan WHERE tenTaiKhoan=?",
                new String[]{tenTaiKhoan}
        );

        boolean check = c.moveToFirst();

        c.close();
        db.close();
        return check;
    }

    // Tên hàm này để tương thích với RegisterActivity cũ
    public boolean kiemTraTonTaiTaiKhoan(String tenTaiKhoan) {
        return kiemTraTonTai(tenTaiKhoan);
    }

    // Lấy 1 tài khoản theo tên
    public TaikhoanMatKhau getTaiKhoan(String tenTaiKhoan) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        Cursor c = db.rawQuery(
                "SELECT * FROM taiKhoan WHERE tenTaiKhoan=?",
                new String[]{tenTaiKhoan}
        );

        TaikhoanMatKhau tk = null;

        if (c.moveToFirst()) {
            tk = new TaikhoanMatKhau(
                    c.getString(0),
                    c.getString(1)
            );
        }

        c.close();
        db.close();
        return tk;
    }

    // Đổi mật khẩu
    public boolean doiMatKhau(String tenTaiKhoan, String matKhauMoi) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put("matKhau", matKhauMoi);

        int kq = db.update(
                "taiKhoan",
                values,
                "tenTaiKhoan=?",
                new String[]{tenTaiKhoan}
        );

        db.close();
        return kq > 0;
    }

    // Xóa tài khoản
    public boolean xoaTaiKhoan(String tenTaiKhoan) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        int kq = db.delete(
                "taiKhoan",
                "tenTaiKhoan=?",
                new String[]{tenTaiKhoan}
        );

        db.close();
        return kq > 0;
    }
}