package com.example.quanylysinhvien.admin;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.example.quanylysinhvien.R;
import com.example.quanylysinhvien.adapter.NganhAdapter;
import com.example.quanylysinhvien.dao.NganhDAO;
import com.example.quanylysinhvien.model.Nganh;

import java.util.ArrayList;

public class DanhSachNganhActivity extends AppCompatActivity {

    private EditText edtTimNganh;
    private Button btnTimNganh, btnThemNganhMoi;
    private ListView lvNganh;
    private TextView tvSoLuong;

    private ArrayList<Nganh> list;
    private NganhAdapter adapter;
    private NganhDAO nganhDAO;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_danh_sach_nganh);

        anhXa();

        nganhDAO = new NganhDAO(this);
        list = new ArrayList<>();

        loadData();

        btnTimNganh.setOnClickListener(v -> timKiem());

        btnThemNganhMoi.setOnClickListener(v -> {
            Intent intent = new Intent(DanhSachNganhActivity.this, QuanLyNganhActivity.class);
            startActivity(intent);
        });

        edtTimNganh.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.toString().trim().isEmpty()) {
                    loadData();
                }
            }

            @Override
            public void afterTextChanged(Editable s) { }
        });

        lvNganh.setOnItemClickListener((parent, view, position, id) -> {

            Nganh nganh = list.get(position);

            Intent intent = new Intent(DanhSachNganhActivity.this, QuanLyNganhActivity.class);

            intent.putExtra("maNganh", nganh.getMaNganh());
            intent.putExtra("tenNganh", nganh.getTenNganh());

            startActivity(intent);
        });
    }

    private void anhXa() {
        edtTimNganh = findViewById(R.id.edtTimNganh);
        btnTimNganh = findViewById(R.id.btnTimNganh);
        btnThemNganhMoi = findViewById(R.id.btnThemNganhMoi);
        lvNganh = findViewById(R.id.lvNganh);
        tvSoLuong = findViewById(R.id.tvSoLuong);
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

        tvSoLuong.setText("Tổng số ngành: " + list.size());
    }

    private void timKiem() {

        String key = edtTimNganh.getText().toString().trim();

        list.clear();

        if (key.isEmpty()) {
            list.addAll(nganhDAO.getAll());
        } else {
            list.addAll(nganhDAO.search(key));
        }

        adapter.notifyDataSetChanged();

        tvSoLuong.setText("Tổng số ngành: " + list.size());
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadData();
    }
}