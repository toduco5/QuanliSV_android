package com.example.quanylysinhvien.user;

// ===============================
// IMPORT THƯ VIỆN CẦN DÙNG
// ===============================

// Activity cơ bản Android
import androidx.appcompat.app.AppCompatActivity;

// Dùng truy vấn SQLite
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

// Bundle dùng nhận dữ liệu khi mở màn hình
import android.os.Bundle;

// Giao diện
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

// Resource XML
import com.example.quanylysinhvien.R;

// Class xử lý tài khoản
import com.example.quanylysinhvien.dao.DaoTaiKhoan;

// Class tạo database
import com.example.quanylysinhvien.database.DBHelper;

// Danh sách
import java.util.ArrayList;
import java.util.HashMap;


// =======================================================
// MÀN HÌNH USER XEM DANH SÁCH MÔN HỌC CỦA MÌNH
// =======================================================

public class XemMonHocActivity extends AppCompatActivity {

    // ===============================
    // KHAI BÁO GIAO DIỆN
    // ===============================

    // Tiêu đề màn hình
    TextView tvTitleMonHoc;

    // Dòng thông báo phía dưới tiêu đề
    TextView tvThongBaoMonHoc;

    // ListView hiển thị môn học
    ListView lvMonHoc;


    // ===============================
    // DATABASE
    // ===============================

    DBHelper dbHelper;


    // ===============================
    // DAO tài khoản
    // Dùng lấy mã sinh viên từ username
    // ===============================

    DaoTaiKhoan daoTaiKhoan;


    // ===============================
    // DỮ LIỆU ĐĂNG NHẬP
    // ===============================

    // username đăng nhập
    String tenDangNhap;

    // mã sinh viên
    String maSv;


    // ===============================
    // DANH SÁCH DỮ LIỆU ĐỂ ĐỔ LISTVIEW
    // ===============================

    ArrayList<HashMap<String, String>> list;


    // Adapter nối dữ liệu với ListView
    SimpleAdapter adapter;



    // =======================================================
    // HÀM CHẠY ĐẦU TIÊN KHI MỞ MÀN HÌNH
    // =======================================================

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // gắn giao diện XML
        setContentView(R.layout.activity_xem_mon_hoc);


        // ===============================
        // ánh xạ id XML sang Java
        // ===============================
        anhXa();


        // ===============================
        // khởi tạo database
        // ===============================
        dbHelper = new DBHelper(this);


        // ===============================
        // khởi tạo DAO tài khoản
        // ===============================
        daoTaiKhoan = new DaoTaiKhoan(this);


        // ===============================
        // NHẬN DỮ LIỆU TỪ MÀN HÌNH TRƯỚC
        // ===============================

        tenDangNhap = getIntent().getStringExtra("tenDangNhap");
        maSv = getIntent().getStringExtra("maSv");


        // Nếu null thì gán rỗng
        if (tenDangNhap == null)
            tenDangNhap = "";


        // ==========================================
        // Nếu chưa truyền mã sinh viên qua
        // thì lấy theo username
        // ==========================================

        if (maSv == null || maSv.trim().isEmpty()) {

            maSv = daoTaiKhoan.getMaSvTheoTaiKhoan(tenDangNhap);
        }


        // ==========================================
        // Nếu tài khoản chưa gắn sinh viên
        // ==========================================

        if (maSv == null || maSv.trim().isEmpty()) {

            tvTitleMonHoc.setText("Danh sách môn học");

            tvThongBaoMonHoc.setText(
                    "Tài khoản này chưa được gán sinh viên."
            );

            Toast.makeText(
                    this,
                    "Admin chưa gán sinh viên",
                    Toast.LENGTH_SHORT
            ).show();

            return;
        }


        // ==========================================
        // Nếu hợp lệ thì load môn học
        // ==========================================

