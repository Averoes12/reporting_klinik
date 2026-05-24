package view;

import connection.DBConnection;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

public class FormReportJadwalDokter extends JFrame {

    private final Connection conn = new DBConnection().connect();
    private DefaultTableModel tableModel;

    public FormReportJadwalDokter() {
        initComponents();
        setupActions();
        resetFilter();
        setLocationRelativeTo(null);
    }

    @SuppressWarnings("unchecked")
    private void initComponents() {
        lblTitle = new javax.swing.JLabel();
        lblHari = new javax.swing.JLabel();
        lblKeyword = new javax.swing.JLabel();
        cmbHari = new javax.swing.JComboBox<>();
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
        setTitle("Report Jadwal Dokter");

        lblTitle.setFont(new java.awt.Font("Segoe UI", 1, 18));
        lblTitle.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblTitle.setText("Report Jadwal Dokter");

        lblHari.setText("Hari");
        lblKeyword.setText("Cari Dokter/Poli/Jadwal");

        cmbHari.setModel(new DefaultComboBoxModel<>(new String[]{
            "Semua", "Senin", "Selasa", "Rabu", "Kamis", "Jumat", "Sabtu", "Minggu"
        }));

        btnTampilkan.setText("Tampilkan");
        btnPreviewJasper.setText("Cetak");
        btnReset.setText("Reset");
        btnKeluar.setText("Keluar");

        panelReport.setBorder(javax.swing.BorderFactory.createTitledBorder("Output Report"));

        tblReport.setRowHeight(24);
        jScrollPaneReport.setViewportView(tblReport);

        lblSummary.setText("Ringkasan jadwal dokter");

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
                                                        .addComponent(lblHari)
                                                        .addComponent(lblKeyword))
                                                .addGap(29, 29, 29)
                                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                                        .addComponent(cmbHari, 0, 260, Short.MAX_VALUE)
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
                                        .addComponent(lblHari)
                                        .addComponent(cmbHari, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
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
    }

    private void setupActions() {
        btnTampilkan.addActionListener(evt -> loadReport());
        btnPreviewJasper.addActionListener(evt -> previewJasperReport());
        btnReset.addActionListener(evt -> resetFilter());
        btnKeluar.addActionListener(evt -> dispose());
        cmbHari.addActionListener(evt -> loadReport());
        txtKeyword.addActionListener(evt -> loadReport());
    }

    private void resetFilter() {
        cmbHari.setSelectedIndex(0);
        txtKeyword.setText("");
        loadReport();
    }

    private void loadReport() {
        tableModel = new DefaultTableModel(null, new Object[]{"ID Jadwal", "Dokter", "Poli", "Hari", "Jam Mulai", "Jam Selesai", "Kuota"}) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        if (conn == null) {
            tblReport.setModel(tableModel);
            return;
        }

        String sql = "SELECT j.id_jadwal, d.nama_dokter, p.nama_poli, j.hari, j.jam_mulai, j.jam_selesai, j.kuota "
                + "FROM jadwal_dokter j "
                + "JOIN dokter d ON d.id_dokter = j.id_dokter "
                + "JOIN poli p ON p.id_poli = j.id_poli "
                + "WHERE (? = 'Semua' OR j.hari = ?) "
                + "AND (j.id_jadwal LIKE ? OR d.nama_dokter LIKE ? OR p.nama_poli LIKE ? OR j.hari LIKE ?) "
                + "ORDER BY FIELD(j.hari, 'Senin', 'Selasa', 'Rabu', 'Kamis', 'Jumat', 'Sabtu', 'Minggu'), j.jam_mulai, d.nama_dokter";
        int totalKuota = 0;
        try (PreparedStatement stat = conn.prepareStatement(sql)) {
            String hari = cmbHari.getSelectedItem().toString();
            String keyword = "%" + txtKeyword.getText().trim() + "%";
            stat.setString(1, hari);
            stat.setString(2, hari);
            for (int i = 3; i <= 6; i++) {
                stat.setString(i, keyword);
            }
            try (ResultSet rs = stat.executeQuery()) {
                while (rs.next()) {
                    int kuota = rs.getInt("kuota");
                    totalKuota += kuota;
                    tableModel.addRow(new Object[]{
                        rs.getString("id_jadwal"),
                        rs.getString("nama_dokter"),
                        rs.getString("nama_poli"),
                        rs.getString("hari"),
                        rs.getString("jam_mulai"),
                        rs.getString("jam_selesai"),
                        kuota
                    });
                }
            }
            tblReport.setModel(tableModel);
            setWidths(new int[]{100, 220, 180, 100, 100, 100, 80});
            lblSummary.setText("Jumlah jadwal: " + tableModel.getRowCount() + " | Total kuota: " + totalKuota);
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Report jadwal dokter gagal dipanggil: " + e.getMessage());
        }
    }

    private void previewJasperReport() {
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("HARI", cmbHari.getSelectedItem().toString());
        parameters.put("KEYWORD", "%" + txtKeyword.getText().trim() + "%");
        JasperReportHelper.previewReport(this, "/report/report_jadwal_dokter.jasper", parameters, conn);
    }

    private void setWidths(int[] widths) {
        for (int i = 0; i < widths.length && i < tblReport.getColumnModel().getColumnCount(); i++) {
            tblReport.getColumnModel().getColumn(i).setPreferredWidth(widths[i]);
        }
    }

    public static void main(String args[]) {
        java.awt.EventQueue.invokeLater(() -> new FormReportJadwalDokter().setVisible(true));
    }

    private javax.swing.JButton btnKeluar;
    private javax.swing.JButton btnPreviewJasper;
    private javax.swing.JButton btnReset;
    private javax.swing.JButton btnTampilkan;
    private javax.swing.JComboBox<String> cmbHari;
    private javax.swing.JScrollPane jScrollPaneReport;
    private javax.swing.JLabel lblHari;
    private javax.swing.JLabel lblKeyword;
    private javax.swing.JLabel lblSummary;
    private javax.swing.JLabel lblTitle;
    private javax.swing.JPanel panelReport;
    private javax.swing.JTable tblReport;
    private javax.swing.JTextField txtKeyword;
}
