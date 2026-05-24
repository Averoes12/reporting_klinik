package view;

import connection.DBConnection;
import java.awt.event.KeyEvent;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

public class FormPoli extends javax.swing.JFrame {

    private static final String ID_POLI_PREFIX = "POL";
    private static final int ID_POLI_DIGITS = 3;

    private final Connection conn = new DBConnection().connect();
    private DefaultTableModel tableModel;

    public FormPoli() {
        initComponents();
        applyHints();
        clear();
        focusForm();
        dataTable();
        setLocationRelativeTo(null);
    }

    private void focusForm() {
        txtNamaPoli.requestFocus();
    }

    private void clear() {
        generateIdPoli();
        txtNamaPoli.setText("");
        txtCari.setText("");
        btnSimpan.setEnabled(true);
    }

    private void generateIdPoli() {
        txtId.setText(nextIdPoli());
    }

    private String nextIdPoli() {
        if (conn == null) {
            return formatIdPoli(1);
        }

        String sql = "SELECT COALESCE(MAX(CASE "
                + "WHEN id_poli REGEXP ? THEN CAST(SUBSTRING(id_poli, ?) AS UNSIGNED) "
                + "WHEN id_poli REGEXP ? THEN CAST(id_poli AS UNSIGNED) "
                + "ELSE 0 END), 0) + 1 AS next_number FROM poli";
        try (PreparedStatement stat = conn.prepareStatement(sql)) {
            stat.setString(1, "^" + ID_POLI_PREFIX + "[0-9]+$");
            stat.setInt(2, ID_POLI_PREFIX.length() + 1);
            stat.setString(3, "^[0-9]+$");

            try (ResultSet hasil = stat.executeQuery()) {
                if (hasil.next()) {
                    return formatIdPoli(hasil.getInt("next_number"));
                }
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "ID otomatis gagal dibuat: " + e.getMessage());
        }
        return formatIdPoli(1);
    }

    private String formatIdPoli(int number) {
        return ID_POLI_PREFIX + String.format("%0" + ID_POLI_DIGITS + "d", number);
    }

    private boolean isInputValid() {
        if (txtId.getText().trim().isEmpty() || txtNamaPoli.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Data poli masih ada yang kosong.");
            return false;
        }
        return true;
    }

    private String valueOrEmpty(String value) {
        return value == null ? "" : value;
    }

    private void dataTable() {
        Object[] columns = {"ID Poli", "Nama Poli"};
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

        String sql = "SELECT id_poli, nama_poli FROM poli "
                + "WHERE id_poli LIKE ? OR nama_poli LIKE ? "
                + "ORDER BY CASE "
                + "WHEN id_poli REGEXP '^" + ID_POLI_PREFIX + "[0-9]+$' THEN CAST(SUBSTRING(id_poli, " + (ID_POLI_PREFIX.length() + 1) + ") AS UNSIGNED) "
                + "WHEN id_poli REGEXP '^[0-9]+$' THEN CAST(id_poli AS UNSIGNED) "
                + "ELSE 0 END ASC";
        try (PreparedStatement stat = conn.prepareStatement(sql)) {
            String keyword = "%" + txtCari.getText().trim() + "%";
            stat.setString(1, keyword);
            stat.setString(2, keyword);

            try (ResultSet hasil = stat.executeQuery()) {
                while (hasil.next()) {
                    tableModel.addRow(new Object[]{
                        valueOrEmpty(hasil.getString("id_poli")),
                        valueOrEmpty(hasil.getString("nama_poli"))
                    });
                }
            }

            tblData.setModel(tableModel);
            setTableColumnWidth();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Data poli gagal dipanggil: " + e.getMessage());
        }
    }

