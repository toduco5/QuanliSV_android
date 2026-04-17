package com.example.quanylysinhvien.admin;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.quanylysinhvien.R;
import com.example.quanylysinhvien.dao.NganhDAO;
import com.example.quanylysinhvien.model.Nganh;

public class QuanLyNganhActivity extends AppCompatActivity {

    private EditText edtMaNganh, edtTenNganh;
    private Button btnThemNganh, btnSuaNganh, btnXoaNganh, btnLamMoiNganh, btnXemDanhSachNganh;

    private NganhDAO nganhDAO;
    private boolean cheDoSua = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quan_ly_nganh);

        anhXa();
        nganhDAO = new NganhDAO(this);

        nhanDuLieuSua();

        btnThemNganh.setOnClickListener(v -> themNganh());
        btnSuaNganh.setOnClickListener(v -> suaNganh());
        btnXoaNganh.setOnClickListener(v -> xoaNganh());
        btnLamMoiNganh.setOnClickListener(v -> lamMoiForm());

        btnXemDanhSachNganh.setOnClickListener(v -> {
            startActivity(new Intent(QuanLyNganhActivity.this, DanhSachNganhActivity.class));
            finish();
        });
    }

    private void anhXa() {
        edtMaNganh = findViewById(R.id.edtMaNganh);
        edtTenNganh = findViewById(R.id.edtTenNganh);

        btnThemNganh = findViewById(R.id.btnThemNganh);
        btnSuaNganh = findViewById(R.id.btnSuaNganh);
        btnXoaNganh = findViewById(R.id.btnXoaNganh);
        btnLamMoiNganh = findViewById(R.id.btnLamMoiNganh);
        btnXemDanhSachNganh = findViewById(R.id.btnXemDanhSachNganh);
    }

    private void nhanDuLieuSua() {
        Intent intent = getIntent();
        if (intent == null) {
            cheDoThem();
            return;
        }

        String maNganh = intent.getStringExtra("maNganh");
        String tenNganh = intent.getStringExtra("tenNganh");

        if (maNganh == null || tenNganh == null) {
            cheDoThem();
            return;
        }

        cheDoSua = true;

        edtMaNganh.setText(maNganh);
        edtTenNganh.setText(tenNganh);
        edtMaNganh.setEnabled(false);

        btnThemNganh.setEnabled(false);
        btnSuaNganh.setEnabled(true);
        btnXoaNganh.setEnabled(true);
    }

    private void cheDoThem() {
        cheDoSua = false;
        edtMaNganh.setEnabled(true);

        btnThemNganh.setEnabled(true);
        btnSuaNganh.setEnabled(false);
        btnXoaNganh.setEnabled(false);
    }

    private void themNganh() {
        String maNganh = edtMaNganh.getText().toString().trim();
        String tenNganh = edtTenNganh.getText().toString().trim();

        if (!kiemTraDuLieu(maNganh, tenNganh)) return;

        boolean kq = nganhDAO.insert(new Nganh(maNganh, tenNganh));

        if (kq) {
            Toast.makeText(this, "Thêm ngành thành công", Toast.LENGTH_SHORT).show();
            lamMoiForm();
        } else {
            Toast.makeText(this, "Thêm ngành thất bại", Toast.LENGTH_SHORT).show();
        }
    }

    private void suaNganh() {
        String maNganh = edtMaNganh.getText().toString().trim();
        String tenNganh = edtTenNganh.getText().toString().trim();

        if (!kiemTraDuLieu(maNganh, tenNganh)) return;

        boolean kq = nganhDAO.update(new Nganh(maNganh, tenNganh));

        if (kq) {
            Toast.makeText(this, "Sửa ngành thành công", Toast.LENGTH_SHORT).show();
            lamMoiForm();
        } else {
            Toast.makeText(this, "Sửa ngành thất bại", Toast.LENGTH_SHORT).show();
        }
    }

    private void xoaNganh() {
        String maNganh = edtMaNganh.getText().toString().trim();

        if (maNganh.isEmpty()) {
            Toast.makeText(this, "Chọn ngành cần xóa", Toast.LENGTH_SHORT).show();
            return;
        }

        new AlertDialog.Builder(this)
                .setTitle("Xác nhận xóa")
                .setMessage("Bạn có chắc muốn xóa ngành " + maNganh + " không?")
                .setPositiveButton("Xóa", (dialog, which) -> {
                    boolean kq = nganhDAO.delete(maNganh);

                    if (kq) {
                        Toast.makeText(this, "Xóa ngành thành công", Toast.LENGTH_SHORT).show();
                        lamMoiForm();
                    } else {
                        Toast.makeText(this, "Xóa ngành thất bại", Toast.LENGTH_SHORT).show();
                    }
                })
                .setNegativeButton("Hủy", null)
                .show();
    }

    private boolean kiemTraDuLieu(String maNganh, String tenNganh) {
        if (maNganh.isEmpty()) {
            edtMaNganh.setError("Nhập mã ngành");
            edtMaNganh.requestFocus();
            return false;
        }

        if (tenNganh.isEmpty()) {
            edtTenNganh.setError("Nhập tên ngành");
            edtTenNganh.requestFocus();
            return false;
        }

        return true;
    }

    private void lamMoiForm() {
        edtMaNganh.setText("");
        edtTenNganh.setText("");

        edtMaNganh.requestFocus();
        cheDoThem();
    }
}