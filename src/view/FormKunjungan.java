package view;

import connection.DBConnection;
import java.awt.event.KeyEvent;
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

public class FormKunjungan extends javax.swing.JFrame {

    private static final String NO_KUNJUNGAN_PREFIX = "KJG";
    private static final int NO_KUNJUNGAN_DIGITS = 3;
    private static final String DATE_FORMAT_HINT = "yyyy-MM-dd";
    private static final String DATE_FORMAT_EXAMPLE = "2026-05-17";
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter
            .ofPattern("uuuu-MM-dd")
            .withResolverStyle(ResolverStyle.STRICT);

    private final Connection conn = new DBConnection().connect();
    private DefaultTableModel tableModel;

    public FormKunjungan() {
        initComponents();
        txtTanggalKunjungan.setDateFormatString(DateChooserHelper.DISPLAY_FORMAT);
        DateChooserHelper.preventBackdate(txtTanggalKunjungan);
        cmbStatus.setModel(new javax.swing.DefaultComboBoxModel<>(new String[]{"Menunggu", "Diperiksa", "Selesai", "Batal"}));
        applyHints();
        clear();
        focusForm();
        dataTable();
        setLocationRelativeTo(null);
    }

    private void focusForm() {
        txtIdPasien.requestFocus();
    }

    private void clear() {
        generateNoKunjungan();
        txtIdPasien.setText("");
        txtIdDokter.setText("");
        txtIdPoli.setText("");
        DateChooserHelper.clear(txtTanggalKunjungan);
        txtKeluhan.setText("");
        txtDiagnosa.setText("");
        txtTindakan.setText("");
        txtCari.setText("");
        cmbStatus.setSelectedIndex(0);
        btnSimpan.setEnabled(true);
    }

    private void generateNoKunjungan() {
        txtNoKunjungan.setText(nextNoKunjungan());
    }

    private String nextNoKunjungan() {
        if (conn == null) {
            return formatNoKunjungan(1);
        }

        String sql = "SELECT COALESCE(MAX(CASE "
                + "WHEN no_kunjungan REGEXP ? THEN CAST(SUBSTRING(no_kunjungan, ?) AS UNSIGNED) "
                + "WHEN no_kunjungan REGEXP ? THEN CAST(no_kunjungan AS UNSIGNED) "
                + "ELSE 0 END), 0) + 1 AS next_number FROM kunjungan";
        try (PreparedStatement stat = conn.prepareStatement(sql)) {
            stat.setString(1, "^" + NO_KUNJUNGAN_PREFIX + "[0-9]+$");
            stat.setInt(2, NO_KUNJUNGAN_PREFIX.length() + 1);
            stat.setString(3, "^[0-9]+$");

            try (ResultSet hasil = stat.executeQuery()) {
                if (hasil.next()) {
                    return formatNoKunjungan(hasil.getInt("next_number"));
                }
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "No kunjungan otomatis gagal dibuat: " + e.getMessage());
        }
        return formatNoKunjungan(1);
    }

    private String formatNoKunjungan(int number) {
        return NO_KUNJUNGAN_PREFIX + String.format("%0" + NO_KUNJUNGAN_DIGITS + "d", number);
    }

    private boolean isInputValid() {
        if (txtNoKunjungan.getText().trim().isEmpty()
                || txtIdPasien.getText().trim().isEmpty()
                || txtIdDokter.getText().trim().isEmpty()
                || txtIdPoli.getText().trim().isEmpty()
                || DateChooserHelper.getText(txtTanggalKunjungan).trim().isEmpty()
                || txtKeluhan.getText().trim().isEmpty()
                || cmbStatus.getSelectedItem() == null) {
            JOptionPane.showMessageDialog(this, "Data kunjungan masih ada yang kosong.");
            return false;
        }
        if (parseDate(DateChooserHelper.getText(txtTanggalKunjungan), "Tanggal kunjungan") == null) {
            return false;
        }
        return true;
    }

