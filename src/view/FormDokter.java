package view;

import connection.DBConnection;
import java.awt.event.KeyEvent;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

public class FormDokter extends javax.swing.JFrame {

    private static final String ID_DOKTER_PREFIX = "DKT";
    private static final int ID_DOKTER_DIGITS = 3;

    private final Connection conn = new DBConnection().connect();
    private DefaultTableModel tableModel;

    public FormDokter() {
        initComponents();
        applyHints();
        clear();
        focusForm();
        dataTable();
        setLocationRelativeTo(null);
    }

    private void focusForm() {
        txtNamaDokter.requestFocus();
    }

    private void clear() {
        generateIdDokter();
        txtNamaDokter.setText("");
        txtIdPoli.setText("");
        txtNoHp.setText("");
        txtAlamat.setText("");
        txtCari.setText("");
        cmbStatus.setSelectedIndex(0);
        btnSimpan.setEnabled(true);
    }

    private void generateIdDokter() {
        txtId.setText(nextIdDokter());
    }

    private String nextIdDokter() {
        if (conn == null) {
            return formatIdDokter(1);
        }

        String sql = "SELECT COALESCE(MAX(CASE "
                + "WHEN id_dokter REGEXP ? THEN CAST(SUBSTRING(id_dokter, ?) AS UNSIGNED) "
                + "WHEN id_dokter REGEXP ? THEN CAST(id_dokter AS UNSIGNED) "
                + "ELSE 0 END), 0) + 1 AS next_number FROM dokter";
        try (PreparedStatement stat = conn.prepareStatement(sql)) {
            stat.setString(1, "^" + ID_DOKTER_PREFIX + "[0-9]+$");
            stat.setInt(2, ID_DOKTER_PREFIX.length() + 1);
            stat.setString(3, "^[0-9]+$");

            try (ResultSet hasil = stat.executeQuery()) {
                if (hasil.next()) {
                    return formatIdDokter(hasil.getInt("next_number"));
                }
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "ID otomatis gagal dibuat: " + e.getMessage());
        }
        return formatIdDokter(1);
    }

    private String formatIdDokter(int number) {
        return ID_DOKTER_PREFIX + String.format("%0" + ID_DOKTER_DIGITS + "d", number);
    }

    private boolean isInputValid() {
        if (txtId.getText().trim().isEmpty()
                || txtNamaDokter.getText().trim().isEmpty()
                || txtIdPoli.getText().trim().isEmpty()
                || cmbStatus.getSelectedItem() == null) {
            JOptionPane.showMessageDialog(this, "Data dokter masih ada yang kosong.");
            return false;
        }
        return true;
    }

    private String valueOrEmpty(String value) {
        return value == null ? "" : value;
    }

    private void dataTable() {
        Object[] columns = {"ID Dokter", "Nama Dokter", "ID Poli", "No. HP", "Alamat", "Status"};
        tableModel = new DefaultTableModel(null, columns) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        if (conn == null) {
            tblData.setModel(tableModel);
            return;
        }

        String sql = "SELECT id_dokter, nama_dokter, id_poli, no_hp, alamat, status FROM dokter "
                + "WHERE id_dokter LIKE ? OR nama_dokter LIKE ? OR status LIKE ? "
                + "ORDER BY CASE "
                + "WHEN id_dokter REGEXP '^" + ID_DOKTER_PREFIX + "[0-9]+$' THEN CAST(SUBSTRING(id_dokter, " + (ID_DOKTER_PREFIX.length() + 1) + ") AS UNSIGNED) "
                + "WHEN id_dokter REGEXP '^[0-9]+$' THEN CAST(id_dokter AS UNSIGNED) "
                + "ELSE 0 END ASC";
        try (PreparedStatement stat = conn.prepareStatement(sql)) {
            String keyword = "%" + txtCari.getText().trim() + "%";
            stat.setString(1, keyword);
            stat.setString(2, keyword);
            stat.setString(3, keyword);

            try (ResultSet hasil = stat.executeQuery()) {
                while (hasil.next()) {
                    tableModel.addRow(new Object[]{
                        valueOrEmpty(hasil.getString("id_dokter")),
                        valueOrEmpty(hasil.getString("nama_dokter")),
                        valueOrEmpty(hasil.getString("id_poli")),
                        valueOrEmpty(hasil.getString("no_hp")),
                        valueOrEmpty(hasil.getString("alamat")),
                        valueOrEmpty(hasil.getString("status"))
                    });
                }
            }

            tblData.setModel(tableModel);
            setTableColumnWidth();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Data dokter gagal dipanggil: " + e.getMessage());
        }
    }

