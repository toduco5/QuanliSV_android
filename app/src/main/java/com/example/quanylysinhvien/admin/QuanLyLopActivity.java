package com.example.quanylysinhvien.admin;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.quanylysinhvien.R;
import com.example.quanylysinhvien.dao.LopDao;
import com.example.quanylysinhvien.model.Lop;

public class QuanLyLopActivity extends AppCompatActivity {

    private LinearLayout linearLayoutThemLop;
    private Animation animation;

    private EditText edtMaLop, edtTenLop;
    private Button btnThemLop, btnXemlop;

    private LopDao lopDao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_them_lop);

        anhXa();

        lopDao = new LopDao(this);
        animation = AnimationUtils.loadAnimation(this, R.anim.uptodowndiagonal);
        linearLayoutThemLop.setAnimation(animation);

        btnThemLop.setOnClickListener(v -> themLop());

        btnXemlop.setOnClickListener(v -> {
            startActivity(new Intent(QuanLyLopActivity.this, DanhSachLopActivity.class));
            finish();
        });
    }

    private void anhXa() {
        linearLayoutThemLop = findViewById(R.id.linearLayoutThemLop);
        edtMaLop = findViewById(R.id.edtMaLop);
        edtTenLop = findViewById(R.id.edtTenLop);
        btnThemLop = findViewById(R.id.btnThemLop);
        btnXemlop = findViewById(R.id.btnXemlop);
    }

    private void themLop() {
        String maLop = edtMaLop.getText().toString().trim();
        String tenLop = edtTenLop.getText().toString().trim();

        if (maLop.isEmpty()) {
            edtMaLop.setError("Nhập mã lớp");
            edtMaLop.requestFocus();
            return;
        }

        if (tenLop.isEmpty()) {
            edtTenLop.setError("Nhập tên lớp");
            edtTenLop.requestFocus();
            return;
        }

        Lop lop = new Lop(maLop, tenLop);

        if (lopDao.insert(lop)) {
            Toast.makeText(this, "Thêm lớp thành công", Toast.LENGTH_SHORT).show();
            edtMaLop.setText("");
            edtTenLop.setText("");
        } else {
            Toast.makeText(this, "Thêm lớp thất bại", Toast.LENGTH_SHORT).show();
        }
    }
}