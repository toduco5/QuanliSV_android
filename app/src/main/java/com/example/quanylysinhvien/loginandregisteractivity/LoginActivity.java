package com.example.quanylysinhvien.loginandregisteractivity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.quanylysinhvien.R;
import com.example.quanylysinhvien.admin.ManagerActivity;
import com.example.quanylysinhvien.dao.DaoTaiKhoan;
import com.example.quanylysinhvien.user.UserActivity;

public class LoginActivity extends AppCompatActivity {

    private EditText edtUserName, edtPassword;

    private Button btnLogin, btnRegister;

    private DaoTaiKhoan daoTaiKhoan;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);


        anhXa();


        daoTaiKhoan = new DaoTaiKhoan(this);

        Intent intentNhan = getIntent();
        if (intentNhan != null) {
            String tk = intentNhan.getStringExtra("taikhoan");
            String mk = intentNhan.getStringExtra("matkhau");

            if (tk != null) edtUserName.setText(tk);
            if (mk != null) edtPassword.setText(mk);
        }

        btnLogin.setOnClickListener(v -> dangNhap());

        btnRegister.setOnClickListener(v -> {
            Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
            startActivity(intent);
        });
    }

    private void anhXa() {
        edtUserName = findViewById(R.id.edtUserName);
        edtPassword = findViewById(R.id.edtPassword);
        btnLogin = findViewById(R.id.btnLogin);
        btnRegister = findViewById(R.id.btnRegister);
    }

    private void dangNhap() {

        String tenDangNhap = edtUserName.getText().toString().trim();
        String matKhau = edtPassword.getText().toString().trim();

        if (tenDangNhap.isEmpty()) {
            Toast.makeText(this, "Vui lòng nhập tên đăng nhập", Toast.LENGTH_SHORT).show();
            edtUserName.requestFocus();
            return;
        }

        if (matKhau.isEmpty()) {
            Toast.makeText(this, "Vui lòng nhập mật khẩu", Toast.LENGTH_SHORT).show();
            edtPassword.requestFocus();
            return;
        }

        boolean check = daoTaiKhoan.kiemTraDangNhap(tenDangNhap, matKhau);

        if (!check) {
            Toast.makeText(this, "Sai tài khoản hoặc mật khẩu", Toast.LENGTH_SHORT).show();
            return;
        }

        String vaiTro = daoTaiKhoan.getVaiTro(tenDangNhap);

        String maSv = daoTaiKhoan.getMaSvTheoTaiKhoan(tenDangNhap);

        Intent intent;


        if ("ADMIN".equalsIgnoreCase(vaiTro)) {
            intent = new Intent(LoginActivity.this, ManagerActivity.class);
            intent.putExtra("quyen", "admin");
        } else {
            intent = new Intent(LoginActivity.this, UserActivity.class);
            intent.putExtra("quyen", "user");
            intent.putExtra("maSv", maSv);
        }

        intent.putExtra("tenDangNhap", tenDangNhap);
        intent.putExtra("vaiTro", vaiTro);

        startActivity(intent);


        finish();
    }
}