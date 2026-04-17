package com.example.quanylysinhvien.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.quanylysinhvien.database.DBHelper;
import com.example.quanylysinhvien.model.Nganh;

import java.util.ArrayList;

public class NganhDAO {

    DBHelper dbHelper;

    public NganhDAO(Context context) {
        dbHelper = new DBHelper(context);
    }

    public ArrayList<Nganh> getAll() {
        ArrayList<Nganh> list = new ArrayList<>();
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        Cursor c = db.rawQuery("SELECT maNganh, tenNganh FROM NGANH ORDER BY maNganh ASC", null);

        if (c.moveToFirst()) {
            do {
                list.add(new Nganh(
                        c.getString(0),
                        c.getString(1)
                ));
            } while (c.moveToNext());
        }

        c.close();
        db.close();
        return list;
    }

    public boolean insert(Nganh nganh) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put("maNganh", nganh.getMaNganh());
        values.put("tenNganh", nganh.getTenNganh());

        long kq = db.insert("NGANH", null, values);
        db.close();
        return kq != -1;
    }

    public boolean update(Nganh nganh) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put("tenNganh", nganh.getTenNganh());

        int kq = db.update("NGANH", values, "maNganh=?", new String[]{nganh.getMaNganh()});
        db.close();
        return kq > 0;
    }

    public boolean delete(String maNganh) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        int kq = db.delete("NGANH", "maNganh=?", new String[]{maNganh});
        db.close();
        return kq > 0;
    }

    public ArrayList<Nganh> search(String key) {
        ArrayList<Nganh> list = new ArrayList<>();
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        Cursor c = db.rawQuery(
                "SELECT maNganh, tenNganh FROM NGANH WHERE maNganh LIKE ? OR tenNganh LIKE ? ORDER BY maNganh ASC",
                new String[]{"%" + key + "%", "%" + key + "%"}
        );

        if (c.moveToFirst()) {
            do {
                list.add(new Nganh(
                        c.getString(0),
                        c.getString(1)
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

        Cursor c = db.rawQuery("SELECT maNganh, tenNganh FROM NGANH ORDER BY maNganh ASC", null);

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