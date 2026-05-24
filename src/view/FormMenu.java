package view;

import app.Session;
import java.awt.Color;
import java.awt.Font;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.border.EmptyBorder;

public class FormMenu extends JFrame {

    public FormMenu() {
        initComponents();
        setupMenuActions();
        applyHints();
        applyRole();
        setLocationRelativeTo(null);
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        panelSidebar = new javax.swing.JPanel();
        lblAppTitle = new javax.swing.JLabel();
        lblAppSubtitle = new javax.swing.JLabel();
        lblMaster = new javax.swing.JLabel();
        btnPasien = new javax.swing.JButton();
        btnPoli = new javax.swing.JButton();
        btnDokter = new javax.swing.JButton();
        btnJadwal = new javax.swing.JButton();
        btnObat = new javax.swing.JButton();
        btnUser = new javax.swing.JButton();
        lblTransaksi = new javax.swing.JLabel();
        btnKunjungan = new javax.swing.JButton();
        btnResep = new javax.swing.JButton();
        btnPembayaran = new javax.swing.JButton();
        lblReport = new javax.swing.JLabel();
        btnReportKunjungan = new javax.swing.JButton();
        btnReportJadwalDokter = new javax.swing.JButton();
        btnReportObat = new javax.swing.JButton();
        btnReportResep = new javax.swing.JButton();
        btnReportPembayaran = new javax.swing.JButton();
        btnLogout = new javax.swing.JButton();
        panelContent = new javax.swing.JPanel();
        lblDashboardTitle = new javax.swing.JLabel();
        lblInfo = new javax.swing.JLabel();
        lblDashboardInfo = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Menu Reporting Klinik");

        panelSidebar.setBackground(new java.awt.Color(245, 247, 251));
        panelSidebar.setBorder(javax.swing.BorderFactory.createEmptyBorder(18, 18, 18, 18));

        lblAppTitle.setFont(new java.awt.Font("Segoe UI", 1, 20)); // NOI18N
        lblAppTitle.setForeground(new java.awt.Color(31, 41, 55));
        lblAppTitle.setText("Reporting Klinik");

        lblAppSubtitle.setFont(new java.awt.Font("Segoe UI", 0, 12)); // NOI18N
        lblAppSubtitle.setForeground(new java.awt.Color(107, 114, 128));
        lblAppSubtitle.setText("Menu Operasional");

        lblMaster.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        lblMaster.setText("MASTER DATA");

        btnPasien.setText("Data Pasien");

        btnPoli.setText("Data Poli");

        btnDokter.setText("Data Dokter");

        btnJadwal.setText("Jadwal Dokter");

        btnObat.setText("Data Obat");

        btnUser.setText("Data User");

        lblTransaksi.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        lblTransaksi.setText("TRANSAKSI");

        btnKunjungan.setText("Kunjungan");

        btnResep.setText("Resep");

        btnPembayaran.setText("Pembayaran");

        lblReport.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        lblReport.setText("REPORT");

        btnReportKunjungan.setText("Report Kunjungan");

        btnReportJadwalDokter.setText("Report Jadwal Dokter");

        btnReportObat.setText("Report Obat");

        btnReportResep.setText("Report Resep");

        btnReportPembayaran.setText("Report Pembayaran");
        btnReportPembayaran.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnReportPembayaranActionPerformed(evt);
            }
        });

        btnLogout.setText("Logout");
        btnLogout.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnLogoutActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout panelSidebarLayout = new javax.swing.GroupLayout(panelSidebar);
        panelSidebar.setLayout(panelSidebarLayout);
        panelSidebarLayout.setHorizontalGroup(
            panelSidebarLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(lblAppTitle, javax.swing.GroupLayout.DEFAULT_SIZE, 230, Short.MAX_VALUE)
            .addComponent(lblAppSubtitle, javax.swing.GroupLayout.DEFAULT_SIZE, 230, Short.MAX_VALUE)
            .addComponent(lblMaster, javax.swing.GroupLayout.DEFAULT_SIZE, 230, Short.MAX_VALUE)
            .addComponent(btnPasien, javax.swing.GroupLayout.DEFAULT_SIZE, 230, Short.MAX_VALUE)
            .addComponent(btnPoli, javax.swing.GroupLayout.DEFAULT_SIZE, 230, Short.MAX_VALUE)
            .addComponent(btnDokter, javax.swing.GroupLayout.DEFAULT_SIZE, 230, Short.MAX_VALUE)
            .addComponent(btnJadwal, javax.swing.GroupLayout.DEFAULT_SIZE, 230, Short.MAX_VALUE)
            .addComponent(btnObat, javax.swing.GroupLayout.DEFAULT_SIZE, 230, Short.MAX_VALUE)
            .addComponent(btnUser, javax.swing.GroupLayout.DEFAULT_SIZE, 230, Short.MAX_VALUE)
            .addComponent(lblTransaksi, javax.swing.GroupLayout.DEFAULT_SIZE, 230, Short.MAX_VALUE)
            .addComponent(btnKunjungan, javax.swing.GroupLayout.DEFAULT_SIZE, 230, Short.MAX_VALUE)
            .addComponent(btnResep, javax.swing.GroupLayout.DEFAULT_SIZE, 230, Short.MAX_VALUE)
            .addComponent(btnPembayaran, javax.swing.GroupLayout.DEFAULT_SIZE, 230, Short.MAX_VALUE)
            .addComponent(lblReport, javax.swing.GroupLayout.DEFAULT_SIZE, 230, Short.MAX_VALUE)
            .addComponent(btnReportKunjungan, javax.swing.GroupLayout.DEFAULT_SIZE, 230, Short.MAX_VALUE)
            .addComponent(btnReportJadwalDokter, javax.swing.GroupLayout.DEFAULT_SIZE, 230, Short.MAX_VALUE)
            .addComponent(btnReportObat, javax.swing.GroupLayout.DEFAULT_SIZE, 230, Short.MAX_VALUE)
            .addComponent(btnReportResep, javax.swing.GroupLayout.DEFAULT_SIZE, 230, Short.MAX_VALUE)
            .addComponent(btnReportPembayaran, javax.swing.GroupLayout.DEFAULT_SIZE, 230, Short.MAX_VALUE)
            .addComponent(btnLogout, javax.swing.GroupLayout.DEFAULT_SIZE, 230, Short.MAX_VALUE)
        );
        panelSidebarLayout.setVerticalGroup(
            panelSidebarLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelSidebarLayout.createSequentialGroup()
                .addComponent(lblAppTitle)
                .addGap(2, 2, 2)
                .addComponent(lblAppSubtitle)
                .addGap(28, 28, 28)
                .addComponent(lblMaster)
                .addGap(8, 8, 8)
                .addComponent(btnPasien, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(8, 8, 8)
                .addComponent(btnPoli, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(8, 8, 8)
                .addComponent(btnDokter, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(8, 8, 8)
                .addComponent(btnJadwal, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(8, 8, 8)
                .addComponent(btnObat, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(8, 8, 8)
                .addComponent(btnUser, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(24, 24, 24)
                .addComponent(lblTransaksi)
                .addGap(8, 8, 8)
                .addComponent(btnKunjungan, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(8, 8, 8)
                .addComponent(btnResep, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(8, 8, 8)
                .addComponent(btnPembayaran, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(20, 20, 20)
                .addComponent(lblReport)
                .addGap(8, 8, 8)
                .addComponent(btnReportKunjungan, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnReportJadwalDokter, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnReportObat, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnReportResep, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnReportPembayaran, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(btnLogout, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        panelContent.setBackground(new java.awt.Color(255, 255, 255));
        panelContent.setBorder(javax.swing.BorderFactory.createEmptyBorder(24, 28, 24, 28));

        lblDashboardTitle.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        lblDashboardTitle.setText("Dashboard Reporting Klinik");

        lblInfo.setText("User aktif");

        lblDashboardInfo.setText("<html>Pilih menu di sisi kiri untuk mengelola master data dan transaksi klinik.</html>");

        javax.swing.GroupLayout panelContentLayout = new javax.swing.GroupLayout(panelContent);
        panelContent.setLayout(panelContentLayout);
        panelContentLayout.setHorizontalGroup(
            panelContentLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(lblDashboardTitle, javax.swing.GroupLayout.DEFAULT_SIZE, 520, Short.MAX_VALUE)
            .addComponent(lblInfo, javax.swing.GroupLayout.DEFAULT_SIZE, 520, Short.MAX_VALUE)
            .addComponent(lblDashboardInfo, javax.swing.GroupLayout.DEFAULT_SIZE, 520, Short.MAX_VALUE)
        );
        panelContentLayout.setVerticalGroup(
            panelContentLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelContentLayout.createSequentialGroup()
                .addComponent(lblDashboardTitle)
                .addGap(8, 8, 8)
                .addComponent(lblInfo)
                .addGap(28, 28, 28)
                .addComponent(lblDashboardInfo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(panelSidebar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0)
                .addComponent(panelContent, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(panelSidebar, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(panelContent, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnReportPembayaranActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnReportPembayaranActionPerformed
        openForm(new FormReportPembayaran());
    }//GEN-LAST:event_btnReportPembayaranActionPerformed

    private void btnLogoutActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnLogoutActionPerformed
        logout();
    }//GEN-LAST:event_btnLogoutActionPerformed

    private void setupMenuActions() {
        btnPasien.addActionListener(evt -> openForm(new FormPasien()));
        btnPoli.addActionListener(evt -> openForm(new FormPoli()));
        btnDokter.addActionListener(evt -> openForm(new FormDokter()));
        btnJadwal.addActionListener(evt -> openForm(new FormJadwalDokter()));
        btnObat.addActionListener(evt -> openForm(new FormObat()));
        btnUser.addActionListener(evt -> openForm(new FormUser()));
        btnKunjungan.addActionListener(evt -> openForm(new FormKunjungan()));
        btnResep.addActionListener(evt -> openForm(new FormResep()));
        btnPembayaran.addActionListener(evt -> openForm(new FormPembayaran()));
        btnReportKunjungan.addActionListener(evt -> openForm(new FormReportKunjungan()));
        btnReportJadwalDokter.addActionListener(evt -> openForm(new FormReportJadwalDokter()));
        btnReportObat.addActionListener(evt -> openForm(new FormReportObat()));
        btnReportResep.addActionListener(evt -> openForm(new FormReportResep()));
    }

    private void openForm(JFrame form) {
        form.setLocationRelativeTo(this);
        form.setVisible(true);
    }

    private void applyRole() {
        lblInfo.setText("Login sebagai " + Session.getUsername() + " | Role: " + Session.getRole());
        if (Session.isDokter()) {
            btnPoli.setEnabled(false);
            btnPasien.setEnabled(false);
            btnDokter.setEnabled(false);
            btnJadwal.setEnabled(false);
            btnObat.setEnabled(false);
            btnUser.setEnabled(false);
            btnPembayaran.setEnabled(false);
            btnReportPembayaran.setEnabled(false);
        } else if (Session.isPetugas()) {
            btnPoli.setEnabled(false);
            btnDokter.setEnabled(false);
            btnJadwal.setEnabled(false);
            btnUser.setEnabled(false);
            btnPembayaran.setEnabled(false);
            btnReportPembayaran.setEnabled(false);
        } else if (Session.isKasir()) {
            btnPoli.setEnabled(false);
            btnPasien.setEnabled(false);
            btnDokter.setEnabled(false);
            btnJadwal.setEnabled(false);
            btnObat.setEnabled(false);
            btnKunjungan.setEnabled(false);
            btnResep.setEnabled(false);
            btnUser.setEnabled(false);
            btnReportPembayaran.setEnabled(false);
        } else {
            btnPembayaran.setEnabled(false);
        }
    }

    private void logout() {
        int ok = JOptionPane.showConfirmDialog(this, "Keluar dari aplikasi?", "Logout", JOptionPane.YES_NO_OPTION);
        if (ok == JOptionPane.YES_OPTION) {
            Session.logout();
            new FormLogin().setVisible(true);
            dispose();
        }
    }

    private void styleMenuButton(javax.swing.JButton button) {
        button.setFocusPainted(false);
        button.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        button.setBackground(Color.WHITE);
        button.setForeground(new Color(31, 41, 55));
        button.setBorder(javax.swing.BorderFactory.createCompoundBorder(
                javax.swing.BorderFactory.createLineBorder(new Color(229, 231, 235)),
                new EmptyBorder(6, 12, 6, 12)));
        button.setFont(new Font("Segoe UI", 0, 13));
    }

    private void applyHints() {
        btnPasien.setToolTipText("Buka form master data pasien.");
        btnPoli.setToolTipText("Buka form master data poli.");
        btnDokter.setToolTipText("Buka form master data dokter.");
        btnJadwal.setToolTipText("Buka form jadwal dokter.");
        btnObat.setToolTipText("Buka form master data obat.");
        btnUser.setToolTipText("Buka form manajemen user.");
        btnKunjungan.setToolTipText("Buka form transaksi kunjungan.");
        btnResep.setToolTipText("Buka form resep dan detail obat.");
        btnPembayaran.setToolTipText("Buka form penerimaan pembayaran khusus kasir.");
        btnReportKunjungan.setToolTipText("Buka output laporan kunjungan pasien.");
        btnReportJadwalDokter.setToolTipText("Buka output laporan jadwal dokter.");
        btnReportObat.setToolTipText("Buka output laporan obat.");
        btnReportResep.setToolTipText("Buka output report resep dan detail obat.");
        btnReportPembayaran.setToolTipText("Buka output report pembayaran.");
        btnLogout.setToolTipText("Keluar dari aplikasi.");
    }

    public static void main(String args[]) {
        java.awt.EventQueue.invokeLater(() -> new FormMenu().setVisible(true));
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnDokter;
    private javax.swing.JButton btnJadwal;
    private javax.swing.JButton btnKunjungan;
    private javax.swing.JButton btnLogout;
    private javax.swing.JButton btnObat;
    private javax.swing.JButton btnPasien;
    private javax.swing.JButton btnPembayaran;
    private javax.swing.JButton btnPoli;
    private javax.swing.JButton btnReportJadwalDokter;
    private javax.swing.JButton btnReportKunjungan;
    private javax.swing.JButton btnReportObat;
    private javax.swing.JButton btnReportPembayaran;
    private javax.swing.JButton btnReportResep;
    private javax.swing.JButton btnResep;
    private javax.swing.JButton btnUser;
    private javax.swing.JLabel lblAppSubtitle;
    private javax.swing.JLabel lblAppTitle;
    private javax.swing.JLabel lblDashboardInfo;
    private javax.swing.JLabel lblDashboardTitle;
    private javax.swing.JLabel lblInfo;
    private javax.swing.JLabel lblMaster;
    private javax.swing.JLabel lblReport;
    private javax.swing.JLabel lblTransaksi;
    private javax.swing.JPanel panelContent;
    private javax.swing.JPanel panelSidebar;
    // End of variables declaration//GEN-END:variables
}
