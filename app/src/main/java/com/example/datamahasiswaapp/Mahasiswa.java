package com.example.datamahasiswaapp;

public class Mahasiswa {

    private String id;
    private String nama;
    private String nim;
    private String jurusan;

    public Mahasiswa(String id, String nama, String nim, String jurusan) {
        this.id = id;
        this.nama = nama;
        this.nim = nim;
        this.jurusan = jurusan;
    }

    public String getId() {
        return id;
    }

    public String getNama() {
        return nama;
    }

    public String getNim() {
        return nim;
    }

    public String getJurusan() {
        return jurusan;
    }
}
