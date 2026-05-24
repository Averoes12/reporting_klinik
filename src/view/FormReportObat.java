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

public class FormReportObat extends JFrame {

    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter
            .ofPattern("uuuu-MM-dd")
            .withResolverStyle(ResolverStyle.STRICT);

    private final Connection conn = new DBConnection().connect();
    private DefaultTableModel tableModel;

    public FormReportObat() {
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
        lblJenis = new javax.swing.JLabel();
        lblTanggalAwal = new javax.swing.JLabel();
        lblTanggalAkhir = new javax.swing.JLabel();
        lblKeyword = new javax.swing.JLabel();
        cmbJenis = new javax.swing.JComboBox();
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
        setTitle("Report Obat");

        lblTitle.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        lblTitle.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblTitle.setText("Report Obat");

        lblJenis.setText("Jenis Report");

        lblTanggalAwal.setText("Tanggal Awal");

        lblTanggalAkhir.setText("Tanggal Akhir");

        lblKeyword.setText("Cari Obat");

        btnTampilkan.setText("Tampilkan");

        btnPreviewJasper.setText("Cetak");

        btnReset.setText("Reset");

        btnKeluar.setText("Keluar");

        panelReport.setBorder(javax.swing.BorderFactory.createTitledBorder("Output Report"));

        tblReport.setRowHeight(24);
        jScrollPaneReport.setViewportView(tblReport);

        lblSummary.setText("Ringkasan obat");

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
                            .addComponent(lblJenis)
                            .addComponent(lblTanggalAwal)
                            .addComponent(lblTanggalAkhir)
                            .addComponent(lblKeyword))
                        .addGap(29, 29, 29)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(cmbJenis, 0, 260, Short.MAX_VALUE)
                            .addComponent(txtTanggalAwal, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
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
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblJenis)
                    .addComponent(cmbJenis, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
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

    private void initComboModels() {
        cmbJenis.setModel(new DefaultComboBoxModel<>(new String[]{"Stok Obat", "Pemakaian Obat"}));
    }

    private void setupActions() {
        btnTampilkan.addActionListener(evt -> loadReport());
        btnPreviewJasper.addActionListener(evt -> previewJasperReport());
        btnReset.addActionListener(evt -> resetFilter());
        btnKeluar.addActionListener(evt -> dispose());
        cmbJenis.addActionListener(evt -> {
            updateDateState();
            loadReport();
        });
        txtKeyword.addActionListener(evt -> loadReport());
    }

    private void initDateListeners() {
        DateChooserHelper.addDateChangeListener(txtTanggalAwal, evt -> loadReport());
        DateChooserHelper.addDateChangeListener(txtTanggalAkhir, evt -> loadReport());
    }

    private void resetFilter() {
        cmbJenis.setSelectedIndex(0);
        DateChooserHelper.setDate(txtTanggalAwal, LocalDate.now().withDayOfMonth(1));
        DateChooserHelper.setDate(txtTanggalAkhir, LocalDate.now());
        txtKeyword.setText("");
        updateDateState();
        loadReport();
    }

    private void updateDateState() {
        boolean pemakaian = "Pemakaian Obat".equals(selectedOrDefault(cmbJenis, "Stok Obat"));
        txtTanggalAwal.setEnabled(pemakaian);
        txtTanggalAkhir.setEnabled(pemakaian);
    }

    private void loadReport() {
        if ("Pemakaian Obat".equals(selectedOrDefault(cmbJenis, "Stok Obat"))) {
            loadPemakaian();
        } else {
            loadStok();
        }
    }

    private void loadStok() {
        tableModel = new DefaultTableModel(null, new Object[]{"ID Obat", "Nama Obat", "Satuan", "Harga", "Stok Awal", "Stok Masuk", "Stok Retur", "Stok Akhir", "Tanggal Expired", "Status Stok"}) {
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        if (conn == null) {
            tblReport.setModel(tableModel);
            return;
        }
        String sql = "SELECT id_obat, nama_obat, satuan, harga, stok_awal, stok_masuk, stok_retur, stok_akhir, tanggal_expired "
                + "FROM obat WHERE id_obat LIKE ? OR nama_obat LIKE ? OR satuan LIKE ? ORDER BY nama_obat";
        int hampirHabis = 0;
        int expired = 0;
        BigDecimal nilaiStok = BigDecimal.ZERO;
        try (PreparedStatement stat = conn.prepareStatement(sql)) {
            String keyword = "%" + txtKeyword.getText().trim() + "%";
            stat.setString(1, keyword);
            stat.setString(2, keyword);
            stat.setString(3, keyword);
            try (ResultSet rs = stat.executeQuery()) {
                while (rs.next()) {
                    int stokAkhir = rs.getInt("stok_akhir");
                    BigDecimal harga = rs.getBigDecimal("harga");
                    Date tanggalExpired = rs.getDate("tanggal_expired");
                    String statusStok = stokAkhir <= 0 ? "Habis" : stokAkhir <= 10 ? "Hampir Habis" : "Aman";
                    if (stokAkhir <= 10) {
                        hampirHabis++;
                    }
                    if (tanggalExpired != null && !tanggalExpired.toLocalDate().isAfter(LocalDate.now())) {
                        expired++;
                    }
                    nilaiStok = nilaiStok.add(harga.multiply(BigDecimal.valueOf(stokAkhir)));
                    tableModel.addRow(new Object[]{
                        rs.getString("id_obat"),
                        rs.getString("nama_obat"),
                        rs.getString("satuan"),
                        harga,
                        rs.getInt("stok_awal"),
                        rs.getInt("stok_masuk"),
                        rs.getInt("stok_retur"),
                        stokAkhir,
                        tanggalExpired,
                        statusStok
                    });
                }
            }
            tblReport.setModel(tableModel);
            setWidths(new int[]{100, 190, 90, 110, 90, 90, 90, 90, 120, 120});
            lblSummary.setText("Jumlah obat: " + tableModel.getRowCount()
                    + " | Hampir/habis: " + hampirHabis
                    + " | Expired: " + expired
                    + " | Nilai stok: " + nilaiStok.toPlainString());
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Report stok obat gagal dipanggil: " + e.getMessage());
        }
    }

    private void loadPemakaian() {
        tableModel = new DefaultTableModel(null, new Object[]{"Tanggal Resep", "ID Obat", "Nama Obat", "Satuan", "Jumlah Terpakai", "Harga Satuan", "Total Nilai"}) {
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
        String sql = "SELECT r.tanggal_resep, o.id_obat, o.nama_obat, o.satuan, SUM(rd.jumlah) AS jumlah_terpakai, "
                + "rd.harga_satuan, SUM(rd.subtotal) AS total_nilai "
                + "FROM resep r "
                + "JOIN resep_detail rd ON rd.id_resep = r.id_resep "
                + "JOIN obat o ON o.id_obat = rd.id_obat "
                + "WHERE r.tanggal_resep BETWEEN ? AND ? "
                + "AND (o.id_obat LIKE ? OR o.nama_obat LIKE ? OR o.satuan LIKE ?) "
                + "GROUP BY r.tanggal_resep, o.id_obat, o.nama_obat, o.satuan, rd.harga_satuan "
                + "ORDER BY r.tanggal_resep DESC, o.nama_obat";
        int totalJumlah = 0;
        BigDecimal totalNilai = BigDecimal.ZERO;
        try (PreparedStatement stat = conn.prepareStatement(sql)) {
            String keyword = "%" + txtKeyword.getText().trim() + "%";
            stat.setDate(1, awal);
            stat.setDate(2, akhir);
            stat.setString(3, keyword);
            stat.setString(4, keyword);
            stat.setString(5, keyword);
            try (ResultSet rs = stat.executeQuery()) {
                while (rs.next()) {
                    int jumlah = rs.getInt("jumlah_terpakai");
                    BigDecimal nilai = rs.getBigDecimal("total_nilai");
                    totalJumlah += jumlah;
                    totalNilai = totalNilai.add(nilai);
                    tableModel.addRow(new Object[]{
                        rs.getDate("tanggal_resep"),
                        rs.getString("id_obat"),
                        rs.getString("nama_obat"),
                        rs.getString("satuan"),
                        jumlah,
                        rs.getBigDecimal("harga_satuan"),
                        nilai
                    });
                }
            }
            tblReport.setModel(tableModel);
            setWidths(new int[]{110, 100, 210, 100, 120, 120, 130});
            lblSummary.setText("Baris pemakaian: " + tableModel.getRowCount()
                    + " | Total jumlah terpakai: " + totalJumlah
                    + " | Total nilai: " + totalNilai.toPlainString());
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Report pemakaian obat gagal dipanggil: " + e.getMessage());
        }
    }

    private void previewJasperReport() {
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("KEYWORD", "%" + txtKeyword.getText().trim() + "%");

        String reportPath = "/report/report_stok_obat.jasper";
        if ("Pemakaian Obat".equals(selectedOrDefault(cmbJenis, "Stok Obat"))) {
            Date awal = parseDate(DateChooserHelper.getText(txtTanggalAwal).trim(), "Tanggal awal");
            Date akhir = parseDate(DateChooserHelper.getText(txtTanggalAkhir).trim(), "Tanggal akhir");
            if (awal == null || akhir == null || awal.after(akhir)) {
                if (awal != null && akhir != null) {
                    JOptionPane.showMessageDialog(this, "Tanggal awal tidak boleh lebih besar dari tanggal akhir.");
                }
                return;
            }
            parameters.put("TANGGAL_AWAL", awal);
            parameters.put("TANGGAL_AKHIR", akhir);
            reportPath = "/report/report_pemakaian_obat.jasper";
        }

        JasperReportHelper.previewReport(this, reportPath, parameters, conn);
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
        java.awt.EventQueue.invokeLater(() -> new FormReportObat().setVisible(true));
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnKeluar;
    private javax.swing.JButton btnPreviewJasper;
    private javax.swing.JButton btnReset;
    private javax.swing.JButton btnTampilkan;
    private javax.swing.JComboBox cmbJenis;
    private javax.swing.JScrollPane jScrollPaneReport;
    private javax.swing.JLabel lblJenis;
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
