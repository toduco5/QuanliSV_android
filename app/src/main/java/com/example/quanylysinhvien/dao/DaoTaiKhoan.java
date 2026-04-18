package com.example.quanylysinhvien.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.quanylysinhvien.database.DBHelper;
import com.example.quanylysinhvien.model.TaikhoanMatKhau;

import java.util.ArrayList;

// DAO dùng để thao tác bảng taiKhoan trong SQLite
// Chức năng: thêm, sửa, xóa, đăng nhập, lấy vai trò...
public class DaoTaiKhoan {

    // Biến hỗ trợ kết nối database
    private DBHelper dbHelper;

    // Constructor
    // Khi gọi DaoTaiKhoan sẽ khởi tạo DBHelper
    public DaoTaiKhoan(Context context) {
        dbHelper = new DBHelper(context);
    }

    // Lấy toàn bộ tài khoản trong bảng taiKhoan
    public ArrayList<TaikhoanMatKhau> getAll() {

        // Tạo list rỗng
        ArrayList<TaikhoanMatKhau> list = new ArrayList<>();

        // Mở database chế độ đọc
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        // Câu lệnh lấy toàn bộ tài khoản
        Cursor c = db.rawQuery(
                "SELECT tenTaiKhoan, matKhau, vaiTro, maSv FROM taiKhoan",
                null
        );

        // Nếu có dữ liệu
        if (c.moveToFirst()) {
            do {

                // Thêm từng dòng vào ArrayList
                list.add(new TaikhoanMatKhau(
                        c.getString(0), // tên tài khoản
                        c.getString(1), // mật khẩu
                        c.getString(2), // vai trò
                        c.getString(3)  // mã sinh viên
                ));

            } while (c.moveToNext());
        }

        // Đóng database
        c.close();
        db.close();

        return list;
    }

    // Thêm tài khoản mới
    public boolean them(TaikhoanMatKhau tk) {

        SQLiteDatabase db = dbHelper.getWritableDatabase();

        // Dùng ContentValues để insert
        ContentValues values = new ContentValues();

        values.put("tenTaiKhoan", tk.getTenTaiKhoan());
        values.put("matKhau", tk.getMatKhau());
        values.put("vaiTro", tk.getVaiTro());
        values.put("maSv", tk.getMaSv());

        // Insert vào bảng
        long kq = db.insert("taiKhoan", null, values);

        db.close();

        // Nếu khác -1 nghĩa là thành công
        return kq != -1;
    }

    // Hàm phụ gọi lại hàm them()
    public boolean Them(TaikhoanMatKhau tk) {
        return them(tk);
    }

    // Sửa tài khoản theo username
    public boolean sua(TaikhoanMatKhau tk) {

        SQLiteDatabase db = dbHelper.getWritableDatabase();

        ContentValues values = new ContentValues();

        // Chỉ sửa các cột sau
        values.put("matKhau", tk.getMatKhau());
        values.put("vaiTro", tk.getVaiTro());
        values.put("maSv", tk.getMaSv());

        // Update theo điều kiện username
        int kq = db.update(
                "taiKhoan",
                values,
                "tenTaiKhoan=?",
                new String[]{tk.getTenTaiKhoan()}
        );

        db.close();

        return kq > 0;
    }

    // Kiểm tra đăng nhập
    // Nếu đúng tài khoản + mật khẩu => true
    public boolean kiemTraDangNhap(String tenTaiKhoan, String matKhau) {

        SQLiteDatabase db = dbHelper.getReadableDatabase();

        Cursor c = db.rawQuery(
                "SELECT * FROM taiKhoan " +
                        "WHERE tenTaiKhoan=? AND matKhau=?",
                new String[]{tenTaiKhoan, matKhau}
        );

        boolean check = c.moveToFirst();

        c.close();
        db.close();

        return check;
    }

    // Lấy vai trò tài khoản
    // ADMIN hoặc USER
    public String getVaiTro(String tenTaiKhoan) {

        SQLiteDatabase db = dbHelper.getReadableDatabase();

        Cursor c = db.rawQuery(
                "SELECT vaiTro FROM taiKhoan WHERE tenTaiKhoan=?",
                new String[]{tenTaiKhoan}
        );

        String vaiTro = "";

        if (c.moveToFirst()) {
            vaiTro = c.getString(0);
        }

        c.close();
        db.close();

        return vaiTro;
    }

    // Lấy mã sinh viên của tài khoản USER
    public String getMaSvTheoTaiKhoan(String tenTaiKhoan) {

        SQLiteDatabase db = dbHelper.getReadableDatabase();

        Cursor c = db.rawQuery(
                "SELECT maSv FROM taiKhoan WHERE tenTaiKhoan=?",
                new String[]{tenTaiKhoan}
        );

        String maSv = null;

        if (c.moveToFirst()) {
            maSv = c.getString(0);
        }

        c.close();
        db.close();

        return maSv;
    }

    // Kiểm tra username đã tồn tại chưa
    public boolean kiemTraTonTai(String tenTaiKhoan) {

        SQLiteDatabase db = dbHelper.getReadableDatabase();

        Cursor c = db.rawQuery(
                "SELECT * FROM taiKhoan WHERE tenTaiKhoan=?",
                new String[]{tenTaiKhoan}
        );

        boolean check = c.moveToFirst();

        c.close();
        db.close();

        return check;
    }

    // Hàm phụ gọi lại kiểm tra tồn tại
    public boolean kiemTraTonTaiTaiKhoan(String tenTaiKhoan) {
        return kiemTraTonTai(tenTaiKhoan);
    }

    // Lấy chi tiết 1 tài khoản
    public TaikhoanMatKhau getTaiKhoan(String tenTaiKhoan) {

        SQLiteDatabase db = dbHelper.getReadableDatabase();

        Cursor c = db.rawQuery(
                "SELECT tenTaiKhoan, matKhau, vaiTro, maSv " +
                        "FROM taiKhoan WHERE tenTaiKhoan=?",
                new String[]{tenTaiKhoan}
        );

        TaikhoanMatKhau tk = null;

        if (c.moveToFirst()) {

            tk = new TaikhoanMatKhau(
                    c.getString(0),
                    c.getString(1),
                    c.getString(2),
                    c.getString(3)
            );
        }

        c.close();
        db.close();

        return tk;
    }

    // Đổi mật khẩu tài khoản
    public boolean doiMatKhau(String tenTaiKhoan, String matKhauMoi) {

        SQLiteDatabase db = dbHelper.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put("matKhau", matKhauMoi);

        int kq = db.update(
                "taiKhoan",
                values,
                "tenTaiKhoan=?",
                new String[]{tenTaiKhoan}
        );

        db.close();

        return kq > 0;
    }

    // Xóa tài khoản theo username
    public boolean xoaTaiKhoan(String tenTaiKhoan) {

        SQLiteDatabase db = dbHelper.getWritableDatabase();

        int kq = db.delete(
                "taiKhoan",
                "tenTaiKhoan=?",
                new String[]{tenTaiKhoan}
        );

        db.close();

        return kq > 0;
    }
}