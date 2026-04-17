package com.example.quanylysinhvien.admin;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.example.quanylysinhvien.R;
import com.example.quanylysinhvien.adapter.LopAdapter;
import com.example.quanylysinhvien.dao.LopDao;
import com.example.quanylysinhvien.model.Lop;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

public class DanhSachLopActivity extends AppCompatActivity {

    private FloatingActionButton fbadd, fab, fbHome;
    private TextView tvanhien;
    private EditText edtSearch;
    private ListView listView;

    private ArrayList<Lop> dsLop = new ArrayList<>();
    private LopAdapter lopAdapter;
    private LopDao lopDao;

    private boolean isOpen = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_danh_sach_lop);

        anhXa();
        lopDao = new LopDao(this);

        loadData();
        timKiem();
        suKienNut();
    }

    private void anhXa() {
        listView = findViewById(R.id.listviewLop);
        fbadd = findViewById(R.id.fbThemLop);
        fbHome = findViewById(R.id.fbHomeLop);
        fab = findViewById(R.id.fab1);
        tvanhien = findViewById(R.id.tvAnHien);
        edtSearch = findViewById(R.id.edtserchLop);
    }

    private void loadData() {
        dsLop.clear();
        dsLop.addAll(lopDao.getAll());

        lopAdapter = new LopAdapter(
                DanhSachLopActivity.this,
                R.layout.dong_xem_lop,
                dsLop
        );

        listView.setAdapter(lopAdapter);

        if (dsLop.isEmpty()) {
            listView.setVisibility(View.INVISIBLE);
            tvanhien.setVisibility(View.VISIBLE);
        } else {
            listView.setVisibility(View.VISIBLE);
            tvanhien.setVisibility(View.INVISIBLE);
        }
    }

    private void timKiem() {
        edtSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (lopAdapter != null) {
                    lopAdapter.getFilter().filter(s);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
    }

    private void suKienNut() {
        fbadd.setOnClickListener(v -> {
            String[] menu = {"Thêm lớp", "Thêm sinh viên", "Xem sinh viên"};

            AlertDialog.Builder builder = new AlertDialog.Builder(DanhSachLopActivity.this);
            builder.setTitle("Chọn chức năng");
            builder.setItems(menu, (dialog, which) -> {
                if (which == 0) {
                    startActivity(new Intent(DanhSachLopActivity.this, QuanLyLopActivity.class));
                } else if (which == 1) {
                    startActivity(new Intent(DanhSachLopActivity.this, ThemSinhVienActivity.class));
                } else if (which == 2) {
                    startActivity(new Intent(DanhSachLopActivity.this, DanhSachSinhVienActivity.class));
                }
            });
            builder.show();
        });

        fbHome.setOnClickListener(v -> {
            Intent intent = new Intent(DanhSachLopActivity.this, ManagerActivity.class);
            startActivity(intent);
            finish();
        });

        fab.setOnClickListener(v -> {
            if (!isOpen) {
                openMenu();
            } else {
                closeMenu();
            }
        });
    }

    private void openMenu() {
        isOpen = true;
        fbHome.animate().translationY(-getResources().getDimension(R.dimen.stan_60));
        fbadd.animate().translationY(-getResources().getDimension(R.dimen.stan_105));
    }

    private void closeMenu() {
        isOpen = false;
        fbHome.animate().translationY(0);
        fbadd.animate().translationY(0);
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadData();
    }
}