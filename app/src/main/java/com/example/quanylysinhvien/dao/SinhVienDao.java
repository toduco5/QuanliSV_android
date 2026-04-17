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
        Cursor cursor = db.rawQuery("SELECT maSv, tenSV, email, hinh, maLop, maNganh FROM SINHVIEN ORDER BY maSv ASC", null);

        if (cursor.moveToFirst()) {
            do {
                ds.add(new SinhVien(
                        cursor.getString(0),
                        cursor.getString(1),
                        cursor.getString(2),
                        cursor.getString(3),
                        cursor.getString(4),
                        cursor.getString(5)
                ));
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

    public ArrayList<SinhVien> search(String key) {
        ArrayList<SinhVien> ds = new ArrayList<>();
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        Cursor cursor = db.rawQuery(
                "SELECT maSv, tenSV, email, hinh, maLop, maNganh FROM SINHVIEN " +
                        "WHERE maSv LIKE ? OR tenSV LIKE ? OR email LIKE ? OR maLop LIKE ? OR maNganh LIKE ? ORDER BY maSv ASC",
                new String[]{
                        "%" + key + "%",
                        "%" + key + "%",
                        "%" + key + "%",
                        "%" + key + "%",
                        "%" + key + "%"
                }
        );

        if (cursor.moveToFirst()) {
            do {
                ds.add(new SinhVien(
                        cursor.getString(0),
                        cursor.getString(1),
                        cursor.getString(2),
                        cursor.getString(3),
                        cursor.getString(4),
                        cursor.getString(5)
                ));
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();
        return ds;
    }

    public ArrayList<String> getDanhSachSpinner() {
        ArrayList<String> list = new ArrayList<>();
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        Cursor cursor = db.rawQuery("SELECT maSv, tenSV FROM SINHVIEN ORDER BY maSv ASC", null);

        if (cursor.moveToFirst()) {
            do {
                list.add(cursor.getString(0) + " - " + cursor.getString(1));
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();
        return list;
    }

    public String getMaNganhTheoSinhVien(String maSv) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        Cursor c = db.rawQuery(
                "SELECT maNganh FROM SINHVIEN WHERE maSv=?",
                new String[]{maSv}
        );

        String maNganh = null;
        if (c.moveToFirst()) {
            maNganh = c.getString(0);
        }

        c.close();
        db.close();
        return maNganh;
    }

    public String getMaLopTheoSinhVien(String maSv) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        Cursor c = db.rawQuery(
                "SELECT maLop FROM SINHVIEN WHERE maSv=?",
                new String[]{maSv}
        );

        String maLop = null;
        if (c.moveToFirst()) {
            maLop = c.getString(0);
        }

        c.close();
        db.close();
        return maLop;
    }
}