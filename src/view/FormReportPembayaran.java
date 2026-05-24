package view;

import connection.DBConnection;
import java.math.BigDecimal;
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

public class FormReportPembayaran extends JFrame {

    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter
            .ofPattern("uuuu-MM-dd")
            .withResolverStyle(ResolverStyle.STRICT);

    private final Connection conn = new DBConnection().connect();
    private DefaultTableModel tableModel;

    public FormReportPembayaran() {
        initComponents();
        initComboModels();
        setupActions();
        DateChooserHelper.setDate(txtTanggalAwal, LocalDate.now().withDayOfMonth(1));
        DateChooserHelper.setDate(txtTanggalAkhir, LocalDate.now());
        initDateListeners();
        loadReport();
        setLocationRelativeTo(null);
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        lblTitle = new javax.swing.JLabel();
        lblTanggalAwal = new javax.swing.JLabel();
        lblTanggalAkhir = new javax.swing.JLabel();
        lblStatus = new javax.swing.JLabel();
        lblMetode = new javax.swing.JLabel();
        txtTanggalAwal = new com.toedter.calendar.JDateChooser();
        txtTanggalAkhir = new com.toedter.calendar.JDateChooser();
        cmbStatus = new javax.swing.JComboBox();
        cmbMetode = new javax.swing.JComboBox();
        btnTampilkan = new javax.swing.JButton();
        btnPreviewJasper = new javax.swing.JButton();
        btnReset = new javax.swing.JButton();
        btnKeluar = new javax.swing.JButton();
        panelReport = new javax.swing.JPanel();
        jScrollPaneReport = new javax.swing.JScrollPane();
        tblReport = new javax.swing.JTable();
        lblSummary = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Report Pembayaran");

        lblTitle.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        lblTitle.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblTitle.setText("Report Pembayaran");

        lblTanggalAwal.setText("Tanggal Awal");

        lblTanggalAkhir.setText("Tanggal Akhir");

        lblStatus.setText("Status Pembayaran");

        lblMetode.setText("Metode Pembayaran");

        btnTampilkan.setText("Tampilkan");

        btnPreviewJasper.setText("Cetak");

        btnReset.setText("Reset");

        btnKeluar.setText("Keluar");

        panelReport.setBorder(javax.swing.BorderFactory.createTitledBorder("Output Report"));

        tblReport.setRowHeight(24);
        jScrollPaneReport.setViewportView(tblReport);

        lblSummary.setText("Ringkasan pembayaran");

        javax.swing.GroupLayout panelReportLayout = new javax.swing.GroupLayout(panelReport);
        panelReport.setLayout(panelReportLayout);
        panelReportLayout.setHorizontalGroup(
            panelReportLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelReportLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(panelReportLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPaneReport, javax.swing.GroupLayout.DEFAULT_SIZE, 1104, Short.MAX_VALUE)
                    .addComponent(lblSummary, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        panelReportLayout.setVerticalGroup(
            panelReportLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelReportLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPaneReport, javax.swing.GroupLayout.PREFERRED_SIZE, 360, javax.swing.GroupLayout.PREFERRED_SIZE)
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
                            .addComponent(lblMetode))
                        .addGap(29, 29, 29)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(txtTanggalAwal, javax.swing.GroupLayout.DEFAULT_SIZE, 260, Short.MAX_VALUE)
                            .addComponent(txtTanggalAkhir, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(cmbStatus, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(cmbMetode, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
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
                    .addComponent(lblMetode)
                    .addComponent(cmbMetode, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
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

    private void initComboModels() {
        cmbStatus.setModel(new DefaultComboBoxModel<>(new String[]{"Semua", "Lunas", "Belum Lunas"}));
        cmbMetode.setModel(new DefaultComboBoxModel<>(new String[]{"Semua", "Tunai", "Transfer", "QRIS", "Debit", "Asuransi"}));
    }

    private void setupActions() {
        btnTampilkan.addActionListener(evt -> loadReport());
        btnPreviewJasper.addActionListener(evt -> previewJasperReport());
        btnReset.addActionListener(evt -> resetFilter());
        btnKeluar.addActionListener(evt -> dispose());
        cmbStatus.addActionListener(evt -> loadReport());
        cmbMetode.addActionListener(evt -> loadReport());
    }

    private void initDateListeners() {
        DateChooserHelper.addDateChangeListener(txtTanggalAwal, evt -> loadReport());
        DateChooserHelper.addDateChangeListener(txtTanggalAkhir, evt -> loadReport());
    }

    private void resetFilter() {
        DateChooserHelper.setDate(txtTanggalAwal, LocalDate.now().withDayOfMonth(1));
        DateChooserHelper.setDate(txtTanggalAkhir, LocalDate.now());
        cmbStatus.setSelectedIndex(0);
        cmbMetode.setSelectedIndex(0);
        loadReport();
    }

    private void loadReport() {
        Object[] columns = {"Tanggal", "ID Pembayaran", "No Kunjungan", "Pasien", "Dokter", "Poli", "Konsultasi", "Tindakan", "Obat", "Total", "Bayar", "Kembalian", "Metode", "Status"};
        tableModel = new DefaultTableModel(null, columns) {
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
        if (awal == null || akhir == null) {
            return;
        }
        if (awal.after(akhir)) {
            JOptionPane.showMessageDialog(this, "Tanggal awal tidak boleh lebih besar dari tanggal akhir.");
            return;
        }

        String sql = "SELECT p.tanggal_pembayaran, p.id_pembayaran, p.no_kunjungan, ps.nama_pasien, d.nama_dokter, po.nama_poli, "
                + "p.biaya_konsultasi, p.biaya_tindakan, p.biaya_obat, p.total_tagihan, p.jumlah_bayar, p.kembalian, "
                + "p.metode_pembayaran, p.status_pembayaran "
                + "FROM pembayaran p "
                + "JOIN kunjungan k ON k.no_kunjungan = p.no_kunjungan "
                + "JOIN pasien ps ON ps.id_pasien = k.id_pasien "
                + "JOIN dokter d ON d.id_dokter = k.id_dokter "
                + "JOIN poli po ON po.id_poli = k.id_poli "
                + "WHERE p.tanggal_pembayaran BETWEEN ? AND ? "
                + "AND (? = 'Semua' OR p.status_pembayaran = ?) "
                + "AND (? = 'Semua' OR p.metode_pembayaran = ?) "
                + "ORDER BY p.tanggal_pembayaran DESC, p.id_pembayaran DESC";

        BigDecimal totalTagihan = BigDecimal.ZERO;
        BigDecimal totalBayar = BigDecimal.ZERO;
        BigDecimal totalKembalian = BigDecimal.ZERO;
        try (PreparedStatement stat = conn.prepareStatement(sql)) {
            String status = selectedOrDefault(cmbStatus, "Semua");
            String metode = selectedOrDefault(cmbMetode, "Semua");
            stat.setDate(1, awal);
            stat.setDate(2, akhir);
            stat.setString(3, status);
            stat.setString(4, status);
            stat.setString(5, metode);
            stat.setString(6, metode);
            try (ResultSet rs = stat.executeQuery()) {
                while (rs.next()) {
                    BigDecimal tagihan = rs.getBigDecimal("total_tagihan");
                    BigDecimal bayar = rs.getBigDecimal("jumlah_bayar");
                    BigDecimal kembalian = rs.getBigDecimal("kembalian");
                    totalTagihan = totalTagihan.add(tagihan);
                    totalBayar = totalBayar.add(bayar);
                    totalKembalian = totalKembalian.add(kembalian);
                    tableModel.addRow(new Object[]{
                        rs.getDate("tanggal_pembayaran"),
                        rs.getString("id_pembayaran"),
                        rs.getString("no_kunjungan"),
                        rs.getString("nama_pasien"),
                        rs.getString("nama_dokter"),
                        rs.getString("nama_poli"),
                        rs.getBigDecimal("biaya_konsultasi"),
                        rs.getBigDecimal("biaya_tindakan"),
                        rs.getBigDecimal("biaya_obat"),
                        tagihan,
                        bayar,
                        kembalian,
                        rs.getString("metode_pembayaran"),
                        rs.getString("status_pembayaran")
                    });
                }
            }
            tblReport.setModel(tableModel);
            int[] widths = {100, 120, 120, 170, 160, 140, 110, 110, 110, 120, 120, 120, 120, 120};
            for (int i = 0; i < widths.length; i++) {
                tblReport.getColumnModel().getColumn(i).setPreferredWidth(widths[i]);
            }
            lblSummary.setText("Jumlah transaksi: " + tableModel.getRowCount()
                    + " | Total tagihan: " + totalTagihan.toPlainString()
                    + " | Total bayar: " + totalBayar.toPlainString()
                    + " | Total kembalian: " + totalKembalian.toPlainString());
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Report pembayaran gagal dipanggil: " + e.getMessage());
        }
    }

    private void previewJasperReport() {
        Date awal = parseDate(DateChooserHelper.getText(txtTanggalAwal).trim(), "Tanggal awal");
        Date akhir = parseDate(DateChooserHelper.getText(txtTanggalAkhir).trim(), "Tanggal akhir");
        if (awal == null || akhir == null) {
            return;
        }
        if (awal.after(akhir)) {
            JOptionPane.showMessageDialog(this, "Tanggal awal tidak boleh lebih besar dari tanggal akhir.");
            return;
        }

        Map<String, Object> parameters = new HashMap<>();
        parameters.put("TANGGAL_AWAL", awal);
        parameters.put("TANGGAL_AKHIR", akhir);
        parameters.put("STATUS", selectedOrDefault(cmbStatus, "Semua"));
        parameters.put("METODE", selectedOrDefault(cmbMetode, "Semua"));
        JasperReportHelper.previewReport(this, "/report/report_pembayaran.jasper", parameters, conn);
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

    private String selectedOrDefault(javax.swing.JComboBox comboBox, String defaultValue) {
        Object selected = comboBox.getSelectedItem();
        return selected == null ? defaultValue : selected.toString();
    }

    public static void main(String args[]) {
        java.awt.EventQueue.invokeLater(() -> new FormReportPembayaran().setVisible(true));
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnKeluar;
    private javax.swing.JButton btnPreviewJasper;
    private javax.swing.JButton btnReset;
    private javax.swing.JButton btnTampilkan;
    private javax.swing.JComboBox cmbMetode;
    private javax.swing.JComboBox cmbStatus;
    private javax.swing.JScrollPane jScrollPaneReport;
    private javax.swing.JLabel lblMetode;
    private javax.swing.JLabel lblStatus;
    private javax.swing.JLabel lblSummary;
    private javax.swing.JLabel lblTanggalAkhir;
    private javax.swing.JLabel lblTanggalAwal;
    private javax.swing.JLabel lblTitle;
    private javax.swing.JPanel panelReport;
    private javax.swing.JTable tblReport;
    private com.toedter.calendar.JDateChooser txtTanggalAkhir;
    private com.toedter.calendar.JDateChooser txtTanggalAwal;
    // End of variables declaration//GEN-END:variables
}
