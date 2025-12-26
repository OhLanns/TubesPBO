package src.model;

import java.math.BigDecimal;

public class Produk {
    private int id;
    private String nama;
    private String deskripsi;
    private BigDecimal harga;
    private String jenis;
    
    public Produk(int id, String nama, String deskripsi, BigDecimal harga, String jenis) {
        this.id = id;
        this.nama = nama;
        this.deskripsi = deskripsi;
        this.harga = harga;
        this.jenis = jenis;
    }
    
    // Constructor alternatif tanpa ID
    public Produk(String nama, String deskripsi, BigDecimal harga, String jenis) {
        this(0, nama, deskripsi, harga, jenis);
    }
    
    // Getters
    public int getId() { return id; }
    public String getNama() { return nama; }
    public String getDeskripsi() { return deskripsi; }
    public BigDecimal getHarga() { return harga; }
    public String getJenis() { return jenis; }
    
    // Setters
    public void setId(int id) { this.id = id; }
    public void setNama(String nama) { this.nama = nama; }
    public void setDeskripsi(String deskripsi) { this.deskripsi = deskripsi; }
    public void setHarga(BigDecimal harga) { this.harga = harga; }
    public void setJenis(String jenis) { this.jenis = jenis; }
    
    @Override
    public String toString() {
        return nama + " - " + getFormattedHarga() + " (" + jenis + ")";
    }
    
    // Format harga dalam Rupiah
    public String getFormattedHarga() {
        java.text.NumberFormat rupiahFormat = java.text.NumberFormat.getCurrencyInstance(
            java.util.Locale.forLanguageTag("id-ID"));
        return rupiahFormat.format(harga);
    }
    
    // Equals and hashCode for proper comparison
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Produk other = (Produk) obj;
        return id == other.id || 
               (nama != null && nama.equals(other.nama) && 
                jenis != null && jenis.equals(other.jenis));
    }
    
    @Override
    public int hashCode() {
        int result = id;
        result = 31 * result + (nama != null ? nama.hashCode() : 0);
        result = 31 * result + (jenis != null ? jenis.hashCode() : 0);
        return result;
    }
    
    // Helper method for cart operations
    public Produk copy() {
        return new Produk(this.id, this.nama, this.deskripsi, this.harga, this.jenis);
    }
}