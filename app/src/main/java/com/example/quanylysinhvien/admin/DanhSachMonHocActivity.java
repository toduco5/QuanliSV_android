package com.example.quanylysinhvien.admin;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.example.quanylysinhvien.R;
import com.example.quanylysinhvien.adapter.MonHocAdapter;
import com.example.quanylysinhvien.dao.MonHocDAO;
import com.example.quanylysinhvien.model.MonHoc;

import java.util.ArrayList;

public class DanhSachMonHocActivity extends AppCompatActivity {

    // Ô nhập từ khóa tìm môn học
    private EditText edtTimMonHoc;

    // Nút tìm kiếm và nút thêm môn học mới
    private Button btnTimMonHoc, btnThemMonHocMoi;

    // ListView hiển thị danh sách môn học
    private ListView lvMonHoc;

    // TextView hiển thị tổng số môn học
    private TextView tvSoLuongMonHoc;

    // Danh sách dữ liệu môn học
    private ArrayList<MonHoc> list;

    // Adapter đổ dữ liệu lên ListView
    private MonHocAdapter adapter;

    // DAO dùng để thao tác với database
    private MonHocDAO monHocDAO;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Gắn layout giao diện cho Activity
        setContentView(R.layout.activity_danh_sach_monhoc);

        // Ánh xạ các control từ XML sang Java
        anhXa();

        // Khởi tạo DAO
        monHocDAO = new MonHocDAO(this);

        // Tạo danh sách rỗng ban đầu
        list = new ArrayList<>();

        // Tải dữ liệu từ database lên ListView
        loadData();

        // Khi bấm nút tìm kiếm thì gọi hàm tìm kiếm môn học
        btnTimMonHoc.setOnClickListener(v -> timKiemMonHoc());

        // Khi bấm nút thêm môn học mới thì chuyển sang màn hình quản lý môn học
        btnThemMonHocMoi.setOnClickListener(v -> {
            Intent intent = new Intent(DanhSachMonHocActivity.this, QuanLyMonHocActivity.class);
            startActivity(intent);
        });

        // Bắt sự kiện khi người dùng gõ trong ô tìm kiếm
        edtTimMonHoc.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // Không xử lý gì trước khi text thay đổi
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // Nếu người dùng xóa hết nội dung tìm kiếm thì load lại toàn bộ dữ liệu
                if (s.toString().trim().isEmpty()) {
                    loadData();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                // Không xử lý gì sau khi text thay đổi
            }
        });

        // Bắt sự kiện khi nhấn vào 1 môn học trong danh sách
        lvMonHoc.setOnItemClickListener((parent, view, position, id) -> {

            // Lấy môn học được chọn
            MonHoc monHoc = list.get(position);

            // Chuyển sang màn hình quản lý môn học
            Intent intent = new Intent(DanhSachMonHocActivity.this, QuanLyMonHocActivity.class);

            // Truyền dữ liệu môn học sang màn hình bên kia
            intent.putExtra("maMon", monHoc.getMaMon());
            intent.putExtra("tenMon", monHoc.getTenMon());
            intent.putExtra("soTinChi", monHoc.getSoTinChi());

            startActivity(intent);
        });
    }

    // Hàm ánh xạ các control từ file XML
    private void anhXa() {
        edtTimMonHoc = findViewById(R.id.edtTimMonHoc);
        btnTimMonHoc = findViewById(R.id.btnTimMonHoc);
        btnThemMonHocMoi = findViewById(R.id.btnThemMonHocMoi);
        lvMonHoc = findViewById(R.id.lvMonHoc);
        tvSoLuongMonHoc = findViewById(R.id.tvSoLuongMonHoc);
    }

    // Hàm load toàn bộ dữ liệu môn học từ database lên ListView
    private void loadData() {
        // Xóa dữ liệu cũ trong list
        list.clear();

        // Lấy toàn bộ môn học từ database
        list.addAll(monHocDAO.getAll());

        // Nếu adapter chưa có thì tạo mới
        if (adapter == null) {
            adapter = new MonHocAdapter(this, list);
            lvMonHoc.setAdapter(adapter);
        } else {
            // Nếu đã có rồi thì chỉ cần cập nhật lại giao diện
            adapter.notifyDataSetChanged();
        }

        // Hiển thị tổng số môn học
        tvSoLuongMonHoc.setText("Tổng số môn học: " + list.size());
    }

    // Hàm tìm kiếm môn học theo từ khóa
    private void timKiemMonHoc() {
        // Lấy từ khóa người dùng nhập
        String key = edtTimMonHoc.getText().toString().trim();

        // Xóa danh sách cũ
        list.clear();

        // Nếu không nhập gì thì lấy tất cả môn học
        if (key.isEmpty()) {
            list.addAll(monHocDAO.getAll());
        } else {
            // Nếu có từ khóa thì tìm kiếm theo key
            list.addAll(monHocDAO.search(key));
        }

        // Cập nhật giao diện ListView
        adapter.notifyDataSetChanged();

        // Cập nhật lại tổng số môn học
        tvSoLuongMonHoc.setText("Tổng số môn học: " + list.size());
    }

    // Hàm onResume chạy khi quay lại màn hình này
    // Dùng để tải lại dữ liệu mới nhất sau khi thêm / sửa / xóa
    @Override
    protected void onResume() {
        super.onResume();
        loadData();
    }
}