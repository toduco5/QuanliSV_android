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

import com.example.quanylysinhvien.R;
import com.example.quanylysinhvien.SuKienActivity;
import com.example.quanylysinhvien.loginandregisteractivity.LoginActivity;

public class ManagerActivity extends AppCompatActivity {

    private ImageView btnLop, btnSinhVien, btnNganh, btnMonHoc, btnDiem,
            btnTaiKhoan, btnPhanHoi, btnThongKe, btnSuKien;

    private TextView tvClass, tvStudent, tvNganh, tvMonHoc, tvDiem,
            tvTaiKhoan, tvPhanHoi, tvThongKe, tvSuKien;

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
        xuLyAnimation();
        xuLyMenu();
        xuLySuKien();
    }

    private void anhXa() {
        gridLayout = findViewById(R.id.girdviewManager);
        imageViewMenu = findViewById(R.id.imageViewMenu);

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

    private void xuLyAnimation() {
        animation = AnimationUtils.loadAnimation(this, R.anim.bottom_top);
        gridLayout.setAnimation(animation);
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
        cardLopHoc.setOnClickListener(v -> moDanhSachLop());
        btnLop.setOnClickListener(v -> moDanhSachLop());
        tvClass.setOnClickListener(v -> moDanhSachLop());

        cardSinhVien.setOnClickListener(v -> moThemSinhVien());
        btnSinhVien.setOnClickListener(v -> moThemSinhVien());
        tvStudent.setOnClickListener(v -> moThemSinhVien());

        cardNganh.setOnClickListener(v -> startActivity(new Intent(this, DanhSachNganhActivity.class)));
        btnNganh.setOnClickListener(v -> startActivity(new Intent(this, DanhSachNganhActivity.class)));
        tvNganh.setOnClickListener(v -> startActivity(new Intent(this, DanhSachNganhActivity.class)));

        cardMonHoc.setOnClickListener(v -> startActivity(new Intent(this, DanhSachMonHocActivity.class)));
        btnMonHoc.setOnClickListener(v -> startActivity(new Intent(this, DanhSachMonHocActivity.class)));
        tvMonHoc.setOnClickListener(v -> startActivity(new Intent(this, DanhSachMonHocActivity.class)));

        cardDiem.setOnClickListener(v -> startActivity(new Intent(this, DanhSachDiemActivity.class)));
        btnDiem.setOnClickListener(v -> startActivity(new Intent(this, DanhSachDiemActivity.class)));
        tvDiem.setOnClickListener(v -> startActivity(new Intent(this, DanhSachDiemActivity.class)));

        cardTaiKhoan.setOnClickListener(v -> startActivity(new Intent(this, QuanLyTaiKhoanActivity.class)));
        btnTaiKhoan.setOnClickListener(v -> startActivity(new Intent(this, QuanLyTaiKhoanActivity.class)));
        tvTaiKhoan.setOnClickListener(v -> startActivity(new Intent(this, QuanLyTaiKhoanActivity.class)));

        cardPhanHoi.setOnClickListener(v -> startActivity(new Intent(this, XemPhanHoiActivity.class)));
        btnPhanHoi.setOnClickListener(v -> startActivity(new Intent(this, XemPhanHoiActivity.class)));
        tvPhanHoi.setOnClickListener(v -> startActivity(new Intent(this, XemPhanHoiActivity.class)));

        cardThongKe.setOnClickListener(v -> startActivity(new Intent(this, ThongKeActivity.class)));
        btnThongKe.setOnClickListener(v -> startActivity(new Intent(this, ThongKeActivity.class)));
        tvThongKe.setOnClickListener(v -> startActivity(new Intent(this, ThongKeActivity.class)));

        cardSuKien.setOnClickListener(v -> startActivity(new Intent(this, SuKienActivity.class)));
        btnSuKien.setOnClickListener(v -> startActivity(new Intent(this, SuKienActivity.class)));
        tvSuKien.setOnClickListener(v -> startActivity(new Intent(this, SuKienActivity.class)));
    }

    private void moDanhSachLop() {
        Intent intent = new Intent(this, DanhSachLopActivity.class);
        intent.putExtra("tenDangNhap", tenDangNhap);
        startActivity(intent);
        overridePendingTransition(R.anim.ani_intent, R.anim.ani_intenexit);
    }

    private void moThemSinhVien() {
        Intent intent = new Intent(this, ThemSinhVienActivity.class);
        intent.putExtra("tenDangNhap", tenDangNhap);
        startActivity(intent);
        overridePendingTransition(R.anim.ani_intent, R.anim.ani_intenexit);
    }

    private void dangXuat() {
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
        overridePendingTransition(R.anim.ani_intent, R.anim.ani_intenexit);
        finish();
    }
}