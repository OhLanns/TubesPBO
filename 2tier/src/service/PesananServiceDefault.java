package src.service;

import src.dao.PesananDao;
import src.model.Pesanan;
import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

public class PesananServiceDefault implements PesananService {
    
    private final PesananDao pesananDao;
    
    public PesananServiceDefault() {
        this.pesananDao = new PesananDao();
    }
    
    // ========== CREATE dengan Validasi ==========
    @Override
    public boolean tambahPesanan(Pesanan pesanan) {
        // VALIDASI BUSINESS RULES
        if (pesanan.getNamaPemesan() == null || pesanan.getNamaPemesan().trim().isEmpty()) {
            throw new IllegalArgumentException("❌ Nama pemesan harus diisi!");
        }
        
        if (pesanan.getNamaProduk() == null || pesanan.getNamaProduk().trim().isEmpty()) {
            throw new IllegalArgumentException("❌ Nama produk harus diisi!");
        }
        
        if (pesanan.getTotalHarga() == null || pesanan.getTotalHarga().compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("❌ Total harga harus lebih dari 0!");
        }
        
        // Set default values jika null
        if (pesanan.getStatus() == null) {
            pesanan.setStatus("PROSES");
        }
        
        System.out.println("🔧 Service: Menambahkan pesanan baru...");
        return pesananDao.tambahPesanan(pesanan);
    }
    
    // ========== READ ==========
    @Override
    public List<Pesanan> getAllPesanan() {
        System.out.println("🔧 Service: Mengambil semua pesanan...");
        return pesananDao.getAllPesanan();
    }
    
    @Override
    public Pesanan getPesananById(int id) {
        System.out.println("🔧 Service: Mencari pesanan ID " + id + "...");
        Pesanan pesanan = pesananDao.getPesananById(id);
        
        if (pesanan == null) {
            throw new RuntimeException("⚠️ Pesanan dengan ID " + id + " tidak ditemukan!");
        }
        
        return pesanan;
    }
    
    @Override
    public List<Pesanan> getPesananByStatus(String status) {
        System.out.println("🔧 Service: Filter pesanan by status: " + status);
        return getAllPesanan().stream()
                .filter(p -> status.equals(p.getStatus()))
                .collect(Collectors.toList());
    }
    
    // ========== UPDATE dengan Business Rules ==========
    @Override
    public boolean updatePesanan(Pesanan pesanan) {
        // BUSINESS RULE: Cek apakah pesanan ada
        Pesanan existing = getPesananById(pesanan.getId());
        
        // BUSINESS RULE: Pesanan yang sudah SUKSES tidak bisa diubah
        if ("SUKSES".equals(existing.getStatus())) {
            throw new IllegalStateException("❌ Pesanan yang sudah SUKSES tidak bisa diubah!");
        }
        
        // BUSINESS RULE: Validasi input
        if (pesanan.getNamaPemesan() == null || pesanan.getNamaPemesan().trim().isEmpty()) {
            throw new IllegalArgumentException("❌ Nama pemesan harus diisi!");
        }
        
        System.out.println("🔧 Service: Mengupdate pesanan ID " + pesanan.getId());
        return pesananDao.updatePesanan(pesanan);
    }
    
    @Override
    public boolean ubahStatusPesanan(int id, String status) {
        // BUSINESS RULE: Validasi status
        List<String> allowedStatus = List.of("PROSES", "SUKSES", "BATAL", "MENUNGGU");
        if (!allowedStatus.contains(status)) {
            throw new IllegalArgumentException("❌ Status tidak valid: " + status);
        }
        
        System.out.println("🔧 Service: Mengubah status pesanan ID " + id + " -> " + status);
        return pesananDao.ubahStatusPesanan(id, status);
    }
    
    // ========== DELETE dengan Business Rules ==========
    @Override
    public boolean hapusPesanan(int id) {
        // BUSINESS RULE: Cek apakah pesanan ada
        Pesanan pesanan = getPesananById(id);
        
        // BUSINESS RULE: Hanya pesanan dengan status PROSES yang bisa dihapus
        if (!"PROSES".equals(pesanan.getStatus())) {
            throw new IllegalStateException("❌ Hanya pesanan dengan status PROSES yang bisa dihapus!");
        }
        
        System.out.println("🔧 Service: Menghapus pesanan ID " + id);
        return pesananDao.hapusPesanan(id);
    }
    
    // ========== BUSINESS METHODS ==========
    @Override
    public double getTotalPendapatan() {
        return getAllPesanan().stream()
                .filter(p -> "SUKSES".equals(p.getStatus()))
                .mapToDouble(p -> p.getTotalHarga().doubleValue())
                .sum();
    }
    
    @Override
    public int getJumlahPesananByStatus(String status) {
        return (int) getAllPesanan().stream()
                .filter(p -> status.equals(p.getStatus()))
                .count();
    }
}