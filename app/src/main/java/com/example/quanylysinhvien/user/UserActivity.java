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

import com.example.quanylysinhvien.LienHeActivity;
import com.example.quanylysinhvien.R;
import com.example.quanylysinhvien.SuKienActivity;
import com.example.quanylysinhvien.loginandregisteractivity.LoginActivity;

public class UserActivity extends AppCompatActivity {

    CardView cardXemSinhVien, cardXemLop, cardWebsite, cardSuKien,
            cardInfo, cardLienHe, cardPhanHoi, cardDangXuat;

    GridLayout gridUser;
    ImageView imageViewMenu;
    Animation animation;

    String tenDangNhap, quyen;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);

        tenDangNhap = getIntent().getStringExtra("tenDangNhap");
        quyen = getIntent().getStringExtra("quyen");

        if (quyen == null) quyen = "user";

        anhXa();
        xuLyAnimation();
        xuLyMenu();
        xuLySuKien();
    }

    private void anhXa() {
        gridUser = findViewById(R.id.gridUser);
        imageViewMenu = findViewById(R.id.imageViewMenuUser);

        cardXemSinhVien = findViewById(R.id.cardXemSinhVien);
        cardXemLop = findViewById(R.id.cardXemLop);
        cardWebsite = findViewById(R.id.cardWebsiteUser);
        cardSuKien = findViewById(R.id.cardSuKienUser);
        cardInfo = findViewById(R.id.cardInfoUser);
        cardLienHe = findViewById(R.id.cardLienHeUser);
        cardPhanHoi = findViewById(R.id.cardPhanHoiUser);
        cardDangXuat = findViewById(R.id.cardDangXuatUser);
    }

    private void xuLyAnimation() {
        animation = AnimationUtils.loadAnimation(this, R.anim.bottom_top);
        gridUser.setAnimation(animation);
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

        // Xem điểm
        cardXemSinhVien.setOnClickListener(v -> {
            Intent intent = new Intent(this, XemDiemActivity.class);
            intent.putExtra("tenDangNhap", tenDangNhap);
            intent.putExtra("quyen", "user");
            startActivity(intent);
            overridePendingTransition(R.anim.ani_intent, R.anim.ani_intenexit);
        });

        // Xem lớp
        cardXemLop.setOnClickListener(v -> {
            Intent intent = new Intent(this, XemLopActivity.class);
            intent.putExtra("tenDangNhap", tenDangNhap);
            intent.putExtra("quyen", "user");
            startActivity(intent);
            overridePendingTransition(R.anim.ani_intent, R.anim.ani_intenexit);
        });

        // Xem môn học
        cardWebsite.setOnClickListener(v -> {
            Intent intent = new Intent(this, XemMonHocActivity.class);
            intent.putExtra("tenDangNhap", tenDangNhap);
            intent.putExtra("quyen", "user");
            startActivity(intent);
            overridePendingTransition(R.anim.ani_intent, R.anim.ani_intenexit);
        });

        // Sự kiện
        cardSuKien.setOnClickListener(v -> {
            startActivity(new Intent(this, SuKienActivity.class));
            overridePendingTransition(R.anim.ani_intent, R.anim.ani_intenexit);
        });

        // Thông tin cá nhân
        cardInfo.setOnClickListener(v -> {
            Intent intent = new Intent(this, ThongTinCaNhanActivity.class);
            intent.putExtra("tenDangNhap", tenDangNhap);
            intent.putExtra("quyen", "user");
            startActivity(intent);
            overridePendingTransition(R.anim.ani_intent, R.anim.ani_intenexit);
        });

        // Liên hệ
        cardLienHe.setOnClickListener(v -> {
            startActivity(new Intent(this, LienHeActivity.class));
            overridePendingTransition(R.anim.ani_intent, R.anim.ani_intenexit);
        });

        // Phản hồi
        cardPhanHoi.setOnClickListener(v -> {
            Intent intent = new Intent(this, PhanHoiActivity.class);
            intent.putExtra("tenDangNhap", tenDangNhap);
            startActivity(intent);
            overridePendingTransition(R.anim.ani_intent, R.anim.ani_intenexit);
        });

        // Đăng xuất
        cardDangXuat.setOnClickListener(v -> dangXuat());
    }

    private void dangXuat() {
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
        overridePendingTransition(R.anim.ani_intent, R.anim.ani_intenexit);
        finish();
    }
}