package com.example.quanylysinhvien.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.quanylysinhvien.database.DBHelper;
import com.example.quanylysinhvien.model.MonHoc;

import java.util.ArrayList;

// Lớp MonHocDAO dùng để thao tác dữ liệu bảng MONHOC trong SQLite
public class MonHocDAO {

    // Biến DBHelper dùng để kết nối database
    DBHelper dbHelper;

    // Constructor: khởi tạo DBHelper
    public MonHocDAO(Context context) {
        dbHelper = new DBHelper(context);
    }

    // Hàm lấy toàn bộ môn học trong bảng MONHOC
    public ArrayList<MonHoc> getAll() {

        // Tạo list rỗng để chứa dữ liệu trả về
        ArrayList<MonHoc> list = new ArrayList<>();

        // Mở database chế độ đọc
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        // Thực hiện câu lệnh SQL lấy toàn bộ môn học
        Cursor c = db.rawQuery(
                "SELECT maMon, tenMon, soTinChi, maNganh FROM MONHOC ORDER BY maMon ASC",
                null
        );

        // Nếu có dữ liệu
        if (c.moveToFirst()) {
            do {
                // Mỗi dòng dữ liệu tạo thành 1 đối tượng MonHoc
                list.add(new MonHoc(
                        c.getString(0), // mã môn
                        c.getString(1), // tên môn
                        c.getInt(2),    // số tín chỉ
                        c.getString(3)  // mã ngành
                ));
            } while (c.moveToNext()); // chuyển sang dòng tiếp theo
        }

        // Đóng con trỏ và database
        c.close();
        db.close();

        // Trả về danh sách môn học
        return list;
    }

    // Hàm thêm môn học mới
    public boolean insert(MonHoc m) {

        // Mở database chế độ ghi
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        // Tạo ContentValues để chứa dữ liệu cần thêm
        ContentValues values = new ContentValues();

        values.put("maMon", m.getMaMon());       // mã môn
        values.put("tenMon", m.getTenMon());     // tên môn
        values.put("soTinChi", m.getSoTinChi()); // số tín chỉ
        values.put("maNganh", m.getMaNganh());   // mã ngành

        // Insert vào bảng MONHOC
        long kq = db.insert("MONHOC", null, values);

        db.close();

        // Nếu kq khác -1 nghĩa là thêm thành công
        return kq != -1;
    }

    // Hàm cập nhật môn học theo mã môn
    public boolean update(MonHoc m) {

        SQLiteDatabase db = dbHelper.getWritableDatabase();

        ContentValues values = new ContentValues();

        // Cho phép sửa các thông tin sau
        values.put("tenMon", m.getTenMon());
        values.put("soTinChi", m.getSoTinChi());
        values.put("maNganh", m.getMaNganh());

        // Update theo điều kiện mã môn
        int kq = db.update(
                "MONHOC",
                values,
                "maMon=?",
                new String[]{m.getMaMon()}
        );

        db.close();

        // Nếu số dòng cập nhật > 0 nghĩa là thành công
        return kq > 0;
    }

    // Hàm xóa môn học theo mã môn
    public boolean delete(String maMon) {

        SQLiteDatabase db = dbHelper.getWritableDatabase();

        // Xóa dữ liệu có mã môn tương ứng
        int kq = db.delete(
                "MONHOC",
                "maMon=?",
                new String[]{maMon}
        );

        db.close();

        return kq > 0;
    }

    // Hàm tìm kiếm môn học
    public ArrayList<MonHoc> search(String key) {

        ArrayList<MonHoc> list = new ArrayList<>();

        SQLiteDatabase db = dbHelper.getReadableDatabase();

        // Tìm theo:
        // mã môn
        // tên môn
        // mã ngành
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

    // Hàm lấy danh sách môn học đưa vào Spinner
    public ArrayList<String> getDanhSachSpinner() {

        ArrayList<String> list = new ArrayList<>();

        SQLiteDatabase db = dbHelper.getReadableDatabase();

        Cursor c = db.rawQuery(
                "SELECT maMon, tenMon FROM MONHOC ORDER BY maMon ASC",
                null
        );

        if (c.moveToFirst()) {
            do {
                // Hiển thị dạng: CT101 - Java
                list.add(c.getString(0) + " - " + c.getString(1));
            } while (c.moveToNext());
        }

        c.close();
        db.close();

        return list;
    }

    // Hàm lấy danh sách môn học theo ngành
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

    // Hàm lấy môn học theo lớp
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
}