    private Date parseDate(String value, String fieldName) {
        String trimmed = value.trim();
        if (!trimmed.matches("\\d{4}-\\d{2}-\\d{2}")) {
            showDateFormatMessage(fieldName);
            return null;
        }

        try {
            LocalDate date = LocalDate.parse(trimmed, DATE_FORMATTER);
            if (date.isBefore(LocalDate.now())) {
                JOptionPane.showMessageDialog(this, fieldName + " tidak boleh sebelum tanggal hari ini.");
                return null;
            }
            return Date.valueOf(date);
        } catch (DateTimeParseException e) {
            showDateFormatMessage(fieldName);
            return null;
        }
    }

    private void showDateFormatMessage(String fieldName) {
        JOptionPane.showMessageDialog(this, fieldName + " harus berformat "
                + DATE_FORMAT_HINT + ". Contoh: " + DATE_FORMAT_EXAMPLE + ".");
    }

    private String valueOrEmpty(String value) {
        return value == null ? "" : value;
    }

    private void dataTable() {
        Object[] columns = {"No Kunjungan", "ID Pasien", "ID Dokter", "ID Poli", "Tanggal Kunjungan", "Keluhan", "Diagnosa", "Tindakan", "Status"};
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

        String sql = "SELECT no_kunjungan, id_pasien, id_dokter, id_poli, tanggal_kunjungan, keluhan, diagnosa, tindakan, status "
                + "FROM kunjungan WHERE no_kunjungan LIKE ? OR status LIKE ? "
                + "ORDER BY CASE "
                + "WHEN no_kunjungan REGEXP '^" + NO_KUNJUNGAN_PREFIX + "[0-9]+$' THEN CAST(SUBSTRING(no_kunjungan, " + (NO_KUNJUNGAN_PREFIX.length() + 1) + ") AS UNSIGNED) "
                + "WHEN no_kunjungan REGEXP '^[0-9]+$' THEN CAST(no_kunjungan AS UNSIGNED) "
                + "ELSE 0 END ASC";
        try (PreparedStatement stat = conn.prepareStatement(sql)) {
            String keyword = "%" + txtCari.getText().trim() + "%";
            stat.setString(1, keyword);
            stat.setString(2, keyword);

            try (ResultSet hasil = stat.executeQuery()) {
                while (hasil.next()) {
                    Date tanggal = hasil.getDate("tanggal_kunjungan");
                    tableModel.addRow(new Object[]{
                        valueOrEmpty(hasil.getString("no_kunjungan")),
                        valueOrEmpty(hasil.getString("id_pasien")),
                        valueOrEmpty(hasil.getString("id_dokter")),
                        valueOrEmpty(hasil.getString("id_poli")),
                        tanggal == null ? "" : tanggal.toString(),
                        valueOrEmpty(hasil.getString("keluhan")),
                        valueOrEmpty(hasil.getString("diagnosa")),
                        valueOrEmpty(hasil.getString("tindakan")),
                        valueOrEmpty(hasil.getString("status"))
                    });
                }
            }

            tblData.setModel(tableModel);
            setTableColumnWidth();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Data kunjungan gagal dipanggil: " + e.getMessage());
        }
    }

