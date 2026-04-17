package com.example.quanylysinhvien.user;

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

public class UserActivity extends AppCompatActivity {

    private CardView cardXemSinhVien;
    private CardView cardXemLop;
    private CardView cardWebsiteUser;
    private CardView cardSuKienUser;
    private CardView cardInfoUser;
    private CardView cardLienHeUser;
    private CardView cardPhanHoiUser;
    private CardView cardDangXuatUser;

    private TextView tvUserTitle;
    private ImageView imageViewMenuUser;
    private GridLayout gridUser;

    private Animation animation;

    private String tenDangNhap;
    private String maSv;
    private String vaiTro;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);

        anhXa();
        nhanDuLieu();
        hienThiThongTinUser();
        xuLyAnimation();
        xuLyMenu();
        xuLySuKien();
    }

    private void anhXa() {
        tvUserTitle = findViewById(R.id.tvUserTitle);
        imageViewMenuUser = findViewById(R.id.imageViewMenuUser);
        gridUser = findViewById(R.id.gridUser);

        cardXemSinhVien = findViewById(R.id.cardXemSinhVien);
        cardXemLop = findViewById(R.id.cardXemLop);
        cardWebsiteUser = findViewById(R.id.cardWebsiteUser);
        cardSuKienUser = findViewById(R.id.cardSuKienUser);
        cardInfoUser = findViewById(R.id.cardInfoUser);
        cardLienHeUser = findViewById(R.id.cardLienHeUser);
        cardPhanHoiUser = findViewById(R.id.cardPhanHoiUser);
        cardDangXuatUser = findViewById(R.id.cardDangXuatUser);
    }

    private void nhanDuLieu() {
        Intent intent = getIntent();

        if (intent != null) {
            tenDangNhap = intent.getStringExtra("tenDangNhap");
            maSv = intent.getStringExtra("maSv");
            vaiTro = intent.getStringExtra("vaiTro");
        }

        if (tenDangNhap == null) tenDangNhap = "";
        if (maSv == null) maSv = "";
        if (vaiTro == null) vaiTro = "user";
    }

    private void hienThiThongTinUser() {
        if (!tenDangNhap.isEmpty()) {
            tvUserTitle.setText("Xin chào, " + tenDangNhap);
        } else {
            tvUserTitle.setText("Trang Sinh Viên");
        }
    }

    private void xuLyAnimation() {
        try {
            animation = AnimationUtils.loadAnimation(this, R.anim.bottom_top);
            gridUser.setAnimation(animation);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void xuLyMenu() {
        imageViewMenuUser.setOnClickListener(v -> {

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

        // Xem điểm
        cardXemSinhVien.setOnClickListener(v -> moXemDiem());

        // Xem lớp
        cardXemLop.setOnClickListener(v -> moXemLop());

        // Xem môn học
        cardWebsiteUser.setOnClickListener(v -> moXemMonHoc());

        // Sự kiện
        cardSuKienUser.setOnClickListener(v -> moSuKien());

        // Thông tin cá nhân
        cardInfoUser.setOnClickListener(v -> moThongTinCaNhan());

        // Liên hệ
        cardLienHeUser.setOnClickListener(v ->
                Toast.makeText(this,
                        "Liên hệ phòng đào tạo: 0123 456 789",
                        Toast.LENGTH_SHORT).show()
        );

        // Phản hồi
        cardPhanHoiUser.setOnClickListener(v ->
                Toast.makeText(this,
                        "Chức năng phản hồi đang cập nhật",
                        Toast.LENGTH_SHORT).show()
        );

        // Đăng xuất
        cardDangXuatUser.setOnClickListener(v -> dangXuat());
    }

    private void moXemDiem() {
        try {
            Intent intent = new Intent(UserActivity.this, XemDiemActivity.class);
            intent.putExtra("tenDangNhap", tenDangNhap);
            intent.putExtra("maSv", maSv);

            startActivity(intent);
            overridePendingTransition(R.anim.ani_intent, R.anim.ani_intenexit);

        } catch (Exception e) {
            Toast.makeText(this,
                    "Chưa mở được màn hình xem điểm",
                    Toast.LENGTH_SHORT).show();
        }
    }

    private void moXemLop() {
        try {
            Intent intent = new Intent(UserActivity.this, XemLopActivity.class);
            intent.putExtra("tenDangNhap", tenDangNhap);
            intent.putExtra("maSv", maSv);

            startActivity(intent);
            overridePendingTransition(R.anim.ani_intent, R.anim.ani_intenexit);

        } catch (Exception e) {
            Toast.makeText(this,
                    "Chưa mở được màn hình xem lớp",
                    Toast.LENGTH_SHORT).show();
        }
    }

    private void moXemMonHoc() {
        try {
            Intent intent = new Intent(UserActivity.this, XemMonHocActivity.class);
            intent.putExtra("tenDangNhap", tenDangNhap);
            intent.putExtra("maSv", maSv);

            startActivity(intent);
            overridePendingTransition(R.anim.ani_intent, R.anim.ani_intenexit);

        } catch (Exception e) {
            Toast.makeText(this,
                    "Chưa mở được màn hình môn học",
                    Toast.LENGTH_SHORT).show();
        }
    }

    private void moSuKien() {
        try {
            Intent intent = new Intent(UserActivity.this, SuKienActivity.class);
            intent.putExtra("tenDangNhap", tenDangNhap);

            startActivity(intent);
            overridePendingTransition(R.anim.ani_intent, R.anim.ani_intenexit);

        } catch (Exception e) {
            Toast.makeText(this,
                    "Chưa mở được màn hình sự kiện",
                    Toast.LENGTH_SHORT).show();
        }
    }

    private void moThongTinCaNhan() {

        if (maSv == null || maSv.trim().isEmpty()) {
            Toast.makeText(this,
                    "Tài khoản này chưa được admin gán sinh viên",
                    Toast.LENGTH_SHORT).show();
            return;
        }

        try {
            Intent intent = new Intent(
                    UserActivity.this,
                    ThongTinCaNhanActivity.class
            );

            intent.putExtra("tenDangNhap", tenDangNhap);
            intent.putExtra("maSv", maSv);

            startActivity(intent);

            overridePendingTransition(
                    R.anim.ani_intent,
                    R.anim.ani_intenexit
            );

        } catch (Exception e) {
            Toast.makeText(this,
                    "Chưa mở được hồ sơ cá nhân",
                    Toast.LENGTH_SHORT).show();
        }
    }

    private void dangXuat() {
        Intent intent = new Intent(UserActivity.this, LoginActivity.class);
        startActivity(intent);

        overridePendingTransition(
                R.anim.ani_intent,
                R.anim.ani_intenexit
        );

        finish();
    }

    @Override
    public void onBackPressed() {
        Toast.makeText(this,
                "Nhấn Đăng xuất để thoát",
                Toast.LENGTH_SHORT).show();
    }
}