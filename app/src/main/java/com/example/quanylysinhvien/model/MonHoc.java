package com.example.quanylysinhvien.model;

public class MonHoc {

    // maMon: lưu mã môn học, ví dụ CT101
    private String maMon;

    // tenMon: lưu tên môn học, ví dụ Lập trình Java
    private String tenMon;

    // soTinChi: lưu số tín chỉ của môn học
    private int soTinChi;

    // maNganh: lưu mã ngành mà môn học thuộc về
    private String maNganh;

    // Constructor rỗng
    // Dùng khi cần tạo đối tượng trước rồi gán dữ liệu sau
    public MonHoc() {
    }

    // Constructor đầy đủ
    // Dùng khi muốn tạo nhanh 1 môn học với đầy đủ thông tin
    public MonHoc(String maMon, String tenMon, int soTinChi, String maNganh) {
        this.maMon = maMon;
        this.tenMon = tenMon;
        this.soTinChi = soTinChi;
        this.maNganh = maNganh;
    }

    // Hàm lấy mã môn
    public String getMaMon() {
        return maMon;
    }

    // Hàm gán mã môn
    public void setMaMon(String maMon) {
        this.maMon = maMon;
    }

    // Hàm lấy tên môn
    public String getTenMon() {
        return tenMon;
    }

    // Hàm gán tên môn
    public void setTenMon(String tenMon) {
        this.tenMon = tenMon;
    }

    // Hàm lấy số tín chỉ
    public int getSoTinChi() {
        return soTinChi;
    }

    // Hàm gán số tín chỉ
    public void setSoTinChi(int soTinChi) {
        this.soTinChi = soTinChi;
    }

    // Hàm lấy mã ngành
    public String getMaNganh() {
        return maNganh;
    }

    // Hàm gán mã ngành
    public void setMaNganh(String maNganh) {
        this.maNganh = maNganh;
    }
}