package com.example.quanylysinhvien.admin;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.quanylysinhvien.R;
import com.example.quanylysinhvien.dao.DaoTaiKhoan;
import com.example.quanylysinhvien.dao.SinhVienDao;
import com.example.quanylysinhvien.model.TaikhoanMatKhau;

import java.util.ArrayList;
import java.util.HashMap;

public class QuanLyTaiKhoanActivity extends AppCompatActivity {

    TextView tvTitleTaiKhoan, tvThongBaoTaiKhoan;

    Button btnThemTaiKhoan;

    ListView lvTaiKhoan;

    DaoTaiKhoan daoTaiKhoan;
    SinhVienDao sinhVienDao;

    ArrayList<HashMap<String, String>> list;
    SimpleAdapter adapter;

    String tenTaiKhoanChon = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quan_ly_tai_khoan);

        anhXa();

        daoTaiKhoan = new DaoTaiKhoan(this);
        sinhVienDao = new SinhVienDao(this);

        loadTaiKhoan();

        btnThemTaiKhoan.setOnClickListener(v -> showDialogThemTaiKhoan());

        lvTaiKhoan.setOnItemClickListener((parent, view, position, id) -> {
            tenTaiKhoanChon = list.get(position).get("tkRaw");
            xuLyChonTaiKhoan();
        });
    }

    private void anhXa() {
        tvTitleTaiKhoan = findViewById(R.id.tvTitleTaiKhoan);
        tvThongBaoTaiKhoan = findViewById(R.id.tvThongBaoTaiKhoan);
        btnThemTaiKhoan = findViewById(R.id.btnThemTaiKhoan);
        lvTaiKhoan = findViewById(R.id.lvTaiKhoan);
    }

    private void loadTaiKhoan() {
        list = new ArrayList<>();
        ArrayList<TaikhoanMatKhau> dsTaiKhoan = daoTaiKhoan.getAll();
        for (TaikhoanMatKhau tk : dsTaiKhoan) {
            HashMap<String, String> map = new HashMap<>();

            map.put("tkRaw", tk.getTenTaiKhoan());

            map.put("tenTaiKhoan", "Tài khoản: " + tk.getTenTaiKhoan());
            map.put("matKhau", "Mật khẩu: " + tk.getMatKhau());
            map.put("vaiTro", "Vai trò: " + tk.getVaiTro());
            map.put("maSv", "Mã SV: " + (tk.getMaSv() == null ? "Chưa gán" : tk.getMaSv()));

            list.add(map);
        }

        tvThongBaoTaiKhoan.setText("Tổng tài khoản: " + list.size());

        adapter = new SimpleAdapter(
                this,
                list,
                R.layout.dong_tai_khoan,
                new String[]{"tenTaiKhoan", "matKhau", "vaiTro", "maSv"},
                new int[]{R.id.tvTenTaiKhoan, R.id.tvMatKhau, R.id.tvVaiTro, R.id.tvMaSvTaiKhoan}
        );

        lvTaiKhoan.setAdapter(adapter);
    }

    private void xuLyChonTaiKhoan() {
        String[] chucNang;

        if (tenTaiKhoanChon.equalsIgnoreCase("admin")) {
            chucNang = new String[]{"Sửa tài khoản", "Đặt lại mật khẩu = 123"};
        } else {
            chucNang = new String[]{"Sửa tài khoản", "Xóa tài khoản", "Đặt lại mật khẩu = 123"};
        }

        new AlertDialog.Builder(this)
                .setTitle("Chọn chức năng")
                .setItems(chucNang, (dialog, which) -> {
                    if (tenTaiKhoanChon.equalsIgnoreCase("admin")) {
                        if (which == 0) showDialogSuaTaiKhoan(tenTaiKhoanChon);
                        else resetMatKhau();
                    } else {
                        if (which == 0) showDialogSuaTaiKhoan(tenTaiKhoanChon);
                        else if (which == 1) xoaTaiKhoan();
                        else resetMatKhau();
                    }
                })
                .show();
    }

    private void showDialogThemTaiKhoan() {
        View view = LayoutInflater.from(this).inflate(R.layout.dialog_tai_khoan, null);

        EditText edtTenTaiKhoan = view.findViewById(R.id.edtTenTaiKhoan);
        EditText edtMatKhau = view.findViewById(R.id.edtMatKhau);
        Spinner spVaiTro = view.findViewById(R.id.spVaiTro);
        Spinner spSinhVien = view.findViewById(R.id.spSinhVien);

        ArrayAdapter<String> vaiTroAdapter = new ArrayAdapter<>(
                this, android.R.layout.simple_spinner_item, new String[]{"ADMIN", "USER"});
        vaiTroAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spVaiTro.setAdapter(vaiTroAdapter);

        // Spinner sinh viên
        ArrayList<String> dsSV = new ArrayList<>();
        dsSV.add("Không gán");
        dsSV.addAll(sinhVienDao.getDanhSachSpinner());

        ArrayAdapter<String> svAdapter = new ArrayAdapter<>(
                this, android.R.layout.simple_spinner_item, dsSV);
        svAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spSinhVien.setAdapter(svAdapter);

        AlertDialog dialog = new AlertDialog.Builder(this)
                .setTitle("Thêm tài khoản")
                .setView(view)
                .setPositiveButton("Thêm", null)
                .setNegativeButton("Hủy", null)
                .create();

        dialog.show();

        dialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(v -> {
            String tenTaiKhoan = edtTenTaiKhoan.getText().toString().trim();
            String matKhau = edtMatKhau.getText().toString().trim();
            String vaiTro = spVaiTro.getSelectedItem().toString();

            String maSv = null;
            String sinhVienChon = spSinhVien.getSelectedItem().toString();
            if (!"Không gán".equals(sinhVienChon)) {
                maSv = sinhVienChon.split(" - ")[0];
            }

            // Kiểm tra dữ liệu
            if (TextUtils.isEmpty(tenTaiKhoan) || TextUtils.isEmpty(matKhau)) {
                Toast.makeText(this, "Nhập đầy đủ thông tin", Toast.LENGTH_SHORT).show();
                return;
            }

            // Không cho trùng tài khoản
            if (daoTaiKhoan.kiemTraTonTai(tenTaiKhoan)) {
                Toast.makeText(this, "Tài khoản đã tồn tại", Toast.LENGTH_SHORT).show();
                return;
            }

            // USER bắt buộc phải gán sinh viên
            if ("USER".equals(vaiTro) && maSv == null) {
                Toast.makeText(this, "Tài khoản USER phải gán sinh viên", Toast.LENGTH_SHORT).show();
                return;
            }

            // ADMIN thì không cần mã sinh viên
            if ("ADMIN".equals(vaiTro)) {
                maSv = null;
            }

            boolean kq = daoTaiKhoan.them(new TaikhoanMatKhau(tenTaiKhoan, matKhau, vaiTro, maSv));

            if (kq) {
                Toast.makeText(this, "Thêm tài khoản thành công", Toast.LENGTH_SHORT).show();
                loadTaiKhoan();
                dialog.dismiss();
            } else {
                Toast.makeText(this, "Thêm tài khoản thất bại", Toast.LENGTH_SHORT).show();
            }
        });
    }

    // Hiển thị dialog sửa tài khoản
    private void showDialogSuaTaiKhoan(String tenTaiKhoan) {
        TaikhoanMatKhau tk = daoTaiKhoan.getTaiKhoan(tenTaiKhoan);

        if (tk == null) {
            Toast.makeText(this, "Không tìm thấy tài khoản", Toast.LENGTH_SHORT).show();
            return;
        }

        View view = LayoutInflater.from(this).inflate(R.layout.dialog_tai_khoan, null);

        EditText edtTenTaiKhoan = view.findViewById(R.id.edtTenTaiKhoan);
        EditText edtMatKhau = view.findViewById(R.id.edtMatKhau);
        Spinner spVaiTro = view.findViewById(R.id.spVaiTro);
        Spinner spSinhVien = view.findViewById(R.id.spSinhVien);

        // Đổ dữ liệu cũ lên form
        edtTenTaiKhoan.setText(tk.getTenTaiKhoan());
        edtTenTaiKhoan.setEnabled(false); // không cho sửa username
        edtMatKhau.setText(tk.getMatKhau());

        ArrayAdapter<String> vaiTroAdapter = new ArrayAdapter<>(
                this, android.R.layout.simple_spinner_item, new String[]{"ADMIN", "USER"});
        vaiTroAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spVaiTro.setAdapter(vaiTroAdapter);
        spVaiTro.setSelection("ADMIN".equalsIgnoreCase(tk.getVaiTro()) ? 0 : 1);

        ArrayList<String> dsSV = new ArrayList<>();
        dsSV.add("Không gán");
        dsSV.addAll(sinhVienDao.getDanhSachSpinner());

        ArrayAdapter<String> svAdapter = new ArrayAdapter<>(
                this, android.R.layout.simple_spinner_item, dsSV);
        svAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spSinhVien.setAdapter(svAdapter);

        // Chọn đúng sinh viên đang gán
        if (tk.getMaSv() == null) {
            spSinhVien.setSelection(0);
        } else {
            for (int i = 0; i < dsSV.size(); i++) {
                if (dsSV.get(i).startsWith(tk.getMaSv() + " - ")) {
                    spSinhVien.setSelection(i);
                    break;
                }
            }
        }

        AlertDialog dialog = new AlertDialog.Builder(this)
                .setTitle("Sửa tài khoản")
                .setView(view)
                .setPositiveButton("Lưu", null)
                .setNegativeButton("Hủy", null)
                .create();

        dialog.show();

        dialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(v -> {
            String matKhauMoi = edtMatKhau.getText().toString().trim();
            String vaiTroMoi = spVaiTro.getSelectedItem().toString();

            String maSvMoi = null;
            String sinhVienChon = spSinhVien.getSelectedItem().toString();
            if (!"Không gán".equals(sinhVienChon)) {
                maSvMoi = sinhVienChon.split(" - ")[0];
            }

            if (TextUtils.isEmpty(matKhauMoi)) {
                Toast.makeText(this, "Mật khẩu không được để trống", Toast.LENGTH_SHORT).show();
                return;
            }

            if ("USER".equals(vaiTroMoi) && maSvMoi == null) {
                Toast.makeText(this, "Tài khoản USER phải gán sinh viên", Toast.LENGTH_SHORT).show();
                return;
            }

            if ("ADMIN".equals(vaiTroMoi)) {
                maSvMoi = null;
            }

            TaikhoanMatKhau tkMoi = new TaikhoanMatKhau(
                    tk.getTenTaiKhoan(),
                    matKhauMoi,
                    vaiTroMoi,
                    maSvMoi
            );

            boolean kq = daoTaiKhoan.sua(tkMoi);

            if (kq) {
                Toast.makeText(this, "Cập nhật thành công", Toast.LENGTH_SHORT).show();
                loadTaiKhoan();
                dialog.dismiss();
            } else {
                Toast.makeText(this, "Cập nhật thất bại", Toast.LENGTH_SHORT).show();
            }
        });
    }

    // Xóa tài khoản đang chọn
    private void xoaTaiKhoan() {
        boolean kq = daoTaiKhoan.xoaTaiKhoan(tenTaiKhoanChon);

        if (kq) {
            Toast.makeText(this, "Xóa thành công", Toast.LENGTH_SHORT).show();
            loadTaiKhoan();
        } else {
            Toast.makeText(this, "Xóa thất bại", Toast.LENGTH_SHORT).show();
        }
    }

    // Đặt lại mật khẩu tài khoản đang chọn về 123
    private void resetMatKhau() {
        boolean kq = daoTaiKhoan.doiMatKhau(tenTaiKhoanChon, "123");

        if (kq) {
            Toast.makeText(this, "Đặt lại mật khẩu thành 123", Toast.LENGTH_SHORT).show();
            loadTaiKhoan();
        } else {
            Toast.makeText(this, "Thất bại", Toast.LENGTH_SHORT).show();
        }
    }
}