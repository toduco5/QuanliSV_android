package com.example.quanylysinhvien.admin;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.quanylysinhvien.R;
import com.example.quanylysinhvien.dao.DiemDAO;
import com.example.quanylysinhvien.dao.MonHocDAO;
import com.example.quanylysinhvien.dao.SinhVienDao;
import com.example.quanylysinhvien.model.Diem;

import java.util.ArrayList;

public class QuanLyDiemActivity extends AppCompatActivity {

    Spinner spSinhVien, spMaMon;
    EditText edtDiem;
    Button btnThemDiem;

    DiemDAO diemDAO;
    SinhVienDao sinhVienDao;
    MonHocDAO monHocDAO;

    ArrayList<String> dsSinhVien;
    ArrayList<String> dsMonHoc;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_them_diem);

        anhXa();

        diemDAO = new DiemDAO(this);
        sinhVienDao = new SinhVienDao(this);
        monHocDAO = new MonHocDAO(this);

        loadSpinnerSinhVien();

        spSinhVien.setOnItemSelectedListener(new android.widget.AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(android.widget.AdapterView<?> parent, android.view.View view, int position, long id) {
                loadSpinnerMonHocTheoSinhVien();
            }

            @Override
            public void onNothingSelected(android.widget.AdapterView<?> parent) {
            }
        });

        btnThemDiem.setOnClickListener(v -> xuLyThemDiem());
    }

    private void anhXa() {
        spSinhVien = findViewById(R.id.spSinhVien);
        spMaMon = findViewById(R.id.spMaMon);
        edtDiem = findViewById(R.id.edtDiem);
        btnThemDiem = findViewById(R.id.btnThemDiem);
    }

    private void loadSpinnerSinhVien() {
        dsSinhVien = sinhVienDao.getDanhSachSpinner();

        ArrayAdapter<String> adapterSinhVien = new ArrayAdapter<>(
                this,
                android.R.layout.simple_spinner_item,
                dsSinhVien
        );
        adapterSinhVien.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spSinhVien.setAdapter(adapterSinhVien);

        loadSpinnerMonHocTheoSinhVien();
    }

    private void loadSpinnerMonHocTheoSinhVien() {
        if (spSinhVien.getSelectedItem() == null) return;

        String sinhVienChon = spSinhVien.getSelectedItem().toString();
        String maSv = sinhVienChon.split(" - ")[0];

        String maNganh = sinhVienDao.getMaNganhTheoSinhVien(maSv);
        if (maNganh == null) {
            dsMonHoc = new ArrayList<>();
        } else {
            dsMonHoc = monHocDAO.getDanhSachMonTheoNganh(maNganh);
        }

        ArrayAdapter<String> adapterMonHoc = new ArrayAdapter<>(
                this,
                android.R.layout.simple_spinner_item,
                dsMonHoc
        );
        adapterMonHoc.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spMaMon.setAdapter(adapterMonHoc);
    }

    private void xuLyThemDiem() {
        if (spSinhVien.getSelectedItem() == null) {
            Toast.makeText(this, "Không có sinh viên để chọn", Toast.LENGTH_SHORT).show();
            return;
        }

        if (spMaMon.getSelectedItem() == null) {
            Toast.makeText(this, "Không có môn học để chọn", Toast.LENGTH_SHORT).show();
            return;
        }

        String sinhVienChon = spSinhVien.getSelectedItem().toString();
        String monHocChon = spMaMon.getSelectedItem().toString();

        String maSv = sinhVienChon.split(" - ")[0];
        String maMon = monHocChon.split(" - ")[0];
        String sDiem = edtDiem.getText().toString().trim();

        if (sDiem.isEmpty()) {
            Toast.makeText(this, "Vui lòng nhập điểm", Toast.LENGTH_SHORT).show();
            edtDiem.requestFocus();
            return;
        }

        float diem;
        try {
            diem = Float.parseFloat(sDiem);
        } catch (Exception e) {
            Toast.makeText(this, "Điểm không hợp lệ", Toast.LENGTH_SHORT).show();
            return;
        }

        if (diem < 0 || diem > 10) {
            Toast.makeText(this, "Điểm phải từ 0 đến 10", Toast.LENGTH_SHORT).show();
            return;
        }

        boolean kq = diemDAO.insert(new Diem(maSv, maMon, diem));

        if (kq) {
            Toast.makeText(this, "Thêm điểm thành công", Toast.LENGTH_SHORT).show();
            edtDiem.setText("");
            spSinhVien.setSelection(0);
        } else {
            Toast.makeText(this, "Thêm thất bại hoặc môn này đã có điểm", Toast.LENGTH_SHORT).show();
        }
    }
}