package com.example.quanylysinhvien.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.quanylysinhvien.database.DBHelper;
import com.example.quanylysinhvien.model.SuKien;

import java.util.ArrayList;

public class SuKienDao {
    DBHelper dbHelper;

    public SuKienDao(Context context) {
        dbHelper = new DBHelper(context);
    }

    public ArrayList<SuKien> getAll() {
        ArrayList<SuKien> list = new ArrayList<>();
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM SUKIEN", null);

        if (cursor.moveToFirst()) {
            do {
                SuKien sk = new SuKien(
                        cursor.getString(0),
                        cursor.getString(1),
                        cursor.getString(2),
                        cursor.getString(3)
                );
                list.add(sk);
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();
        return list;
    }

    public boolean insert(SuKien sk) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("maSk", sk.getMaSk());
        values.put("tenSk", sk.getTenSk());
        values.put("ngaySk", sk.getNgaySk());
        values.put("noiDung", sk.getNoiDung());

        long result = db.insert("SUKIEN", null, values);
        db.close();
        return result > 0;
    }

    public boolean update(SuKien sk) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("tenSk", sk.getTenSk());
        values.put("ngaySk", sk.getNgaySk());
        values.put("noiDung", sk.getNoiDung());

        int result = db.update("SUKIEN", values, "maSk=?", new String[]{sk.getMaSk()});
        db.close();
        return result > 0;
    }

    public boolean delete(String maSk) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        int result = db.delete("SUKIEN", "maSk=?", new String[]{maSk});
        db.close();
        return result > 0;
    }

    public ArrayList<SuKien> search(String key) {
        ArrayList<SuKien> list = new ArrayList<>();
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery(
                "SELECT * FROM SUKIEN WHERE maSk LIKE ? OR tenSk LIKE ? OR ngaySk LIKE ?",
                new String[]{"%" + key + "%", "%" + key + "%", "%" + key + "%"}
        );

        if (cursor.moveToFirst()) {
            do {
                SuKien sk = new SuKien(
                        cursor.getString(0),
                        cursor.getString(1),
                        cursor.getString(2),
                        cursor.getString(3)
                );
                list.add(sk);
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();
        return list;
    }
}