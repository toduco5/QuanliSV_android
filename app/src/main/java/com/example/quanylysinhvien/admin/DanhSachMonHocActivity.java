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
import com.example.quanylysinhvien.adapter.MonHocAdapter;
import com.example.quanylysinhvien.dao.MonHocDAO;
import com.example.quanylysinhvien.model.MonHoc;

import java.util.ArrayList;

public class DanhSachMonHocActivity extends AppCompatActivity {

    private EditText edtTimMonHoc;
    private Button btnTimMonHoc, btnThemMonHocMoi;
    private ListView lvMonHoc;
    private TextView tvSoLuongMonHoc;

    private ArrayList<MonHoc> list;
    private MonHocAdapter adapter;
    private MonHocDAO monHocDAO;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_danh_sach_monhoc);

        anhXa();

        monHocDAO = new MonHocDAO(this);
        list = new ArrayList<>();

        loadData();

        btnTimMonHoc.setOnClickListener(v -> timKiemMonHoc());

        btnThemMonHocMoi.setOnClickListener(v -> {
            Intent intent = new Intent(DanhSachMonHocActivity.this, QuanLyMonHocActivity.class);
            startActivity(intent);
        });

        edtTimMonHoc.addTextChangedListener(new TextWatcher() {
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

        lvMonHoc.setOnItemClickListener((parent, view, position, id) -> {
            MonHoc monHoc = list.get(position);

            Intent intent = new Intent(DanhSachMonHocActivity.this, QuanLyMonHocActivity.class);
            intent.putExtra("maMon", monHoc.getMaMon());
            intent.putExtra("tenMon", monHoc.getTenMon());
            intent.putExtra("soTinChi", monHoc.getSoTinChi());

            startActivity(intent);
        });
    }

    private void anhXa() {
        edtTimMonHoc = findViewById(R.id.edtTimMonHoc);
        btnTimMonHoc = findViewById(R.id.btnTimMonHoc);
        btnThemMonHocMoi = findViewById(R.id.btnThemMonHocMoi);
        lvMonHoc = findViewById(R.id.lvMonHoc);
        tvSoLuongMonHoc = findViewById(R.id.tvSoLuongMonHoc);
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

        tvSoLuongMonHoc.setText("Tổng số môn học: " + list.size());
    }

    private void timKiemMonHoc() {
        String key = edtTimMonHoc.getText().toString().trim();

        list.clear();

        if (key.isEmpty()) {
            list.addAll(monHocDAO.getAll());
        } else {
            list.addAll(monHocDAO.search(key));
        }

        adapter.notifyDataSetChanged();
        tvSoLuongMonHoc.setText("Tổng số môn học: " + list.size());
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadData();
    }
}