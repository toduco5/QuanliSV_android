package com.example.quanylysinhvien.loginandregisteractivity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.example.quanylysinhvien.R;
import com.example.quanylysinhvien.dao.DaoTaiKhoan;
import com.example.quanylysinhvien.model.TaikhoanMatKhau;

public class RegisterActivity extends AppCompatActivity {

    private RelativeLayout rlayout;
    private Animation animation;

    private EditText txtRegTk, txtRegMk, txtRegMkk;
    private Button btDangKy, btNhapLai;

    private DaoTaiKhoan tkDao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        init();

        tkDao = new DaoTaiKhoan(this);

        animation = AnimationUtils.loadAnimation(this, R.anim.uptodowndiagonal);
        rlayout.setAnimation(animation);

        btNhapLai.setOnClickListener(v -> xoaDuLieuNhap());
        btDangKy.setOnClickListener(v -> dangKyTaiKhoan());
    }

    private void init() {
        rlayout = findViewById(R.id.rlayout);
        txtRegTk = findViewById(R.id.edtRegUser);
        txtRegMk = findViewById(R.id.edtRegPassword);
        txtRegMkk = findViewById(R.id.edtRePassword);
        btDangKy = findViewById(R.id.btnReg);
        btNhapLai = findViewById(R.id.btnRelay);
    }

    private void xoaDuLieuNhap() {
        txtRegTk.setText("");
        txtRegMk.setText("");
        txtRegMkk.setText("");
        txtRegTk.requestFocus();
    }

    private void dangKyTaiKhoan() {
        String tk = txtRegTk.getText().toString().trim();
        String mk = txtRegMk.getText().toString().trim();
        String mkk = txtRegMkk.getText().toString().trim();

        if (tk.isEmpty()) {
            Toast.makeText(this, "Tên tài khoản không được để trống!", Toast.LENGTH_SHORT).show();
            txtRegTk.requestFocus();
            return;
        }

        if (mk.isEmpty()) {
            Toast.makeText(this, "Mật khẩu không được để trống!", Toast.LENGTH_SHORT).show();
            txtRegMk.requestFocus();
            return;
        }

        if (mkk.isEmpty()) {
            Toast.makeText(this, "Vui lòng nhập lại mật khẩu!", Toast.LENGTH_SHORT).show();
            txtRegMkk.requestFocus();
            return;
        }

        if (mk.length() < 3) {
            Toast.makeText(this, "Mật khẩu phải có ít nhất 3 ký tự!", Toast.LENGTH_SHORT).show();
            txtRegMk.requestFocus();
            return;
        }

        if (!mk.equals(mkk)) {
            Toast.makeText(this, "Mật khẩu không khớp nhau!", Toast.LENGTH_SHORT).show();
            txtRegMkk.requestFocus();
            return;
        }

        if (tk.equalsIgnoreCase("admin")) {
            Toast.makeText(this, "Không được đăng ký tên tài khoản admin!", Toast.LENGTH_SHORT).show();
            txtRegTk.requestFocus();
            return;
        }

        if (tkDao.kiemTraTonTaiTaiKhoan(tk)) {
            Toast.makeText(this, "Tên tài khoản không được trùng!", Toast.LENGTH_SHORT).show();
            txtRegTk.requestFocus();
            return;
        }

        TaikhoanMatKhau taiKhoanMoi = new TaikhoanMatKhau(tk, mk, "USER", null);
        boolean kq = tkDao.them(taiKhoanMoi);

        if (kq) {
            Toast.makeText(this, "Đăng ký tài khoản thành công!", Toast.LENGTH_SHORT).show();

            Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
            intent.putExtra("taikhoan", tk);
            intent.putExtra("matkhau", mk);
            startActivity(intent);
            finish();
        } else {
            Toast.makeText(this, "Đăng ký thất bại!", Toast.LENGTH_SHORT).show();
        }
    }
}