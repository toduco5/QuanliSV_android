package com.example.quanylysinhvien.adapter;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.quanylysinhvien.R;
import com.example.quanylysinhvien.dao.SinhVienDao;
import com.example.quanylysinhvien.model.SinhVien;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class SinhVienAdapter extends BaseAdapter implements Filterable {

    Context context;
    ArrayList<SinhVien> ds;
    ArrayList<SinhVien> dsGoc;
    SinhVienDao dao;

    public SinhVienAdapter(Context context, ArrayList<SinhVien> ds) {
        this.context = context;
        this.ds = ds;
        this.dsGoc = new ArrayList<>(ds);
        dao = new SinhVienDao(context);
    }

    @Override
    public int getCount() {
        return ds.size();
    }

    @Override
    public Object getItem(int position) {
        return ds.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    class ViewHolder {
        TextView tvMa, tvTen, tvEmail, tvLop;
        CircleImageView img;
        ImageView btnXoa;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder holder;

        if (convertView == null) {
            holder = new ViewHolder();

            convertView = LayoutInflater.from(context)
                    .inflate(R.layout.dong_sinhvien, parent, false);

            holder.tvMa = convertView.findViewById(R.id.tvMaSV);
            holder.tvTen = convertView.findViewById(R.id.tvTenSV);
            holder.tvEmail = convertView.findViewById(R.id.tvEmailSV);
            holder.tvLop = convertView.findViewById(R.id.tvLopSV);
            holder.img = convertView.findViewById(R.id.imageViewHinh);
            holder.btnXoa = convertView.findViewById(R.id.imageViewDelete);

            convertView.setTag(holder);

        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        SinhVien s = ds.get(position);

        holder.tvMa.setText(s.getMaSv());
        holder.tvTen.setText(s.getTenSv());
        holder.tvEmail.setText(s.getEmail());
        holder.tvLop.setText(s.getMaLop());

        holder.btnXoa.setOnClickListener(v -> {
            dao.delete(s);
            ds.clear();
            ds.addAll(dao.getALL());
            notifyDataSetChanged();
        });

        return convertView;
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {

                ArrayList<SinhVien> loc = new ArrayList<>();

                if (constraint == null || constraint.length() == 0) {
                    loc.addAll(dsGoc);
                } else {
                    String key = constraint.toString().toLowerCase();

                    for (SinhVien sv : dsGoc) {
                        if (sv.getTenSv().toLowerCase().contains(key)) {
                            loc.add(sv);
                        }
                    }
                }

                FilterResults results = new FilterResults();
                results.values = loc;
                results.count = loc.size();
                return results;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                ds = (ArrayList<SinhVien>) results.values;
                notifyDataSetChanged();
            }
        };
    }
}