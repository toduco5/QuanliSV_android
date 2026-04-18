package com.example.quanylysinhvien.admin;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.TextUtils;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.quanylysinhvien.R;
import com.example.quanylysinhvien.adapter.MonHocAdapter;
import com.example.quanylysinhvien.dao.MonHocDAO;
import com.example.quanylysinhvien.dao.NganhDAO;
import com.example.quanylysinhvien.model.MonHoc;

import java.util.ArrayList;

// Màn hình quản lý môn học
// Chức năng: thêm, sửa, xóa, làm mới, hiển thị danh sách môn học
public class QuanLyMonHocActivity extends AppCompatActivity {

    // Ô nhập mã môn, tên môn, số tín chỉ
    private EditText edtMaMon, edtTenMon, edtSoTinChi;

    // Spinner chọn ngành
    private Spinner spNganhMonHoc;

    // Các nút chức năng
    private Button btnThemMonHoc, btnSuaMonHoc, btnXoaMonHoc, btnLamMoi;

    // ListView hiển thị danh sách môn học
    private ListView lvMonHoc;

    // DAO xử lý database môn học
    private MonHocDAO monHocDAO;

    // DAO lấy danh sách ngành
    private NganhDAO nganhDAO;

    // Danh sách môn học
    private ArrayList<MonHoc> list;

    // Adapter hiển thị ListView
    private MonHocAdapter adapter;

    // Danh sách ngành đưa vào Spinner
    private ArrayList<String> dsNganh;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Gắn giao diện XML
        setContentView(R.layout.activity_them_monhoc);

        // Ánh xạ control
        anhXa();

        // Khởi tạo DAO
        monHocDAO = new MonHocDAO(this);
        nganhDAO = new NganhDAO(this);

        // Tạo list rỗng
        list = new ArrayList<>();

        // Load ngành lên Spinner
        loadSpinnerNganh();

        // Load danh sách môn học
        loadData();

        // Đưa form về chế độ thêm
        cheDoThem();

        // Nhấn nút thêm
        btnThemMonHoc.setOnClickListener(v -> themMonHoc());

        // Nhấn nút sửa
        btnSuaMonHoc.setOnClickListener(v -> suaMonHoc());

        // Nhấn nút xóa
        btnXoaMonHoc.setOnClickListener(v -> xoaMonHoc());

        // Nhấn nút làm mới
        btnLamMoi.setOnClickListener(v -> lamMoiForm());