    private void setTableColumnWidth() {
        int[] widths = {100, 260};
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
        lblNamaPoli = new javax.swing.JLabel();
        txtNamaPoli = new javax.swing.JTextField();
        btnSimpan = new javax.swing.JButton();
        btnUbah = new javax.swing.JButton();
        btnHapus = new javax.swing.JButton();
        btnBatal = new javax.swing.JButton();
        btnKeluar = new javax.swing.JButton();
        txtCari = new javax.swing.JTextField();
        btnCari = new javax.swing.JButton();
        jScrollPaneTable = new javax.swing.JScrollPane();
        tblData = new javax.swing.JTable();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Form Poli");

        lblTitle.setFont(new java.awt.Font("Segoe UI", 1, 18));
        lblTitle.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblTitle.setText("Data Poli");
        lblId.setText("Id Poli");
        txtId.setEditable(false);
        lblNamaPoli.setText("Nama Poli");
        btnSimpan.setText("Simpan");
        btnUbah.setText("Ubah");
        btnHapus.setText("Hapus");
        btnBatal.setText("Batal");
        btnKeluar.setText("Keluar");
        btnCari.setText("Cari");
        txtCari.setColumns(20);

        btnSimpan.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSimpanActionPerformed(evt);
            }
        });
        btnUbah.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnUbahActionPerformed(evt);
            }
        });
        btnHapus.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnHapusActionPerformed(evt);
            }
        });
        btnBatal.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnBatalActionPerformed(evt);
            }
        });
        btnKeluar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnKeluarActionPerformed(evt);
            }
        });
        btnCari.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCariActionPerformed(evt);
            }
        });
        txtCari.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtCariKeyPressed(evt);
            }
        });
        tblData.setAutoCreateRowSorter(true);
        tblData.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_OFF);
        tblData.setRowHeight(24);
        tblData.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblDataMouseClicked(evt);
            }
        });
        jScrollPaneTable.setViewportView(tblData);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(24, 24, 24)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lblTitle, javax.swing.GroupLayout.PREFERRED_SIZE, 820, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(lblId, javax.swing.GroupLayout.PREFERRED_SIZE, 170, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(txtId, javax.swing.GroupLayout.PREFERRED_SIZE, 310, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(lblNamaPoli, javax.swing.GroupLayout.PREFERRED_SIZE, 170, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(txtNamaPoli, javax.swing.GroupLayout.PREFERRED_SIZE, 310, javax.swing.GroupLayout.PREFERRED_SIZE))
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
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(txtCari, javax.swing.GroupLayout.PREFERRED_SIZE, 190, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnCari))
                    .addComponent(jScrollPaneTable, javax.swing.GroupLayout.PREFERRED_SIZE, 820, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(24, Short.MAX_VALUE))
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
                .addGap(10, 10, 10)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblNamaPoli)
                    .addComponent(txtNamaPoli, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(10, 10, 10)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnSimpan)
                    .addComponent(btnUbah)
                    .addComponent(btnHapus)
                    .addComponent(btnBatal)
                    .addComponent(btnKeluar))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtCari, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnCari))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPaneTable, javax.swing.GroupLayout.PREFERRED_SIZE, 220, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(24, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void applyHints() {
        txtId.setToolTipText("ID poli dibuat otomatis.");
        txtNamaPoli.setToolTipText("Masukkan nama poli.");
        txtCari.setToolTipText("Ketik kata kunci pencarian poli lalu tekan Enter atau tombol Cari.");
    }

    private void btnSimpanActionPerformed(java.awt.event.ActionEvent evt) {
        simpanData();
    }

    private void btnUbahActionPerformed(java.awt.event.ActionEvent evt) {
        ubahData();
    }

    private void btnHapusActionPerformed(java.awt.event.ActionEvent evt) {
        hapusData();
    }

    private void btnBatalActionPerformed(java.awt.event.ActionEvent evt) {
        clear();
        focusForm();
        dataTable();
    }

    private void btnKeluarActionPerformed(java.awt.event.ActionEvent evt) {
        dispose();
    }

    private void btnCariActionPerformed(java.awt.event.ActionEvent evt) {
        dataTable();
    }

    private void txtCariKeyPressed(java.awt.event.KeyEvent evt) {
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            dataTable();
        }
    }

    private void tblDataMouseClicked(java.awt.event.MouseEvent evt) {
        tableMouseClicked();
    }

    private void simpanData() {
        if (conn == null) {
            JOptionPane.showMessageDialog(this, "Koneksi database belum berhasil.");
            return;
        }
        if (!isInputValid()) {
            return;
        }

        String sql = "INSERT INTO poli (id_poli, nama_poli) VALUES (?, ?)";
        try (PreparedStatement stat = conn.prepareStatement(sql)) {
            stat.setString(1, txtId.getText().trim());
            stat.setString(2, txtNamaPoli.getText().trim());
            stat.executeUpdate();

            JOptionPane.showMessageDialog(this, "Data poli berhasil disimpan.");
            clear();
            focusForm();
            dataTable();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Data poli gagal disimpan: " + e.getMessage());
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

        String sql = "UPDATE poli SET nama_poli = ? WHERE id_poli = ?";
        try (PreparedStatement stat = conn.prepareStatement(sql)) {
            stat.setString(1, txtNamaPoli.getText().trim());
            stat.setString(2, txtId.getText().trim());
            stat.executeUpdate();

            JOptionPane.showMessageDialog(this, "Data poli berhasil diubah.");
            clear();
            focusForm();
            dataTable();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Data poli gagal diubah: " + e.getMessage());
        }
    }

    private void hapusData() {
        if (conn == null) {
            JOptionPane.showMessageDialog(this, "Koneksi database belum berhasil.");
            return;
        }
        if (txtId.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Pilih data poli yang ingin dihapus terlebih dahulu.");
            return;
        }

        int ok = JOptionPane.showConfirmDialog(this, "Hapus data ini?", "Konfirmasi", JOptionPane.YES_NO_OPTION);
        if (ok != JOptionPane.YES_OPTION) {
            return;
        }

        String sql = "DELETE FROM poli WHERE id_poli = ?";
        try (PreparedStatement stat = conn.prepareStatement(sql)) {
            stat.setString(1, txtId.getText().trim());
            stat.executeUpdate();

            JOptionPane.showMessageDialog(this, "Data poli berhasil dihapus.");
            clear();
            focusForm();
            dataTable();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Data poli gagal dihapus: " + e.getMessage());
        }
    }

    private void tableMouseClicked() {
        int row = tblData.getSelectedRow();
        if (row < 0 || tableModel == null) {
            return;
        }

        int modelRow = tblData.convertRowIndexToModel(row);
        txtId.setText(tableModel.getValueAt(modelRow, 0).toString());
        txtNamaPoli.setText(tableModel.getValueAt(modelRow, 1).toString());
        btnSimpan.setEnabled(false);
    }

    public static void main(String args[]) {
        java.awt.EventQueue.invokeLater(() -> new FormPoli().setVisible(true));
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnBatal;
    private javax.swing.JButton btnCari;
    private javax.swing.JButton btnHapus;
    private javax.swing.JButton btnKeluar;
    private javax.swing.JButton btnSimpan;
    private javax.swing.JButton btnUbah;
    private javax.swing.JLabel lblId;
    private javax.swing.JLabel lblNamaPoli;
    private javax.swing.JLabel lblTitle;
    private javax.swing.JScrollPane jScrollPaneTable;
    private javax.swing.JTable tblData;
    private javax.swing.JTextField txtCari;
    private javax.swing.JTextField txtId;
    private javax.swing.JTextField txtNamaPoli;
    // End of variables declaration//GEN-END:variables
}
