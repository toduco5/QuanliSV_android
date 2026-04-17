package com.example.quanylysinhvien.admin;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import com.example.quanylysinhvien.R;
import com.example.quanylysinhvien.SuKienActivity;
import com.example.quanylysinhvien.loginandregisteractivity.LoginActivity;

public class ManagerActivity extends AppCompatActivity {

    private ImageView btnLop, btnSinhVien, btnNganh, btnMonHoc, btnDiem,
            btnTaiKhoan, btnPhanHoi, btnThongKe, btnSuKien;

    private TextView tvClass, tvStudent, tvNganh, tvMonHoc, tvDiem,
            tvTaiKhoan, tvPhanHoi, tvThongKe, tvSuKien, mywelcome;

    private CardView cardLopHoc, cardSinhVien, cardNganh, cardMonHoc, cardDiem,
            cardTaiKhoan, cardPhanHoi, cardThongKe, cardSuKien;

    private GridLayout gridLayout;
    private ImageView imageViewMenu;
    private Animation animation;

    private String tenDangNhap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manager);

        tenDangNhap = getIntent().getStringExtra("tenDangNhap");

        anhXa();
        hienThiThongTinAdmin();
        xuLyAnimation();
        xuLyMenu();
        xuLySuKien();
    }

    private void anhXa() {
        gridLayout = findViewById(R.id.girdviewManager);
        imageViewMenu = findViewById(R.id.imageViewMenu);
        mywelcome = findViewById(R.id.mywelcome);

        btnLop = findViewById(R.id.btnLop);
        btnSinhVien = findViewById(R.id.btnsinhvien);
        btnNganh = findViewById(R.id.btnNganh);
        btnMonHoc = findViewById(R.id.btnMonHoc);
        btnDiem = findViewById(R.id.btnDiem);
        btnTaiKhoan = findViewById(R.id.btnTaiKhoan);
        btnPhanHoi = findViewById(R.id.btnPhanHoi);
        btnThongKe = findViewById(R.id.btnThongKe);
        btnSuKien = findViewById(R.id.btnSuKien);

        tvClass = findViewById(R.id.tvclass);
        tvStudent = findViewById(R.id.tvstudent);
        tvNganh = findViewById(R.id.tvNganh);
        tvMonHoc = findViewById(R.id.tvMonHoc);
        tvDiem = findViewById(R.id.tvDiem);
        tvTaiKhoan = findViewById(R.id.tvTaiKhoan);
        tvPhanHoi = findViewById(R.id.tvPhanHoi);
        tvThongKe = findViewById(R.id.tvThongKe);
        tvSuKien = findViewById(R.id.tvSuKien);

        cardLopHoc = findViewById(R.id.cardLopHoc);
        cardSinhVien = findViewById(R.id.cardSinhVien);
        cardNganh = findViewById(R.id.cardNganh);
        cardMonHoc = findViewById(R.id.cardMonHoc);
        cardDiem = findViewById(R.id.cardDiem);
        cardTaiKhoan = findViewById(R.id.cardTaiKhoan);
        cardPhanHoi = findViewById(R.id.cardPhanHoi);
        cardThongKe = findViewById(R.id.cardThongKe);
        cardSuKien = findViewById(R.id.cardSuKien);
    }

    private void hienThiThongTinAdmin() {
        if (tenDangNhap != null && !tenDangNhap.trim().isEmpty()) {
            mywelcome.setText("Xin chào, " + tenDangNhap);
        } else {
            mywelcome.setText("Admin");
        }
    }

    private void xuLyAnimation() {
        try {
            animation = AnimationUtils.loadAnimation(this, R.anim.bottom_top);
            gridLayout.setAnimation(animation);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void xuLyMenu() {
        imageViewMenu.setOnClickListener(v -> {
            PopupMenu popupMenu = new PopupMenu(this, v);
            popupMenu.getMenuInflater().inflate(R.menu.drawer_menu, popupMenu.getMenu());
            popupMenu.show();

            popupMenu.setOnMenuItemClickListener(item -> {
                if (item.getItemId() == R.id.menuDangXuat) {
                    dangXuat();
                    return true;
                }
                return false;
            });
        });
    }

    private void xuLySuKien() {
        // Lớp
        cardLopHoc.setOnClickListener(v -> moDanhSachLop());
        btnLop.setOnClickListener(v -> moDanhSachLop());
        tvClass.setOnClickListener(v -> moDanhSachLop());

        // Sinh viên
        cardSinhVien.setOnClickListener(v -> moSinhVien());
        btnSinhVien.setOnClickListener(v -> moSinhVien());
        tvStudent.setOnClickListener(v -> moSinhVien());

        // Ngành
        cardNganh.setOnClickListener(v -> moNganh());
        btnNganh.setOnClickListener(v -> moNganh());
        tvNganh.setOnClickListener(v -> moNganh());

        // Môn học
        cardMonHoc.setOnClickListener(v -> moMonHoc());
        btnMonHoc.setOnClickListener(v -> moMonHoc());
        tvMonHoc.setOnClickListener(v -> moMonHoc());

        // Điểm
        cardDiem.setOnClickListener(v -> moDiem());
        btnDiem.setOnClickListener(v -> moDiem());
        tvDiem.setOnClickListener(v -> moDiem());

        // Tài khoản
        cardTaiKhoan.setOnClickListener(v -> moTaiKhoan());
        btnTaiKhoan.setOnClickListener(v -> moTaiKhoan());
        tvTaiKhoan.setOnClickListener(v -> moTaiKhoan());

        // Phản hồi
        cardPhanHoi.setOnClickListener(v -> moPhanHoi());
        btnPhanHoi.setOnClickListener(v -> moPhanHoi());
        tvPhanHoi.setOnClickListener(v -> moPhanHoi());

        // Thống kê
        cardThongKe.setOnClickListener(v -> moThongKe());
        btnThongKe.setOnClickListener(v -> moThongKe());
        tvThongKe.setOnClickListener(v -> moThongKe());

        // Sự kiện
        cardSuKien.setOnClickListener(v -> moSuKien());
        btnSuKien.setOnClickListener(v -> moSuKien());
        tvSuKien.setOnClickListener(v -> moSuKien());
    }

    private void moDanhSachLop() {
        try {
            Intent intent = new Intent(this, DanhSachLopActivity.class);
            intent.putExtra("tenDangNhap", tenDangNhap);
            startActivity(intent);
            overridePendingTransition(R.anim.ani_intent, R.anim.ani_intenexit);
        } catch (Exception e) {
            Toast.makeText(this, "Chưa mở được màn hình lớp học", Toast.LENGTH_SHORT).show();
        }
    }

    private void moSinhVien() {
        try {
            Intent intent = new Intent(this, ThemSinhVienActivity.class);
            intent.putExtra("tenDangNhap", tenDangNhap);
            startActivity(intent);
            overridePendingTransition(R.anim.ani_intent, R.anim.ani_intenexit);
        } catch (Exception e) {
            Toast.makeText(this, "Chưa mở được màn hình sinh viên", Toast.LENGTH_SHORT).show();
        }
    }

    private void moNganh() {
        try {
            startActivity(new Intent(this, DanhSachNganhActivity.class));
            overridePendingTransition(R.anim.ani_intent, R.anim.ani_intenexit);
        } catch (Exception e) {
            Toast.makeText(this, "Chưa mở được màn hình ngành", Toast.LENGTH_SHORT).show();
        }
    }

    private void moMonHoc() {
        try {
            startActivity(new Intent(this, DanhSachMonHocActivity.class));
            overridePendingTransition(R.anim.ani_intent, R.anim.ani_intenexit);
        } catch (Exception e) {
            Toast.makeText(this, "Chưa mở được màn hình môn học", Toast.LENGTH_SHORT).show();
        }
    }

    private void moDiem() {
        try {
            startActivity(new Intent(this, DanhSachDiemActivity.class));
            overridePendingTransition(R.anim.ani_intent, R.anim.ani_intenexit);
        } catch (Exception e) {
            Toast.makeText(this, "Chưa mở được màn hình điểm", Toast.LENGTH_SHORT).show();
        }
    }

    private void moTaiKhoan() {
        try {
            startActivity(new Intent(this, QuanLyTaiKhoanActivity.class));
            overridePendingTransition(R.anim.ani_intent, R.anim.ani_intenexit);
        } catch (Exception e) {
            Toast.makeText(this, "Chưa mở được màn hình tài khoản", Toast.LENGTH_SHORT).show();
        }
    }

    private void moPhanHoi() {
        try {
            startActivity(new Intent(this, XemPhanHoiActivity.class));
            overridePendingTransition(R.anim.ani_intent, R.anim.ani_intenexit);
        } catch (Exception e) {
            Toast.makeText(this, "Chưa mở được màn hình phản hồi", Toast.LENGTH_SHORT).show();
        }
    }

    private void moThongKe() {
        try {
            startActivity(new Intent(this, ThongKeActivity.class));
            overridePendingTransition(R.anim.ani_intent, R.anim.ani_intenexit);
        } catch (Exception e) {
            Toast.makeText(this, "Chưa mở được màn hình thống kê", Toast.LENGTH_SHORT).show();
        }
    }

    private void moSuKien() {
        try {
            startActivity(new Intent(this, SuKienActivity.class));
            overridePendingTransition(R.anim.ani_intent, R.anim.ani_intenexit);
        } catch (Exception e) {
            Toast.makeText(this, "Chưa mở được màn hình sự kiện", Toast.LENGTH_SHORT).show();
        }
    }

    private void dangXuat() {
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
        overridePendingTransition(R.anim.ani_intent, R.anim.ani_intenexit);
        finish();
    }
}