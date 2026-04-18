package com.example.quanylysinhvien.model;

public class TaikhoanMatKhau {

    // tenTaiKhoan: lưu tên đăng nhập
    private String tenTaiKhoan;

    // matKhau: lưu mật khẩu
    private String matKhau;

    // vaiTro: lưu vai trò tài khoản, ví dụ ADMIN hoặc USER
    private String vaiTro;

    // maSv: mã sinh viên liên kết với tài khoản USER
    private String maSv;

    // Constructor rỗng
    // Dùng khi muốn tạo đối tượng trước, gán dữ liệu sau
    public TaikhoanMatKhau() {
    }

    // Constructor đầy đủ
    // Dùng khi tạo nhanh 1 tài khoản với đầy đủ thông tin
    public TaikhoanMatKhau(String tenTaiKhoan, String matKhau, String vaiTro, String maSv) {
        this.tenTaiKhoan = tenTaiKhoan;
        this.matKhau = matKhau;
        this.vaiTro = vaiTro;
        this.maSv = maSv;
    }

    // Lấy tên tài khoản
    public String getTenTaiKhoan() {
        return tenTaiKhoan;
    }

    // Gán tên tài khoản
    public void setTenTaiKhoan(String tenTaiKhoan) {
        this.tenTaiKhoan = tenTaiKhoan;
    }

    // Lấy mật khẩu
    public String getMatKhau() {
        return matKhau;
    }

    // Gán mật khẩu
    public void setMatKhau(String matKhau) {
        this.matKhau = matKhau;
    }

    // Lấy vai trò
    public String getVaiTro() {
        return vaiTro;
    }

    // Gán vai trò
    public void setVaiTro(String vaiTro) {
        this.vaiTro = vaiTro;
    }

    // Lấy mã sinh viên
    public String getMaSv() {
        return maSv;
    }

    // Gán mã sinh viên
    public void setMaSv(String maSv) {
        this.maSv = maSv;
    }
}