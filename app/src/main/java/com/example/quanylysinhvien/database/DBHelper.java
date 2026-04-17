package com.example.quanylysinhvien.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class DBHelper extends SQLiteOpenHelper {

    private static final String DB_NAME = "QUANLYSINHVIENQNUDB.sqlite";
    private static final int DB_VERSION = 2;

    public DBHelper(@Nullable Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        db.execSQL("PRAGMA foreign_keys=ON");

        // ================= LOP =================
        db.execSQL("CREATE TABLE LOP(" +
                "maLop TEXT PRIMARY KEY," +
                "tenLop TEXT)");

        db.execSQL("INSERT INTO LOP VALUES('LT1','Lập Trình Android')");
        db.execSQL("INSERT INTO LOP VALUES('LT2','Lập Trình PHP')");
        db.execSQL("INSERT INTO LOP VALUES('LT3','Lập Trình C#')");

        // ================= NGANH =================
        db.execSQL("CREATE TABLE NGANH(" +
                "maNganh TEXT PRIMARY KEY," +
                "tenNganh TEXT)");

        db.execSQL("INSERT INTO NGANH VALUES('CNPM','Công Nghệ Phần Mềm')");
        db.execSQL("INSERT INTO NGANH VALUES('HTTT','Hệ Thống Thông Tin')");
        db.execSQL("INSERT INTO NGANH VALUES('KTMT','Kỹ Thuật Máy Tính')");

        // ================= MONHOC =================
        db.execSQL("CREATE TABLE MONHOC(" +
                "maMon TEXT PRIMARY KEY," +
                "tenMon TEXT," +
                "soTinChi INTEGER)");

        db.execSQL("INSERT INTO MONHOC VALUES('MH01','Lập Trình Java',3)");
        db.execSQL("INSERT INTO MONHOC VALUES('MH02','Cơ Sở Dữ Liệu',3)");
        db.execSQL("INSERT INTO MONHOC VALUES('MH03','Lập Trình Android',4)");

        // ================= SINHVIEN =================
        db.execSQL("CREATE TABLE SINHVIEN(" +
                "maSv TEXT PRIMARY KEY," +
                "tenSV TEXT," +
                "email TEXT," +
                "hinh TEXT," +
                "maLop TEXT," +
                "maNganh TEXT," +
                "FOREIGN KEY(maLop) REFERENCES LOP(maLop)," +
                "FOREIGN KEY(maNganh) REFERENCES NGANH(maNganh))");

        db.execSQL("INSERT INTO SINHVIEN VALUES('001','Lê Trung Hậu','haule123@gmail.com','hau','LT1','CNPM')");
        db.execSQL("INSERT INTO SINHVIEN VALUES('002','Nguyễn Ngọc Khoan','khoan@gmail.com','khoan','LT2','HTTT')");
        db.execSQL("INSERT INTO SINHVIEN VALUES('003','Nguyễn Hoàng Vũ','vu@gmail.com','vu','LT3','KTMT')");

        // ================= DIEM =================
        db.execSQL("CREATE TABLE DIEM(" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "maSv TEXT," +
                "maMon TEXT," +
                "diem REAL," +
                "FOREIGN KEY(maSv) REFERENCES SINHVIEN(maSv)," +
                "FOREIGN KEY(maMon) REFERENCES MONHOC(maMon))");

        db.execSQL("INSERT INTO DIEM(maSv,maMon,diem) VALUES('001','MH01',8.5)");
        db.execSQL("INSERT INTO DIEM(maSv,maMon,diem) VALUES('001','MH02',7.0)");
        db.execSQL("INSERT INTO DIEM(maSv,maMon,diem) VALUES('002','MH01',9.0)");
        db.execSQL("INSERT INTO DIEM(maSv,maMon,diem) VALUES('003','MH03',8.0)");

        // ================= SUKIEN =================
        db.execSQL("CREATE TABLE SUKIEN(" +
                "maSk TEXT PRIMARY KEY," +
                "tenSk TEXT," +
                "ngaySk TEXT," +
                "noiDung TEXT)");

        db.execSQL("INSERT INTO SUKIEN VALUES('SK01','Chào Tân Sinh Viên','10/09/2026','Sự kiện toàn trường')");
        db.execSQL("INSERT INTO SUKIEN VALUES('SK02','Hội Thảo Android','15/09/2026','Android từ cơ bản đến nâng cao')");

        // ================= TAIKHOAN =================
        db.execSQL("CREATE TABLE taiKhoan(" +
                "tenTaiKhoan TEXT PRIMARY KEY," +
                "matKhau TEXT)");

        db.execSQL("INSERT INTO taiKhoan VALUES('admin','admin')");
        db.execSQL("INSERT INTO taiKhoan VALUES('user1','123456')");

        // ================= PHANHOI =================
        db.execSQL("CREATE TABLE PHANHOI(" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "tenTaiKhoan TEXT," +
                "noiDung TEXT," +
                "ngayGui TEXT)");

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        db.execSQL("DROP TABLE IF EXISTS PHANHOI");
        db.execSQL("DROP TABLE IF EXISTS DIEM");
        db.execSQL("DROP TABLE IF EXISTS SINHVIEN");
        db.execSQL("DROP TABLE IF EXISTS MONHOC");
        db.execSQL("DROP TABLE IF EXISTS NGANH");
        db.execSQL("DROP TABLE IF EXISTS LOP");
        db.execSQL("DROP TABLE IF EXISTS SUKIEN");
        db.execSQL("DROP TABLE IF EXISTS taiKhoan");

        onCreate(db);
    }
}