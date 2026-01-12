import view.LoginDialog;
import view.MainFrame;

import javax.swing.*;

public class PanesyaApp {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (Exception e) {
                e.printStackTrace();
            }

            LoginDialog loginDialog = new LoginDialog(null);
            loginDialog.setVisible(true);

            if (loginDialog.isLoginSuccess()) {
                new MainFrame();
            } else {
                System.exit(0);
            }
        });
    }
}
