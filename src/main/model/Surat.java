package main.model;

import java.sql.Date;

public class Surat {
    private int id;
    private String noSurat; // Generated if needed, or ID
    private String nik;
    private String namaPemohon;
    private String jenisSurat;
    private Date tanggalRequest;
    private String status; // Menunggu, Disetujui, Ditolak
    private String keperluan;

    public Surat() {
    }

    public Surat(int id, String nik, String namaPemohon, String jenisSurat, Date tanggalRequest, String status,
            String keperluan) {
        this.id = id;
        this.nik = nik;
        this.namaPemohon = namaPemohon;
        this.jenisSurat = jenisSurat;
        this.tanggalRequest = tanggalRequest;
        this.status = status;
        this.keperluan = keperluan;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNik() {
        return nik;
    }

    public void setNik(String nik) {
        this.nik = nik;
    }

    public String getNamaPemohon() {
        return namaPemohon;
    }

    public void setNamaPemohon(String namaPemohon) {
        this.namaPemohon = namaPemohon;
    }

    public String getJenisSurat() {
        return jenisSurat;
    }

    public void setJenisSurat(String jenisSurat) {
        this.jenisSurat = jenisSurat;
    }

    public Date getTanggalRequest() {
        return tanggalRequest;
    }

    public void setTanggalRequest(Date tanggalRequest) {
        this.tanggalRequest = tanggalRequest;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getKeperluan() {
        return keperluan;
    }

    public void setKeperluan(String keperluan) {
        this.keperluan = keperluan;
    }
}
