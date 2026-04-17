package com.example.quanylysinhvien.admin;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.quanylysinhvien.R;
import com.example.quanylysinhvien.dao.LopDAO;
import com.example.quanylysinhvien.dao.MonHocDAO;
import com.example.quanylysinhvien.dao.NganhDAO;
import com.example.quanylysinhvien.database.DBHelper;
import com.example.quanylysinhvien.model.Lop;

import java.util.ArrayList;

public class QuanLyLopActivity extends AppCompatActivity {

    private LinearLayout linearLayoutThemLop;
    private Animation animation;

    private EditText edtMaLop, edtTenLop;
    private Spinner spNganh, spMonHocLop;
    private Button btnThemLop, btnSuaLop, btnXoaLop, btnLamMoiLop, btnXemlop;

    private LopDAO lopDAO;
    private NganhDAO nganhDAO;
    private MonHocDAO monHocDAO;

    private ArrayList<String> dsNganh;
    private ArrayList<String> dsMonHoc;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_them_lop);

        anhXa();

        lopDAO = new LopDAO(this);
        nganhDAO = new NganhDAO(this);
        monHocDAO = new MonHocDAO(this);

        animation = AnimationUtils.loadAnimation(this, R.anim.uptodowndiagonal);
        linearLayoutThemLop.setAnimation(animation);

        loadSpinnerNganh();
        nhanDuLieuSua();

        spNganh.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                loadSpinnerMonHocTheoNganh();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        btnThemLop.setOnClickListener(v -> themLop());
        btnSuaLop.setOnClickListener(v -> suaLop());
        btnXoaLop.setOnClickListener(v -> xoaLop());
        btnLamMoiLop.setOnClickListener(v -> lamMoiForm());

        btnXemlop.setOnClickListener(v -> {
            startActivity(new Intent(QuanLyLopActivity.this, DanhSachLopActivity.class));
            finish();
        });
    }

    private void anhXa() {
        linearLayoutThemLop = findViewById(R.id.linearLayoutThemLop);
        edtMaLop = findViewById(R.id.edtMaLop);
        edtTenLop = findViewById(R.id.edtTenLop);

        spNganh = findViewById(R.id.spNganhLop);
        spMonHocLop = findViewById(R.id.spMonHocLop);

        btnThemLop = findViewById(R.id.btnThemLop);
        btnSuaLop = findViewById(R.id.btnSuaLop);
        btnXoaLop = findViewById(R.id.btnXoaLop);
        btnLamMoiLop = findViewById(R.id.btnLamMoiLop);
        btnXemlop = findViewById(R.id.btnXemlop);
    }

    private void loadSpinnerNganh() {
        dsNganh = nganhDAO.getDanhSachSpinner();

        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_spinner_item,
                dsNganh
        );
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spNganh.setAdapter(adapter);
    }

    private void loadSpinnerMonHocTheoNganh() {
        if (spNganh.getSelectedItem() == null) return;

        String nganhChon = spNganh.getSelectedItem().toString();
        String maNganh = nganhChon.split(" - ")[0];

        dsMonHoc = monHocDAO.getDanhSachMonTheoNganh(maNganh);

        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_spinner_item,
                dsMonHoc
        );
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spMonHocLop.setAdapter(adapter);
    }

    private void nhanDuLieuSua() {
        Intent intent = getIntent();
        if (intent == null) {
            cheDoThem();
            return;
        }

        String maLop = intent.getStringExtra("maLop");
        String tenLop = intent.getStringExtra("tenLop");
        String maNganh = intent.getStringExtra("maNganh");

        if (maLop == null || tenLop == null || maNganh == null) {
            cheDoThem();
            return;
        }

        edtMaLop.setText(maLop);
        edtTenLop.setText(tenLop);
        edtMaLop.setEnabled(false);

        for (int i = 0; i < dsNganh.size(); i++) {
            if (dsNganh.get(i).startsWith(maNganh + " - ")) {
                spNganh.setSelection(i);
                break;
            }
        }

        btnThemLop.setEnabled(false);
        btnSuaLop.setEnabled(true);
        btnXoaLop.setEnabled(true);
    }

    private void cheDoThem() {
        edtMaLop.setEnabled(true);
        btnThemLop.setEnabled(true);
        btnSuaLop.setEnabled(false);
        btnXoaLop.setEnabled(false);
    }

    private void themLop() {
        String maLop = edtMaLop.getText().toString().trim();
        String tenLop = edtTenLop.getText().toString().trim();

        if (!kiemTraDuLieu(maLop, tenLop)) return;

        if (spNganh.getSelectedItem() == null) {
            Toast.makeText(this, "Chọn ngành", Toast.LENGTH_SHORT).show();
            return;
        }

        String maNganh = spNganh.getSelectedItem().toString().split(" - ")[0];

        Lop lop = new Lop(maLop, tenLop, maNganh);

        if (lopDAO.insert(lop)) {

            if (spMonHocLop.getSelectedItem() != null) {
                String maMon = spMonHocLop.getSelectedItem().toString().split(" - ")[0];
                ganMonHocChoLop(maLop, maMon);
            }

            Toast.makeText(this, "Thêm lớp thành công", Toast.LENGTH_SHORT).show();
            lamMoiForm();

        } else {
            Toast.makeText(this, "Thêm lớp thất bại", Toast.LENGTH_SHORT).show();
        }
    }

    private void suaLop() {
        String maLop = edtMaLop.getText().toString().trim();
        String tenLop = edtTenLop.getText().toString().trim();

        if (!kiemTraDuLieu(maLop, tenLop)) return;

        if (spNganh.getSelectedItem() == null) {
            Toast.makeText(this, "Chọn ngành", Toast.LENGTH_SHORT).show();
            return;
        }

        String maNganh = spNganh.getSelectedItem().toString().split(" - ")[0];

        Lop lop = new Lop(maLop, tenLop, maNganh);

        if (lopDAO.update(lop)) {
            Toast.makeText(this, "Sửa lớp thành công", Toast.LENGTH_SHORT).show();
            lamMoiForm();
        } else {
            Toast.makeText(this, "Sửa lớp thất bại", Toast.LENGTH_SHORT).show();
        }
    }

    private void xoaLop() {
        String maLop = edtMaLop.getText().toString().trim();

        if (maLop.isEmpty()) {
            Toast.makeText(this, "Chọn lớp cần xóa", Toast.LENGTH_SHORT).show();
            return;
        }

        new AlertDialog.Builder(this)
                .setTitle("Xác nhận xóa")
                .setMessage("Bạn có chắc muốn xóa lớp " + maLop + " không?")
                .setPositiveButton("Xóa", (dialog, which) -> {
                    boolean kq = lopDAO.delete(maLop);

                    if (kq) {
                        Toast.makeText(this, "Xóa lớp thành công", Toast.LENGTH_SHORT).show();
                        lamMoiForm();
                    } else {
                        Toast.makeText(this, "Xóa lớp thất bại", Toast.LENGTH_SHORT).show();
                    }
                })
                .setNegativeButton("Hủy", null)
                .show();
    }

    private void ganMonHocChoLop(String maLop, String maMon) {
        try {
            SQLiteDatabase db = new DBHelper(this).getWritableDatabase();

            ContentValues values = new ContentValues();
            values.put("maLop", maLop);
            values.put("maMon", maMon);
            values.put("hocKy", 1);
            values.put("namHoc", "2025-2026");

            db.insert("LOP_MONHOC", null, values);
            db.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private boolean kiemTraDuLieu(String maLop, String tenLop) {
        if (maLop.isEmpty()) {
            edtMaLop.setError("Nhập mã lớp");
            edtMaLop.requestFocus();
            return false;
        }

        if (tenLop.isEmpty()) {
            edtTenLop.setError("Nhập tên lớp");
            edtTenLop.requestFocus();
            return false;
        }

        return true;
    }

    private void lamMoiForm() {
        edtMaLop.setText("");
        edtTenLop.setText("");

        if (spNganh.getAdapter() != null) spNganh.setSelection(0);
        if (spMonHocLop.getAdapter() != null) spMonHocLop.setSelection(0);

        edtMaLop.requestFocus();
        cheDoThem();
    }
}