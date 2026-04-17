package com.example.quanylysinhvien.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.quanylysinhvien.database.DBHelper;
import com.example.quanylysinhvien.model.SinhVien;

import java.util.ArrayList;

public class SinhVienDao {
    DBHelper dbHelper;

    public SinhVienDao(Context context) {
        dbHelper = new DBHelper(context);
    }

    public ArrayList<SinhVien> getALL() {
        ArrayList<SinhVien> ds = new ArrayList<>();
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM SINHVIEN", null);

        if (cursor.moveToFirst()) {
            do {
                try {
                    String ma = cursor.getString(0);
                    String ten = cursor.getString(1);
                    String email = cursor.getString(2);
                    String hinh = cursor.getString(3);
                    String maLop = cursor.getString(4);
                    String maNganh = cursor.getString(5);

                    SinhVien sinhVien = new SinhVien(ma, ten, email, hinh, maLop, maNganh);
                    ds.add(sinhVien);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();
        return ds;
    }

    public boolean insert(SinhVien s) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("maSv", s.getMaSv());
        contentValues.put("tenSV", s.getTenSv());
        contentValues.put("email", s.getEmail());
        contentValues.put("hinh", s.getHinh());
        contentValues.put("maLop", s.getMaLop());
        contentValues.put("maNganh", s.getMaNganh());

        long r = db.insert("SINHVIEN", null, contentValues);
        db.close();
        return r > 0;
    }

    public boolean update(SinhVien s) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("tenSV", s.getTenSv());
        contentValues.put("email", s.getEmail());
        contentValues.put("hinh", s.getHinh());
        contentValues.put("maLop", s.getMaLop());
        contentValues.put("maNganh", s.getMaNganh());

        int r = db.update("SINHVIEN", contentValues, "maSv=?", new String[]{s.getMaSv()});
        db.close();
        return r > 0;
    }

    public boolean delete(SinhVien s) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        int r = db.delete("SINHVIEN", "maSv=?", new String[]{s.getMaSv()});
        db.close();
        return r > 0;
    }
}