    private void setTableColumnWidth() {
        int[] widths = {95, 180, 90, 120, 220, 100};
        for (int i = 0; i < widths.length && i < tblData.getColumnModel().getColumnCount(); i++) {
            tblData.getColumnModel().getColumn(i).setPreferredWidth(widths[i]);
        }
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {
        lblTitle = new javax.swing.JLabel();
        lblId = new javax.swing.JLabel();
        txtId = new javax.swing.JTextField();
        lblNamaDokter = new javax.swing.JLabel();
        txtNamaDokter = new javax.swing.JTextField();
        lblIdPoli = new javax.swing.JLabel();
        txtIdPoli = new javax.swing.JTextField();
        btnPilihIdPoli = new javax.swing.JButton();
        lblNoHp = new javax.swing.JLabel();
        txtNoHp = new javax.swing.JTextField();
        lblAlamat = new javax.swing.JLabel();
        jScrollPaneAlamat = new javax.swing.JScrollPane();
        txtAlamat = new javax.swing.JTextArea();
        lblStatus = new javax.swing.JLabel();
        cmbStatus = new javax.swing.JComboBox<>();
        btnSimpan = new javax.swing.JButton();
        btnUbah = new javax.swing.JButton();
        btnHapus = new javax.swing.JButton();
        btnBatal = new javax.swing.JButton();
        btnKeluar = new javax.swing.JButton();
        panelData = new javax.swing.JPanel();
        txtCari = new javax.swing.JTextField();
        btnCari = new javax.swing.JButton();
        jScrollPaneTable = new javax.swing.JScrollPane();
        tblData = new javax.swing.JTable();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Form Dokter");

        lblTitle.setFont(new java.awt.Font("Segoe UI", 1, 18));
        lblTitle.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblTitle.setText("Data Dokter");
        lblId.setText("Id Dokter");
        txtId.setEditable(false);
        lblNamaDokter.setText("Nama Dokter");
        lblIdPoli.setText("Poli");
        txtIdPoli.setEditable(false);
        btnPilihIdPoli.setText("Pilih");
        btnPilihIdPoli.addActionListener(evt -> pilihPoli());
        lblNoHp.setText("No. HP");
        lblAlamat.setText("Alamat");
        txtAlamat.setColumns(20);
        txtAlamat.setRows(3);
        jScrollPaneAlamat.setViewportView(txtAlamat);
        lblStatus.setText("Status");
        cmbStatus.setModel(new javax.swing.DefaultComboBoxModel<>(new String[]{"Aktif", "Nonaktif"}));
        btnSimpan.setText("Simpan");
        btnUbah.setText("Ubah");
        btnHapus.setText("Hapus");
        btnBatal.setText("Batal");
        btnKeluar.setText("Keluar");
        btnCari.setText("Cari");
        txtCari.setColumns(20);

        btnSimpan.addActionListener(evt -> simpanData());
        btnUbah.addActionListener(evt -> ubahData());
        btnHapus.addActionListener(evt -> hapusData());
        btnBatal.addActionListener(evt -> { clear(); focusForm(); dataTable(); });
        btnKeluar.addActionListener(evt -> dispose());
        btnCari.addActionListener(evt -> dataTable());
        txtCari.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
                    dataTable();
                }
            }
        });
        tblData.setAutoCreateRowSorter(true);
        tblData.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_OFF);
        tblData.setRowHeight(24);
        tblData.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tableMouseClicked();
            }
        });
        jScrollPaneTable.setViewportView(tblData);

        panelData.setBorder(javax.swing.BorderFactory.createTitledBorder("Data Dokter"));

        javax.swing.GroupLayout panelDataLayout = new javax.swing.GroupLayout(panelData);
        panelData.setLayout(panelDataLayout);
        panelDataLayout.setHorizontalGroup(
            panelDataLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelDataLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(panelDataLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPaneTable, javax.swing.GroupLayout.DEFAULT_SIZE, 846, Short.MAX_VALUE)
                    .addGroup(panelDataLayout.createSequentialGroup()
                        .addComponent(txtCari, javax.swing.GroupLayout.PREFERRED_SIZE, 180, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnCari)
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        panelDataLayout.setVerticalGroup(
            panelDataLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelDataLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(panelDataLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtCari, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnCari))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPaneTable, javax.swing.GroupLayout.PREFERRED_SIZE, 210, javax.swing.GroupLayout.PREFERRED_SIZE)
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
                            .addComponent(lblId)
                            .addComponent(lblNamaDokter)
                            .addComponent(lblIdPoli)
                            .addComponent(lblNoHp)
                            .addComponent(lblAlamat)
                            .addComponent(lblStatus))
                        .addGap(29, 29, 29)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(txtId)
                            .addComponent(txtNamaDokter)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(txtIdPoli, javax.swing.GroupLayout.PREFERRED_SIZE, 180, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(btnPilihIdPoli))
                            .addComponent(txtNoHp)
                            .addComponent(jScrollPaneAlamat, javax.swing.GroupLayout.DEFAULT_SIZE, 260, Short.MAX_VALUE)
                            .addComponent(cmbStatus, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(btnSimpan)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnUbah)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnHapus)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnBatal)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnKeluar))
                    .addComponent(panelData, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(24, 24, 24))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(20, 20, 20)
                .addComponent(lblTitle)
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblId)
                    .addComponent(txtId, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblNamaDokter)
                    .addComponent(txtNamaDokter, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblIdPoli)
                    .addComponent(txtIdPoli, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnPilihIdPoli))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblNoHp)
                    .addComponent(txtNoHp, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lblAlamat)
                    .addComponent(jScrollPaneAlamat, javax.swing.GroupLayout.PREFERRED_SIZE, 76, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblStatus)
                    .addComponent(cmbStatus, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnSimpan)
                    .addComponent(btnUbah)
                    .addComponent(btnHapus)
                    .addComponent(btnBatal)
                    .addComponent(btnKeluar))
                .addGap(18, 18, 18)
                .addComponent(panelData, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    public static void main(String args[]) {
        java.awt.EventQueue.invokeLater(() -> new FormDokter().setVisible(true));
    }

    private void pilihPoli() {
        LookupDialog dialog = new LookupDialog(this, "Pilih Poli", "poli",
                new String[]{"id_poli", "nama_poli"},
                new String[]{"id_poli", "nama_poli"});
        dialog.setVisible(true);
        if (dialog.isSelected()) {
            txtIdPoli.setText(dialog.getSelectedId());
        }
    }

    private void applyHints() {
        txtId.setToolTipText("ID dokter dibuat otomatis oleh sistem.");
        txtNamaDokter.setToolTipText("Masukkan nama lengkap dokter.");
        txtIdPoli.setToolTipText("Klik tombol Pilih untuk memilih poli dokter.");
        btnPilihIdPoli.setToolTipText("Buka popup pencarian data poli.");
        txtNoHp.setToolTipText("Masukkan nomor HP dokter.");
        txtAlamat.setToolTipText("Masukkan alamat dokter.");
        cmbStatus.setToolTipText("Pilih status dokter agar nilainya konsisten.");
        txtCari.setToolTipText("Ketik kata kunci pencarian dokter lalu tekan Enter atau tombol Cari.");
    }

    private void simpanData() {
        if (conn == null) {
            JOptionPane.showMessageDialog(this, "Koneksi database belum berhasil.");
            return;
        }
        if (!isInputValid()) {
            return;
        }

        String sql = "INSERT INTO dokter (id_dokter, nama_dokter, id_poli, no_hp, alamat, status) VALUES (?, ?, ?, ?, ?, ?)";
        try (PreparedStatement stat = conn.prepareStatement(sql)) {
            bindInput(stat);
            stat.executeUpdate();

            JOptionPane.showMessageDialog(this, "Data dokter berhasil disimpan.");
            clear();
            focusForm();
            dataTable();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Data dokter gagal disimpan: " + e.getMessage());
        }
    }

    private void ubahData() {
        if (conn == null) {
            JOptionPane.showMessageDialog(this, "Koneksi database belum berhasil.");
            return;
        }
        if (!isInputValid()) {
            return;
        }

        String sql = "UPDATE dokter SET nama_dokter = ?, id_poli = ?, no_hp = ?, alamat = ?, status = ? WHERE id_dokter = ?";
        try (PreparedStatement stat = conn.prepareStatement(sql)) {
            stat.setString(1, txtNamaDokter.getText().trim());
            stat.setString(2, txtIdPoli.getText().trim());
            stat.setString(3, txtNoHp.getText().trim());
            stat.setString(4, txtAlamat.getText().trim());
            stat.setString(5, cmbStatus.getSelectedItem().toString());
            stat.setString(6, txtId.getText().trim());
            stat.executeUpdate();

            JOptionPane.showMessageDialog(this, "Data dokter berhasil diubah.");
            clear();
            focusForm();
            dataTable();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Data dokter gagal diubah: " + e.getMessage());
        }
    }

    private void bindInput(PreparedStatement stat) throws SQLException {
        stat.setString(1, txtId.getText().trim());
        stat.setString(2, txtNamaDokter.getText().trim());
        stat.setString(3, txtIdPoli.getText().trim());
        stat.setString(4, txtNoHp.getText().trim());
        stat.setString(5, txtAlamat.getText().trim());
        stat.setString(6, cmbStatus.getSelectedItem().toString());
    }

    private void hapusData() {
        if (conn == null) {
            JOptionPane.showMessageDialog(this, "Koneksi database belum berhasil.");
            return;
        }
        if (txtId.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Pilih data dokter yang ingin dihapus terlebih dahulu.");
            return;
        }

        int ok = JOptionPane.showConfirmDialog(this, "Hapus data ini?", "Konfirmasi", JOptionPane.YES_NO_OPTION);
        if (ok != JOptionPane.YES_OPTION) {
            return;
        }

        String sql = "DELETE FROM dokter WHERE id_dokter = ?";
        try (PreparedStatement stat = conn.prepareStatement(sql)) {
            stat.setString(1, txtId.getText().trim());
            stat.executeUpdate();

            JOptionPane.showMessageDialog(this, "Data dokter berhasil dihapus.");
            clear();
            focusForm();
            dataTable();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Data dokter gagal dihapus: " + e.getMessage());
        }
    }

    private void tableMouseClicked() {
        int row = tblData.getSelectedRow();
        if (row < 0 || tableModel == null) {
            return;
        }

        int modelRow = tblData.convertRowIndexToModel(row);
        txtId.setText(tableModel.getValueAt(modelRow, 0).toString());
        txtNamaDokter.setText(tableModel.getValueAt(modelRow, 1).toString());
        txtIdPoli.setText(tableModel.getValueAt(modelRow, 2).toString());
        txtNoHp.setText(tableModel.getValueAt(modelRow, 3).toString());
        txtAlamat.setText(tableModel.getValueAt(modelRow, 4).toString());
        cmbStatus.setSelectedItem(tableModel.getValueAt(modelRow, 5).toString());
        btnSimpan.setEnabled(false);
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnBatal;
    private javax.swing.JButton btnCari;
    private javax.swing.JButton btnHapus;
    private javax.swing.JButton btnKeluar;
    private javax.swing.JButton btnPilihIdPoli;
    private javax.swing.JButton btnSimpan;
    private javax.swing.JButton btnUbah;
    private javax.swing.JLabel lblAlamat;
    private javax.swing.JLabel lblId;
    private javax.swing.JLabel lblIdPoli;
    private javax.swing.JLabel lblNamaDokter;
    private javax.swing.JLabel lblNoHp;
    private javax.swing.JLabel lblStatus;
    private javax.swing.JLabel lblTitle;
    private javax.swing.JPanel panelData;
    private javax.swing.JScrollPane jScrollPaneAlamat;
    private javax.swing.JScrollPane jScrollPaneTable;
    private javax.swing.JTable tblData;
    private javax.swing.JTextArea txtAlamat;
    private javax.swing.JTextField txtCari;
    private javax.swing.JTextField txtIdPoli;
    private javax.swing.JTextField txtId;
    private javax.swing.JTextField txtNamaDokter;
    private javax.swing.JTextField txtNoHp;
    private javax.swing.JComboBox<String> cmbStatus;
    // End of variables declaration//GEN-END:variables
}
