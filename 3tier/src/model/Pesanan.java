package model;

import java.awt.Color;
import java.math.BigDecimal;
import java.time.LocalDateTime;

public class Pesanan {
    private Integer id;
    private String produk;
    private String pemesan;
    private String telepon;
    private String email;
    private LocalDateTime tanggalPesan;
    private BigDecimal total;
    private String status; // "PROSES", "SUKSES", "BATAL", "MENUNGGU"
    private String keterangan;
    private LocalDateTime tanggalUpdate;
    
    // Constructor
    public Pesanan() {
        this.tanggalPesan = LocalDateTime.now();
        this.status = "PROSES";
        this.tanggalUpdate = LocalDateTime.now();
    }
    
    // Constructor dengan parameter
    public Pesanan(String produk, String pemesan, BigDecimal total) {
        this();
        this.produk = produk;
        this.pemesan = pemesan;
        this.total = total;
    }
    
    // Constructor lengkap
    public Pesanan(Integer id, String produk, String pemesan, String telepon, String email,
                   LocalDateTime tanggalPesan, BigDecimal total, String status, String keterangan) {
        this.id = id;
        this.produk = produk;
        this.pemesan = pemesan;
        this.telepon = telepon;
        this.email = email;
        this.tanggalPesan = tanggalPesan;
        this.total = total;
        this.status = status;
        this.keterangan = keterangan;
        this.tanggalUpdate = LocalDateTime.now();
    }
    
    // Getter dan Setter
    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }
    
    public String getProduk() { return produk; }
    public void setProduk(String produk) { this.produk = produk; }
    
    public String getPemesan() { return pemesan; }
    public void setPemesan(String pemesan) { this.pemesan = pemesan; }
    
    public String getTelepon() { return telepon; }
    public void setTelepon(String telepon) { this.telepon = telepon; }
    
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    
    public LocalDateTime getTanggalPesan() { return tanggalPesan; }
    public void setTanggalPesan(LocalDateTime tanggalPesan) { this.tanggalPesan = tanggalPesan; }
    
    public BigDecimal getTotal() { return total; }
    public void setTotal(BigDecimal total) { this.total = total; }
    
    public String getStatus() { return status; }
    public void setStatus(String status) { 
        this.status = status; 
        this.tanggalUpdate = LocalDateTime.now();
    }
    
    public String getKeterangan() { return keterangan; }
    public void setKeterangan(String keterangan) { this.keterangan = keterangan; }
    
    public LocalDateTime getTanggalUpdate() { return tanggalUpdate; }
    public void setTanggalUpdate(LocalDateTime tanggalUpdate) { this.tanggalUpdate = tanggalUpdate; }
    
    // Compatibility methods
    public String getNamaProduk() { return produk; }
    public void setNamaProduk(String namaProduk) { this.produk = namaProduk; }
    
    public String getNamaPemesan() { return pemesan; }
    public void setNamaPemesan(String namaPemesan) { this.pemesan = namaPemesan; }
    
    public BigDecimal getTotalHarga() { return total; }
    public void setTotalHarga(BigDecimal totalHarga) { this.total = totalHarga; }
    
    @Override
    public String toString() {
        return String.format("Pesanan[ID=%d, Produk=%s, Pemesan=%s, Status=%s]", 
            id, produk, pemesan, status);
    }
    
    // Status check methods
    public boolean isProses() {
        return "PROSES".equals(status);
    }
    
    public boolean isSukses() {
        return "SUKSES".equals(status);
    }
    
    public boolean isBatal() {
        return "BATAL".equals(status);
    }
    
    public boolean isMenunggu() {
        return "MENUNGGU".equals(status);
    }
    
    // UI helper methods
    public Color getStatusColor() {
        switch (status.toUpperCase()) {
            case "PROSES": return new Color(52, 152, 219); // Biru
            case "SUKSES": return new Color(46, 204, 113); // Hijau
            case "BATAL": return new Color(231, 76, 60);   // Merah
            case "MENUNGGU": return new Color(243, 156, 18); // Oranye
            default: return Color.GRAY;
        }
    }
    
    public String getFormattedTanggal() {
        if (tanggalPesan == null) return "-";
        return tanggalPesan.format(java.time.format.DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm"));
    }
    
    public String getFormattedTanggalUpdate() {
        if (tanggalUpdate == null) return "-";
        return tanggalUpdate.format(java.time.format.DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm"));
    }
    
    public String getFormattedTotal() {
        if (total == null) return "Rp 0";
        java.text.NumberFormat rupiahFormat = java.text.NumberFormat.getCurrencyInstance(
            java.util.Locale.forLanguageTag("id-ID"));
        return rupiahFormat.format(total);
    }
    
    // Validation method
    public boolean isValid() {
        return produk != null && !produk.trim().isEmpty() &&
               pemesan != null && !pemesan.trim().isEmpty() &&
               total != null && total.compareTo(BigDecimal.ZERO) > 0;
    }
}