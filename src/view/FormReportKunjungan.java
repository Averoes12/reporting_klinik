package view;

import connection.DBConnection;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.format.ResolverStyle;
import java.util.HashMap;
import java.util.Map;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

public class FormReportKunjungan extends JFrame {

    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter
            .ofPattern("uuuu-MM-dd")
            .withResolverStyle(ResolverStyle.STRICT);

    private final Connection conn = new DBConnection().connect();
    private DefaultTableModel tableModel;

    public FormReportKunjungan() {
        initComponents();
        initComboModels();
        setupActions();
        resetFilter();
        initDateListeners();
        setLocationRelativeTo(null);
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        lblTitle = new javax.swing.JLabel();
        lblTanggalAwal = new javax.swing.JLabel();
        lblTanggalAkhir = new javax.swing.JLabel();
        lblStatus = new javax.swing.JLabel();
        lblKeyword = new javax.swing.JLabel();
        txtTanggalAwal = new com.toedter.calendar.JDateChooser();
        txtTanggalAkhir = new com.toedter.calendar.JDateChooser();
        cmbStatus = new javax.swing.JComboBox();
        txtKeyword = new javax.swing.JTextField();
        btnTampilkan = new javax.swing.JButton();
        btnPreviewJasper = new javax.swing.JButton();
        btnReset = new javax.swing.JButton();
        btnKeluar = new javax.swing.JButton();
        panelReport = new javax.swing.JPanel();
        jScrollPaneReport = new javax.swing.JScrollPane();
        tblReport = new javax.swing.JTable();
        lblSummary = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Report Kunjungan Pasien");

        lblTitle.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        lblTitle.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblTitle.setText("Report Kunjungan Pasien");

        lblTanggalAwal.setText("Tanggal Awal");

        lblTanggalAkhir.setText("Tanggal Akhir");

        lblStatus.setText("Status");

        lblKeyword.setText("Cari Pasien/Dokter/Poli");

        btnTampilkan.setText("Tampilkan");

        btnPreviewJasper.setText("Cetak");
        btnPreviewJasper.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnPreviewJasperActionPerformed(evt);
            }
        });

        btnReset.setText("Reset");

        btnKeluar.setText("Keluar");

        panelReport.setBorder(javax.swing.BorderFactory.createTitledBorder("Output Report"));

        tblReport.setRowHeight(24);
        jScrollPaneReport.setViewportView(tblReport);

        lblSummary.setText("Ringkasan kunjungan");

        javax.swing.GroupLayout panelReportLayout = new javax.swing.GroupLayout(panelReport);
        panelReport.setLayout(panelReportLayout);
        panelReportLayout.setHorizontalGroup(
            panelReportLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelReportLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(panelReportLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPaneReport, javax.swing.GroupLayout.DEFAULT_SIZE, 1064, Short.MAX_VALUE)
                    .addComponent(lblSummary, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        panelReportLayout.setVerticalGroup(
            panelReportLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelReportLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPaneReport, javax.swing.GroupLayout.PREFERRED_SIZE, 340, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(lblSummary)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(24, 24, 24)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(lblTitle, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(lblTanggalAwal)
                            .addComponent(lblTanggalAkhir)
                            .addComponent(lblStatus)
                            .addComponent(lblKeyword))
                        .addGap(29, 29, 29)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(txtTanggalAwal, javax.swing.GroupLayout.DEFAULT_SIZE, 260, Short.MAX_VALUE)
                            .addComponent(txtTanggalAkhir, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(cmbStatus, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(txtKeyword)))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(btnTampilkan)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnPreviewJasper)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnReset)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnKeluar))
                    .addComponent(panelReport, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(24, 24, 24))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(20, 20, 20)
                .addComponent(lblTitle)
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lblTanggalAwal)
                    .addComponent(txtTanggalAwal, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lblTanggalAkhir)
                    .addComponent(txtTanggalAkhir, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblStatus)
                    .addComponent(cmbStatus, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblKeyword)
                    .addComponent(txtKeyword, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnTampilkan)
                    .addComponent(btnPreviewJasper)
                    .addComponent(btnReset)
                    .addComponent(btnKeluar))
                .addGap(18, 18, 18)
                .addComponent(panelReport, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnPreviewJasperActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnPreviewJasperActionPerformed
        previewJasperReport();
    }//GEN-LAST:event_btnPreviewJasperActionPerformed

    private void initComboModels() {
        cmbStatus.setModel(new DefaultComboBoxModel<>(new String[]{"Semua", "Menunggu", "Diperiksa", "Selesai", "Batal"}));
    }

    private void setupActions() {
        btnTampilkan.addActionListener(evt -> loadReport());
        btnReset.addActionListener(evt -> resetFilter());
        btnKeluar.addActionListener(evt -> dispose());
        txtKeyword.addActionListener(evt -> loadReport());
        cmbStatus.addActionListener(evt -> loadReport());
    }

    private void initDateListeners() {
        DateChooserHelper.addDateChangeListener(txtTanggalAwal, evt -> loadReport());
        DateChooserHelper.addDateChangeListener(txtTanggalAkhir, evt -> loadReport());
    }

    private void resetFilter() {
        DateChooserHelper.setDate(txtTanggalAwal, LocalDate.now().withDayOfMonth(1));
        DateChooserHelper.setDate(txtTanggalAkhir, LocalDate.now());
        txtKeyword.setText("");
        cmbStatus.setSelectedIndex(0);
        loadReport();
    }

    private void loadReport() {
        tableModel = new DefaultTableModel(null, new Object[]{"Tanggal", "No Kunjungan", "Pasien", "Jenis Kelamin", "Dokter", "Poli", "Keluhan", "Status"}) {
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        if (conn == null) {
            tblReport.setModel(tableModel);
            return;
        }
        Date awal = parseDate(DateChooserHelper.getText(txtTanggalAwal).trim(), "Tanggal awal");
        Date akhir = parseDate(DateChooserHelper.getText(txtTanggalAkhir).trim(), "Tanggal akhir");
        if (awal == null || akhir == null || awal.after(akhir)) {
            if (awal != null && akhir != null) {
                JOptionPane.showMessageDialog(this, "Tanggal awal tidak boleh lebih besar dari tanggal akhir.");
            }
            return;
        }

        String sql = "SELECT k.tanggal_kunjungan, k.no_kunjungan, ps.nama_pasien, ps.jenis_kelamin, "
                + "d.nama_dokter, po.nama_poli, k.keluhan, k.status "
                + "FROM kunjungan k "
                + "JOIN pasien ps ON ps.id_pasien = k.id_pasien "
                + "JOIN dokter d ON d.id_dokter = k.id_dokter "
                + "JOIN poli po ON po.id_poli = k.id_poli "
                + "WHERE k.tanggal_kunjungan BETWEEN ? AND ? "
                + "AND (? = 'Semua' OR k.status = ?) "
                + "AND (ps.nama_pasien LIKE ? OR d.nama_dokter LIKE ? OR po.nama_poli LIKE ? OR k.no_kunjungan LIKE ?) "
                + "ORDER BY k.tanggal_kunjungan DESC, k.no_kunjungan DESC";
        int selesai = 0;
        int batal = 0;
        try (PreparedStatement stat = conn.prepareStatement(sql)) {
            String status = selectedOrDefault(cmbStatus, "Semua");
            String keyword = "%" + txtKeyword.getText().trim() + "%";
            stat.setDate(1, awal);
            stat.setDate(2, akhir);
            stat.setString(3, status);
            stat.setString(4, status);
            for (int i = 5; i <= 8; i++) {
                stat.setString(i, keyword);
            }
            try (ResultSet rs = stat.executeQuery()) {
                while (rs.next()) {
                    String rowStatus = rs.getString("status");
                    if ("Selesai".equalsIgnoreCase(rowStatus)) {
                        selesai++;
                    } else if ("Batal".equalsIgnoreCase(rowStatus)) {
                        batal++;
                    }
                    tableModel.addRow(new Object[]{
                        rs.getDate("tanggal_kunjungan"),
                        rs.getString("no_kunjungan"),
                        rs.getString("nama_pasien"),
                        rs.getString("jenis_kelamin"),
                        rs.getString("nama_dokter"),
                        rs.getString("nama_poli"),
                        rs.getString("keluhan"),
                        rowStatus
                    });
                }
            }
            tblReport.setModel(tableModel);
            setWidths(new int[]{100, 120, 170, 110, 160, 140, 260, 100});
            lblSummary.setText("Jumlah kunjungan: " + tableModel.getRowCount() + " | Selesai: " + selesai + " | Batal: " + batal);
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Report kunjungan gagal dipanggil: " + e.getMessage());
        }
    }

    private void previewJasperReport() {
        Date awal = parseDate(DateChooserHelper.getText(txtTanggalAwal).trim(), "Tanggal awal");
        Date akhir = parseDate(DateChooserHelper.getText(txtTanggalAkhir).trim(), "Tanggal akhir");
        if (awal == null || akhir == null || awal.after(akhir)) {
            if (awal != null && akhir != null) {
                JOptionPane.showMessageDialog(this, "Tanggal awal tidak boleh lebih besar dari tanggal akhir.");
            }
            return;
        }

        Map<String, Object> parameters = new HashMap<>();
        parameters.put("TANGGAL_AWAL", awal);
        parameters.put("TANGGAL_AKHIR", akhir);
        parameters.put("STATUS", selectedOrDefault(cmbStatus, "Semua"));
        parameters.put("KEYWORD", "%" + txtKeyword.getText().trim() + "%");
        JasperReportHelper.previewReport(this, "/report/report_kunjungan.jasper", parameters, conn);
    }

    private Date parseDate(String value, String fieldName) {
        if (!value.matches("\\d{4}-\\d{2}-\\d{2}")) {
            JOptionPane.showMessageDialog(this, fieldName + " harus berformat yyyy-MM-dd.");
            return null;
        }
        try {
            return Date.valueOf(LocalDate.parse(value, DATE_FORMATTER));
        } catch (DateTimeParseException e) {
            JOptionPane.showMessageDialog(this, fieldName + " tidak valid.");
            return null;
        }
    }

    private void setWidths(int[] widths) {
        for (int i = 0; i < widths.length; i++) {
            tblReport.getColumnModel().getColumn(i).setPreferredWidth(widths[i]);
        }
    }

    private String selectedOrDefault(javax.swing.JComboBox comboBox, String defaultValue) {
        Object selected = comboBox.getSelectedItem();
        return selected == null ? defaultValue : selected.toString();
    }

    public static void main(String args[]) {
        java.awt.EventQueue.invokeLater(() -> new FormReportKunjungan().setVisible(true));
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnKeluar;
    private javax.swing.JButton btnPreviewJasper;
    private javax.swing.JButton btnReset;
    private javax.swing.JButton btnTampilkan;
    private javax.swing.JComboBox cmbStatus;
    private javax.swing.JScrollPane jScrollPaneReport;
    private javax.swing.JLabel lblKeyword;
    private javax.swing.JLabel lblStatus;
    private javax.swing.JLabel lblSummary;
    private javax.swing.JLabel lblTanggalAkhir;
    private javax.swing.JLabel lblTanggalAwal;
    private javax.swing.JLabel lblTitle;
    private javax.swing.JPanel panelReport;
    private javax.swing.JTable tblReport;
    private javax.swing.JTextField txtKeyword;
    private com.toedter.calendar.JDateChooser txtTanggalAkhir;
    private com.toedter.calendar.JDateChooser txtTanggalAwal;
    // End of variables declaration//GEN-END:variables
}
