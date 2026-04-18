package com.example.quanylysinhvien.user;

// =====================================================
// IMPORT THƯ VIỆN CẦN DÙNG
// =====================================================

// Activity cơ bản của Android
import androidx.appcompat.app.AppCompatActivity;

// Dùng để đọc dữ liệu từ SQLite
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

// Bundle dùng khi mở Activity
import android.os.Bundle;

// Thành phần giao diện
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

// File resource XML
import com.example.quanylysinhvien.R;

// DAO tài khoản: dùng lấy mã sinh viên theo username
import com.example.quanylysinhvien.dao.DaoTaiKhoan;

// Class tạo và quản lý database
import com.example.quanylysinhvien.database.DBHelper;

// Dùng tạo danh sách dữ liệu cho ListView
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;


// =====================================================
// MÀN HÌNH USER XEM ĐIỂM CÁ NHÂN
// =====================================================
public class XemDiemActivity extends AppCompatActivity {

    // =====================================================
    // KHAI BÁO GIAO DIỆN
    // =====================================================

    // Tiêu đề màn hình
    TextView tvTitle;

    // Dòng thông báo / tổng kết
    TextView tvThongBao;

    // ListView hiển thị bảng điểm
    ListView lvDiem;


    // =====================================================
    // DATABASE
    // =====================================================

    // Đối tượng kết nối SQLite
    DBHelper dbHelper;


    // =====================================================
    // DAO TÀI KHOẢN
    // =====================================================

    // Dùng để lấy mã sinh viên từ tài khoản đăng nhập
    DaoTaiKhoan daoTaiKhoan;


    // =====================================================
    // DỮ LIỆU ĐĂNG NHẬP
    // =====================================================

    // Tên đăng nhập user hiện tại
    String tenDangNhap;

    // Mã sinh viên tương ứng với tài khoản
    String maSv;


    // =====================================================
    // DỮ LIỆU ĐỂ HIỂN THỊ LISTVIEW
    // =====================================================

    // Danh sách mỗi dòng điểm sẽ lưu dưới dạng HashMap
    ArrayList<HashMap<String, String>> list;

    // Adapter nối dữ liệu với ListView
    SimpleAdapter adapter;



    // =====================================================
    // HÀM CHẠY ĐẦU TIÊN KHI MỞ MÀN HÌNH
    // =====================================================
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Gắn giao diện XML cho Activity này
        setContentView(R.layout.activity_xem_diem);

        // Ánh xạ các id trong XML sang Java
        anhXa();

        // Khởi tạo database
        dbHelper = new DBHelper(this);

        // Khởi tạo DAO tài khoản
        daoTaiKhoan = new DaoTaiKhoan(this);

        // =================================================
        // NHẬN DỮ LIỆU TỪ MÀN HÌNH TRƯỚC
        // =================================================

        // Lấy username được gửi qua Intent
        tenDangNhap = getIntent().getStringExtra("tenDangNhap");

        // Lấy mã sinh viên được gửi qua Intent
        maSv = getIntent().getStringExtra("maSv");

        // Nếu username bị null thì gán chuỗi rỗng để tránh lỗi
        if (tenDangNhap == null)
            tenDangNhap = "";

        // =================================================
        // NẾU CHƯA CÓ MÃ SINH VIÊN
        // THÌ TÌM THEO TÀI KHOẢN
        // =================================================
        if (maSv == null || maSv.trim().isEmpty()) {
            maSv = daoTaiKhoan.getMaSvTheoTaiKhoan(tenDangNhap);
        }

        // =================================================
        // NẾU TÀI KHOẢN CHƯA GẮN VỚI SINH VIÊN
        // =================================================
        if (maSv == null || maSv.trim().isEmpty()) {

            // Đặt lại tiêu đề
            tvTitle.setText("Bảng điểm");

            // Hiện thông báo trên màn hình
            tvThongBao.setText("Tài khoản này chưa được gán sinh viên.");

            // Thông báo nhanh
            Toast.makeText(
                    this,
                    "Admin chưa gán sinh viên",
                    Toast.LENGTH_SHORT
            ).show();

            // Dừng, không load điểm nữa
            return;
        }

