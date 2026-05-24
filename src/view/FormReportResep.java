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
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

public class FormReportResep extends JFrame {

    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter
            .ofPattern("uuuu-MM-dd")
            .withResolverStyle(ResolverStyle.STRICT);

    private final Connection conn = new DBConnection().connect();
    private DefaultTableModel tableModel;

    public FormReportResep() {
        initComponents();
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
        lblKeyword = new javax.swing.JLabel();
        txtTanggalAwal = new com.toedter.calendar.JDateChooser();
        txtTanggalAkhir = new com.toedter.calendar.JDateChooser();
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
        setTitle("Report Resep");

        lblTitle.setFont(new java.awt.Font("Segoe UI", 1, 18));
        lblTitle.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblTitle.setText("Report Resep dan Detail Obat");

        lblTanggalAwal.setText("Tanggal Awal");
        lblTanggalAkhir.setText("Tanggal Akhir");
        lblKeyword.setText("Cari Resep/Pasien/Dokter/Obat");

        txtTanggalAwal.setDateFormatString("yyyy-MM-dd");

        txtTanggalAkhir.setDateFormatString("yyyy-MM-dd");

        btnTampilkan.setText("Tampilkan");
        btnPreviewJasper.setText("Cetak");
        btnReset.setText("Reset");
        btnKeluar.setText("Keluar");

        panelReport.setBorder(javax.swing.BorderFactory.createTitledBorder("Output Report"));

        tblReport.setRowHeight(24);
        tblReport.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_OFF);
        jScrollPaneReport.setViewportView(tblReport);

        lblSummary.setText("Ringkasan resep");