    private void setTableColumnWidth() {
        int[] widths = {110, 95, 95, 95, 120, 180, 180, 180, 100};
        for (int i = 0; i < widths.length && i < tblData.getColumnModel().getColumnCount(); i++) {
            tblData.getColumnModel().getColumn(i).setPreferredWidth(widths[i]);
        }
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        btnBatal = new javax.swing.JButton();
        btnCari = new javax.swing.JButton();
        btnHapus = new javax.swing.JButton();
        btnKeluar = new javax.swing.JButton();
        btnPilihIdDokter = new javax.swing.JButton();
        btnPilihIdPasien = new javax.swing.JButton();
        btnPilihIdPoli = new javax.swing.JButton();
        btnSimpan = new javax.swing.JButton();
        btnUbah = new javax.swing.JButton();
        lblDiagnosa = new javax.swing.JLabel();
        lblIdDokter = new javax.swing.JLabel();
        lblIdPasien = new javax.swing.JLabel();
        lblIdPoli = new javax.swing.JLabel();
        lblKeluhan = new javax.swing.JLabel();
        lblNoKunjungan = new javax.swing.JLabel();
        lblStatus = new javax.swing.JLabel();
        lblTanggalKunjungan = new javax.swing.JLabel();
        lblTindakan = new javax.swing.JLabel();
        lblTitle = new javax.swing.JLabel();
        jScrollPaneDiagnosa = new javax.swing.JScrollPane();
        jScrollPaneKeluhan = new javax.swing.JScrollPane();
        jScrollPaneTable = new javax.swing.JScrollPane();
        jScrollPaneTindakan = new javax.swing.JScrollPane();
        tblData = new javax.swing.JTable();
        txtDiagnosa = new javax.swing.JTextArea();
        txtKeluhan = new javax.swing.JTextArea();
        txtTindakan = new javax.swing.JTextArea();
        txtCari = new javax.swing.JTextField();
        txtIdDokter = new javax.swing.JTextField();
        txtIdPasien = new javax.swing.JTextField();
        txtIdPoli = new javax.swing.JTextField();
        txtNoKunjungan = new javax.swing.JTextField();
        cmbStatus = new javax.swing.JComboBox();
        txtTanggalKunjungan = new com.toedter.calendar.JDateChooser();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Form Kunjungan");

        btnBatal.setText("Batal");

        btnCari.setText("Cari");

        btnHapus.setText("Hapus");

        btnKeluar.setText("Keluar");

        btnPilihIdDokter.setText("Id Dokter");

        btnPilihIdPasien.setText("Id Pasien");

        btnPilihIdPoli.setText("Id Poli");

        btnSimpan.setText("Simpan");

        btnUbah.setText("Ubah");

        btnBatal.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnBatalActionPerformed(evt);
            }
        });
        btnCari.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCariActionPerformed(evt);
            }
        });
        btnHapus.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnHapusActionPerformed(evt);
            }
        });
        btnKeluar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnKeluarActionPerformed(evt);
            }
        });
        btnPilihIdDokter.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnPilihIdDokterActionPerformed(evt);
            }
        });
        btnPilihIdPasien.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnPilihIdPasienActionPerformed(evt);
            }
        });
        btnPilihIdPoli.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnPilihIdPoliActionPerformed(evt);
            }
        });
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

        lblDiagnosa.setText("Diagnosa");

        lblIdDokter.setText("Id Dokter");

        lblIdPasien.setText("Id Pasien");

        lblIdPoli.setText("Id Poli");

        lblKeluhan.setText("Keluhan");

        lblNoKunjungan.setText("No Kunjungan");

        lblStatus.setText("Status");

        lblTanggalKunjungan.setText("Tanggal Kunjungan");

        lblTindakan.setText("Tindakan");

        lblTitle.setText("Title");

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
        jScrollPaneDiagnosa.setViewportView(txtDiagnosa);
        jScrollPaneKeluhan.setViewportView(txtKeluhan);
        jScrollPaneTindakan.setViewportView(txtTindakan);

        lblTitle.setFont(new java.awt.Font("Segoe UI", 1, 18));
        lblTitle.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblTitle.setText("Data Kunjungan");
        btnPilihIdDokter.setText("Pilih");
        btnPilihIdPasien.setText("Pilih");
        btnPilihIdPoli.setText("Pilih");
        txtNoKunjungan.setEditable(false);
        txtKeluhan.setRows(2);
        txtDiagnosa.setRows(2);
        txtTindakan.setRows(2);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(24, 24, 24)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lblTitle, javax.swing.GroupLayout.PREFERRED_SIZE, 820, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(lblNoKunjungan, javax.swing.GroupLayout.PREFERRED_SIZE, 170, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(lblIdPasien, javax.swing.GroupLayout.PREFERRED_SIZE, 170, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(lblIdDokter, javax.swing.GroupLayout.PREFERRED_SIZE, 170, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(lblIdPoli, javax.swing.GroupLayout.PREFERRED_SIZE, 170, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(lblTanggalKunjungan, javax.swing.GroupLayout.PREFERRED_SIZE, 170, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(lblStatus, javax.swing.GroupLayout.PREFERRED_SIZE, 170, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(lblKeluhan, javax.swing.GroupLayout.PREFERRED_SIZE, 170, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(lblDiagnosa, javax.swing.GroupLayout.PREFERRED_SIZE, 170, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(lblTindakan, javax.swing.GroupLayout.PREFERRED_SIZE, 170, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(txtNoKunjungan, javax.swing.GroupLayout.PREFERRED_SIZE, 570, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(txtIdPasien, javax.swing.GroupLayout.PREFERRED_SIZE, 480, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(btnPilihIdPasien))
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(txtIdDokter, javax.swing.GroupLayout.PREFERRED_SIZE, 480, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(btnPilihIdDokter))
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(txtIdPoli, javax.swing.GroupLayout.PREFERRED_SIZE, 480, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(btnPilihIdPoli))
                            .addComponent(txtTanggalKunjungan, javax.swing.GroupLayout.PREFERRED_SIZE, 570, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(cmbStatus, javax.swing.GroupLayout.PREFERRED_SIZE, 570, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jScrollPaneKeluhan, javax.swing.GroupLayout.PREFERRED_SIZE, 570, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jScrollPaneDiagnosa, javax.swing.GroupLayout.PREFERRED_SIZE, 570, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jScrollPaneTindakan, javax.swing.GroupLayout.PREFERRED_SIZE, 570, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(btnSimpan)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(btnUbah)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(btnHapus)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(btnBatal)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(btnKeluar))))
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
                    .addComponent(lblNoKunjungan)
                    .addComponent(txtNoKunjungan, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblIdPasien)
                    .addComponent(txtIdPasien, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnPilihIdPasien))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblIdDokter)
                    .addComponent(txtIdDokter, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnPilihIdDokter))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblIdPoli)
                    .addComponent(txtIdPoli, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnPilihIdPoli))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lblTanggalKunjungan)
                    .addComponent(txtTanggalKunjungan, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblStatus)
                    .addComponent(cmbStatus, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lblKeluhan)
                    .addComponent(jScrollPaneKeluhan, javax.swing.GroupLayout.PREFERRED_SIZE, 58, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lblDiagnosa)
                    .addComponent(jScrollPaneDiagnosa, javax.swing.GroupLayout.PREFERRED_SIZE, 58, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lblTindakan)
                    .addComponent(jScrollPaneTindakan, javax.swing.GroupLayout.PREFERRED_SIZE, 58, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
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
        txtNoKunjungan.setToolTipText("No kunjungan dibuat otomatis.");        
        txtIdPasien.setToolTipText("Klik tombol Pilih untuk memilih pasien.");
        txtIdDokter.setToolTipText("Klik tombol Pilih untuk memilih dokter.");
        txtIdPoli.setToolTipText("Klik tombol Pilih untuk memilih poli.");
        txtTanggalKunjungan.setToolTipText("Masukkan tanggal kunjungan format yyyy-MM-dd.");
        txtKeluhan.setToolTipText("Masukkan keluhan pasien.");
        txtDiagnosa.setToolTipText("Masukkan diagnosa dokter.");
        txtTindakan.setToolTipText("Masukkan tindakan yang diberikan.");
        cmbStatus.setToolTipText("Pilih status kunjungan agar nilainya konsisten.");
        txtCari.setToolTipText("Ketik kata kunci pencarian kunjungan lalu tekan Enter atau tombol Cari.");
        btnPilihIdPasien.setToolTipText("Buka popup pencarian pasien.");
        btnPilihIdDokter.setToolTipText("Buka popup pencarian dokter.");
        btnPilihIdPoli.setToolTipText("Buka popup pencarian poli.");
    }

    public static void main(String args[]) {
        java.awt.EventQueue.invokeLater(() -> new FormKunjungan().setVisible(true));
    }

    private void btnBatalActionPerformed(java.awt.event.ActionEvent evt) {
        clear();
        focusForm();
        dataTable();
    }

    private void btnCariActionPerformed(java.awt.event.ActionEvent evt) {
        dataTable();
    }

    private void btnHapusActionPerformed(java.awt.event.ActionEvent evt) {
        hapusData();
    }

    private void btnKeluarActionPerformed(java.awt.event.ActionEvent evt) {
        dispose();
    }

    private void btnPilihIdDokterActionPerformed(java.awt.event.ActionEvent evt) {
        pilihDokter();
    }

    private void btnPilihIdPasienActionPerformed(java.awt.event.ActionEvent evt) {
        pilihPasien();
    }

    private void btnPilihIdPoliActionPerformed(java.awt.event.ActionEvent evt) {
        pilihPoli();
    }

    private void btnSimpanActionPerformed(java.awt.event.ActionEvent evt) {
        simpanData();
    }

    private void btnUbahActionPerformed(java.awt.event.ActionEvent evt) {
        ubahData();
    }

    private void txtCariKeyPressed(java.awt.event.KeyEvent evt) {
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            dataTable();
        }
    }

    private void tblDataMouseClicked(java.awt.event.MouseEvent evt) {
        tableMouseClicked();
    }

    private void pilihPasien() {
        LookupDialog dialog = new LookupDialog(this, "Pilih Pasien", "pasien",
                new String[]{"id_pasien", "nik", "nama_pasien", "no_hp"},
                new String[]{"id_pasien", "nik", "nama_pasien", "no_hp"});
        dialog.setVisible(true);
        if (dialog.isSelected()) {
            txtIdPasien.setText(dialog.getSelectedId());
        }
    }

    private void pilihDokter() {
        LookupDialog dialog = new LookupDialog(this, "Pilih Dokter", "dokter",
                new String[]{"id_dokter", "nama_dokter", "id_poli", "status"},
                new String[]{"id_dokter", "nama_dokter", "status"});
        dialog.setVisible(true);
        if (dialog.isSelected()) {
            txtIdDokter.setText(dialog.getSelectedId());
        }
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

    private void simpanData() {
        if (conn == null) {
            JOptionPane.showMessageDialog(this, "Koneksi database belum berhasil.");
            return;
        }
        if (!isInputValid()) {
            return;
        }

        String sql = "INSERT INTO kunjungan (no_kunjungan, id_pasien, id_dokter, id_poli, tanggal_kunjungan, keluhan, diagnosa, tindakan, status) "
                + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement stat = conn.prepareStatement(sql)) {
            bindInput(stat);
            stat.executeUpdate();

            JOptionPane.showMessageDialog(this, "Data kunjungan berhasil disimpan.");
            clear();
            focusForm();
            dataTable();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Data kunjungan gagal disimpan: " + e.getMessage());
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

        String sql = "UPDATE kunjungan SET id_pasien = ?, id_dokter = ?, id_poli = ?, tanggal_kunjungan = ?, keluhan = ?, diagnosa = ?, tindakan = ?, status = ? "
                + "WHERE no_kunjungan = ?";
        try (PreparedStatement stat = conn.prepareStatement(sql)) {
            Date tanggal = parseDate(DateChooserHelper.getText(txtTanggalKunjungan), "Tanggal kunjungan");
            if (tanggal == null) {
                return;
            }
            stat.setString(1, txtIdPasien.getText().trim());
            stat.setString(2, txtIdDokter.getText().trim());
            stat.setString(3, txtIdPoli.getText().trim());
            stat.setDate(4, tanggal);
            stat.setString(5, txtKeluhan.getText().trim());
            stat.setString(6, txtDiagnosa.getText().trim());
            stat.setString(7, txtTindakan.getText().trim());
            stat.setString(8, cmbStatus.getSelectedItem().toString());
            stat.setString(9, txtNoKunjungan.getText().trim());
            stat.executeUpdate();

            JOptionPane.showMessageDialog(this, "Data kunjungan berhasil diubah.");
            clear();
            focusForm();
            dataTable();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Data kunjungan gagal diubah: " + e.getMessage());
        }
    }

    private void bindInput(PreparedStatement stat) throws SQLException {
        Date tanggal = parseDate(DateChooserHelper.getText(txtTanggalKunjungan), "Tanggal kunjungan");
        if (tanggal == null) {
            throw new SQLException("Tanggal kunjungan tidak valid.");
        }
        stat.setString(1, txtNoKunjungan.getText().trim());
        stat.setString(2, txtIdPasien.getText().trim());
        stat.setString(3, txtIdDokter.getText().trim());
        stat.setString(4, txtIdPoli.getText().trim());
        stat.setDate(5, tanggal);
        stat.setString(6, txtKeluhan.getText().trim());
        stat.setString(7, txtDiagnosa.getText().trim());
        stat.setString(8, txtTindakan.getText().trim());
        stat.setString(9, cmbStatus.getSelectedItem().toString());
    }

    private void hapusData() {
        if (conn == null) {
            JOptionPane.showMessageDialog(this, "Koneksi database belum berhasil.");
            return;
        }
        if (txtNoKunjungan.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Pilih data kunjungan yang ingin dihapus terlebih dahulu.");
            return;
        }

        int ok = JOptionPane.showConfirmDialog(this, "Hapus data ini?", "Konfirmasi", JOptionPane.YES_NO_OPTION);
        if (ok != JOptionPane.YES_OPTION) {
            return;
        }

        String sql = "DELETE FROM kunjungan WHERE no_kunjungan = ?";
        try (PreparedStatement stat = conn.prepareStatement(sql)) {
            stat.setString(1, txtNoKunjungan.getText().trim());
            stat.executeUpdate();

            JOptionPane.showMessageDialog(this, "Data kunjungan berhasil dihapus.");
            clear();
            focusForm();
            dataTable();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Data kunjungan gagal dihapus: " + e.getMessage());
        }
    }

    private void tableMouseClicked() {
        int row = tblData.getSelectedRow();
        if (row < 0 || tableModel == null) {
            return;
        }

        int modelRow = tblData.convertRowIndexToModel(row);
        txtNoKunjungan.setText(tableModel.getValueAt(modelRow, 0).toString());
        txtIdPasien.setText(tableModel.getValueAt(modelRow, 1).toString());
        txtIdDokter.setText(tableModel.getValueAt(modelRow, 2).toString());
        txtIdPoli.setText(tableModel.getValueAt(modelRow, 3).toString());
        DateChooserHelper.setDateText(txtTanggalKunjungan, tableModel.getValueAt(modelRow, 4).toString());
        txtKeluhan.setText(tableModel.getValueAt(modelRow, 5).toString());
        txtDiagnosa.setText(tableModel.getValueAt(modelRow, 6).toString());
        txtTindakan.setText(tableModel.getValueAt(modelRow, 7).toString());
        cmbStatus.setSelectedItem(tableModel.getValueAt(modelRow, 8).toString());
        btnSimpan.setEnabled(false);
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnBatal;
    private javax.swing.JButton btnCari;
    private javax.swing.JButton btnHapus;
    private javax.swing.JButton btnKeluar;
    private javax.swing.JButton btnPilihIdDokter;
    private javax.swing.JButton btnPilihIdPasien;
    private javax.swing.JButton btnPilihIdPoli;
    private javax.swing.JButton btnSimpan;
    private javax.swing.JButton btnUbah;
    private javax.swing.JComboBox cmbStatus;
    private javax.swing.JScrollPane jScrollPaneDiagnosa;
    private javax.swing.JScrollPane jScrollPaneKeluhan;
    private javax.swing.JScrollPane jScrollPaneTable;
    private javax.swing.JScrollPane jScrollPaneTindakan;
    private javax.swing.JLabel lblDiagnosa;
    private javax.swing.JLabel lblIdDokter;
    private javax.swing.JLabel lblIdPasien;
    private javax.swing.JLabel lblIdPoli;
    private javax.swing.JLabel lblKeluhan;
    private javax.swing.JLabel lblNoKunjungan;
    private javax.swing.JLabel lblStatus;
    private javax.swing.JLabel lblTanggalKunjungan;
    private javax.swing.JLabel lblTindakan;
    private javax.swing.JLabel lblTitle;
    private javax.swing.JTable tblData;
    private javax.swing.JTextField txtCari;
    private javax.swing.JTextArea txtDiagnosa;
    private javax.swing.JTextField txtIdDokter;
    private javax.swing.JTextField txtIdPasien;
    private javax.swing.JTextField txtIdPoli;
    private javax.swing.JTextArea txtKeluhan;
    private javax.swing.JTextField txtNoKunjungan;
    private com.toedter.calendar.JDateChooser txtTanggalKunjungan;
    private javax.swing.JTextArea txtTindakan;
    // End of variables declaration//GEN-END:variables
}
