package com.example.quanylysinhvien.model;

// class Diem dùng để lưu thông tin điểm số của sinh viên
public class Diem {

    // id của bảng điểm (tự tăng trong database)
    private int id;

    // mã sinh viên
    private String maSv;

    // mã môn học
    private String maMon;

    // điểm số
    private float diem;

    // constructor rỗng
    // dùng khi tạo object trước rồi gán dữ liệu sau
    public Diem() {
    }

    // constructor đầy đủ có id
    // dùng khi lấy dữ liệu từ database ra
    public Diem(int id, String maSv, String maMon, float diem) {
        this.id = id;
        this.maSv = maSv;
        this.maMon = maMon;
        this.diem = diem;
    }

    // constructor không có id
    // dùng khi thêm mới điểm vào database
    public Diem(String maSv, String maMon, float diem) {
        this.maSv = maSv;
        this.maMon = maMon;
        this.diem = diem;
    }

    // lấy id
    public int getId() {
        return id;
    }

    // lấy mã sinh viên
    public String getMaSv() {
        return maSv;
    }

    // lấy mã môn
    public String getMaMon() {
        return maMon;
    }

    // lấy điểm
    public float getDiem() {
        return diem;
    }

    // gán id
    public void setId(int id) {
        this.id = id;
    }

    // gán mã sinh viên
    public void setMaSv(String maSv) {
        this.maSv = maSv;
    }

    // gán mã môn
    public void setMaMon(String maMon) {
        this.maMon = maMon;
    }

    // gán điểm
    public void setDiem(float diem) {
        this.diem = diem;
    }
}