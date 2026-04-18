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

// màn hình chính của ADMIN
// sau khi admin đăng nhập đúng sẽ vào đây
public class ManagerActivity extends AppCompatActivity {

    // các icon chức năng
    private ImageView btnLop, btnSinhVien, btnNganh, btnMonHoc, btnDiem,
            btnTaiKhoan, btnPhanHoi, btnThongKe, btnSuKien;

    // chữ hiển thị dưới icon
    private TextView tvClass, tvStudent, tvNganh, tvMonHoc, tvDiem,
            tvTaiKhoan, tvPhanHoi, tvThongKe, tvSuKien, mywelcome;

    // các khung cardview để bấm
    private CardView cardLopHoc, cardSinhVien, cardNganh, cardMonHoc, cardDiem,
            cardTaiKhoan, cardPhanHoi, cardThongKe, cardSuKien;

    // layout chứa toàn bộ menu
    private GridLayout gridLayout;

    // nút menu góc phải
    private ImageView imageViewMenu;

    // hiệu ứng chuyển động
    private Animation animation;

    // tên tài khoản admin đăng nhập
    private String tenDangNhap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // mở giao diện admin
        setContentView(R.layout.activity_manager);

        // nhận tên đăng nhập từ LoginActivity gửi qua
        tenDangNhap = getIntent().getStringExtra("tenDangNhap");

        // nối Java với XML
        anhXa();

        // hiện xin chào admin
        hienThiThongTinAdmin();

        // chạy hiệu ứng giao diện
        xuLyAnimation();

        // menu đăng xuất
        xuLyMenu();

        // xử lý click các chức năng
        xuLySuKien();
    }

    // tìm control trong XML
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

    // hiện lời chào admin
    private void hienThiThongTinAdmin() {

        if (tenDangNhap != null &&
                !tenDangNhap.trim().isEmpty()) {

            mywelcome.setText(
                    "Xin chào, " + tenDangNhap
            );

        } else {

            mywelcome.setText("Admin");
        }
    }

    // chạy animation cho menu
    private void xuLyAnimation() {

        try {

            animation =
                    AnimationUtils.loadAnimation(
                            this,
                            R.anim.bottom_top
                    );

            gridLayout.setAnimation(animation);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // menu góc phải -> đăng xuất
    private void xuLyMenu() {

        imageViewMenu.setOnClickListener(v -> {

            PopupMenu popupMenu =
                    new PopupMenu(this, v);

            popupMenu.getMenuInflater()
                    .inflate(
                            R.menu.drawer_menu,
                            popupMenu.getMenu()
                    );

            popupMenu.show();

            popupMenu.setOnMenuItemClickListener(item -> {

                // bấm đăng xuất
                if (item.getItemId()
                        == R.id.menuDangXuat) {

                    dangXuat();
                    return true;
                }

                return false;
            });
        });
    }

    // xử lý click từng chức năng
    private void xuLySuKien() {

        // bấm quản lý lớp
        cardLopHoc.setOnClickListener(v -> moDanhSachLop());

        // bấm quản lý sinh viên
        cardSinhVien.setOnClickListener(v -> moSinhVien());

        // bấm quản lý ngành
        cardNganh.setOnClickListener(v -> moNganh());

        // bấm quản lý môn học
        cardMonHoc.setOnClickListener(v -> moMonHoc());

        // bấm quản lý điểm
        cardDiem.setOnClickListener(v -> moDiem());

        // bấm quản lý tài khoản
        cardTaiKhoan.setOnClickListener(v -> moTaiKhoan());

        // bấm phản hồi
        cardPhanHoi.setOnClickListener(v -> moPhanHoi());

        // bấm thống kê
        cardThongKe.setOnClickListener(v -> moThongKe());

        // bấm sự kiện
        cardSuKien.setOnClickListener(v -> moSuKien());
    }

    // mở màn hình lớp học
    private void moDanhSachLop() {
        startActivity(
                new Intent(this,
                        DanhSachLopActivity.class)
        );
    }

    // mở màn hình sinh viên
    private void moSinhVien() {
        startActivity(
                new Intent(this,
                        ThemSinhVienActivity.class)
        );
    }

    // mở màn hình ngành
    private void moNganh() {
        startActivity(
                new Intent(this,
                        DanhSachNganhActivity.class)
        );
    }

    // mở màn hình môn học
    private void moMonHoc() {
        startActivity(
                new Intent(this,
                        DanhSachMonHocActivity.class)
        );
    }

    // mở màn hình điểm
    private void moDiem() {
        startActivity(
                new Intent(this,
                        DanhSachDiemActivity.class)
        );
    }

    // mở màn hình tài khoản
    private void moTaiKhoan() {
        startActivity(
                new Intent(this,
                        QuanLyTaiKhoanActivity.class)
        );
    }

    // mở phản hồi
    private void moPhanHoi() {
        startActivity(
                new Intent(this,
                        XemPhanHoiActivity.class)
        );
    }

    // mở thống kê
    private void moThongKe() {
        startActivity(
                new Intent(this,
                        ThongKeActivity.class)
        );
    }

    // mở sự kiện
    private void moSuKien() {
        startActivity(
                new Intent(this,
                        SuKienActivity.class)
        );
    }

    // đăng xuất
    private void dangXuat() {

        Intent intent =
                new Intent(this,
                        LoginActivity.class);

        startActivity(intent);

        // đóng màn hình admin
        finish();
    }
}