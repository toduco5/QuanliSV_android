package com.example.quanylysinhvien.admin;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.quanylysinhvien.R;
import com.example.quanylysinhvien.dao.DiemDAO;
import com.example.quanylysinhvien.model.Diem;

public class QuanLyDiemActivity extends AppCompatActivity {

    EditText edtMaSv, edtMaMon, edtDiem;
    Button btnThem;
    DiemDAO diemDAO;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_them_diem);

        edtMaSv = findViewById(R.id.edtMaSv);
        edtMaMon = findViewById(R.id.edtMaMon);
        edtDiem = findViewById(R.id.edtDiem);
        btnThem = findViewById(R.id.btnThemDiem);

        diemDAO = new DiemDAO(this);

        btnThem.setOnClickListener(v -> {
            String maSv = edtMaSv.getText().toString().trim();
            String maMon = edtMaMon.getText().toString().trim();
            String sDiem = edtDiem.getText().toString().trim();

            if (maSv.isEmpty() || maMon.isEmpty() || sDiem.isEmpty()) {
                Toast.makeText(this, "Nhập đầy đủ thông tin", Toast.LENGTH_SHORT).show();
                return;
            }

            float diem;
            try {
                diem = Float.parseFloat(sDiem);
            } catch (Exception e) {
                Toast.makeText(this, "Điểm không hợp lệ", Toast.LENGTH_SHORT).show();
                return;
            }

            if (diem < 0 || diem > 10) {
                Toast.makeText(this, "Điểm phải từ 0 đến 10", Toast.LENGTH_SHORT).show();
                return;
            }

            boolean kq = diemDAO.insert(new Diem(maSv, maMon, diem));
            if (kq) {
                Toast.makeText(this, "Thêm điểm thành công", Toast.LENGTH_SHORT).show();
                edtMaSv.setText("");
                edtMaMon.setText("");
                edtDiem.setText("");
            } else {
                Toast.makeText(this, "Thêm thất bại", Toast.LENGTH_SHORT).show();
            }
        });
    }
}