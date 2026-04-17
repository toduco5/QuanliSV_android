package com.example.quanylysinhvien;

import android.app.AlertDialog;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.quanylysinhvien.adapter.SuKienAdapter;
import com.example.quanylysinhvien.dao.SuKienDao;
import com.example.quanylysinhvien.model.SuKien;

import java.util.ArrayList;

public class SuKienActivity extends AppCompatActivity {

    EditText edtTimSuKien;
    Button btnThemSuKien;
    ListView lvSuKien;

    ArrayList<SuKien> list;
    SuKienAdapter adapter;
    SuKienDao suKienDao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sukien);

        edtTimSuKien = findViewById(R.id.edtTimSuKien);
        btnThemSuKien = findViewById(R.id.btnThemSuKien);
        lvSuKien = findViewById(R.id.lvSuKien);

        suKienDao = new SuKienDao(this);

        loadData();

        btnThemSuKien.setOnClickListener(v -> showDialogThem());

        edtTimSuKien.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String key = s.toString().trim();
                if (key.isEmpty()) {
                    loadData();
                } else {
                    list.clear();
                    list.addAll(suKienDao.search(key));
                    adapter.notifyDataSetChanged();
                }
            }

            @Override
            public void afterTextChanged(Editable s) { }
        });
    }

    private void loadData() {
        list = suKienDao.getAll();
        adapter = new SuKienAdapter(this, list);
        lvSuKien.setAdapter(adapter);
    }

    private void showDialogThem() {
        View view = LayoutInflater.from(this).inflate(R.layout.dialog_sukien, null);

        EditText edtMaSk = view.findViewById(R.id.edtMaSk);
        EditText edtTenSk = view.findViewById(R.id.edtTenSk);
        EditText edtNgaySk = view.findViewById(R.id.edtNgaySk);
        EditText edtNoiDungSk = view.findViewById(R.id.edtNoiDungSk);

        new AlertDialog.Builder(this)
                .setTitle("Thêm sự kiện")
                .setView(view)
                .setPositiveButton("Thêm", (dialog, which) -> {
                    String ma = edtMaSk.getText().toString().trim();
                    String ten = edtTenSk.getText().toString().trim();
                    String ngay = edtNgaySk.getText().toString().trim();
                    String noiDung = edtNoiDungSk.getText().toString().trim();

                    if (ma.isEmpty() || ten.isEmpty() || ngay.isEmpty() || noiDung.isEmpty()) {
                        Toast.makeText(this, "Nhập đầy đủ thông tin", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    boolean kq = suKienDao.insert(new SuKien(ma, ten, ngay, noiDung));
                    if (kq) {
                        loadData();
                        Toast.makeText(this, "Thêm thành công", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(this, "Thêm thất bại", Toast.LENGTH_SHORT).show();
                    }
                })
                .setNegativeButton("Hủy", null)
                .show();
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadData();
    }
}