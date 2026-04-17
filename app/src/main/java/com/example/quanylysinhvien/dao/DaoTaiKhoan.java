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

    public ArrayList<TaikhoanMatKhau> getAll() {
        ArrayList<TaikhoanMatKhau> list = new ArrayList<>();
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        Cursor c = db.rawQuery("SELECT tenTaiKhoan, matKhau, vaiTro, maSv FROM taiKhoan", null);

        if (c.moveToFirst()) {
            do {
                list.add(new TaikhoanMatKhau(
                        c.getString(0),
                        c.getString(1),
                        c.getString(2),
                        c.getString(3)
                ));
            } while (c.moveToNext());
        }

        c.close();
        db.close();
        return list;
    }

    public boolean them(TaikhoanMatKhau tk) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put("tenTaiKhoan", tk.getTenTaiKhoan());
        values.put("matKhau", tk.getMatKhau());
        values.put("vaiTro", tk.getVaiTro());
        values.put("maSv", tk.getMaSv());

        long kq = db.insert("taiKhoan", null, values);
        db.close();
        return kq != -1;
    }

    public boolean Them(TaikhoanMatKhau tk) {
        return them(tk);
    }

    public boolean sua(TaikhoanMatKhau tk) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put("matKhau", tk.getMatKhau());
        values.put("vaiTro", tk.getVaiTro());
        values.put("maSv", tk.getMaSv());

        int kq = db.update(
                "taiKhoan",
                values,
                "tenTaiKhoan=?",
                new String[]{tk.getTenTaiKhoan()}
        );

        db.close();
        return kq > 0;
    }

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

    public String getVaiTro(String tenTaiKhoan) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        Cursor c = db.rawQuery(
                "SELECT vaiTro FROM taiKhoan WHERE tenTaiKhoan=?",
                new String[]{tenTaiKhoan}
        );

        String vaiTro = "";
        if (c.moveToFirst()) {
            vaiTro = c.getString(0);
        }

        c.close();
        db.close();
        return vaiTro;
    }

    public String getMaSvTheoTaiKhoan(String tenTaiKhoan) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        Cursor c = db.rawQuery(
                "SELECT maSv FROM taiKhoan WHERE tenTaiKhoan=?",
                new String[]{tenTaiKhoan}
        );

        String maSv = null;
        if (c.moveToFirst()) {
            maSv = c.getString(0);
        }

        c.close();
        db.close();
        return maSv;
    }

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

    public boolean kiemTraTonTaiTaiKhoan(String tenTaiKhoan) {
        return kiemTraTonTai(tenTaiKhoan);
    }

    public TaikhoanMatKhau getTaiKhoan(String tenTaiKhoan) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        Cursor c = db.rawQuery(
                "SELECT tenTaiKhoan, matKhau, vaiTro, maSv FROM taiKhoan WHERE tenTaiKhoan=?",
                new String[]{tenTaiKhoan}
        );

        TaikhoanMatKhau tk = null;
        if (c.moveToFirst()) {
            tk = new TaikhoanMatKhau(
                    c.getString(0),
                    c.getString(1),
                    c.getString(2),
                    c.getString(3)
            );
        }

        c.close();
        db.close();
        return tk;
    }

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