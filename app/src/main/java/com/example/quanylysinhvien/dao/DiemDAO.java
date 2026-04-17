package com.example.quanylysinhvien.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.quanylysinhvien.database.DBHelper;
import com.example.quanylysinhvien.model.Diem;

import java.util.ArrayList;

public class DiemDAO {
    DBHelper dbHelper;

    public DiemDAO(Context context) {
        dbHelper = new DBHelper(context);
    }

    public ArrayList<Diem> getAll() {
        ArrayList<Diem> list = new ArrayList<>();
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor c = db.rawQuery("SELECT * FROM DIEM", null);

        while (c.moveToNext()) {
            list.add(new Diem(
                    c.getInt(0),
                    c.getString(1),
                    c.getString(2),
                    c.getFloat(3)
            ));
        }

        c.close();
        db.close();
        return list;
    }

    public boolean insert(Diem d) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("maSv", d.getMaSv());
        values.put("maMon", d.getMaMon());
        values.put("diem", d.getDiem());

        long kq = db.insert("DIEM", null, values);
        db.close();
        return kq != -1;
    }

    public boolean update(Diem d) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("maSv", d.getMaSv());
        values.put("maMon", d.getMaMon());
        values.put("diem", d.getDiem());

        int kq = db.update("DIEM", values, "id=?", new String[]{String.valueOf(d.getId())});
        db.close();
        return kq > 0;
    }

    public boolean delete(int id) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        int kq = db.delete("DIEM", "id=?", new String[]{String.valueOf(id)});
        db.close();
        return kq > 0;
    }

    public ArrayList<Diem> search(String tuKhoa) {
        ArrayList<Diem> list = new ArrayList<>();
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor c = db.rawQuery(
                "SELECT * FROM DIEM WHERE maSv LIKE ? OR maMon LIKE ?",
                new String[]{"%" + tuKhoa + "%", "%" + tuKhoa + "%"}
        );

        while (c.moveToNext()) {
            list.add(new Diem(
                    c.getInt(0),
                    c.getString(1),
                    c.getString(2),
                    c.getFloat(3)
            ));
        }

        c.close();
        db.close();
        return list;
    }
}