package com.example.quanylysinhvien.model;

public class TaikhoanMatKhau {

    private String tenTaiKhoan;
    private String matKhau;

    public TaikhoanMatKhau() {
    }

    public TaikhoanMatKhau(String tenTaiKhoan, String matKhau) {
        this.tenTaiKhoan = tenTaiKhoan;
        this.matKhau = matKhau;
    }

    public String getTenTaiKhoan() {
        return tenTaiKhoan;
    }

    public void setTenTaiKhoan(String tenTaiKhoan) {
        this.tenTaiKhoan = tenTaiKhoan;
    }

    public String getMatKhau() {
        return matKhau;
    }

    public void setMatKhau(String matKhau) {
        this.matKhau = matKhau;
    }

    @Override
    public String toString() {
        return tenTaiKhoan;
    }
}