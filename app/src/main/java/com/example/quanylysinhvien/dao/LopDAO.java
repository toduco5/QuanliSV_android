package com.example.quanylysinhvien.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.quanylysinhvien.database.DBHelper;
import com.example.quanylysinhvien.model.Lop;

import java.util.ArrayList;

public class LopDAO {

    private DBHelper dbHelper;

    public LopDAO(Context context) {
        dbHelper = new DBHelper(context);
    }

    // ===============================
    // LẤY TOÀN BỘ DANH SÁCH LỚP
    // ===============================
    public ArrayList<Lop> getAll() {

        ArrayList<Lop> list = new ArrayList<>();

        SQLiteDatabase db = dbHelper.getReadableDatabase();

        Cursor c = db.rawQuery(
                "SELECT maLop, tenLop, maNganh FROM LOP ORDER BY maLop ASC",
                null
        );

        if (c.moveToFirst()) {
            do {
                Lop lop = new Lop(
                        c.getString(0),
                        c.getString(1),
                        c.getString(2)
                );

                list.add(lop);

            } while (c.moveToNext());
        }

        c.close();
        db.close();

        return list;
    }

    // ===============================
    // THÊM LỚP
    // ===============================
    public boolean insert(Lop lop) {

        SQLiteDatabase db = dbHelper.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put("maLop", lop.getMaLop());
        values.put("tenLop", lop.getTenLop());
        values.put("maNganh", lop.getMaNganh());

        long kq = db.insert("LOP", null, values);

        db.close();

        return kq != -1;
    }

    // ===============================
    // CẬP NHẬT LỚP
    // ===============================
    public boolean update(Lop lop) {

        SQLiteDatabase db = dbHelper.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put("tenLop", lop.getTenLop());
        values.put("maNganh", lop.getMaNganh());

        int kq = db.update(
                "LOP",
                values,
                "maLop=?",
                new String[]{lop.getMaLop()}
        );

        db.close();

        return kq > 0;
    }

    // ===============================
    // XÓA LỚP
    // ===============================
    public boolean delete(String maLop) {

        SQLiteDatabase db = dbHelper.getWritableDatabase();

        int kq = db.delete(
                "LOP",
                "maLop=?",
                new String[]{maLop}
        );

        db.close();

        return kq > 0;
    }

    // ===============================
    // TÌM KIẾM LỚP
    // ===============================
    public ArrayList<Lop> search(String key) {

        ArrayList<Lop> list = new ArrayList<>();

        SQLiteDatabase db = dbHelper.getReadableDatabase();

        Cursor c = db.rawQuery(
                "SELECT maLop, tenLop, maNganh FROM LOP " +
                        "WHERE maLop LIKE ? OR tenLop LIKE ? OR maNganh LIKE ? " +
                        "ORDER BY maLop ASC",
                new String[]{
                        "%" + key + "%",
                        "%" + key + "%",
                        "%" + key + "%"
                }
        );

        if (c.moveToFirst()) {
            do {
                Lop lop = new Lop(
                        c.getString(0),
                        c.getString(1),
                        c.getString(2)
                );

                list.add(lop);

            } while (c.moveToNext());
        }

        c.close();
        db.close();

        return list;
    }

    // ===============================
    // KIỂM TRA TỒN TẠI
    // ===============================
    public boolean kiemTraTonTai(String maLop) {

        SQLiteDatabase db = dbHelper.getReadableDatabase();

        Cursor c = db.rawQuery(
                "SELECT maLop FROM LOP WHERE maLop=?",
                new String[]{maLop}
        );

        boolean check = c.moveToFirst();

        c.close();
        db.close();

        return check;
    }

    // ===============================
    // SPINNER TOÀN BỘ LỚP
    // ===============================
    public ArrayList<String> getDanhSachSpinner() {

        ArrayList<String> list = new ArrayList<>();

        SQLiteDatabase db = dbHelper.getReadableDatabase();

        Cursor c = db.rawQuery(
                "SELECT maLop, tenLop FROM LOP ORDER BY maLop ASC",
                null
        );

        if (c.moveToFirst()) {
            do {
                list.add(
                        c.getString(0) + " - " +
                                c.getString(1)
                );

            } while (c.moveToNext());
        }

        c.close();
        db.close();

        return list;
    }

    // ===============================
    // SPINNER LỚP THEO NGÀNH
    // ===============================
    public ArrayList<String> getDanhSachLopTheoNganh(String maNganh) {

        ArrayList<String> list = new ArrayList<>();

        SQLiteDatabase db = dbHelper.getReadableDatabase();

        Cursor c = db.rawQuery(
                "SELECT maLop, tenLop FROM LOP " +
                        "WHERE maNganh=? ORDER BY maLop ASC",
                new String[]{maNganh}
        );

        if (c.moveToFirst()) {
            do {
                list.add(
                        c.getString(0) + " - " +
                                c.getString(1)
                );

            } while (c.moveToNext());
        }

        c.close();
        db.close();

        return list;
    }

    // ===============================
    // LẤY 1 LỚP
    // ===============================
    public Lop getLop(String maLop) {

        SQLiteDatabase db = dbHelper.getReadableDatabase();

        Cursor c = db.rawQuery(
                "SELECT maLop, tenLop, maNganh FROM LOP WHERE maLop=?",
                new String[]{maLop}
        );

        Lop lop = null;

        if (c.moveToFirst()) {
            lop = new Lop(
                    c.getString(0),
                    c.getString(1),
                    c.getString(2)
            );
        }

        c.close();
        db.close();

        return lop;
    }

    // ===============================
    // CHUỖI MÔN HỌC CỦA LỚP
    // ===============================
    public String getChuoiMonHocTheoLop(String maLop) {

        SQLiteDatabase db = dbHelper.getReadableDatabase();

        Cursor c = db.rawQuery(
                "SELECT MONHOC.tenMon " +
                        "FROM LOP_MONHOC " +
                        "INNER JOIN MONHOC " +
                        "ON LOP_MONHOC.maMon = MONHOC.maMon " +
                        "WHERE LOP_MONHOC.maLop=? " +
                        "ORDER BY MONHOC.maMon ASC",
                new String[]{maLop}
        );

        StringBuilder builder = new StringBuilder();

        if (c.moveToFirst()) {
            do {
                if (builder.length() > 0) {
                    builder.append(", ");
                }

                builder.append(c.getString(0));

            } while (c.moveToNext());
        }

        c.close();
        db.close();

        if (builder.length() == 0) {
            return "Chưa có môn học";
        }

        return builder.toString();
    }

    // ===============================
    // ĐẾM SỐ LƯỢNG LỚP
    // ===============================
    public int getSoLuongLop() {

        SQLiteDatabase db = dbHelper.getReadableDatabase();

        Cursor c = db.rawQuery(
                "SELECT COUNT(*) FROM LOP",
                null
        );

        int soLuong = 0;

        if (c.moveToFirst()) {
            soLuong = c.getInt(0);
        }

        c.close();
        db.close();

        return soLuong;
    }
}