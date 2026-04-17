package com.example.quanylysinhvien.model;

public class TaikhoanMatKhau {
    private String tenTaiKhoan;
    private String matKhau;
    private String vaiTro;
    private String maSv;

    public TaikhoanMatKhau() {
    }

    public TaikhoanMatKhau(String tenTaiKhoan, String matKhau, String vaiTro, String maSv) {
        this.tenTaiKhoan = tenTaiKhoan;
        this.matKhau = matKhau;
        this.vaiTro = vaiTro;
        this.maSv = maSv;
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

    public String getVaiTro() {
        return vaiTro;
    }

    public void setVaiTro(String vaiTro) {
        this.vaiTro = vaiTro;
    }

    public String getMaSv() {
        return maSv;
    }

    public void setMaSv(String maSv) {
        this.maSv = maSv;
    }
}