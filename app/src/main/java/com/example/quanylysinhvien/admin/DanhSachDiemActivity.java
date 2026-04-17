package com.example.quanylysinhvien.admin;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import com.example.quanylysinhvien.R;
import com.example.quanylysinhvien.adapter.DiemAdapter;
import com.example.quanylysinhvien.dao.DiemDAO;
import com.example.quanylysinhvien.model.Diem;

import java.util.ArrayList;

public class DanhSachDiemActivity extends AppCompatActivity {

    EditText edtTimDiem;
    Button btnTimDiem, btnThemDiemMoi;
    ListView lvDiem;

    ArrayList<Diem> list;
    DiemAdapter adapter;
    DiemDAO diemDAO;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_danh_sach_diem);

        edtTimDiem = findViewById(R.id.edtTimDiem);
        btnTimDiem = findViewById(R.id.btnTimDiem);
        btnThemDiemMoi = findViewById(R.id.btnThemDiemMoi);
        lvDiem = findViewById(R.id.lvDiem);

        diemDAO = new DiemDAO(this);
        loadData();

        btnTimDiem.setOnClickListener(v -> {
            String key = edtTimDiem.getText().toString().trim();
            if (key.isEmpty()) {
                loadData();
            } else {
                list.clear();
                list.addAll(diemDAO.search(key));
                adapter.notifyDataSetChanged();
            }
        });

        btnThemDiemMoi.setOnClickListener(v -> {
            startActivity(new Intent(DanhSachDiemActivity.this, QuanLyDiemActivity.class));
        });
    }

    private void loadData() {
        list = diemDAO.getAll();
        adapter = new DiemAdapter(this, list);
        lvDiem.setAdapter(adapter);
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadData();
    }
}