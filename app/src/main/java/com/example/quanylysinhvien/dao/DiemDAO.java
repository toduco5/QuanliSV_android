package com.example.quanylysinhvien.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.quanylysinhvien.database.DBHelper;
import com.example.quanylysinhvien.model.Diem;

import java.util.ArrayList;

// DAO điểm
// chức năng: lấy danh sách điểm, thêm, sửa, xóa, tìm kiếm
public class DiemDAO {

    // biến kết nối database
    DBHelper dbHelper;

    // constructor
    public DiemDAO(Context context) {
        dbHelper = new DBHelper(context);
    }

    // lấy toàn bộ điểm
    public ArrayList<Diem> getAll() {

        ArrayList<Diem> list = new ArrayList<>();

        // mở database chế độ đọc
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        // lấy tất cả dữ liệu bảng DIEM
        Cursor c = db.rawQuery("SELECT * FROM DIEM", null);

        // duyệt từng dòng dữ liệu
        while (c.moveToNext()) {

            list.add(new Diem(
                    c.getInt(0),     // id
                    c.getString(1),  // mã sinh viên
                    c.getString(2),  // mã môn
                    c.getFloat(3)    // điểm
            ));
        }

        c.close();
        db.close();

        return list;
    }

    // thêm điểm mới
    public boolean insert(Diem d) {

        SQLiteDatabase db = dbHelper.getWritableDatabase();

        ContentValues values = new ContentValues();

        values.put("maSv", d.getMaSv());
        values.put("maMon", d.getMaMon());
        values.put("diem", d.getDiem());

        // thêm vào bảng DIEM
        long kq = db.insert("DIEM", null, values);

        db.close();

        // nếu khác -1 là thành công
        return kq != -1;
    }

    // sửa điểm theo id
    public boolean update(Diem d) {

        SQLiteDatabase db = dbHelper.getWritableDatabase();

        ContentValues values = new ContentValues();

        values.put("maSv", d.getMaSv());
        values.put("maMon", d.getMaMon());
        values.put("diem", d.getDiem());

        // update theo id
        int kq = db.update(
                "DIEM",
                values,
                "id=?",
                new String[]{String.valueOf(d.getId())}
        );

        db.close();

        return kq > 0;
    }

    // xóa điểm theo id
    public boolean delete(int id) {

        SQLiteDatabase db = dbHelper.getWritableDatabase();

        int kq = db.delete(
                "DIEM",
                "id=?",
                new String[]{String.valueOf(id)}
        );

        db.close();

        return kq > 0;
    }

    // tìm kiếm điểm theo mã sinh viên hoặc mã môn
    public ArrayList<Diem> search(String tuKhoa) {

        ArrayList<Diem> list = new ArrayList<>();

        SQLiteDatabase db = dbHelper.getReadableDatabase();

        Cursor c = db.rawQuery(
                "SELECT * FROM DIEM " +
                        "WHERE maSv LIKE ? OR maMon LIKE ?",

                new String[]{
                        "%" + tuKhoa + "%",
                        "%" + tuKhoa + "%"
                }
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