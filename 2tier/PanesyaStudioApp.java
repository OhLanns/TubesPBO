
import src.view.LoginDialog;
import src.view.MainFrame;
import javax.swing.*;

public class PanesyaStudioApp {
    public static void main(String[] args) {
        // Set Look and Feel
        SwingUtilities.invokeLater(() -> {
            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (Exception e) {
                e.printStackTrace();
            }
            
            System.out.println("=== APLIKASI PANESYA STUDIO ===");
            System.out.println("Memulai aplikasi...");
            
            // Tampilkan login dialog
            LoginDialog loginDialog = new LoginDialog(null);
            loginDialog.setVisible(true);
            
            // Jika login sukses, buka main frame
            if (loginDialog.isLoginSuccess()) {
                System.out.println("Login berhasil!");
                new MainFrame();
            } else {
                System.out.println("Aplikasi ditutup.");
                System.exit(0);
            }
        });
    }
}