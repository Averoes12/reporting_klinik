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
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

public class FormResep extends javax.swing.JFrame {

    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter
            .ofPattern("uuuu-MM-dd")
            .withResolverStyle(ResolverStyle.STRICT);
    private final Connection conn = new DBConnection().connect();
    private DefaultTableModel detailModel;

    public FormResep() {
        initComponents();initDetailTable();
        clear();
        setLocationRelativeTo(null);
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {
        lblTitle = new javax.swing.JLabel();
        lblIdResep = new javax.swing.JLabel();
        txtIdResep = new javax.swing.JTextField();
        lblKunjungan = new javax.swing.JLabel();
        txtNoKunjungan = new javax.swing.JTextField();
        btnPilihKunjungan = new javax.swing.JButton();
        lblTanggal = new javax.swing.JLabel();
        txtTanggalResep = DateChooserHelper.createDateChooser();
        DateChooserHelper.preventBackdate(txtTanggalResep);
        lblCatatan = new javax.swing.JLabel();
        jScrollPaneCatatan = new javax.swing.JScrollPane();
        txtCatatan = new javax.swing.JTextArea();
        lblObat = new javax.swing.JLabel();
        txtIdObat = new javax.swing.JTextField();
        btnPilihObat = new javax.swing.JButton();
        lblJumlah = new javax.swing.JLabel();
        txtJumlah = new javax.swing.JTextField();
        lblAturan = new javax.swing.JLabel();
        txtAturanPakai = new javax.swing.JTextField();
        lblHarga = new javax.swing.JLabel();
        txtHargaSatuan = new javax.swing.JTextField();
        lblSubtotal = new javax.swing.JLabel();
        txtSubtotal = new javax.swing.JTextField();
        btnTambahDetail = new javax.swing.JButton();
        btnHapusDetail = new javax.swing.JButton();
        btnSimpan = new javax.swing.JButton();
        btnBatal = new javax.swing.JButton();
        btnKeluar = new javax.swing.JButton();
        panelDetail = new javax.swing.JPanel();
        panelDetailButton = new javax.swing.JPanel();
        panelButton = new javax.swing.JPanel();
        jScrollPaneDetail = new javax.swing.JScrollPane();
        tblDetail = new javax.swing.JTable();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Form Resep");

        lblTitle.setFont(new java.awt.Font("Segoe UI", 1, 18));
        lblTitle.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblTitle.setText("Input Resep dan Detail Obat");
        lblIdResep.setText("ID Resep");
        txtIdResep.setEditable(false);
        lblKunjungan.setText("Kunjungan");
        txtNoKunjungan.setEditable(false);
        btnPilihKunjungan.setText("Pilih");
        lblTanggal.setText("Tanggal Resep (yyyy-MM-dd)");
        lblCatatan.setText("Catatan");
        txtCatatan.setColumns(20);
        txtCatatan.setRows(3);
        jScrollPaneCatatan.setViewportView(txtCatatan);
        lblObat.setText("Obat");
        txtIdObat.setEditable(false);
        btnPilihObat.setText("Pilih");
        lblJumlah.setText("Jumlah");
        lblAturan.setText("Aturan Pakai");
        lblHarga.setText("Harga Satuan");
        lblSubtotal.setText("Subtotal");
        txtSubtotal.setEditable(false);
        btnTambahDetail.setText("Tambah Detail");
        btnHapusDetail.setText("Hapus Detail");
        btnSimpan.setText("Simpan Resep");
        btnBatal.setText("Batal");
        btnKeluar.setText("Keluar");

        btnPilihKunjungan.addActionListener(evt -> pilihKunjungan());
        btnPilihObat.addActionListener(evt -> pilihObat());
        btnTambahDetail.addActionListener(evt -> tambahDetail());
        btnHapusDetail.addActionListener(evt -> hapusDetail());
        btnSimpan.addActionListener(evt -> simpanResep());
        btnBatal.addActionListener(evt -> clear());
        btnKeluar.addActionListener(evt -> dispose());
        txtJumlah.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                hitungSubtotal();
            }
        });
        txtHargaSatuan.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                hitungSubtotal();
            }
        });
        jScrollPaneDetail.setViewportView(tblDetail);

        panelDetail.setBorder(javax.swing.BorderFactory.createTitledBorder("Detail Obat"));
        panelDetailButton.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 6, 0));
        panelDetailButton.add(btnTambahDetail);
        panelDetailButton.add(btnHapusDetail);

        javax.swing.GroupLayout panelDetailLayout = new javax.swing.GroupLayout(panelDetail);
        panelDetail.setLayout(panelDetailLayout);
        panelDetailLayout.setHorizontalGroup(
            panelDetailLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelDetailLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(panelDetailLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPaneDetail, javax.swing.GroupLayout.DEFAULT_SIZE, 846, Short.MAX_VALUE)
                    .addComponent(panelDetailButton, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        panelDetailLayout.setVerticalGroup(
            panelDetailLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelDetailLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(panelDetailButton, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPaneDetail, javax.swing.GroupLayout.PREFERRED_SIZE, 180, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        panelButton.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 6, 0));
        panelButton.add(btnSimpan);
        panelButton.add(btnBatal);
        panelButton.add(btnKeluar);

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
                            .addComponent(lblIdResep)
                            .addComponent(lblKunjungan)
                            .addComponent(lblTanggal)
                            .addComponent(lblCatatan)
                            .addComponent(lblObat)
                            .addComponent(lblJumlah)
                            .addComponent(lblAturan))
                        .addGap(29, 29, 29)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(txtIdResep)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(txtNoKunjungan, javax.swing.GroupLayout.PREFERRED_SIZE, 180, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(btnPilihKunjungan))
                            .addComponent(txtTanggalResep)
                            .addComponent(jScrollPaneCatatan, javax.swing.GroupLayout.DEFAULT_SIZE, 260, Short.MAX_VALUE)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(txtIdObat, javax.swing.GroupLayout.PREFERRED_SIZE, 180, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(btnPilihObat))
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(txtJumlah, javax.swing.GroupLayout.PREFERRED_SIZE, 54, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(lblHarga)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(txtHargaSatuan, javax.swing.GroupLayout.PREFERRED_SIZE, 88, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(lblSubtotal)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(txtSubtotal, javax.swing.GroupLayout.PREFERRED_SIZE, 88, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(txtAturanPakai)))
                    .addComponent(panelDetail, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(panelButton, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(24, 24, 24))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(20, 20, 20)
                .addComponent(lblTitle)
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblIdResep)
                    .addComponent(txtIdResep, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblKunjungan)
                    .addComponent(txtNoKunjungan, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnPilihKunjungan))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblTanggal)
                    .addComponent(txtTanggalResep, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lblCatatan)
                    .addComponent(jScrollPaneCatatan, javax.swing.GroupLayout.PREFERRED_SIZE, 62, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblObat)
                    .addComponent(txtIdObat, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnPilihObat))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblJumlah)
                    .addComponent(txtJumlah, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblHarga)
                    .addComponent(txtHargaSatuan, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblSubtotal)
                    .addComponent(txtSubtotal, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblAturan)
                    .addComponent(txtAturanPakai, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addComponent(panelDetail, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(panelButton, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void initDetailTable() {
        detailModel = new DefaultTableModel(null, new Object[]{"ID Obat", "Jumlah", "Aturan Pakai", "Harga Satuan", "Subtotal"}) {
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        tblDetail.setModel(detailModel);
    }

    private void clear() {
        txtIdResep.setText(nextId("resep", "id_resep", "RSP"));
        txtNoKunjungan.setText("");
        DateChooserHelper.setDate(txtTanggalResep, LocalDate.now());
        txtCatatan.setText("");
        clearDetailInput();
        detailModel.setRowCount(0);
    }

    private void clearDetailInput() {
        txtIdObat.setText("");
        txtJumlah.setText("");
        txtAturanPakai.setText("");
        txtHargaSatuan.setText("");
        txtSubtotal.setText("");
    }

    private String nextId(String table, String idColumn, String prefix) {
        if (conn == null) {
            return prefix + "001";
        }
        String sql = "SELECT COALESCE(MAX(CASE WHEN " + idColumn + " REGEXP ? "
                + "THEN CAST(SUBSTRING(" + idColumn + ", ?) AS UNSIGNED) ELSE 0 END), 0) + 1 FROM " + table;
        try (PreparedStatement stat = conn.prepareStatement(sql)) {
            stat.setString(1, "^" + prefix + "[0-9]+$");
            stat.setInt(2, prefix.length() + 1);
            try (ResultSet rs = stat.executeQuery()) {
                if (rs.next()) {
                    return prefix + String.format("%03d", rs.getInt(1));
                }
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "ID otomatis gagal dibuat: " + e.getMessage());
        }
        return prefix + "001";
    }

    private void pilihKunjungan() {
        LookupDialog dialog = new LookupDialog(this, "Pilih Kunjungan", "kunjungan",
                new String[]{"no_kunjungan", "id_pasien", "tanggal_kunjungan", "status"},
                new String[]{"no_kunjungan", "id_pasien", "status"});
        dialog.setVisible(true);
        if (dialog.isSelected()) {
            txtNoKunjungan.setText(dialog.getSelectedId());
        }
    }

    private void pilihObat() {
        LookupDialog dialog = new LookupDialog(this, "Pilih Obat", "obat",
                new String[]{"id_obat", "nama_obat", "satuan", "harga", "stok_akhir"},
                new String[]{"id_obat", "nama_obat", "satuan"});
        dialog.setVisible(true);
        if (dialog.isSelected()) {
            txtIdObat.setText(dialog.getSelectedId());
            isiHargaObat(dialog.getSelectedId());
        }
    }

    private void isiHargaObat(String idObat) {
        if (conn == null) {
            return;
        }
        try (PreparedStatement stat = conn.prepareStatement("SELECT harga FROM obat WHERE id_obat = ?")) {
            stat.setString(1, idObat);
            try (ResultSet rs = stat.executeQuery()) {
                if (rs.next()) {
                    txtHargaSatuan.setText(rs.getBigDecimal("harga").toPlainString());
                    hitungSubtotal();
                }
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Harga obat gagal dipanggil: " + e.getMessage());
        }
    }

    private void hitungSubtotal() {
        try {
            int jumlah = Integer.parseInt(txtJumlah.getText().trim());
            BigDecimal harga = new BigDecimal(txtHargaSatuan.getText().trim());
            txtSubtotal.setText(harga.multiply(BigDecimal.valueOf(jumlah)).toPlainString());
        } catch (Exception e) {
            txtSubtotal.setText("");
        }
    }

    private void tambahDetail() {
        hitungSubtotal();
        if (txtIdObat.getText().trim().isEmpty()
                || txtJumlah.getText().trim().isEmpty()
                || txtAturanPakai.getText().trim().isEmpty()
                || txtHargaSatuan.getText().trim().isEmpty()
                || txtSubtotal.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Detail obat masih ada yang kosong.");
            return;
        }
        detailModel.addRow(new Object[]{
            txtIdObat.getText().trim(),
            txtJumlah.getText().trim(),
            txtAturanPakai.getText().trim(),
            txtHargaSatuan.getText().trim(),
            txtSubtotal.getText().trim()
        });
        clearDetailInput();
    }

    private void hapusDetail() {
        int row = tblDetail.getSelectedRow();
        if (row >= 0) {
            detailModel.removeRow(tblDetail.convertRowIndexToModel(row));
        }
    }

    private Date parseTanggal() {
        String value = DateChooserHelper.getText(txtTanggalResep).trim();
        if (!value.matches("\\d{4}-\\d{2}-\\d{2}")) {
            JOptionPane.showMessageDialog(this, "Tanggal resep harus berformat yyyy-MM-dd.");
            return null;
        }
        try {
            LocalDate date = LocalDate.parse(value, DATE_FORMATTER);
            if (date.isBefore(LocalDate.now())) {
                JOptionPane.showMessageDialog(this, "Tanggal resep tidak boleh sebelum tanggal hari ini.");
                return null;
            }
            return Date.valueOf(date);
        } catch (DateTimeParseException e) {
            JOptionPane.showMessageDialog(this, "Tanggal resep tidak valid.");
            return null;
        }
    }

    private void simpanResep() {
        if (conn == null) {
            JOptionPane.showMessageDialog(this, "Koneksi database belum berhasil.");
            return;
        }
        if (txtNoKunjungan.getText().trim().isEmpty() || detailModel.getRowCount() == 0) {
            JOptionPane.showMessageDialog(this, "Pilih kunjungan dan tambahkan minimal satu obat.");
            return;
        }
        Date tanggal = parseTanggal();
        if (tanggal == null) {
            return;
        }

        String idResep = txtIdResep.getText().trim();
        try {
            conn.setAutoCommit(false);
            try (PreparedStatement stat = conn.prepareStatement(
                    "INSERT INTO resep (id_resep, no_kunjungan, tanggal_resep, catatan) VALUES (?, ?, ?, ?)")) {
                stat.setString(1, idResep);
                stat.setString(2, txtNoKunjungan.getText().trim());
                stat.setDate(3, tanggal);
                stat.setString(4, txtCatatan.getText().trim());
                stat.executeUpdate();
            }
            for (int i = 0; i < detailModel.getRowCount(); i++) {
                String idDetail = nextId("resep_detail", "id_resep_detail", "RDT");
                try (PreparedStatement stat = conn.prepareStatement(
                        "INSERT INTO resep_detail (id_resep_detail, id_resep, id_obat, jumlah, aturan_pakai, harga_satuan, subtotal) "
                        + "VALUES (?, ?, ?, ?, ?, ?, ?)")) {
                    stat.setString(1, idDetail);
                    stat.setString(2, idResep);
                    stat.setString(3, detailModel.getValueAt(i, 0).toString());
                    stat.setInt(4, Integer.parseInt(detailModel.getValueAt(i, 1).toString()));
                    stat.setString(5, detailModel.getValueAt(i, 2).toString());
                    stat.setBigDecimal(6, new BigDecimal(detailModel.getValueAt(i, 3).toString()));
                    stat.setBigDecimal(7, new BigDecimal(detailModel.getValueAt(i, 4).toString()));
                    stat.executeUpdate();
                }
            }
            conn.commit();
            JOptionPane.showMessageDialog(this, "Resep dan detail obat berhasil disimpan.");
            clear();
        } catch (SQLException | NumberFormatException e) {
            try {
                conn.rollback();
            } catch (SQLException ignored) {
            }
            JOptionPane.showMessageDialog(this, "Resep gagal disimpan: " + e.getMessage());
        } finally {
            try {
                conn.setAutoCommit(true);
            } catch (SQLException ignored) {
            }
        }
    }

    private void applyHints() {
        txtIdResep.setToolTipText("ID resep dibuat otomatis.");
        txtNoKunjungan.setToolTipText("Klik tombol Pilih untuk memilih kunjungan.");
        txtTanggalResep.setToolTipText("Masukkan tanggal resep format yyyy-MM-dd.");
        txtCatatan.setToolTipText("Masukkan catatan resep jika ada.");
        txtIdObat.setToolTipText("Klik tombol Pilih untuk memilih obat.");
        txtJumlah.setToolTipText("Masukkan jumlah obat.");
        txtAturanPakai.setToolTipText("Masukkan aturan pakai obat.");
        txtHargaSatuan.setToolTipText("Harga satuan obat.");
        txtSubtotal.setToolTipText("Subtotal dihitung dari jumlah dikali harga.");
        btnPilihKunjungan.setToolTipText("Buka popup pencarian kunjungan.");
        btnPilihObat.setToolTipText("Buka popup pencarian obat.");
    }

    public static void main(String args[]) {
        java.awt.EventQueue.invokeLater(() -> new FormResep().setVisible(true));
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnBatal;
    private javax.swing.JButton btnHapusDetail;
    private javax.swing.JButton btnKeluar;
    private javax.swing.JButton btnPilihKunjungan;
    private javax.swing.JButton btnPilihObat;
    private javax.swing.JButton btnSimpan;
    private javax.swing.JButton btnTambahDetail;
    private javax.swing.JLabel lblAturan;
    private javax.swing.JLabel lblCatatan;
    private javax.swing.JLabel lblHarga;
    private javax.swing.JLabel lblIdResep;
    private javax.swing.JLabel lblJumlah;
    private javax.swing.JLabel lblKunjungan;
    private javax.swing.JLabel lblObat;
    private javax.swing.JLabel lblSubtotal;
    private javax.swing.JLabel lblTanggal;
    private javax.swing.JLabel lblTitle;
    private javax.swing.JScrollPane jScrollPaneCatatan;
    private javax.swing.JScrollPane jScrollPaneDetail;
    private javax.swing.JPanel panelButton;
    private javax.swing.JPanel panelDetail;
    private javax.swing.JPanel panelDetailButton;
    private javax.swing.JTable tblDetail;
    private javax.swing.JTextArea txtCatatan;
    private javax.swing.JTextField txtAturanPakai;
    private javax.swing.JTextField txtHargaSatuan;
    private javax.swing.JTextField txtNoKunjungan;
    private javax.swing.JTextField txtIdObat;
    private javax.swing.JTextField txtIdResep;
    private javax.swing.JTextField txtJumlah;
    private javax.swing.JTextField txtSubtotal;
    private com.toedter.calendar.JDateChooser txtTanggalResep;
    // End of variables declaration//GEN-END:variables
}