        javax.swing.GroupLayout panelReportLayout = new javax.swing.GroupLayout(panelReport);
        panelReport.setLayout(panelReportLayout);
        panelReportLayout.setHorizontalGroup(
            panelReportLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelReportLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(panelReportLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPaneReport, javax.swing.GroupLayout.DEFAULT_SIZE, 1164, Short.MAX_VALUE)
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
                            .addComponent(lblKeyword))
                        .addGap(29, 29, 29)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(txtTanggalAwal, javax.swing.GroupLayout.DEFAULT_SIZE, 260, Short.MAX_VALUE)
                            .addComponent(txtTanggalAkhir, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
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

    private void setupActions() {
        btnTampilkan.addActionListener(evt -> loadReport());
        btnPreviewJasper.addActionListener(evt -> previewJasperReport());
        btnReset.addActionListener(evt -> resetFilter());
        btnKeluar.addActionListener(evt -> dispose());
        txtKeyword.addActionListener(evt -> loadReport());
    }

    private void initDateListeners() {
        DateChooserHelper.addDateChangeListener(txtTanggalAwal, evt -> loadReport());
        DateChooserHelper.addDateChangeListener(txtTanggalAkhir, evt -> loadReport());
    }

    private void resetFilter() {
        DateChooserHelper.setDate(txtTanggalAwal, LocalDate.now().withDayOfMonth(1));
        DateChooserHelper.setDate(txtTanggalAkhir, LocalDate.now());
        txtKeyword.setText("");
        loadReport();
    }

    private void loadReport() {
        tableModel = new DefaultTableModel(null, new Object[]{
            "Tanggal Resep", "ID Resep", "No Kunjungan", "Pasien", "Dokter", "Poli",
            "ID Detail", "ID Obat", "Nama Obat", "Jumlah", "Aturan Pakai",
            "Harga Satuan", "Subtotal", "Catatan"
        }) {
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

        String sql = "SELECT r.tanggal_resep, r.id_resep, r.no_kunjungan, ps.nama_pasien, "
                + "d.nama_dokter, po.nama_poli, rd.id_resep_detail, o.id_obat, o.nama_obat, "
                + "rd.jumlah, rd.aturan_pakai, rd.harga_satuan, rd.subtotal, r.catatan "
                + "FROM resep r "
                + "JOIN kunjungan k ON k.no_kunjungan = r.no_kunjungan "
                + "JOIN pasien ps ON ps.id_pasien = k.id_pasien "
                + "JOIN dokter d ON d.id_dokter = k.id_dokter "
                + "JOIN poli po ON po.id_poli = k.id_poli "
                + "JOIN resep_detail rd ON rd.id_resep = r.id_resep "
                + "JOIN obat o ON o.id_obat = rd.id_obat "
                + "WHERE r.tanggal_resep BETWEEN ? AND ? "
                + "AND (r.id_resep LIKE ? OR r.no_kunjungan LIKE ? OR ps.nama_pasien LIKE ? "
                + "OR d.nama_dokter LIKE ? OR po.nama_poli LIKE ? OR o.id_obat LIKE ? OR o.nama_obat LIKE ?) "
                + "ORDER BY r.tanggal_resep DESC, r.id_resep DESC, rd.id_resep_detail";

        Set<String> resepIds = new HashSet<>();
        int totalJumlah = 0;
        BigDecimal totalNilai = BigDecimal.ZERO;
        try (PreparedStatement stat = conn.prepareStatement(sql)) {
            String keyword = "%" + txtKeyword.getText().trim() + "%";
            stat.setDate(1, awal);
            stat.setDate(2, akhir);
            for (int i = 3; i <= 9; i++) {
                stat.setString(i, keyword);
            }
            try (ResultSet rs = stat.executeQuery()) {
                while (rs.next()) {
                    String idResep = rs.getString("id_resep");
                    int jumlah = rs.getInt("jumlah");
                    BigDecimal subtotal = rs.getBigDecimal("subtotal");
                    resepIds.add(idResep);
                    totalJumlah += jumlah;
                    totalNilai = totalNilai.add(subtotal);
                    tableModel.addRow(new Object[]{
                        rs.getDate("tanggal_resep"),
                        idResep,
                        rs.getString("no_kunjungan"),
                        rs.getString("nama_pasien"),
                        rs.getString("nama_dokter"),
                        rs.getString("nama_poli"),
                        rs.getString("id_resep_detail"),
                        rs.getString("id_obat"),
                        rs.getString("nama_obat"),
                        jumlah,
                        rs.getString("aturan_pakai"),
                        rs.getBigDecimal("harga_satuan"),
                        subtotal,
                        rs.getString("catatan")
                    });
                }
            }
            tblReport.setModel(tableModel);
            setWidths(new int[]{110, 90, 130, 170, 160, 130, 100, 90, 190, 70, 170, 110, 110, 220});
            lblSummary.setText("Jumlah resep: " + resepIds.size()
                    + " | Baris detail: " + tableModel.getRowCount()
                    + " | Total jumlah obat: " + totalJumlah
                    + " | Total nilai obat: " + totalNilai.toPlainString());
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Report resep gagal dipanggil: " + e.getMessage());
        }
    }

    private void previewJasperReport() {
        String idResep = selectedResepIdForPrint();
        if (idResep.isEmpty()) {
            return;
        }
        if (!hasPrintableResepDetail(idResep)) {
            JOptionPane.showMessageDialog(this, "Detail obat untuk resep " + idResep + " tidak ditemukan.");
            return;
        }

        Map<String, Object> parameters = new HashMap<>();
        parameters.put("ID_RESEP", idResep);
        JasperReportHelper.previewReport(this, "/report/report_resep.jrxml", parameters, conn);
    }

    private boolean hasPrintableResepDetail(String idResep) {
        if (conn == null) {
            return false;
        }
        String sql = "SELECT COUNT(*) FROM resep_detail WHERE id_resep = ?";
        try (PreparedStatement stat = conn.prepareStatement(sql)) {
            stat.setString(1, idResep);
            try (ResultSet rs = stat.executeQuery()) {
                return rs.next() && rs.getInt(1) > 0;
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Validasi detail resep gagal: " + e.getMessage());
            return false;
        }
    }

    private String selectedResepIdForPrint() {
        if (tableModel == null || tableModel.getRowCount() == 0) {
            JOptionPane.showMessageDialog(this, "Tidak ada data resep untuk dicetak.");
            return "";
        }

        int selectedRow = tblReport.getSelectedRow();
        if (selectedRow >= 0) {
            int modelRow = tblReport.convertRowIndexToModel(selectedRow);
            return tableModel.getValueAt(modelRow, 1).toString();
        }

        Set<String> resepIds = new HashSet<>();
        for (int i = 0; i < tableModel.getRowCount(); i++) {
            resepIds.add(tableModel.getValueAt(i, 1).toString());
        }
        if (resepIds.size() == 1) {
            return resepIds.iterator().next();
        }

        JOptionPane.showMessageDialog(this, "Pilih salah satu baris resep terlebih dahulu agar cetak hanya untuk satu pasien.");
        return "";
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

    public static void main(String args[]) {
        java.awt.EventQueue.invokeLater(() -> new FormReportResep().setVisible(true));
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnKeluar;
    private javax.swing.JButton btnPreviewJasper;
    private javax.swing.JButton btnReset;
    private javax.swing.JButton btnTampilkan;
    private javax.swing.JScrollPane jScrollPaneReport;
    private javax.swing.JLabel lblKeyword;
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
