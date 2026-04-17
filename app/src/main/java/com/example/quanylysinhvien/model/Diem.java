package com.example.quanylysinhvien.model;

public class Diem {
    private int id;
    private String maSv;
    private String maMon;
    private float diem;

    public Diem() {
    }

    public Diem(int id, String maSv, String maMon, float diem) {
        this.id = id;
        this.maSv = maSv;
        this.maMon = maMon;
        this.diem = diem;
    }

    public Diem(String maSv, String maMon, float diem) {
        this.maSv = maSv;
        this.maMon = maMon;
        this.diem = diem;
    }

    public int getId() {
        return id;
    }

    public String getMaSv() {
        return maSv;
    }

    public String getMaMon() {
        return maMon;
    }

    public float getDiem() {
        return diem;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setMaSv(String maSv) {
        this.maSv = maSv;
    }

    public void setMaMon(String maMon) {
        this.maMon = maMon;
    }

    public void setDiem(float diem) {
        this.diem = diem;
    }
}