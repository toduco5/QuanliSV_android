package com.example.quanylysinhvien.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.quanylysinhvien.database.DBHelper;
import com.example.quanylysinhvien.model.MonHoc;

import java.util.ArrayList;

public class MonHocDAO {

    DBHelper dbHelper;

    public MonHocDAO(Context context) {
        dbHelper = new DBHelper(context);
    }

    public ArrayList<MonHoc> getAll() {
        ArrayList<MonHoc> list = new ArrayList<>();
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        Cursor c = db.rawQuery(
                "SELECT maMon, tenMon, soTinChi, maNganh FROM MONHOC ORDER BY maMon ASC",
                null
        );

        if (c.moveToFirst()) {
            do {
                list.add(new MonHoc(
                        c.getString(0),
                        c.getString(1),
                        c.getInt(2),
                        c.getString(3)
                ));
            } while (c.moveToNext());
        }

        c.close();
        db.close();
        return list;
    }

    public boolean insert(MonHoc m) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put("maMon", m.getMaMon());
        values.put("tenMon", m.getTenMon());
        values.put("soTinChi", m.getSoTinChi());
        values.put("maNganh", m.getMaNganh());

        long kq = db.insert("MONHOC", null, values);
        db.close();

        return kq != -1;
    }

    public boolean update(MonHoc m) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put("tenMon", m.getTenMon());
        values.put("soTinChi", m.getSoTinChi());
        values.put("maNganh", m.getMaNganh());

        int kq = db.update(
                "MONHOC",
                values,
                "maMon=?",
                new String[]{m.getMaMon()}
        );

        db.close();
        return kq > 0;
    }

    public boolean delete(String maMon) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        int kq = db.delete(
                "MONHOC",
                "maMon=?",
                new String[]{maMon}
        );

        db.close();
        return kq > 0;
    }

    public ArrayList<MonHoc> search(String key) {
        ArrayList<MonHoc> list = new ArrayList<>();
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        Cursor c = db.rawQuery(
                "SELECT maMon, tenMon, soTinChi, maNganh FROM MONHOC " +
                        "WHERE maMon LIKE ? OR tenMon LIKE ? OR maNganh LIKE ? " +
                        "ORDER BY maMon ASC",
                new String[]{
                        "%" + key + "%",
                        "%" + key + "%",
                        "%" + key + "%"
                }
        );

        if (c.moveToFirst()) {
            do {
                list.add(new MonHoc(
                        c.getString(0),
                        c.getString(1),
                        c.getInt(2),
                        c.getString(3)
                ));
            } while (c.moveToNext());
        }

        c.close();
        db.close();
        return list;
    }

    public ArrayList<String> getDanhSachSpinner() {
        ArrayList<String> list = new ArrayList<>();
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        Cursor c = db.rawQuery(
                "SELECT maMon, tenMon FROM MONHOC ORDER BY maMon ASC",
                null
        );

        if (c.moveToFirst()) {
            do {
                list.add(c.getString(0) + " - " + c.getString(1));
            } while (c.moveToNext());
        }

        c.close();
        db.close();
        return list;
    }

    public ArrayList<String> getDanhSachMonTheoNganh(String maNganh) {
        ArrayList<String> list = new ArrayList<>();
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        Cursor c = db.rawQuery(
                "SELECT maMon, tenMon FROM MONHOC " +
                        "WHERE maNganh=? ORDER BY maMon ASC",
                new String[]{maNganh}
        );

        if (c.moveToFirst()) {
            do {
                list.add(c.getString(0) + " - " + c.getString(1));
            } while (c.moveToNext());
        }

        c.close();
        db.close();
        return list;
    }

    public ArrayList<String> getDanhSachMonTheoLop(String maLop) {
        ArrayList<String> list = new ArrayList<>();
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        Cursor c = db.rawQuery(
                "SELECT MONHOC.maMon, MONHOC.tenMon " +
                        "FROM LOP_MONHOC " +
                        "INNER JOIN MONHOC ON LOP_MONHOC.maMon = MONHOC.maMon " +
                        "WHERE LOP_MONHOC.maLop=? " +
                        "ORDER BY MONHOC.maMon ASC",
                new String[]{maLop}
        );

        if (c.moveToFirst()) {
            do {
                list.add(c.getString(0) + " - " + c.getString(1));
            } while (c.moveToNext());
        }

        c.close();
        db.close();
        return list;
    }

    public ArrayList<String> getDanhSachMonChuaCoDiem(String maSv) {
        ArrayList<String> list = new ArrayList<>();
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        Cursor c = db.rawQuery(
                "SELECT MONHOC.maMon, MONHOC.tenMon " +
                        "FROM MONHOC " +
                        "WHERE MONHOC.maMon NOT IN (" +
                        "SELECT maMon FROM DIEM WHERE maSv=?" +
                        ") ORDER BY MONHOC.maMon ASC",
                new String[]{maSv}
        );

        if (c.moveToFirst()) {
            do {
                list.add(c.getString(0) + " - " + c.getString(1));
            } while (c.moveToNext());
        }

        c.close();
        db.close();
        return list;
    }

    public ArrayList<String> getDanhSachMonTheoLopChuaCoDiem(String maLop, String maSv) {
        ArrayList<String> list = new ArrayList<>();
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        Cursor c = db.rawQuery(
                "SELECT MONHOC.maMon, MONHOC.tenMon " +
                        "FROM LOP_MONHOC " +
                        "INNER JOIN MONHOC ON LOP_MONHOC.maMon = MONHOC.maMon " +
                        "WHERE LOP_MONHOC.maLop=? " +
                        "AND MONHOC.maMon NOT IN (" +
                        "SELECT maMon FROM DIEM WHERE maSv=?" +
                        ") ORDER BY MONHOC.maMon ASC",
                new String[]{maLop, maSv}
        );

        if (c.moveToFirst()) {
            do {
                list.add(c.getString(0) + " - " + c.getString(1));
            } while (c.moveToNext());
        }

        c.close();
        db.close();
        return list;
    }
}