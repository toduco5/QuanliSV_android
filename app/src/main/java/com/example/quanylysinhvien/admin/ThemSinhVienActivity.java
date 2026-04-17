package com.example.quanylysinhvien.admin;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.quanylysinhvien.R;
import com.example.quanylysinhvien.dao.LopDao;
import com.example.quanylysinhvien.dao.SinhVienDao;
import com.example.quanylysinhvien.model.Lop;
import com.example.quanylysinhvien.model.SinhVien;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class ThemSinhVienActivity extends AppCompatActivity {

    EditText edtTensv, edtMasv, edtemail, edtHinh;
    Spinner spMaLop;
    Button btnThem, btnNhapLai, btnDanhSach, btnReview;

    SinhVienDao daoSach;
    LopDao lsDao;

    LinearLayout linearLayout;
    CircleImageView imgAvata;

    ArrayList<Lop> lsList = new ArrayList<>();

    Animation animation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_them_sinh_vien);

        lsDao = new LopDao(this);
        daoSach = new SinhVienDao(this);

        linearLayout = findViewById(R.id.linearLayout);
        edtMasv = findViewById(R.id.txtMaSV);
        edtTensv = findViewById(R.id.txtTenSV);
        edtHinh = findViewById(R.id.txtHinh);
        edtemail = findViewById(R.id.txtemail);
        spMaLop = findViewById(R.id.txtMalop);

        btnThem = findViewById(R.id.btntThem);
        btnNhapLai = findViewById(R.id.btnNhapLai);
        btnDanhSach = findViewById(R.id.btnDanhSach);
        btnReview = findViewById(R.id.btnReviewThem);

        imgAvata = findViewById(R.id.imgAvata);

        animation = AnimationUtils.loadAnimation(this, R.anim.uptodowndiagonal);
        linearLayout.setAnimation(animation);

        lsList = lsDao.getAll();

        ArrayAdapter<Lop> adapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_spinner_item,
                lsList
        );

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spMaLop.setAdapter(adapter);

        btnNhapLai.setOnClickListener(v -> {
            edtMasv.setText("");
            edtTensv.setText("");
            edtemail.setText("");
            edtHinh.setText("");
        });

        btnDanhSach.setOnClickListener(v -> {
            startActivity(new Intent(
                    ThemSinhVienActivity.this,
                    DanhSachSinhVienActivity.class
            ));
        });

        btnReview.setOnClickListener(v -> {
            String tenHinh = edtHinh.getText().toString().trim();

            if (tenHinh.isEmpty()) {
                imgAvata.setImageResource(R.drawable.avatasinhvien);
            } else {
                int idHinh = getResources().getIdentifier(
                        tenHinh,
                        "drawable",
                        getPackageName()
                );

                if (idHinh != 0) {
                    imgAvata.setImageResource(idHinh);
                } else {
                    imgAvata.setImageResource(R.drawable.avatasinhvien);
                }
            }
        });

        btnThem.setOnClickListener(v -> {

            String ma = edtMasv.getText().toString().trim();
            String ten = edtTensv.getText().toString().trim();
            String email = edtemail.getText().toString().trim();
            String hinh = edtHinh.getText().toString().trim();

            Lop lop = (Lop) spMaLop.getSelectedItem();
            String maLop = lop.getMaLop();

            if (ma.isEmpty()) {
                Toast.makeText(this, "Nhập mã sinh viên", Toast.LENGTH_SHORT).show();
                return;
            }

            if (ten.isEmpty()) {
                Toast.makeText(this, "Nhập tên sinh viên", Toast.LENGTH_SHORT).show();
                return;
            }

            if (email.isEmpty()) {
                Toast.makeText(this, "Nhập email", Toast.LENGTH_SHORT).show();
                return;
            }

            if (hinh.isEmpty()) {
                hinh = "avatasinhvien";
            }

            SinhVien s = new SinhVien(
                    ma,
                    ten,
                    email,
                    hinh,
                    maLop,
                    "CNPM"
            );

            if (daoSach.insert(s)) {
                Toast.makeText(this, "Thêm thành công", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Mã sinh viên đã tồn tại", Toast.LENGTH_SHORT).show();
            }

        });
    }
}