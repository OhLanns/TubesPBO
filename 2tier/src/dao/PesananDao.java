package src.dao;

import src.config.DatabaseConnection;
import src.model.Pesanan;
import java.sql.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class PesananDao {
    
    // ========== CREATE ==========
    public boolean tambahPesanan(Pesanan pesanan) {
        String sql = "INSERT INTO pesanan (nama_produk, nama_pemesan, telepon, email, " +
                     "tanggal_pesan, total_harga, status, keterangan) " +
                     "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        
        Connection conn = null;
        PreparedStatement pstmt = null;
        
        try {
            conn = DatabaseConnection.getConnection();
            pstmt = conn.prepareStatement(sql);
            
            pstmt.setString(1, pesanan.getNamaProduk());
            pstmt.setString(2, pesanan.getNamaPemesan());
            pstmt.setString(3, pesanan.getTelepon());
            pstmt.setString(4, pesanan.getEmail());
            pstmt.setTimestamp(5, Timestamp.valueOf(pesanan.getTanggalPesan()));
            pstmt.setBigDecimal(6, pesanan.getTotalHarga());
            pstmt.setString(7, pesanan.getStatus());
            pstmt.setString(8, pesanan.getKeterangan());
            
            int rowsAffected = pstmt.executeUpdate();
            conn.commit();
            
            System.out.println("✅ Pesanan ditambahkan: " + pesanan.getNamaProduk());
            return rowsAffected > 0;
            
        } catch (SQLException e) {
            System.err.println("❌ Error tambah pesanan: " + e.getMessage());
            rollback(conn);
            return false;
        } finally {
            closeResources(pstmt, null);
        }
    }
    
    // ========== READ ALL ==========
    public List<Pesanan> getAllPesanan() {
        List<Pesanan> pesananList = new ArrayList<>();
        String sql = "SELECT * FROM pesanan ORDER BY tanggal_pesan DESC";
        
        Connection conn = null;
        Statement stmt = null;
        ResultSet rs = null;
        
        try {
            conn = DatabaseConnection.getConnection();
            stmt = conn.createStatement();
            rs = stmt.executeQuery(sql);
            
            while (rs.next()) {
                Pesanan pesanan = mapResultSetToPesanan(rs);
                pesananList.add(pesanan);
            }
            
            System.out.println("✅ Mengambil " + pesananList.size() + " pesanan");
            return pesananList;
            
        } catch (SQLException e) {
            System.err.println("❌ Error get all pesanan: " + e.getMessage());
            return pesananList;
        } finally {
            closeResources(stmt, rs);
        }
    }
    
    // ========== READ BY ID ==========
    public Pesanan getPesananById(int id) {
        String sql = "SELECT * FROM pesanan WHERE id = ?";
        
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        
        try {
            conn = DatabaseConnection.getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, id);
            rs = pstmt.executeQuery();
            
            if (rs.next()) {
                return mapResultSetToPesanan(rs);
            }
            
            return null;
            
        } catch (SQLException e) {
            System.err.println("❌ Error get pesanan by id: " + e.getMessage());
            return null;
        } finally {
            closeResources(pstmt, rs);
        }
    }
    
    // ========== UPDATE ==========
    public boolean updatePesanan(Pesanan pesanan) {
        String sql = "UPDATE pesanan SET nama_produk = ?, nama_pemesan = ?, " +
                     "telepon = ?, email = ?, total_harga = ?, status = ?, " +
                     "keterangan = ? WHERE id = ?";
        
        Connection conn = null;
        PreparedStatement pstmt = null;
        
        try {
            conn = DatabaseConnection.getConnection();
            pstmt = conn.prepareStatement(sql);
            
            pstmt.setString(1, pesanan.getNamaProduk());
            pstmt.setString(2, pesanan.getNamaPemesan());
            pstmt.setString(3, pesanan.getTelepon());
            pstmt.setString(4, pesanan.getEmail());
            pstmt.setBigDecimal(5, pesanan.getTotalHarga());
            pstmt.setString(6, pesanan.getStatus());
            pstmt.setString(7, pesanan.getKeterangan());
            pstmt.setInt(8, pesanan.getId());
            
            int rowsAffected = pstmt.executeUpdate();
            conn.commit();
            
            System.out.println("✅ Pesanan diupdate: ID " + pesanan.getId());
            return rowsAffected > 0;
            
        } catch (SQLException e) {
            System.err.println("❌ Error update pesanan: " + e.getMessage());
            rollback(conn);
            return false;
        } finally {
            closeResources(pstmt, null);
        }
    }
    
    // ========== DELETE ==========
    public boolean hapusPesanan(int id) {
        String sql = "DELETE FROM pesanan WHERE id = ?";
        
        Connection conn = null;
        PreparedStatement pstmt = null;
        
        try {
            conn = DatabaseConnection.getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, id);
            
            int rowsAffected = pstmt.executeUpdate();
            conn.commit();
            
            System.out.println("✅ Pesanan dihapus: ID " + id);
            return rowsAffected > 0;
            
        } catch (SQLException e) {
            System.err.println("❌ Error hapus pesanan: " + e.getMessage());
            rollback(conn);
            return false;
        } finally {
            closeResources(pstmt, null);
        }
    }
    
    // ========== UPDATE STATUS ==========
    public boolean ubahStatusPesanan(int id, String status) {
        String sql = "UPDATE pesanan SET status = ? WHERE id = ?";
        
        Connection conn = null;
        PreparedStatement pstmt = null;
        
        try {
            conn = DatabaseConnection.getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, status);
            pstmt.setInt(2, id);
            
            int rowsAffected = pstmt.executeUpdate();
            conn.commit();
            
            System.out.println("✅ Status diubah: ID " + id + " -> " + status);
            return rowsAffected > 0;
            
        } catch (SQLException e) {
            System.err.println("❌ Error ubah status: " + e.getMessage());
            rollback(conn);
            return false;
        } finally {
            closeResources(pstmt, null);
        }
    }
    
    // ========== HELPER METHODS ==========
    private Pesanan mapResultSetToPesanan(ResultSet rs) throws SQLException {
        Pesanan pesanan = new Pesanan();
        pesanan.setId(rs.getInt("id"));
        pesanan.setNamaProduk(rs.getString("nama_produk"));
        pesanan.setNamaPemesan(rs.getString("nama_pemesan"));
        pesanan.setTelepon(rs.getString("telepon"));
        pesanan.setEmail(rs.getString("email"));
        pesanan.setTanggalPesan(rs.getTimestamp("tanggal_pesan").toLocalDateTime());
        pesanan.setTotalHarga(rs.getBigDecimal("total_harga"));
        pesanan.setStatus(rs.getString("status"));
        pesanan.setKeterangan(rs.getString("keterangan"));
        return pesanan;
    }
    
    private void closeResources(Statement stmt, ResultSet rs) {
        try {
            if (rs != null) rs.close();
            if (stmt != null) stmt.close();
        } catch (SQLException e) {
            System.err.println("Error closing resources: " + e.getMessage());
        }
    }
    
    private void rollback(Connection conn) {
        if (conn != null) {
            try {
                conn.rollback();
            } catch (SQLException ex) {
                System.err.println("Error rollback: " + ex.getMessage());
            }
        }
    }
}