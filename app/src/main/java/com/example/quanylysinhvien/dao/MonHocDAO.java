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
        Cursor c = db.rawQuery("SELECT * FROM MONHOC", null);

        while (c.moveToNext()) {
            list.add(new MonHoc(
                    c.getString(0),
                    c.getString(1),
                    c.getInt(2)
            ));
        }

        c.close();
        db.close();
        return list;
    }

    public boolean insert(MonHoc mh) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("maMon", mh.getMaMon());
        values.put("tenMon", mh.getTenMon());
        values.put("soTinChi", mh.getSoTinChi());

        long kq = db.insert("MONHOC", null, values);
        db.close();
        return kq != -1;
    }

    public boolean update(MonHoc mh) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("tenMon", mh.getTenMon());
        values.put("soTinChi", mh.getSoTinChi());

        int kq = db.update("MONHOC", values, "maMon=?", new String[]{mh.getMaMon()});
        db.close();
        return kq > 0;
    }

    public boolean delete(String maMon) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        int kq = db.delete("MONHOC", "maMon=?", new String[]{maMon});
        db.close();
        return kq > 0;
    }

    public ArrayList<MonHoc> search(String tuKhoa) {
        ArrayList<MonHoc> list = new ArrayList<>();
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor c = db.rawQuery(
                "SELECT * FROM MONHOC WHERE maMon LIKE ? OR tenMon LIKE ?",
                new String[]{"%" + tuKhoa + "%", "%" + tuKhoa + "%"}
        );

        while (c.moveToNext()) {
            list.add(new MonHoc(
                    c.getString(0),
                    c.getString(1),
                    c.getInt(2)
            ));
        }

        c.close();
        db.close();
        return list;
    }
}