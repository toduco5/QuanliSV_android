package com.example.quanylysinhvien.admin;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.example.quanylysinhvien.R;
import com.example.quanylysinhvien.adapter.NganhAdapter;
import com.example.quanylysinhvien.dao.NganhDAO;
import com.example.quanylysinhvien.model.Nganh;

import java.util.ArrayList;

public class QuanLyNganhActivity extends AppCompatActivity {

    private EditText edtMaNganh, edtTenNganh;
    private Button btnThemNganh, btnSuaNganh, btnXoaNganh, btnLamMoi;
    private ListView lvNganh;

    private NganhDAO nganhDAO;
    private ArrayList<Nganh> list;
    private NganhAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_them_nganh);

        anhXa();

        nganhDAO = new NganhDAO(this);
        list = new ArrayList<>();

        loadData();
        cheDoThem();

        btnThemNganh.setOnClickListener(v -> themNganh());
        btnSuaNganh.setOnClickListener(v -> suaNganh());
        btnXoaNganh.setOnClickListener(v -> xoaNganh());
        btnLamMoi.setOnClickListener(v -> lamMoiForm());

        lvNganh.setOnItemClickListener((parent, view, position, id) -> {
            Nganh n = list.get(position);
            edtMaNganh.setText(n.getMaNganh());
            edtTenNganh.setText(n.getTenNganh());

            edtMaNganh.setEnabled(false);
            btnThemNganh.setEnabled(false);
            btnSuaNganh.setEnabled(true);
            btnXoaNganh.setEnabled(true);
        });
    }

    private void anhXa() {
        edtMaNganh = findViewById(R.id.edtMaNganh);
        edtTenNganh = findViewById(R.id.edtTenNganh);

        btnThemNganh = findViewById(R.id.btnThemNganh);
        btnSuaNganh = findViewById(R.id.btnSuaNganh);
        btnXoaNganh = findViewById(R.id.btnXoaNganh);
        btnLamMoi = findViewById(R.id.btnLamMoi);

        lvNganh = findViewById(R.id.lvNganh);
    }

    private void loadData() {
        list.clear();
        list.addAll(nganhDAO.getAll());

        if (adapter == null) {
            adapter = new NganhAdapter(this, list);
            lvNganh.setAdapter(adapter);
        } else {
            adapter.notifyDataSetChanged();
        }
    }

    private void cheDoThem() {
        edtMaNganh.setEnabled(true);
        btnThemNganh.setEnabled(true);
        btnSuaNganh.setEnabled(false);
        btnXoaNganh.setEnabled(false);
    }

    private void themNganh() {
        String ma = edtMaNganh.getText().toString().trim();
        String ten = edtTenNganh.getText().toString().trim();

        if (!kiemTraDuLieu(ma, ten)) return;

        boolean ketQua = nganhDAO.insert(new Nganh(ma, ten));

        if (ketQua) {
            Toast.makeText(this, "Thêm ngành thành công", Toast.LENGTH_SHORT).show();
            lamMoiForm();
            loadData();
        } else {
            Toast.makeText(this, "Thêm ngành thất bại", Toast.LENGTH_SHORT).show();
        }
    }

    private void suaNganh() {
        String ma = edtMaNganh.getText().toString().trim();
        String ten = edtTenNganh.getText().toString().trim();

        if (!kiemTraDuLieu(ma, ten)) return;

        boolean ketQua = nganhDAO.update(new Nganh(ma, ten));

        if (ketQua) {
            Toast.makeText(this, "Sửa ngành thành công", Toast.LENGTH_SHORT).show();
            lamMoiForm();
            loadData();
        } else {
            Toast.makeText(this, "Sửa ngành thất bại", Toast.LENGTH_SHORT).show();
        }
    }

    private void xoaNganh() {
        String ma = edtMaNganh.getText().toString().trim();

        if (TextUtils.isEmpty(ma)) {
            Toast.makeText(this, "Vui lòng chọn ngành cần xóa", Toast.LENGTH_SHORT).show();
            return;
        }

        new AlertDialog.Builder(this)
                .setTitle("Xác nhận xóa")
                .setMessage("Bạn có chắc muốn xóa ngành " + ma + " không?")
                .setPositiveButton("Xóa", (dialog, which) -> {
                    boolean ketQua = nganhDAO.delete(ma);

                    if (ketQua) {
                        Toast.makeText(this, "Xóa ngành thành công", Toast.LENGTH_SHORT).show();
                        lamMoiForm();
                        loadData();
                    } else {
                        Toast.makeText(this, "Xóa ngành thất bại", Toast.LENGTH_SHORT).show();
                    }
                })
                .setNegativeButton("Hủy", null)
                .show();
    }

    private boolean kiemTraDuLieu(String ma, String ten) {
        if (ma.isEmpty()) {
            edtMaNganh.setError("Vui lòng nhập mã ngành");
            edtMaNganh.requestFocus();
            return false;
        }

        if (ten.isEmpty()) {
            edtTenNganh.setError("Vui lòng nhập tên ngành");
            edtTenNganh.requestFocus();
            return false;
        }

        return true;
    }

    private void lamMoiForm() {
        edtMaNganh.setText("");
        edtTenNganh.setText("");

        edtMaNganh.setEnabled(true);
        edtMaNganh.requestFocus();

        cheDoThem();
    }
}