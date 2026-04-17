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
        Cursor c = db.rawQuery("SELECT * FROM NGANH", null);

        while (c.moveToNext()) {
            String ma = c.getString(0);
            String ten = c.getString(1);
            list.add(new Nganh(ma, ten));
        }

        c.close();
        db.close();
        return list;
    }

    public boolean insert(Nganh n) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("maNganh", n.getMaNganh());
        values.put("tenNganh", n.getTenNganh());

        long kq = db.insert("NGANH", null, values);
        db.close();
        return kq != -1;
    }

    public boolean update(Nganh n) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("tenNganh", n.getTenNganh());

        int kq = db.update("NGANH", values, "maNganh=?", new String[]{n.getMaNganh()});
        db.close();
        return kq > 0;
    }

    public boolean delete(String maNganh) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        int kq = db.delete("NGANH", "maNganh=?", new String[]{maNganh});
        db.close();
        return kq > 0;
    }

    public ArrayList<Nganh> search(String tuKhoa) {
        ArrayList<Nganh> list = new ArrayList<>();
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor c = db.rawQuery(
                "SELECT * FROM NGANH WHERE maNganh LIKE ? OR tenNganh LIKE ?",
                new String[]{"%" + tuKhoa + "%", "%" + tuKhoa + "%"}
        );

        while (c.moveToNext()) {
            list.add(new Nganh(c.getString(0), c.getString(1)));
        }

        c.close();
        db.close();
        return list;
    }
}