        loadMonHocTheoLop();
    }



    // =======================================================
    // HÀM ÁNH XẠ ID XML
    // =======================================================

    private void anhXa() {

        tvTitleMonHoc = findViewById(R.id.tvTitleMonHoc);

        tvThongBaoMonHoc = findViewById(R.id.tvThongBaoMonHoc);

        lvMonHoc = findViewById(R.id.lvMonHocUser);
    }



    // =======================================================
    // LOAD MÔN HỌC THEO LỚP SINH VIÊN
    // =======================================================

    private void loadMonHocTheoLop() {

        // tạo list rỗng
        list = new ArrayList<>();


        // mở database đọc dữ liệu
        SQLiteDatabase db = dbHelper.getReadableDatabase();


        // biến lưu thông tin sinh viên
        String tenSV = "";
        String maLop = "";
        String tenLop = "";


        // ===================================================
        // TRUY VẤN THÔNG TIN SINH VIÊN + LỚP
        // ===================================================

        Cursor cSinhVien = db.rawQuery(

                "SELECT SINHVIEN.tenSV, SINHVIEN.maLop, LOP.tenLop " +
                        "FROM SINHVIEN " +
                        "INNER JOIN LOP ON SINHVIEN.maLop = LOP.maLop " +
                        "WHERE SINHVIEN.maSv=?",

                new String[]{maSv}
        );


        // Nếu tìm thấy
        if (cSinhVien.moveToFirst()) {

            tenSV = cSinhVien.getString(0); // tên sinh viên
            maLop = cSinhVien.getString(1); // mã lớp
            tenLop = cSinhVien.getString(2); // tên lớp
        }

        cSinhVien.close();


        // ===================================================
        // Nếu sinh viên chưa có lớp
        // ===================================================

        if (maLop.isEmpty()) {

            tvTitleMonHoc.setText("Danh sách môn học");

            tvThongBaoMonHoc.setText(
                    "Không tìm thấy lớp của sinh viên."
            );

            db.close();

            return;
        }



        // ===================================================
        // LẤY DANH SÁCH MÔN HỌC THEO LỚP
        // ===================================================

        Cursor c = db.rawQuery(

                "SELECT MONHOC.maMon, MONHOC.tenMon, MONHOC.soTinChi, " +
                        "NGANH.tenNganh, LOP_MONHOC.hocKy, LOP_MONHOC.namHoc " +
                        "FROM LOP_MONHOC " +
                        "INNER JOIN MONHOC ON LOP_MONHOC.maMon = MONHOC.maMon " +
                        "INNER JOIN NGANH ON MONHOC.maNganh = NGANH.maNganh " +
                        "WHERE LOP_MONHOC.maLop=? " +
                        "ORDER BY MONHOC.maMon ASC",

                new String[]{maLop}
        );



        // ===================================================
        // NẾU CÓ MÔN HỌC
        // ===================================================

        if (c.moveToFirst()) {

            do {

                // tạo từng dòng dữ liệu
                HashMap<String, String> map = new HashMap<>();

                map.put("maMon", "Mã môn: " + c.getString(0));
                map.put("tenMon", "Tên môn: " + c.getString(1));
                map.put("soTinChi", "Số tín chỉ: " + c.getInt(2));
                map.put("tenNganh", "Ngành: " + c.getString(3));
                map.put("hocKy", "Học kỳ: " + c.getInt(4));
                map.put("namHoc", "Năm học: " + c.getString(5));

                // thêm vào list
                list.add(map);

            } while (c.moveToNext());


            // =======================================
            // HIỆN THÔNG TIN
            // =======================================

            tvTitleMonHoc.setText(
                    "Môn học của " + tenSV
            );

            tvThongBaoMonHoc.setText(
                    "Lớp: " + tenLop + " (" + maLop + ")" +
                            "\nTổng số môn học: " + list.size()
            );

        } else {

            // =======================================
            // CHƯA CÓ MÔN HỌC
            // =======================================

            tvTitleMonHoc.setText("Danh sách môn học");

            tvThongBaoMonHoc.setText(
                    "Lớp này chưa được gán môn học."
            );

            Toast.makeText(
                    this,
                    "Không tìm thấy môn học",
                    Toast.LENGTH_SHORT
            ).show();
        }


        // đóng cursor và database
        c.close();
        db.close();



        // ===================================================
        // ĐỔ DỮ LIỆU LÊN LISTVIEW
        // ===================================================

        adapter = new SimpleAdapter(

                this,
                list,

                R.layout.dong_xem_mon_hoc,

                new String[]{
                        "maMon",
                        "tenMon",
                        "soTinChi",
                        "tenNganh",
                        "hocKy",
                        "namHoc"
                },

                new int[]{
                        R.id.tvMaMon,
                        R.id.tvTenMon,
                        R.id.tvSoTinChi,
                        R.id.tvTenNganhMon,
                        R.id.tvHocKyMon,
                        R.id.tvNamHocMon
                }
        );


        // gắn adapter vào listview
        lvMonHoc.setAdapter(adapter);
    }
}