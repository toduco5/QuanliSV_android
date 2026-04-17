package com.example.quanylysinhvien.user;

import androidx.appcompat.app.AppCompatActivity;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.example.quanylysinhvien.R;
import com.example.quanylysinhvien.database.DBHelper;

import java.util.ArrayList;
import java.util.HashMap;

public class XemLopActivity extends AppCompatActivity {

    TextView tvTitleLop, tvThongBaoLop;
    ListView lvLopUser;

    DBHelper dbHelper;
    ArrayList<HashMap<String, String>> list;
    SimpleAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_xem_lop);

        anhXa();
        dbHelper = new DBHelper(this);
        loadLop();
    }

    private void anhXa() {
        tvTitleLop = findViewById(R.id.tvTitleLop);
        tvThongBaoLop = findViewById(R.id.tvThongBaoLop);
        lvLopUser = findViewById(R.id.lvLopUser);
    }

    private void loadLop() {
        list = new ArrayList<>();
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        Cursor c = db.rawQuery("SELECT maLop, tenLop FROM LOP", null);

        if (c.moveToFirst()) {
            do {
                HashMap<String, String> map = new HashMap<>();
                map.put("maLop", "Mã lớp: " + c.getString(0));
                map.put("tenLop", "Tên lớp: " + c.getString(1));
                list.add(map);
            } while (c.moveToNext());

            tvThongBaoLop.setText("Tổng số lớp: " + list.size());
        } else {
            tvThongBaoLop.setText("Chưa có dữ liệu lớp.");
        }

        c.close();
        db.close();

        adapter = new SimpleAdapter(
                this,
                list,
                R.layout.dong_xem_lop_user,
                new String[]{"maLop", "tenLop"},
                new int[]{R.id.tvMaLopUser, R.id.tvTenLopUser}
        );

        lvLopUser.setAdapter(adapter);
    }
}