        // Khi chọn 1 môn học trong ListView
        lvMonHoc.setOnItemClickListener((parent, view, position, id) -> {

            // Lấy môn học được chọn
            MonHoc m = list.get(position);

            // Đổ dữ liệu lên form
            edtMaMon.setText(m.getMaMon());
            edtTenMon.setText(m.getTenMon());
            edtSoTinChi.setText(String.valueOf(m.getSoTinChi()));

            // Chọn đúng ngành trong Spinner
            for (int i = 0; i < dsNganh.size(); i++) {
                if (dsNganh.get(i).startsWith(m.getMaNganh() + " - ")) {
                    spNganhMonHoc.setSelection(i);
                    break;
                }
            }

            // Không cho sửa mã môn
            edtMaMon.setEnabled(false);

            // Tắt thêm, bật sửa và xóa
            btnThemMonHoc.setEnabled(false);
            btnSuaMonHoc.setEnabled(true);
            btnXoaMonHoc.setEnabled(true);
        });
    }

    // Ánh xạ control XML sang Java
    private void anhXa() {
        edtMaMon = findViewById(R.id.edtMaMon);
        edtTenMon = findViewById(R.id.edtTenMon);
        edtSoTinChi = findViewById(R.id.edtSoTinChi);

        spNganhMonHoc = findViewById(R.id.spNganhMonHoc);

        btnThemMonHoc = findViewById(R.id.btnThemMonHoc);
        btnSuaMonHoc = findViewById(R.id.btnSuaMonHoc);
        btnXoaMonHoc = findViewById(R.id.btnXoaMonHoc);
        btnLamMoi = findViewById(R.id.btnLamMoi);

        lvMonHoc = findViewById(R.id.lvMonHoc);
    }

    // Load danh sách ngành vào Spinner
    private void loadSpinnerNganh() {

        // Lấy dữ liệu ngành từ database
        dsNganh = nganhDAO.getDanhSachSpinner();

        // Tạo adapter cho Spinner
        ArrayAdapter<String> adapterNganh =
                new ArrayAdapter<>(
                        this,
                        android.R.layout.simple_spinner_item,
                        dsNganh
                );

        adapterNganh.setDropDownViewResource(
                android.R.layout.simple_spinner_dropdown_item
        );

        spNganhMonHoc.setAdapter(adapterNganh);
    }

    // Load danh sách môn học lên ListView
    private void loadData() {

        // Xóa dữ liệu cũ
        list.clear();

        // Lấy toàn bộ môn học từ database
        list.addAll(monHocDAO.getAll());

        // Nếu chưa có adapter thì tạo mới
        if (adapter == null) {
            adapter = new MonHocAdapter(this, list);
            lvMonHoc.setAdapter(adapter);
        } else {

            // Nếu có rồi thì cập nhật lại giao diện
            adapter.notifyDataSetChanged();
        }
    }

    // Đưa form về trạng thái thêm mới
    private void cheDoThem() {

        // Cho nhập mã môn
        edtMaMon.setEnabled(true);

        // Bật nút thêm
        btnThemMonHoc.setEnabled(true);

        // Tắt sửa và xóa
        btnSuaMonHoc.setEnabled(false);
        btnXoaMonHoc.setEnabled(false);
    }

    // Hàm thêm môn học
    private void themMonHoc() {

        // Lấy dữ liệu từ form
        String ma = edtMaMon.getText().toString().trim();
        String ten = edtTenMon.getText().toString().trim();
        String stc = edtSoTinChi.getText().toString().trim();

        // Nếu dữ liệu sai thì dừng
        if (!kiemTraDuLieu(ma, ten, stc)) return;

        // Lấy mã ngành từ Spinner
        String maNganh =
                spNganhMonHoc.getSelectedItem()
                        .toString()
                        .split(" - ")[0];

        // Đổi số tín chỉ sang số nguyên
        int soTinChi = Integer.parseInt(stc);

        // Thêm vào database
        boolean ketQua =
                monHocDAO.insert(
                        new MonHoc(ma, ten, soTinChi, maNganh)
                );

        if (ketQua) {
            Toast.makeText(this,
                    "Thêm môn học thành công",
                    Toast.LENGTH_SHORT).show();

            lamMoiForm();
            loadData();

        } else {
            Toast.makeText(this,
                    "Thêm môn học thất bại",
                    Toast.LENGTH_SHORT).show();
        }
    }

    // Hàm sửa môn học
    private void suaMonHoc() {

        String ma = edtMaMon.getText().toString().trim();
        String ten = edtTenMon.getText().toString().trim();
        String stc = edtSoTinChi.getText().toString().trim();

        if (!kiemTraDuLieu(ma, ten, stc)) return;

        String maNganh =
                spNganhMonHoc.getSelectedItem()
                        .toString()
                        .split(" - ")[0];

        int soTinChi = Integer.parseInt(stc);

        // Update database
        boolean ketQua =
                monHocDAO.update(
                        new MonHoc(ma, ten, soTinChi, maNganh)
                );

        if (ketQua) {
            Toast.makeText(this,
                    "Sửa môn học thành công",
                    Toast.LENGTH_SHORT).show();

            lamMoiForm();
            loadData();

        } else {
            Toast.makeText(this,
                    "Sửa môn học thất bại",
                    Toast.LENGTH_SHORT).show();
        }
    }

    // Hàm xóa môn học
    private void xoaMonHoc() {

        String ma = edtMaMon.getText().toString().trim();

        // Nếu chưa chọn môn học
        if (TextUtils.isEmpty(ma)) {
            Toast.makeText(this,
                    "Vui lòng chọn môn học cần xóa",
                    Toast.LENGTH_SHORT).show();
            return;
        }

        // Hộp thoại xác nhận xóa
        new AlertDialog.Builder(this)
                .setTitle("Xác nhận xóa")
                .setMessage("Bạn có chắc muốn xóa " + ma + " ?")
                .setPositiveButton("Xóa", (dialog, which) -> {

                    boolean ketQua = monHocDAO.delete(ma);

                    if (ketQua) {
                        Toast.makeText(this,
                                "Xóa thành công",
                                Toast.LENGTH_SHORT).show();

                        lamMoiForm();
                        loadData();

                    } else {
                        Toast.makeText(this,
                                "Xóa thất bại",
                                Toast.LENGTH_SHORT).show();
                    }

                })
                .setNegativeButton("Hủy", null)
                .show();
    }

    // Kiểm tra dữ liệu nhập vào
    private boolean kiemTraDuLieu(String ma, String ten, String stc) {

        // Mã môn rỗng
        if (ma.isEmpty()) {
            edtMaMon.setError("Nhập mã môn");
            edtMaMon.requestFocus();
            return false;
        }

        // Tên môn rỗng
        if (ten.isEmpty()) {
            edtTenMon.setError("Nhập tên môn");
            edtTenMon.requestFocus();
            return false;
        }

        // Số tín chỉ rỗng
        if (stc.isEmpty()) {
            edtSoTinChi.setError("Nhập số tín chỉ");
            edtSoTinChi.requestFocus();
            return false;
        }

        int soTinChi;

        try {
            soTinChi = Integer.parseInt(stc);
        } catch (Exception e) {
            edtSoTinChi.setError("Số không hợp lệ");
            return false;
        }

        // Số tín chỉ phải > 0
        if (soTinChi <= 0) {
            edtSoTinChi.setError("Phải lớn hơn 0");
            return false;
        }

        return true;
    }

    // Làm mới form nhập
    private void lamMoiForm() {

        edtMaMon.setText("");
        edtTenMon.setText("");
        edtSoTinChi.setText("");

        // Chọn ngành đầu tiên
        spNganhMonHoc.setSelection(0);

        // Cho nhập lại mã môn
        edtMaMon.setEnabled(true);
        edtMaMon.requestFocus();

        // Về chế độ thêm
        cheDoThem();
    }
}