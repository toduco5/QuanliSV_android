package com.example.quanylysinhvien.model;

public class SinhVien {

    private String maSv;
    private String tenSv;
    private String email;
    private String hinh;
    private String maLop;
    private String maNganh;

    public SinhVien() {
    }

    public SinhVien(String maSv, String tenSv, String email,
                    String hinh, String maLop, String maNganh) {
        this.maSv = maSv;
        this.tenSv = tenSv;
        this.email = email;
        this.hinh = hinh;
        this.maLop = maLop;
        this.maNganh = maNganh;
    }

    public String getMaSv() {
        return maSv;
    }

    public void setMaSv(String maSv) {
        this.maSv = maSv;
    }

    public String getTenSv() {
        return tenSv;
    }

    public void setTenSv(String tenSv) {
        this.tenSv = tenSv;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getHinh() {
        return hinh;
    }

    public void setHinh(String hinh) {
        this.hinh = hinh;
    }

    public String getMaLop() {
        return maLop;
    }

    public void setMaLop(String maLop) {
        this.maLop = maLop;
    }

    public String getMaNganh() {
        return maNganh;
    }

    public void setMaNganh(String maNganh) {
        this.maNganh = maNganh;
    }
}