package com.example.quanylysinhvien.admin;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.TextUtils;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.quanylysinhvien.R;
import com.example.quanylysinhvien.adapter.MonHocAdapter;
import com.example.quanylysinhvien.dao.MonHocDAO;
import com.example.quanylysinhvien.dao.NganhDAO;
import com.example.quanylysinhvien.model.MonHoc;

import java.util.ArrayList;

public class QuanLyMonHocActivity extends AppCompatActivity {

    private EditText edtMaMon, edtTenMon, edtSoTinChi;
    private Spinner spNganhMonHoc;
    private Button btnThemMonHoc, btnSuaMonHoc, btnXoaMonHoc, btnLamMoi;
    private ListView lvMonHoc;

    private MonHocDAO monHocDAO;
    private NganhDAO nganhDAO;
    private ArrayList<MonHoc> list;
    private MonHocAdapter adapter;
    private ArrayList<String> dsNganh;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_them_monhoc);

        anhXa();

        monHocDAO = new MonHocDAO(this);
        nganhDAO = new NganhDAO(this);
        list = new ArrayList<>();

        loadSpinnerNganh();
        loadData();
        cheDoThem();

        btnThemMonHoc.setOnClickListener(v -> themMonHoc());
        btnSuaMonHoc.setOnClickListener(v -> suaMonHoc());
        btnXoaMonHoc.setOnClickListener(v -> xoaMonHoc());
        btnLamMoi.setOnClickListener(v -> lamMoiForm());

        lvMonHoc.setOnItemClickListener((parent, view, position, id) -> {
            MonHoc m = list.get(position);

            edtMaMon.setText(m.getMaMon());
            edtTenMon.setText(m.getTenMon());
            edtSoTinChi.setText(String.valueOf(m.getSoTinChi()));

            for (int i = 0; i < dsNganh.size(); i++) {
                if (dsNganh.get(i).startsWith(m.getMaNganh() + " - ")) {
                    spNganhMonHoc.setSelection(i);
                    break;
                }
            }

            edtMaMon.setEnabled(false);
            btnThemMonHoc.setEnabled(false);
            btnSuaMonHoc.setEnabled(true);
            btnXoaMonHoc.setEnabled(true);
        });
    }

    private void anhXa() {
        edtMaMon = findViewById(R.id.edtMaMon);
        edtTenMon = findViewById(R.id.edtTenMon);
        edtSoTinChi = findViewById(R.id.edtSoTinChi);
        spNganhMonHoc = findViewById(R.id.spNganhMonHoc);

        btnThemMonHoc = findViewById(R.id.btnThemMonHoc);
        btnSuaMonHoc = findViewById(R.id.btnSuaMonHoc);
        btnXoaMonHoc = findViewById(R.id.btnXoaMonHoc);
        btnLamMoi = findViewById(R.id.btnLamMoi);

        lvMonHoc = findViewById(R.id.lvMonHoc);
    }

    private void loadSpinnerNganh() {
        dsNganh = nganhDAO.getDanhSachSpinner();

        ArrayAdapter<String> adapterNganh = new ArrayAdapter<>(
                this,
                android.R.layout.simple_spinner_item,
                dsNganh
        );
        adapterNganh.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spNganhMonHoc.setAdapter(adapterNganh);
    }

    private void loadData() {
        list.clear();
        list.addAll(monHocDAO.getAll());

        if (adapter == null) {
            adapter = new MonHocAdapter(this, list);
            lvMonHoc.setAdapter(adapter);
        } else {
            adapter.notifyDataSetChanged();
        }
    }

    private void cheDoThem() {
        edtMaMon.setEnabled(true);
        btnThemMonHoc.setEnabled(true);
        btnSuaMonHoc.setEnabled(false);
        btnXoaMonHoc.setEnabled(false);
    }

    private void themMonHoc() {
        String ma = edtMaMon.getText().toString().trim();
        String ten = edtTenMon.getText().toString().trim();
        String stc = edtSoTinChi.getText().toString().trim();

        if (!kiemTraDuLieu(ma, ten, stc)) return;

        String maNganh = spNganhMonHoc.getSelectedItem().toString().split(" - ")[0];
        int soTinChi = Integer.parseInt(stc);

        boolean ketQua = monHocDAO.insert(new MonHoc(ma, ten, soTinChi, maNganh));

        if (ketQua) {
            Toast.makeText(this, "Thêm môn học thành công", Toast.LENGTH_SHORT).show();
            lamMoiForm();
            loadData();
        } else {
            Toast.makeText(this, "Thêm môn học thất bại", Toast.LENGTH_SHORT).show();
        }
    }

    private void suaMonHoc() {
        String ma = edtMaMon.getText().toString().trim();
        String ten = edtTenMon.getText().toString().trim();
        String stc = edtSoTinChi.getText().toString().trim();

        if (!kiemTraDuLieu(ma, ten, stc)) return;

        String maNganh = spNganhMonHoc.getSelectedItem().toString().split(" - ")[0];
        int soTinChi = Integer.parseInt(stc);

        boolean ketQua = monHocDAO.update(new MonHoc(ma, ten, soTinChi, maNganh));

        if (ketQua) {
            Toast.makeText(this, "Sửa môn học thành công", Toast.LENGTH_SHORT).show();
            lamMoiForm();
            loadData();
        } else {
            Toast.makeText(this, "Sửa môn học thất bại", Toast.LENGTH_SHORT).show();
        }
    }

    private void xoaMonHoc() {
        String ma = edtMaMon.getText().toString().trim();

        if (TextUtils.isEmpty(ma)) {
            Toast.makeText(this, "Vui lòng chọn môn học cần xóa", Toast.LENGTH_SHORT).show();
            return;
        }

        new AlertDialog.Builder(this)
                .setTitle("Xác nhận xóa")
                .setMessage("Bạn có chắc muốn xóa môn học " + ma + " không?")
                .setPositiveButton("Xóa", (dialog, which) -> {
                    boolean ketQua = monHocDAO.delete(ma);

                    if (ketQua) {
                        Toast.makeText(this, "Xóa môn học thành công", Toast.LENGTH_SHORT).show();
                        lamMoiForm();
                        loadData();
                    } else {
                        Toast.makeText(this, "Xóa môn học thất bại", Toast.LENGTH_SHORT).show();
                    }
                })
                .setNegativeButton("Hủy", null)
                .show();
    }

    private boolean kiemTraDuLieu(String ma, String ten, String stc) {
        if (ma.isEmpty()) {
            edtMaMon.setError("Vui lòng nhập mã môn");
            edtMaMon.requestFocus();
            return false;
        }

        if (ten.isEmpty()) {
            edtTenMon.setError("Vui lòng nhập tên môn");
            edtTenMon.requestFocus();
            return false;
        }

        if (stc.isEmpty()) {
            edtSoTinChi.setError("Vui lòng nhập số tín chỉ");
            edtSoTinChi.requestFocus();
            return false;
        }

        int soTinChi;
        try {
            soTinChi = Integer.parseInt(stc);
        } catch (Exception e) {
            edtSoTinChi.setError("Số tín chỉ không hợp lệ");
            edtSoTinChi.requestFocus();
            return false;
        }

        if (soTinChi <= 0) {
            edtSoTinChi.setError("Số tín chỉ phải lớn hơn 0");
            edtSoTinChi.requestFocus();
            return false;
        }

        return true;
    }

    private void lamMoiForm() {
        edtMaMon.setText("");
        edtTenMon.setText("");
        edtSoTinChi.setText("");
        spNganhMonHoc.setSelection(0);

        edtMaMon.setEnabled(true);
        edtMaMon.requestFocus();

        cheDoThem();
    }
}