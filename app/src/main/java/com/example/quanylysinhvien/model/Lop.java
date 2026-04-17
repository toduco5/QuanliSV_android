package com.example.quanylysinhvien.model;

public class Lop {
    private String maLop;
    private String tenLop;
    private String maNganh;

    public Lop() {
    }

    public Lop(String maLop, String tenLop, String maNganh) {
        this.maLop = maLop;
        this.tenLop = tenLop;
        this.maNganh = maNganh;
    }

    public String getMaLop() {
        return maLop;
    }

    public void setMaLop(String maLop) {
        this.maLop = maLop;
    }

    public String getTenLop() {
        return tenLop;
    }

    public void setTenLop(String tenLop) {
        this.tenLop = tenLop;
    }

    public String getMaNganh() {
        return maNganh;
    }

    public void setMaNganh(String maNganh) {
        this.maNganh = maNganh;
    }
}