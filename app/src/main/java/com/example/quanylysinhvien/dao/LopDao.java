package com.example.quanylysinhvien.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.quanylysinhvien.database.DBHelper;
import com.example.quanylysinhvien.model.Lop;

import java.util.ArrayList;

public class LopDao {

    DBHelper dbHelper;

    public LopDao(Context context) {
        dbHelper = new DBHelper(context);
    }

    public ArrayList<Lop> getAll() {
        ArrayList<Lop> list = new ArrayList<>();
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor c = db.rawQuery("SELECT * FROM LOP", null);

        while (c.moveToNext()) {
            String maLop = c.getString(0);
            String tenLop = c.getString(1);
            list.add(new Lop(maLop, tenLop));
        }

        c.close();
        db.close();
        return list;
    }

    public boolean insert(Lop lop) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("maLop", lop.getMaLop());
        values.put("tenLop", lop.getTenLop());

        long kq = db.insert("LOP", null, values);
        db.close();
        return kq != -1;
    }

    public boolean update(Lop lop) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("tenLop", lop.getTenLop());

        int kq = db.update("LOP", values, "maLop=?", new String[]{lop.getMaLop()});
        db.close();
        return kq > 0;
    }

    public boolean delete(String maLop) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        int kq = db.delete("LOP", "maLop=?", new String[]{maLop});
        db.close();
        return kq > 0;
    }
}