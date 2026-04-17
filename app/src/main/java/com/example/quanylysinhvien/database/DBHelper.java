package com.example.quanylysinhvien.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class DBHelper extends SQLiteOpenHelper {

    private static final String DB_NAME = "QUANLYSINHVIENQNUDB.sqlite";
    private static final int DB_VERSION = 6;

    public DBHelper(@Nullable Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onConfigure(SQLiteDatabase db) {
        super.onConfigure(db);
        db.setForeignKeyConstraintsEnabled(true);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        // ================= NGANH =================
        db.execSQL("CREATE TABLE NGANH(" +
                "maNganh TEXT PRIMARY KEY," +
                "tenNganh TEXT NOT NULL" +
                ")");

        db.execSQL("INSERT INTO NGANH VALUES('CNPM','Công Nghệ Phần Mềm')");
        db.execSQL("INSERT INTO NGANH VALUES('HTTT','Hệ Thống Thông Tin')");
        db.execSQL("INSERT INTO NGANH VALUES('KTMT','Kỹ Thuật Máy Tính')");
        db.execSQL("INSERT INTO NGANH VALUES('QTKD','Quản Trị Kinh Doanh')");

        // ================= LOP =================
        db.execSQL("CREATE TABLE LOP(" +
                "maLop TEXT PRIMARY KEY," +
                "tenLop TEXT NOT NULL," +
                "maNganh TEXT NOT NULL," +
                "FOREIGN KEY(maNganh) REFERENCES NGANH(maNganh) " +
                "ON UPDATE CASCADE ON DELETE RESTRICT" +
                ")");

        db.execSQL("INSERT INTO LOP VALUES('LT1','Lập Trình Android','CNPM')");
        db.execSQL("INSERT INTO LOP VALUES('LT2','Lập Trình PHP','HTTT')");
        db.execSQL("INSERT INTO LOP VALUES('LT3','Lập Trình C#','KTMT')");
        db.execSQL("INSERT INTO LOP VALUES('QTKD1','Quản Trị Kinh Doanh 1','QTKD')");

        // ================= MONHOC =================
        db.execSQL("CREATE TABLE MONHOC(" +
                "maMon TEXT PRIMARY KEY," +
                "tenMon TEXT NOT NULL," +
                "soTinChi INTEGER NOT NULL," +
                "maNganh TEXT NOT NULL," +
                "FOREIGN KEY(maNganh) REFERENCES NGANH(maNganh) " +
                "ON UPDATE CASCADE ON DELETE RESTRICT" +
                ")");

        db.execSQL("INSERT INTO MONHOC VALUES('MH01','Lập Trình Java',3,'CNPM')");
        db.execSQL("INSERT INTO MONHOC VALUES('MH02','Cơ Sở Dữ Liệu',3,'HTTT')");
        db.execSQL("INSERT INTO MONHOC VALUES('MH03','Lập Trình Android',4,'CNPM')");
        db.execSQL("INSERT INTO MONHOC VALUES('MH04','Lập Trình C#',3,'KTMT')");
        db.execSQL("INSERT INTO MONHOC VALUES('MH05','Marketing',2,'QTKD')");
        db.execSQL("INSERT INTO MONHOC VALUES('MH06','Quản Trị Doanh Nghiệp',3,'QTKD')");
        db.execSQL("INSERT INTO MONHOC VALUES('MH07','Phân Tích Thiết Kế Hệ Thống',3,'HTTT')");

        // ================= LOP_MONHOC =================
        db.execSQL("CREATE TABLE LOP_MONHOC(" +
                "maLop TEXT NOT NULL," +
                "maMon TEXT NOT NULL," +
                "hocKy INTEGER," +
                "namHoc TEXT," +
                "PRIMARY KEY(maLop, maMon)," +
                "FOREIGN KEY(maLop) REFERENCES LOP(maLop) " +
                "ON UPDATE CASCADE ON DELETE CASCADE," +
                "FOREIGN KEY(maMon) REFERENCES MONHOC(maMon) " +
                "ON UPDATE CASCADE ON DELETE CASCADE" +
                ")");

        db.execSQL("INSERT INTO LOP_MONHOC VALUES('LT1','MH01',1,'2025-2026')");
        db.execSQL("INSERT INTO LOP_MONHOC VALUES('LT1','MH03',1,'2025-2026')");
        db.execSQL("INSERT INTO LOP_MONHOC VALUES('LT2','MH02',1,'2025-2026')");
        db.execSQL("INSERT INTO LOP_MONHOC VALUES('LT2','MH07',2,'2025-2026')");
        db.execSQL("INSERT INTO LOP_MONHOC VALUES('LT3','MH04',1,'2025-2026')");
        db.execSQL("INSERT INTO LOP_MONHOC VALUES('QTKD1','MH05',1,'2025-2026')");
        db.execSQL("INSERT INTO LOP_MONHOC VALUES('QTKD1','MH06',2,'2025-2026')");

        // ================= SINHVIEN =================
        db.execSQL("CREATE TABLE SINHVIEN(" +
                "maSv TEXT PRIMARY KEY," +
                "tenSV TEXT NOT NULL," +
                "email TEXT," +
                "hinh TEXT," +
                "maLop TEXT NOT NULL," +
                "maNganh TEXT NOT NULL," +
                "FOREIGN KEY(maLop) REFERENCES LOP(maLop) " +
                "ON UPDATE CASCADE ON DELETE RESTRICT," +
                "FOREIGN KEY(maNganh) REFERENCES NGANH(maNganh) " +
                "ON UPDATE CASCADE ON DELETE RESTRICT" +
                ")");

        db.execSQL("INSERT INTO SINHVIEN VALUES('001','Lê Trung Hậu','haule123@gmail.com','hau','LT1','CNPM')");
        db.execSQL("INSERT INTO SINHVIEN VALUES('002','Nguyễn Ngọc Khoan','khoan@gmail.com','khoan','LT2','HTTT')");
        db.execSQL("INSERT INTO SINHVIEN VALUES('003','Nguyễn Hoàng Vũ','vu@gmail.com','vu','LT3','KTMT')");
        db.execSQL("INSERT INTO SINHVIEN VALUES('004','Trần Minh Anh','anh@gmail.com','anh','QTKD1','QTKD')");

        // ================= DIEM =================
        db.execSQL("CREATE TABLE DIEM(" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "maSv TEXT NOT NULL," +
                "maMon TEXT NOT NULL," +
                "diem REAL NOT NULL," +
                "FOREIGN KEY(maSv) REFERENCES SINHVIEN(maSv) " +
                "ON UPDATE CASCADE ON DELETE CASCADE," +
                "FOREIGN KEY(maMon) REFERENCES MONHOC(maMon) " +
                "ON UPDATE CASCADE ON DELETE RESTRICT," +
                "UNIQUE(maSv, maMon)" +
                ")");

        db.execSQL("INSERT INTO DIEM(maSv,maMon,diem) VALUES('001','MH01',8.5)");
        db.execSQL("INSERT INTO DIEM(maSv,maMon,diem) VALUES('001','MH03',7.0)");
        db.execSQL("INSERT INTO DIEM(maSv,maMon,diem) VALUES('002','MH02',9.0)");
        db.execSQL("INSERT INTO DIEM(maSv,maMon,diem) VALUES('003','MH04',8.0)");
        db.execSQL("INSERT INTO DIEM(maSv,maMon,diem) VALUES('004','MH05',8.8)");

        // ================= TAIKHOAN =================
        db.execSQL("CREATE TABLE taiKhoan(" +
                "tenTaiKhoan TEXT PRIMARY KEY," +
                "matKhau TEXT NOT NULL," +
                "vaiTro TEXT NOT NULL," +
                "maSv TEXT," +
                "FOREIGN KEY(maSv) REFERENCES SINHVIEN(maSv) " +
                "ON UPDATE CASCADE ON DELETE SET NULL" +
                ")");

        db.execSQL("INSERT INTO taiKhoan VALUES('admin','admin','ADMIN',NULL)");
        db.execSQL("INSERT INTO taiKhoan VALUES('user001','123','USER','001')");
        db.execSQL("INSERT INTO taiKhoan VALUES('user002','123','USER','002')");
        db.execSQL("INSERT INTO taiKhoan VALUES('user003','123','USER','003')");
        db.execSQL("INSERT INTO taiKhoan VALUES('user004','123','USER','004')");

        // ================= SUKIEN =================
        db.execSQL("CREATE TABLE SUKIEN(" +
                "maSk TEXT PRIMARY KEY," +
                "tenSk TEXT NOT NULL," +
                "ngaySk TEXT," +
                "noiDung TEXT" +
                ")");

        db.execSQL("INSERT INTO SUKIEN VALUES('SK01','Chào Tân Sinh Viên','10/09/2026','Sự kiện toàn trường')");
        db.execSQL("INSERT INTO SUKIEN VALUES('SK02','Hội Thảo Android','15/09/2026','Android từ cơ bản đến nâng cao')");
        db.execSQL("INSERT INTO SUKIEN VALUES('SK03','Thông Báo Lịch Thi','20/12/2026','Sinh viên theo dõi lịch thi trên hệ thống')");

        // ================= PHANHOI =================
        db.execSQL("CREATE TABLE PHANHOI(" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "tenTaiKhoan TEXT," +
                "noiDung TEXT," +
                "ngayGui TEXT," +
                "FOREIGN KEY(tenTaiKhoan) REFERENCES taiKhoan(tenTaiKhoan) " +
                "ON UPDATE CASCADE ON DELETE SET NULL" +
                ")");

        db.execSQL("INSERT INTO PHANHOI(tenTaiKhoan,noiDung,ngayGui) VALUES('user001','Ứng dụng dễ dùng','2026-04-18')");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS PHANHOI");
        db.execSQL("DROP TABLE IF EXISTS DIEM");
        db.execSQL("DROP TABLE IF EXISTS LOP_MONHOC");
        db.execSQL("DROP TABLE IF EXISTS taiKhoan");
        db.execSQL("DROP TABLE IF EXISTS SINHVIEN");
        db.execSQL("DROP TABLE IF EXISTS MONHOC");
        db.execSQL("DROP TABLE IF EXISTS LOP");
        db.execSQL("DROP TABLE IF EXISTS NGANH");
        db.execSQL("DROP TABLE IF EXISTS SUKIEN");
        onCreate(db);
    }
}