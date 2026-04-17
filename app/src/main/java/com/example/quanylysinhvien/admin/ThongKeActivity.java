package com.example.quanylysinhvien.admin;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.quanylysinhvien.R;
import com.example.quanylysinhvien.database.DBHelper;

public class ThongKeActivity extends AppCompatActivity {

    TextView tvTitleThongKe, tvThongBaoThongKe;

    TextView tvTongSinhVien, tvTongLop, tvTongMonHoc,
            tvTongNganh, tvTongTaiKhoan, tvTongPhanHoi, tvTongDiem;

    ImageView imgThongKe;

    CardView cardSinhVien, cardLop, cardMonHoc,
            cardNganh, cardTaiKhoan, cardPhanHoi, cardDiem;

    DBHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_thong_ke);

        anhXa();

        dbHelper = new DBHelper(this);

        loadThongKe();
    }

    private void anhXa() {

        tvTitleThongKe = findViewById(R.id.tvTitleThongKe);
        tvThongBaoThongKe = findViewById(R.id.tvThongBaoThongKe);

        tvTongSinhVien = findViewById(R.id.tvTongSinhVien);
        tvTongLop = findViewById(R.id.tvTongLop);
        tvTongMonHoc = findViewById(R.id.tvTongMonHoc);
        tvTongNganh = findViewById(R.id.tvTongNganh);
        tvTongTaiKhoan = findViewById(R.id.tvTongTaiKhoan);
        tvTongPhanHoi = findViewById(R.id.tvTongPhanHoi);
        tvTongDiem = findViewById(R.id.tvTongDiem);

        imgThongKe = findViewById(R.id.imgThongKe);

        cardSinhVien = findViewById(R.id.cardSinhVienTK);
        cardLop = findViewById(R.id.cardLopTK);
        cardMonHoc = findViewById(R.id.cardMonHocTK);
        cardNganh = findViewById(R.id.cardNganhTK);
        cardTaiKhoan = findViewById(R.id.cardTaiKhoanTK);
        cardPhanHoi = findViewById(R.id.cardPhanHoiTK);
        cardDiem = findViewById(R.id.cardDiemTK);
    }

    private void loadThongKe() {

        SQLiteDatabase db = dbHelper.getReadableDatabase();

        int tongSinhVien = demBang(db, "SINHVIEN");
        int tongLop = demBang(db, "LOP");
        int tongMonHoc = demBang(db, "MONHOC");
        int tongNganh = demBang(db, "NGANH");
        int tongTaiKhoan = demBang(db, "taiKhoan");
        int tongPhanHoi = demBang(db, "PHANHOI");
        int tongDiem = demBang(db, "DIEM");

        tvTongSinhVien.setText("Sinh viên: " + tongSinhVien);
        tvTongLop.setText("Lớp học: " + tongLop);
        tvTongMonHoc.setText("Môn học: " + tongMonHoc);
        tvTongNganh.setText("Ngành học: " + tongNganh);
        tvTongTaiKhoan.setText("Tài khoản: " + tongTaiKhoan);
        tvTongPhanHoi.setText("Phản hồi: " + tongPhanHoi);
        tvTongDiem.setText("Bảng điểm: " + tongDiem);

        tvThongBaoThongKe.setText(
                "Tổng dữ liệu hệ thống: "
                        + (tongSinhVien + tongLop + tongMonHoc +
                        tongNganh + tongTaiKhoan + tongPhanHoi + tongDiem)
        );

        db.close();
    }

    private int demBang(SQLiteDatabase db, String tenBang) {

        Cursor c = db.rawQuery(
                "SELECT COUNT(*) FROM " + tenBang,
                null
        );

        int tong = 0;

        if (c.moveToFirst()) {
            tong = c.getInt(0);
        }

        c.close();

        return tong;
    }
}