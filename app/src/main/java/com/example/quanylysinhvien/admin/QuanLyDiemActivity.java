package com.example.quanylysinhvien.admin;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.quanylysinhvien.R;
import com.example.quanylysinhvien.dao.DiemDAO;
import com.example.quanylysinhvien.dao.MonHocDAO;
import com.example.quanylysinhvien.dao.SinhVienDao;
import com.example.quanylysinhvien.model.Diem;

import java.util.ArrayList;

// màn hình quản lý điểm
// chức năng: chọn sinh viên, chọn môn học, nhập điểm và lưu vào database
public class QuanLyDiemActivity extends AppCompatActivity {

    // spinner chọn sinh viên
    Spinner spSinhVien;

    // spinner chọn môn học
    Spinner spMaMon;

    // ô nhập điểm
    EditText edtDiem;

    // nút thêm điểm
    Button btnThemDiem;

    // DAO xử lý bảng điểm
    DiemDAO diemDAO;

    // DAO xử lý sinh viên
    SinhVienDao sinhVienDao;

    // DAO xử lý môn học
    MonHocDAO monHocDAO;

    // danh sách sinh viên để đổ lên spinner
    ArrayList<String> dsSinhVien;

    // danh sách môn học để đổ lên spinner
    ArrayList<String> dsMonHoc;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_them_diem);

        // nối biến Java với giao diện XML
        anhXa();

        // khởi tạo DAO
        diemDAO = new DiemDAO(this);
        sinhVienDao = new SinhVienDao(this);
        monHocDAO = new MonHocDAO(this);

        // load danh sách sinh viên lên spinner
        loadSpinnerSinhVien();

        // khi đổi sinh viên thì load lại môn học tương ứng
        spSinhVien.setOnItemSelectedListener(new android.widget.AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(android.widget.AdapterView<?> parent, android.view.View view, int position, long id) {
                loadSpinnerMonHocTheoSinhVien();
            }

            @Override
            public void onNothingSelected(android.widget.AdapterView<?> parent) {
            }
        });

        // bấm nút thêm điểm thì gọi hàm xử lý
        btnThemDiem.setOnClickListener(v -> xuLyThemDiem());
    }

    // ánh xạ control từ XML
    private void anhXa() {
        spSinhVien = findViewById(R.id.spSinhVien);
        spMaMon = findViewById(R.id.spMaMon);
        edtDiem = findViewById(R.id.edtDiem);
        btnThemDiem = findViewById(R.id.btnThemDiem);
    }

    // load danh sách sinh viên lên spinner
    private void loadSpinnerSinhVien() {

        // lấy danh sách sinh viên từ database
        dsSinhVien = sinhVienDao.getDanhSachSpinner();

        // tạo adapter cho spinner sinh viên
        ArrayAdapter<String> adapterSinhVien = new ArrayAdapter<>(
                this,
                android.R.layout.simple_spinner_item,
                dsSinhVien
        );

        adapterSinhVien.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spSinhVien.setAdapter(adapterSinhVien);

        // sau khi có sinh viên thì load môn học luôn
        loadSpinnerMonHocTheoSinhVien();
    }

    // load môn học theo sinh viên đã chọn
    private void loadSpinnerMonHocTheoSinhVien() {

        // nếu chưa chọn sinh viên thì thoát
        if (spSinhVien.getSelectedItem() == null) return;

        // ví dụ spinner có dạng: SV01 - Nguyễn Văn A
        String sinhVienChon = spSinhVien.getSelectedItem().toString();

        // tách lấy mã sinh viên
        String maSv = sinhVienChon.split(" - ")[0];

        // lấy mã ngành của sinh viên
        String maNganh = sinhVienDao.getMaNganhTheoSinhVien(maSv);

        // nếu không có ngành thì danh sách môn rỗng
        if (maNganh == null) {
            dsMonHoc = new ArrayList<>();
        } else {
            // lấy các môn thuộc ngành đó
            dsMonHoc = monHocDAO.getDanhSachMonTheoNganh(maNganh);
        }

        // tạo adapter cho spinner môn học
        ArrayAdapter<String> adapterMonHoc = new ArrayAdapter<>(
                this,
                android.R.layout.simple_spinner_item,
                dsMonHoc
        );

        adapterMonHoc.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spMaMon.setAdapter(adapterMonHoc);
    }

    // xử lý thêm điểm
    private void xuLyThemDiem() {

        // nếu không có sinh viên để chọn
        if (spSinhVien.getSelectedItem() == null) {
            Toast.makeText(this, "Không có sinh viên để chọn", Toast.LENGTH_SHORT).show();
            return;
        }

        // nếu không có môn học để chọn
        if (spMaMon.getSelectedItem() == null) {
            Toast.makeText(this, "Không có môn học để chọn", Toast.LENGTH_SHORT).show();
            return;
        }

        // lấy sinh viên và môn học đang chọn
        String sinhVienChon = spSinhVien.getSelectedItem().toString();
        String monHocChon = spMaMon.getSelectedItem().toString();

        // tách lấy mã sinh viên và mã môn
        String maSv = sinhVienChon.split(" - ")[0];
        String maMon = monHocChon.split(" - ")[0];

        // lấy điểm nhập từ ô text
        String sDiem = edtDiem.getText().toString().trim();

        // kiểm tra rỗng
        if (sDiem.isEmpty()) {
            Toast.makeText(this, "Vui lòng nhập điểm", Toast.LENGTH_SHORT).show();
            edtDiem.requestFocus();
            return;
        }

        float diem;

        try {
            // đổi chuỗi sang số thực
            diem = Float.parseFloat(sDiem);
        } catch (Exception e) {
            Toast.makeText(this, "Điểm không hợp lệ", Toast.LENGTH_SHORT).show();
            return;
        }

        // kiểm tra điểm từ 0 đến 10
        if (diem < 0 || diem > 10) {
            Toast.makeText(this, "Điểm phải từ 0 đến 10", Toast.LENGTH_SHORT).show();
            return;
        }

        // thêm điểm vào database
        boolean kq = diemDAO.insert(new Diem(maSv, maMon, diem));

        if (kq) {
            Toast.makeText(this, "Thêm điểm thành công", Toast.LENGTH_SHORT).show();

            // xóa ô điểm sau khi thêm
            edtDiem.setText("");

            // đưa spinner sinh viên về dòng đầu
            spSinhVien.setSelection(0);
        } else {
            Toast.makeText(this, "Thêm thất bại hoặc môn này đã có điểm", Toast.LENGTH_SHORT).show();
        }
    }
}