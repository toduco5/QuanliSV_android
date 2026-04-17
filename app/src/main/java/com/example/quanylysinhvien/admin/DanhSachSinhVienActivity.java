package com.example.quanylysinhvien.admin;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.quanylysinhvien.R;
import com.example.quanylysinhvien.adapter.SinhVienAdapter;
import com.example.quanylysinhvien.database.DBHelper;
import com.example.quanylysinhvien.model.SinhVien;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class DanhSachSinhVienActivity extends AppCompatActivity {

    TextView tvTitleSinhVien, tvThongBaoSinhVien;
    ListView lvSinhVien;

    DBHelper dbHelper;
    ArrayList<SinhVien> dsSinhVien;
    SinhVienAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_danh_sach_sinh_vien);

        anhXa();
        dbHelper = new DBHelper(this);
        loadSinhVien();
    }

    private void anhXa() {
        tvTitleSinhVien = findViewById(R.id.tvTitleSinhVien);
        tvThongBaoSinhVien = findViewById(R.id.tvThongBaoSinhVien);
        lvSinhVien = findViewById(R.id.lvSinhVien);
    }

    private void loadSinhVien() {
        dsSinhVien = new ArrayList<>();

        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor c = db.rawQuery(
                "SELECT maSv, tenSV, email, hinh, maLop, maNganh FROM SINHVIEN",
                null
        );

        if (c.moveToFirst()) {
            do {
                String maSv = c.getString(0);
                String tenSv = c.getString(1);
                String email = c.getString(2);
                String hinh = c.getString(3);
                String maLop = c.getString(4);
                String maNganh = c.getString(5);

                dsSinhVien.add(new SinhVien(maSv, tenSv, email, hinh, maLop, maNganh));
            } while (c.moveToNext());
        }

        c.close();
        db.close();

        tvThongBaoSinhVien.setText("Tổng sinh viên: " + dsSinhVien.size());

        adapter = new SinhVienAdapter(this, dsSinhVien, new SinhVienAdapter.OnSinhVienActionListener() {
            @Override
            public void onSua(SinhVien sv) {
                suaSinhVien(sv);
            }

            @Override
            public void onReload() {
                loadSinhVien();
            }
        });

        lvSinhVien.setAdapter(adapter);
    }

    private void suaSinhVien(SinhVien sv) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View view = getLayoutInflater().inflate(R.layout.dialog_sua_sinhvien, null);
        builder.setView(view);

        AlertDialog dialog = builder.create();
        dialog.show();

        EditText edMaSvedit = view.findViewById(R.id.edMaSvedit);
        EditText edTenSvedit = view.findViewById(R.id.edTenSvedit);
        EditText edemail = view.findViewById(R.id.edemail);
        EditText edhinh = view.findViewById(R.id.edhinh);
        Spinner spEdmalop = view.findViewById(R.id.spEdmalop);
        Button btnEdSua = view.findViewById(R.id.btnEdSua);
        Button btnEdNhapLai = view.findViewById(R.id.btnEdNhapLai);
        Button btnHuyeditSV = view.findViewById(R.id.btnHuyeditSV);
        Button btnReviewSuaSV = view.findViewById(R.id.btnReviewSuaSV);
        CircleImageView imageView_avata_edit = view.findViewById(R.id.imageView_avata_edit);

        ArrayList<String> dsLop = new ArrayList<>();
        ArrayAdapter<String> adapterLop = new ArrayAdapter<>(
                this,
                android.R.layout.simple_spinner_item,
                dsLop
        );
        adapterLop.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spEdmalop.setAdapter(adapterLop);

        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cLop = db.rawQuery("SELECT maLop FROM LOP", null);

        if (cLop.moveToFirst()) {
            do {
                dsLop.add(cLop.getString(0));
            } while (cLop.moveToNext());
        }
        cLop.close();
        db.close();
        adapterLop.notifyDataSetChanged();

        edMaSvedit.setText(sv.getMaSv());
        edTenSvedit.setText(sv.getTenSv());
        edemail.setText(sv.getEmail());
        edhinh.setText(sv.getHinh());
        edMaSvedit.setEnabled(false);

        int viTri = dsLop.indexOf(sv.getMaLop());
        if (viTri >= 0) {
            spEdmalop.setSelection(viTri);
        }

        setPreviewAvatar(imageView_avata_edit, sv.getHinh());

        btnReviewSuaSV.setOnClickListener(v -> {
            String tenHinh = chuanHoaTenHinh(edhinh.getText().toString());
            int resId = getResources().getIdentifier(tenHinh, "drawable", getPackageName());

            if (resId != 0) {
                imageView_avata_edit.setImageResource(resId);
            } else {
                imageView_avata_edit.setImageResource(R.drawable.avatamacdinh);
                Toast.makeText(this, "Không tìm thấy hình", Toast.LENGTH_SHORT).show();
            }
        });

        btnEdNhapLai.setOnClickListener(v -> {
            edMaSvedit.setText(sv.getMaSv());
            edTenSvedit.setText(sv.getTenSv());
            edemail.setText(sv.getEmail());
            edhinh.setText(sv.getHinh());

            int vt = dsLop.indexOf(sv.getMaLop());
            if (vt >= 0) {
                spEdmalop.setSelection(vt);
            }

            setPreviewAvatar(imageView_avata_edit, sv.getHinh());
        });

        btnHuyeditSV.setOnClickListener(v -> dialog.dismiss());

        btnEdSua.setOnClickListener(v -> {
            String tenMoi = edTenSvedit.getText().toString().trim();
            String emailMoi = edemail.getText().toString().trim();
            String hinhMoi = chuanHoaTenHinh(edhinh.getText().toString());
            String lopMoi = spEdmalop.getSelectedItem() != null ? spEdmalop.getSelectedItem().toString() : "";

            if (tenMoi.isEmpty() || emailMoi.isEmpty() || lopMoi.isEmpty()) {
                Toast.makeText(this, "Vui lòng nhập đầy đủ thông tin", Toast.LENGTH_SHORT).show();
                return;
            }

            SQLiteDatabase dbUpdate = dbHelper.getWritableDatabase();
            ContentValues values = new ContentValues();
            values.put("tenSV", tenMoi);
            values.put("email", emailMoi);
            values.put("hinh", hinhMoi);
            values.put("maLop", lopMoi);
            values.put("maNganh", sv.getMaNganh());

            int kq = dbUpdate.update("SINHVIEN", values, "maSv=?", new String[]{sv.getMaSv()});
            dbUpdate.close();

            if (kq > 0) {
                Toast.makeText(this, "Sửa sinh viên thành công", Toast.LENGTH_SHORT).show();
                dialog.dismiss();
                loadSinhVien();
            } else {
                Toast.makeText(this, "Sửa sinh viên thất bại", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void setPreviewAvatar(CircleImageView imageView, String tenHinh) {
        String fileName = chuanHoaTenHinh(tenHinh);
        int resId = getResources().getIdentifier(fileName, "drawable", getPackageName());

        if (resId != 0) {
            imageView.setImageResource(resId);
        } else {
            imageView.setImageResource(R.drawable.avatamacdinh);
        }
    }

    private String chuanHoaTenHinh(String input) {
        if (input == null) return "";

        String s = input.trim().toLowerCase();

        if (s.endsWith(".png")) {
            s = s.substring(0, s.length() - 4);
        } else if (s.endsWith(".jpg")) {
            s = s.substring(0, s.length() - 4);
        } else if (s.endsWith(".jpeg")) {
            s = s.substring(0, s.length() - 5);
        }

        s = s.replace(" ", "_");
        return s;
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadSinhVien();
    }
}