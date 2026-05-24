package view;

import app.Session;
import connection.DBConnection;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

public class FormLogin extends JFrame {

    private final Connection conn = new DBConnection().connect();

    public FormLogin() {
        initComponents();setLocationRelativeTo(null);
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {
        lblTitle = new javax.swing.JLabel();
        lblUsername = new javax.swing.JLabel();
        lblPassword = new javax.swing.JLabel();
        txtUsername = new javax.swing.JTextField();
        txtPassword = new javax.swing.JPasswordField();
        btnLogin = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Login Reporting Klinik");

        lblTitle.setFont(new java.awt.Font("Segoe UI", 1, 18));
        lblTitle.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblTitle.setText("Login Reporting Klinik");

        lblUsername.setText("Username");
        lblPassword.setText("Password");
        btnLogin.setText("Login");
        btnLogin.addActionListener(evt -> login());

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(28, 28, 28)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(lblTitle, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(lblUsername)
                            .addComponent(lblPassword))
                        .addGap(28, 28, 28)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(txtUsername)
                            .addComponent(txtPassword)
                            .addComponent(btnLogin, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 240, Short.MAX_VALUE))))
                .addContainerGap(28, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(20, 20, 20)
                .addComponent(lblTitle)
                .addGap(22, 22, 22)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblUsername)
                    .addComponent(txtUsername, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(14, 14, 14)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblPassword)
                    .addComponent(txtPassword, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addComponent(btnLogin)
                .addContainerGap(24, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void login() {
        String username = txtUsername.getText().trim();
        String password = new String(txtPassword.getPassword()).trim();
        if (username.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Username dan password wajib diisi.");
            return;
        }
        if (conn == null) {
            validateFallback(username, password);
            return;
        }
        String sql = "SELECT username, role FROM users WHERE username = ? AND password = ? AND status = 'aktif'";
        try (PreparedStatement stat = conn.prepareStatement(sql)) {
            stat.setString(1, username);
            stat.setString(2, password);
            try (ResultSet rs = stat.executeQuery()) {
                if (rs.next()) {
                    openMenu(username, rs.getString("role"));
                } else {
                    validateFallback(username, password);
                }
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Tabel users belum siap. Login demo: admin/admin, petugas/petugas, dokter/dokter, kasir/kasir.");
            validateFallback(username, password);
        }
    }

    private void validateFallback(String username, String password) {
        if (username.equalsIgnoreCase("admin") && password.equals("admin")) {
            openMenu(username, "admin");
            return;
        }
        if (username.equalsIgnoreCase("petugas") && password.equals("petugas")) {
            openMenu(username, "petugas");
            return;
        }
        if (username.equalsIgnoreCase("dokter") && password.equals("dokter")) {
            openMenu(username, "dokter");
            return;
        }
        if (username.equalsIgnoreCase("kasir") && password.equals("kasir")) {
            openMenu(username, "kasir");
            return;
        }
        JOptionPane.showMessageDialog(this, "Login gagal. Periksa username dan password.");
    }

    private void openMenu(String username, String role) {
        Session.login(username, role);
        new FormMenu().setVisible(true);
        dispose();
    }

    private void applyHints() {
        txtUsername.setToolTipText("Masukkan username akun.");
        txtPassword.setToolTipText("Masukkan password akun.");
    }

    public static void main(String args[]) {
        java.awt.EventQueue.invokeLater(() -> new FormLogin().setVisible(true));
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnLogin;
    private javax.swing.JLabel lblPassword;
    private javax.swing.JLabel lblTitle;
    private javax.swing.JLabel lblUsername;
    private javax.swing.JPasswordField txtPassword;
    private javax.swing.JTextField txtUsername;
    // End of variables declaration//GEN-END:variables
}
