package com.example.quanylysinhvien.admin;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.quanylysinhvien.R;
import com.example.quanylysinhvien.dao.LopDAO;
import com.example.quanylysinhvien.dao.NganhDAO;
import com.example.quanylysinhvien.dao.SinhVienDao;
import com.example.quanylysinhvien.model.SinhVien;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class ThemSinhVienActivity extends AppCompatActivity {

    EditText edtTenSv, edtMaSv, edtEmail, edtHinh;
    Spinner spMaLop, spMaNganh;
    Button btnThem, btnNhapLai, btnDanhSach, btnReview;

    SinhVienDao sinhVienDao;
    LopDAO lopDAO;
    NganhDAO nganhDAO;

    LinearLayout linearLayout;
    CircleImageView imgAvata;

    ArrayList<String> dsNganh = new ArrayList<>();
    ArrayList<String> dsLop = new ArrayList<>();

    Animation animation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_them_sinh_vien);

        initView();

        sinhVienDao = new SinhVienDao(this);
        lopDAO = new LopDAO(this);
        nganhDAO = new NganhDAO(this);

        animation = AnimationUtils.loadAnimation(this, R.anim.uptodowndiagonal);
        linearLayout.setAnimation(animation);

        loadSpinnerNganh();

        spMaNganh.setOnItemSelectedListener(
                new android.widget.AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(android.widget.AdapterView<?> parent,
                                               android.view.View view,
                                               int position,
                                               long id) {
                        loadSpinnerLopTheoNganh();
                    }

                    @Override
                    public void onNothingSelected(android.widget.AdapterView<?> parent) {
                    }
                });

        btnThem.setOnClickListener(v -> themSinhVien());

        btnNhapLai.setOnClickListener(v -> resetForm());

        btnDanhSach.setOnClickListener(v -> {
            startActivity(new Intent(
                    ThemSinhVienActivity.this,
                    DanhSachSinhVienActivity.class
            ));
        });

        btnReview.setOnClickListener(v -> xemAnh());
    }

    private void initView() {

        linearLayout = findViewById(R.id.linearLayout);

        edtMaSv = findViewById(R.id.txtMaSV);
        edtTenSv = findViewById(R.id.txtTenSV);
        edtEmail = findViewById(R.id.txtemail);
        edtHinh = findViewById(R.id.txtHinh);

        spMaLop = findViewById(R.id.txtMalop);
        spMaNganh = findViewById(R.id.spMaNganh);

        btnThem = findViewById(R.id.btntThem);
        btnNhapLai = findViewById(R.id.btnNhapLai);
        btnDanhSach = findViewById(R.id.btnDanhSach);
        btnReview = findViewById(R.id.btnReviewThem);

        imgAvata = findViewById(R.id.imgAvata);
    }

    // =============================
    // LOAD SPINNER NGÀNH
    // =============================
    private void loadSpinnerNganh() {

        dsNganh = nganhDAO.getDanhSachSpinner();

        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_spinner_item,
                dsNganh
        );

        adapter.setDropDownViewResource(
                android.R.layout.simple_spinner_dropdown_item
        );

        spMaNganh.setAdapter(adapter);
    }

    // =============================
    // LOAD LỚP THEO NGÀNH
    // =============================
    private void loadSpinnerLopTheoNganh() {

        if (spMaNganh.getSelectedItem() == null) return;

        String item = spMaNganh.getSelectedItem().toString();
        String maNganh = item.split(" - ")[0];

        dsLop = lopDAO.getDanhSachLopTheoNganh(maNganh);

        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_spinner_item,
                dsLop
        );

        adapter.setDropDownViewResource(
                android.R.layout.simple_spinner_dropdown_item
        );

        spMaLop.setAdapter(adapter);

        if (dsLop.size() == 0) {
            Toast.makeText(this,
                    "Ngành này chưa có lớp",
                    Toast.LENGTH_SHORT).show();
        }
    }

    // =============================
    // THÊM SINH VIÊN
    // =============================
    private void themSinhVien() {

        String ma = edtMaSv.getText().toString().trim();
        String ten = edtTenSv.getText().toString().trim();
        String email = edtEmail.getText().toString().trim();
        String hinh = edtHinh.getText().toString().trim();

        if (ma.isEmpty()) {
            edtMaSv.setError("Nhập mã sinh viên");
            edtMaSv.requestFocus();
            return;
        }

        if (ten.isEmpty()) {
            edtTenSv.setError("Nhập tên sinh viên");
            edtTenSv.requestFocus();
            return;
        }

        if (email.isEmpty()) {
            edtEmail.setError("Nhập email");
            edtEmail.requestFocus();
            return;
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            edtEmail.setError("Email không hợp lệ");
            edtEmail.requestFocus();
            return;
        }

        if (spMaNganh.getSelectedItem() == null) {
            Toast.makeText(this,
                    "Chọn ngành",
                    Toast.LENGTH_SHORT).show();
            return;
        }

        if (spMaLop.getSelectedItem() == null) {
            Toast.makeText(this,
                    "Chọn lớp",
                    Toast.LENGTH_SHORT).show();
            return;
        }

        if (hinh.isEmpty()) {
            hinh = "avatamacdinh";
        }

        String maNganh =
                spMaNganh.getSelectedItem().toString().split(" - ")[0];

        String maLop =
                spMaLop.getSelectedItem().toString().split(" - ")[0];

        SinhVien s = new SinhVien(
                ma,
                ten,
                email,
                hinh,
                maLop,
                maNganh
        );

        boolean kq = sinhVienDao.insert(s);

        if (kq) {

            Toast.makeText(this,
                    "Thêm sinh viên thành công",
                    Toast.LENGTH_SHORT).show();

            resetForm();

        } else {

            Toast.makeText(this,
                    "Mã sinh viên đã tồn tại",
                    Toast.LENGTH_SHORT).show();
        }
    }

    // =============================
    // RESET FORM
    // =============================
    private void resetForm() {

        edtMaSv.setText("");
        edtTenSv.setText("");
        edtEmail.setText("");
        edtHinh.setText("");

        if (spMaNganh.getAdapter() != null) {
            spMaNganh.setSelection(0);
        }

        imgAvata.setImageResource(R.drawable.avatamacdinh);

        edtMaSv.requestFocus();
    }

    // =============================
    // XEM ẢNH
    // =============================
    private void xemAnh() {

        String tenHinh = edtHinh.getText().toString().trim();

        if (tenHinh.isEmpty()) {
            imgAvata.setImageResource(R.drawable.avatamacdinh);
            return;
        }

        int id = getResources().getIdentifier(
                tenHinh,
                "drawable",
                getPackageName()
        );

        if (id != 0) {

            imgAvata.setImageResource(id);

        } else {

            imgAvata.setImageResource(R.drawable.avatamacdinh);

            Toast.makeText(this,
                    "Không tìm thấy ảnh",
                    Toast.LENGTH_SHORT).show();
        }
    }
}