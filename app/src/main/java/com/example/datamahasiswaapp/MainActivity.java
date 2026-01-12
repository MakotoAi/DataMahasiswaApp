package com.example.datamahasiswaapp;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    RecyclerView rvMahasiswa;
    MahasiswaAdapter adapter;
    List<Mahasiswa> list = new ArrayList<>();

    EditText etCari;
    Button btnCari;
    ProgressBar progressBar;
    TextView tvError;



    String API_URL = "https://raw.githubusercontent.com/MakotoAi/api-mahasiswa/main/mahasiswa.json";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tvError = findViewById(R.id.tvError);
        rvMahasiswa = findViewById(R.id.rvMahasiswa);
        etCari = findViewById(R.id.etCari);
        btnCari = findViewById(R.id.btnCari);
        progressBar = findViewById(R.id.progressBar);
        progressBar.setVisibility(View.GONE);

        rvMahasiswa.setLayoutManager(new LinearLayoutManager(this));
        adapter = new MahasiswaAdapter(list);
        rvMahasiswa.setAdapter(adapter);
        adapter.notifyDataSetChanged();

        getDataMahasiswa("");

        btnCari.setOnClickListener(v ->
                getDataMahasiswa(etCari.getText().toString())
        );
    }

    void getDataMahasiswa(String keyword) {
        list.clear();
        adapter.notifyDataSetChanged();
        tvError.setVisibility(View.GONE);

        progressBar.setVisibility(View.VISIBLE);
        rvMahasiswa.setVisibility(View.GONE);
        etCari.setVisibility(View.GONE);
        btnCari.setVisibility(View.GONE);
        rvMahasiswa.setVisibility(View.VISIBLE);
        progressBar.setVisibility(View.VISIBLE);
        etCari.setVisibility(View.VISIBLE);
        btnCari.setVisibility(View.VISIBLE);
        tvError.setVisibility(View.GONE);
        rvMahasiswa.setVisibility(View.VISIBLE);
        adapter.notifyDataSetChanged();

        new Thread(() -> {
            try {
                URL url = new URL(API_URL);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("GET");
                conn.setConnectTimeout(15000);
                conn.setReadTimeout(15000);
                conn.connect();

                BufferedReader reader = new BufferedReader(
                        new InputStreamReader(conn.getInputStream())
                );

                StringBuilder json = new StringBuilder();
                String line;

                while ((line = reader.readLine()) != null) {
                    json.append(line);
                }

                reader.close();

                JSONArray array = new JSONArray(json.toString());
                list.clear();

                for (int i = 0; i < array.length(); i++) {
                    JSONObject obj = array.getJSONObject(i);
                    String nama = obj.getString("nama");

                    if (keyword.isEmpty() ||
                            nama.toLowerCase().contains(keyword.toLowerCase())) {

                        list.add(new Mahasiswa(
                                obj.getString("id"),
                                nama,
                                obj.getString("nim"),
                                obj.getString("jurusan")
                        ));
                    }
                }

                new Handler(Looper.getMainLooper()).post(() -> {
                    progressBar.setVisibility(View.GONE);
                    adapter.notifyDataSetChanged();

                    new Handler(Looper.getMainLooper()).post(() -> {
                        progressBar.setVisibility(View.GONE);
                        adapter.notifyDataSetChanged();

                        if (list.isEmpty()) {
                            rvMahasiswa.setVisibility(View.GONE);
                            tvError.setVisibility(View.VISIBLE);
                            tvError.setText("Data tidak ditemukan");
                        } else {
                            rvMahasiswa.setVisibility(View.VISIBLE);
                            tvError.setVisibility(View.GONE);
                        }
                    });
                });

            } catch (Exception e) {
                new Handler(Looper.getMainLooper()).post(() -> {
                    progressBar.setVisibility(View.GONE);
                    rvMahasiswa.setVisibility(View.GONE);
                    tvError.setVisibility(View.VISIBLE);
                    tvError.setText("Gagal mengambil data.\nPeriksa koneksi internet");

                    Toast.makeText(
                            MainActivity.this,
                            "Gagal mengambil data",
                            Toast.LENGTH_SHORT
                    ).show();
                });
            }
        }).start();
    }
}
