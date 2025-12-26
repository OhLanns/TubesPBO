package src.config;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {
    private static final String URL = "jdbc:mysql://localhost:3306/panesya_studio";
    private static final String USER = "root";
    private static final String PASSWORD = "";
    
    
    // Private constructor tetap untuk singleton pattern
    private DatabaseConnection() {}
    
    public static Connection getConnection() throws SQLException {
        // JANGAN gunakan static connection, buat baru setiap kali
        try {
            // Untuk JDK 8+, DriverManager otomatis load driver
            // Class.forName tidak wajib tapi tetap baik untuk kompatibilitas
            try {
                Class.forName("com.mysql.cj.jdbc.Driver");
            } catch (ClassNotFoundException e) {
                System.err.println("⚠️  Driver tidak ditemukan, JDBC 4.0+ mungkin otomatis load");
            }
            
            // Buat koneksi BARU setiap kali dipanggil
            Connection connection = DriverManager.getConnection(URL, USER, PASSWORD);
            System.out.println("✅ Koneksi database BERHASIL dibuat!");
            
            // ⚠️ JANGAN setAutoCommit(false) KECUALI untuk transaksi kompleks
            // Biarkan default true untuk auto commit
            // connection.setAutoCommit(true); // Default sudah true
            
            return connection;
            
        } catch (SQLException e) {
            System.err.println("❌ Gagal koneksi ke database!");
            System.err.println("URL: " + URL);
            System.err.println("User: " + USER);
            System.err.println("Pastikan:");
            System.err.println("1. MySQL Server berjalan: `mysql -u root`");
            System.err.println("2. Database ada: `CREATE DATABASE panesya_studio;`");
            System.err.println("3. Port 3306 terbuka");
            throw e; // Lempar exception ke caller
        }
    }
    
    // Method helper untuk penggunaan mudah dengan try-with-resources
    public static void testConnection() {
        System.out.println("🧪 Testing database connection...");
        System.out.println("URL: " + URL);
        System.out.println("User: " + USER);
        
        try (Connection conn = getConnection()) {
            if (conn != null) {
                System.out.println("✅ Connection test SUCCESS!");
                System.out.println("Database: " + conn.getMetaData().getDatabaseProductName());
                System.out.println("Version: " + conn.getMetaData().getDatabaseProductVersion());
            }
        } catch (SQLException e) {
            System.err.println("❌ Connection test FAILED!");
            System.err.println("Error: " + e.getMessage());
        }
    }
    
    // Main untuk testing langsung
    public static void main(String[] args) {
        testConnection();
    }
}