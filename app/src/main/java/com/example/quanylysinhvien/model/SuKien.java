package com.example.quanylysinhvien.model;

public class SuKien {
    private String maSk;
    private String tenSk;
    private String ngaySk;
    private String noiDung;

    public SuKien() {
    }

    public SuKien(String maSk, String tenSk, String ngaySk, String noiDung) {
        this.maSk = maSk;
        this.tenSk = tenSk;
        this.ngaySk = ngaySk;
        this.noiDung = noiDung;
    }

    public String getMaSk() {
        return maSk;
    }

    public void setMaSk(String maSk) {
        this.maSk = maSk;
    }

    public String getTenSk() {
        return tenSk;
    }

    public void setTenSk(String tenSk) {
        this.tenSk = tenSk;
    }

    public String getNgaySk() {
        return ngaySk;
    }

    public void setNgaySk(String ngaySk) {
        this.ngaySk = ngaySk;
    }

    public String getNoiDung() {
        return noiDung;
    }

    public void setNoiDung(String noiDung) {
        this.noiDung = noiDung;
    }
}