package com.example.quanylysinhvien.admin;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import com.example.quanylysinhvien.R;
import com.example.quanylysinhvien.adapter.DiemAdapter;
import com.example.quanylysinhvien.dao.DiemDAO;
import com.example.quanylysinhvien.model.Diem;

import java.util.ArrayList;

// màn hình danh sách điểm
// chức năng: xem điểm, tìm điểm, mở màn hình thêm điểm
public class DanhSachDiemActivity extends AppCompatActivity {

    // ô nhập từ khóa tìm kiếm
    EditText edtTimDiem;

    // nút tìm và nút mở màn hình thêm điểm
    Button btnTimDiem, btnThemDiemMoi;

    // listview hiển thị danh sách điểm
    ListView lvDiem;

    // danh sách điểm
    ArrayList<Diem> list;

    // adapter hiển thị điểm
    DiemAdapter adapter;

    // DAO xử lý database điểm
    DiemDAO diemDAO;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_danh_sach_diem);

        // ánh xạ control từ XML
        edtTimDiem = findViewById(R.id.edtTimDiem);
        btnTimDiem = findViewById(R.id.btnTimDiem);
        btnThemDiemMoi = findViewById(R.id.btnThemDiemMoi);
        lvDiem = findViewById(R.id.lvDiem);

        // khởi tạo DAO
        diemDAO = new DiemDAO(this);

        // load toàn bộ dữ liệu điểm
        loadData();

        // bấm nút tìm kiếm
        btnTimDiem.setOnClickListener(v -> {

            // lấy từ khóa người dùng nhập
            String key = edtTimDiem.getText().toString().trim();

            // nếu ô tìm kiếm rỗng thì load lại toàn bộ
            if (key.isEmpty()) {
                loadData();
            } else {

                // xóa list cũ
                list.clear();

                // lấy dữ liệu tìm kiếm từ database
                list.addAll(diemDAO.search(key));

                // cập nhật lại ListView
                adapter.notifyDataSetChanged();
            }
        });

        // bấm nút thêm điểm mới
        btnThemDiemMoi.setOnClickListener(v -> {

            // mở màn hình quản lý điểm
            startActivity(
                    new Intent(
                            DanhSachDiemActivity.this,
                            QuanLyDiemActivity.class
                    )
            );
        });
    }

    // hàm load dữ liệu điểm từ database lên ListView
    private void loadData() {

        // lấy toàn bộ điểm
        list = diemDAO.getAll();

        // tạo adapter
        adapter = new DiemAdapter(this, list);

        // gắn adapter vào listview
        lvDiem.setAdapter(adapter);
    }

    @Override
    protected void onResume() {
        super.onResume();

        // khi quay lại màn hình thì load lại dữ liệu mới
        loadData();
    }
}