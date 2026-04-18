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


    RelativeLayout rlayout;


    Animation animation;


    EditText txtRegTk;


    EditText txtRegMk;


    EditText txtRegMkk;


    Button btDangKy;


    Button btNhapLai;


    DaoTaiKhoan tkDao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        setContentView(R.layout.activity_register);


        init();


        tkDao = new DaoTaiKhoan(this);


        animation = AnimationUtils.loadAnimation(
                this,
                R.anim.uptodowndiagonal
        );

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


        String tk =
                txtRegTk.getText().toString().trim();

        String mk =
                txtRegMk.getText().toString().trim();

        String mkk =
                txtRegMkk.getText().toString().trim();


        if (tk.isEmpty()) {
            Toast.makeText(
                    this,
                    "Nhập tên tài khoản",
                    Toast.LENGTH_SHORT
            ).show();
            return;
        }


        if (mk.isEmpty()) {
            Toast.makeText(
                    this,
                    "Nhập mật khẩu",
                    Toast.LENGTH_SHORT
            ).show();
            return;
        }


        if (mkk.isEmpty()) {
            Toast.makeText(
                    this,
                    "Nhập lại mật khẩu",
                    Toast.LENGTH_SHORT
            ).show();
            return;
        }


        if (mk.length() < 3) {
            Toast.makeText(
                    this,
                    "Mật khẩu ít nhất 3 ký tự",
                    Toast.LENGTH_SHORT
            ).show();
            return;
        }


        if (!mk.equals(mkk)) {
            Toast.makeText(
                    this,
                    "Mật khẩu không trùng",
                    Toast.LENGTH_SHORT
            ).show();
            return;
        }


        if (tk.equalsIgnoreCase("admin")) {
            Toast.makeText(
                    this,
                    "Không dùng tên admin",
                    Toast.LENGTH_SHORT
            ).show();
            return;
        }


        if (tkDao.kiemTraTonTaiTaiKhoan(tk)) {
            Toast.makeText(
                    this,
                    "Tài khoản đã tồn tại",
                    Toast.LENGTH_SHORT
            ).show();
            return;
        }


        TaikhoanMatKhau taiKhoanMoi =
                new TaikhoanMatKhau(
                        tk,       // tên tài khoản
                        mk,       // mật khẩu
                        "USER",   // quyền mặc định
                        null      // chưa gán mã sinh viên
                );

        boolean kq = tkDao.them(taiKhoanMoi);


        if (kq) {

            Toast.makeText(
                    this,
                    "Đăng ký thành công",
                    Toast.LENGTH_SHORT
            ).show();


            Intent intent =
                    new Intent(
                            RegisterActivity.this,
                            LoginActivity.class
                    );


            intent.putExtra("taikhoan", tk);
            intent.putExtra("matkhau", mk);

            startActivity(intent);


            finish();

        } else {


            Toast.makeText(
                    this,
                    "Đăng ký thất bại",
                    Toast.LENGTH_SHORT
            ).show();
        }
    }
}