        // =================================================
        // NẾU ĐÃ CÓ MÃ SINH VIÊN THÌ LOAD ĐIỂM
        // =================================================
        loadDiemTheoMaSV();
    }



    // =====================================================
    // HÀM ÁNH XẠ ID XML
    // =====================================================
    private void anhXa() {

        // Nối TextView tiêu đề trong XML với biến Java
        tvTitle = findViewById(R.id.tvTitleXemDiem);

        // Nối TextView thông báo trong XML với biến Java
        tvThongBao = findViewById(R.id.tvThongBaoDiem);

        // Nối ListView trong XML với biến Java
        lvDiem = findViewById(R.id.lvXemDiem);
    }



    // =====================================================
    // HÀM LOAD ĐIỂM THEO MÃ SINH VIÊN
    // =====================================================
    private void loadDiemTheoMaSV() {

        // Tạo list rỗng để chứa dữ liệu bảng điểm
        list = new ArrayList<>();

        // Mở database ở chế độ đọc
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        // Biến lưu thông tin sinh viên
        String tenSV = "";
        String lop = "";
        String nganh = "";

        // =================================================
        // TRUY VẤN THÔNG TIN SINH VIÊN
        // =================================================
        Cursor cSinhVien = db.rawQuery(
                "SELECT tenSV, maLop, maNganh FROM SINHVIEN WHERE maSv=?",
                new String[]{maSv}
        );

        // Nếu tìm thấy sinh viên
        if (cSinhVien.moveToFirst()) {

            // Cột 0 = tên sinh viên
            tenSV = cSinhVien.getString(0);

            // Cột 1 = mã lớp
            lop = cSinhVien.getString(1);

            // Cột 2 = mã ngành
            nganh = cSinhVien.getString(2);
        }

        // Đóng cursor sau khi đọc xong
        cSinhVien.close();

        // =================================================
        // NẾU KHÔNG TÌM THẤY SINH VIÊN
        // =================================================
        if (tenSV.isEmpty()) {

            // Đặt tiêu đề mặc định
            tvTitle.setText("Bảng điểm");

            // Hiện thông báo lỗi
            tvThongBao.setText("Không tìm thấy sinh viên.");

            // Đóng database
            db.close();

            // Dừng hàm
            return;
        }

        // =================================================
        // HIỆN TIÊU ĐỀ CÓ TÊN SINH VIÊN
        // =================================================
        tvTitle.setText("Bảng điểm của " + tenSV);

        // =================================================
        // TRUY VẤN BẢNG ĐIỂM
        // LẤY: mã SV, tên SV, mã môn, tên môn, số tín chỉ, điểm
        // =================================================
        Cursor cDiem = db.rawQuery(
                "SELECT DIEM.maSv, SINHVIEN.tenSV, " +
                        "MONHOC.maMon, MONHOC.tenMon, MONHOC.soTinChi, DIEM.diem " +
                        "FROM DIEM " +
                        "INNER JOIN SINHVIEN ON DIEM.maSv = SINHVIEN.maSv " +
                        "INNER JOIN MONHOC ON DIEM.maMon = MONHOC.maMon " +
                        "WHERE DIEM.maSv=? " +
                        "ORDER BY MONHOC.maMon ASC",
                new String[]{maSv}
        );

        // Biến tính tổng điểm có nhân tín chỉ
        float tongDiem = 0;

        // Biến tính tổng số tín chỉ
        int tongTinChi = 0;

        // =================================================
        // NẾU SINH VIÊN CÓ ĐIỂM
        // =================================================
        if (cDiem.moveToFirst()) {

            do {

                // Lấy dữ liệu từng cột trong 1 dòng điểm
                String maSinhVien = cDiem.getString(0);
                String tenSinhVien = cDiem.getString(1);
                String maMon = cDiem.getString(2);
                String tenMon = cDiem.getString(3);
                int soTinChi = cDiem.getInt(4);
                float diem = cDiem.getFloat(5);

                // Tạo 1 dòng dữ liệu để hiển thị lên ListView
                HashMap<String, String> map = new HashMap<>();

                // Thêm từng thông tin vào map
                map.put("maSv", "Mã SV: " + maSinhVien);
                map.put("tenSv", "Tên SV: " + tenSinhVien);
                map.put("maMon", "Mã môn: " + maMon);
                map.put("tenMon", "Tên môn: " + tenMon);
                map.put("soTinChi", "Số tín chỉ: " + soTinChi);
                map.put("diem", "Điểm: " + diem);

                // Thêm dòng này vào list
                list.add(map);

                // =================================================
                // TÍNH GPA
                // GPA = tổng(điểm * tín chỉ) / tổng tín chỉ
                // =================================================
                tongDiem += diem * soTinChi;
                tongTinChi += soTinChi;

            } while (cDiem.moveToNext());

            // Biến lưu GPA
            float gpa = 0;

            // Nếu có tín chỉ thì mới tính GPA
            if (tongTinChi > 0) {
                gpa = tongDiem / tongTinChi;
            }

            // =================================================
            // HIỆN THÔNG TIN TỔNG KẾT
            // =================================================
            tvThongBao.setText(
                    "MSSV: " + maSv +
                            " | Lớp: " + lop +
                            " | Ngành: " + nganh +
                            "\nTổng môn: " + list.size() +
                            " | GPA: " +
                            String.format(Locale.getDefault(), "%.2f", gpa)
            );

        } else {

            // =================================================
            // NẾU CHƯA CÓ ĐIỂM
            // =================================================
            tvThongBao.setText(
                    "MSSV: " + maSv +
                            "\nSinh viên chưa có điểm."
            );
        }

        // Đóng cursor điểm
        cDiem.close();

        // Đóng database
        db.close();

        // =================================================
        // TẠO ADAPTER ĐỂ ĐỔ DỮ LIỆU LÊN LISTVIEW
        // =================================================
        adapter = new SimpleAdapter(
                this,                       // context
                list,                       // dữ liệu
                R.layout.dong_xem_diem_user, // layout mỗi dòng
                new String[]{
                        "maSv",
                        "tenSv",
                        "maMon",
                        "tenMon",
                        "soTinChi",
                        "diem"
                },                          // key dữ liệu
                new int[]{
                        R.id.tvMaSvXem,
                        R.id.tvTenSvXem,
                        R.id.tvMaMonXem,
                        R.id.tvTenMonXem,
                        R.id.tvSoTinChiXem,
                        R.id.tvDiemXem
                }                           // id TextView tương ứng
        );

        // Gắn adapter vào ListView để hiển thị
        lvDiem.setAdapter(adapter);
    }
}