package view;

import app.Session;
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
import javax.swing.DefaultComboBoxModel;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

public class FormPembayaran extends JFrame {

    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter
            .ofPattern("uuuu-MM-dd")
            .withResolverStyle(ResolverStyle.STRICT);

    private final Connection conn = new DBConnection().connect();
    private DefaultTableModel tableModel;

    public FormPembayaran() {
        initComponents();
        if (!Session.isKasir()) {
            JOptionPane.showMessageDialog(this, "Hanya role kasir yang dapat menerima pembayaran.");
            dispose();
            return;
        }
        clear();
        dataTable();
        setLocationRelativeTo(null);
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        lblTitle = new javax.swing.JLabel();
        lblIdPembayaran = new javax.swing.JLabel();
        lblNoKunjungan = new javax.swing.JLabel();
        lblTanggal = new javax.swing.JLabel();
        lblBiayaKonsultasi = new javax.swing.JLabel();
        lblBiayaTindakan = new javax.swing.JLabel();
        lblBiayaObat = new javax.swing.JLabel();
        lblTotalTagihan = new javax.swing.JLabel();
        lblJumlahBayar = new javax.swing.JLabel();
        lblKembalian = new javax.swing.JLabel();
        lblMetode = new javax.swing.JLabel();
        lblStatus = new javax.swing.JLabel();
        txtIdPembayaran = new javax.swing.JTextField();
        txtNoKunjungan = new javax.swing.JTextField();
        txtTanggal = DateChooserHelper.createDateChooser();
        DateChooserHelper.preventBackdate(txtTanggal);
        txtBiayaKonsultasi = new javax.swing.JTextField();
        txtBiayaTindakan = new javax.swing.JTextField();
        txtBiayaObat = new javax.swing.JTextField();
        txtTotalTagihan = new javax.swing.JTextField();
        txtJumlahBayar = new javax.swing.JTextField();
        txtKembalian = new javax.swing.JTextField();
        cmbMetode = new javax.swing.JComboBox<>();
        cmbStatus = new javax.swing.JComboBox<>();
        btnPilihKunjungan = new javax.swing.JButton();
        btnHitung = new javax.swing.JButton();
        btnSimpan = new javax.swing.JButton();
        btnUbah = new javax.swing.JButton();
        btnHapus = new javax.swing.JButton();
        btnBatal = new javax.swing.JButton();
        btnKeluar = new javax.swing.JButton();
        panelData = new javax.swing.JPanel();
        txtCari = new javax.swing.JTextField();
        btnCari = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        tblPembayaran = new javax.swing.JTable();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Form Pembayaran");

        lblTitle.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        lblTitle.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblTitle.setText("Pembayaran Kunjungan");

        lblIdPembayaran.setText("ID Pembayaran");

        lblNoKunjungan.setText("No Kunjungan");

        lblTanggal.setText("Tanggal Pembayaran");

        lblBiayaKonsultasi.setText("Biaya Konsultasi");

        lblBiayaTindakan.setText("Biaya Tindakan");

        lblBiayaObat.setText("Biaya Obat");

        lblTotalTagihan.setText("Total Tagihan");

        lblJumlahBayar.setText("Jumlah Bayar");

        lblKembalian.setText("Kembalian");

        lblMetode.setText("Metode Pembayaran");

        lblStatus.setText("Status Pembayaran");

        txtIdPembayaran.setEditable(false);

        txtNoKunjungan.setEditable(false);

        txtBiayaObat.setEditable(false);

        txtTotalTagihan.setEditable(false);

        txtKembalian.setEditable(false);

        cmbMetode.setModel(new DefaultComboBoxModel<>(new String[]{"Tunai", "Transfer", "QRIS", "Debit", "Asuransi"}));

        cmbStatus.setModel(new DefaultComboBoxModel<>(new String[]{"Lunas", "Belum Lunas"}));

        btnPilihKunjungan.setText("Pilih");
        btnPilihKunjungan.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnPilihKunjunganActionPerformed(evt);
            }
        });

        btnHitung.setText("Hitung");
        btnHitung.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnHitungActionPerformed(evt);
            }
        });

        btnSimpan.setText("Simpan");
        btnSimpan.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSimpanActionPerformed(evt);
            }
        });

        btnUbah.setText("Ubah");
        btnUbah.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnUbahActionPerformed(evt);
            }
        });

        btnHapus.setText("Hapus");
        btnHapus.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnHapusActionPerformed(evt);
            }
        });

        btnBatal.setText("Batal");
        btnBatal.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnBatalActionPerformed(evt);
            }
        });

        btnKeluar.setText("Keluar");
        btnKeluar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnKeluarActionPerformed(evt);
            }
        });

        txtBiayaKonsultasi.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtBiayaKonsultasiActionPerformed(evt);
            }
        });
        txtBiayaTindakan.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtBiayaTindakanActionPerformed(evt);
            }
        });
        txtJumlahBayar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtJumlahBayarActionPerformed(evt);
            }
        });

        panelData.setBorder(javax.swing.BorderFactory.createTitledBorder("Data Pembayaran"));

        txtCari.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtCariActionPerformed(evt);
            }
        });

        btnCari.setText("Cari");
        btnCari.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCariActionPerformed(evt);
            }
        });

        tblPembayaran.setAutoCreateRowSorter(true);
        tblPembayaran.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_OFF);
        tblPembayaran.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "ID Pembayaran", "No Kunjungan", "Tanggal", "Konsultasi", "Tindakan", "Obat", "Total", "Bayar", "Kembalian", "Metode", "Status"
            }
        ));
        tblPembayaran.setRowHeight(24);
        tblPembayaran.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblPembayaranMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(tblPembayaran);

        javax.swing.GroupLayout panelDataLayout = new javax.swing.GroupLayout(panelData);
        panelData.setLayout(panelDataLayout);
        panelDataLayout.setHorizontalGroup(
            panelDataLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelDataLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(panelDataLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 996, Short.MAX_VALUE)
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
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 220, javax.swing.GroupLayout.PREFERRED_SIZE)
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
                            .addComponent(lblIdPembayaran)
                            .addComponent(lblNoKunjungan)
                            .addComponent(lblTanggal)
                            .addComponent(lblBiayaKonsultasi)
                            .addComponent(lblBiayaTindakan)
                            .addComponent(lblBiayaObat)
                            .addComponent(lblTotalTagihan)
                            .addComponent(lblJumlahBayar)
                            .addComponent(lblKembalian)
                            .addComponent(lblMetode)
                            .addComponent(lblStatus))
                        .addGap(29, 29, 29)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(txtIdPembayaran)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(txtNoKunjungan, javax.swing.GroupLayout.PREFERRED_SIZE, 180, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(btnPilihKunjungan))
                            .addComponent(txtTanggal)
                            .addComponent(txtBiayaKonsultasi)
                            .addComponent(txtBiayaTindakan)
                            .addComponent(txtBiayaObat)
                            .addComponent(txtTotalTagihan)
                            .addComponent(txtJumlahBayar)
                            .addComponent(txtKembalian)
                            .addComponent(cmbMetode, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(cmbStatus, 0, 260, Short.MAX_VALUE)))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(btnHitung)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
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
                    .addComponent(lblIdPembayaran)
                    .addComponent(txtIdPembayaran, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblNoKunjungan)
                    .addComponent(txtNoKunjungan, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnPilihKunjungan))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblTanggal)
                    .addComponent(txtTanggal, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblBiayaKonsultasi)
                    .addComponent(txtBiayaKonsultasi, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblBiayaTindakan)
                    .addComponent(txtBiayaTindakan, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblBiayaObat)
                    .addComponent(txtBiayaObat, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblTotalTagihan)
                    .addComponent(txtTotalTagihan, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblJumlahBayar)
                    .addComponent(txtJumlahBayar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblKembalian)
                    .addComponent(txtKembalian, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblMetode)
                    .addComponent(cmbMetode, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblStatus)
                    .addComponent(cmbStatus, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnHitung)
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

    private void btnPilihKunjunganActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnPilihKunjunganActionPerformed
        pilihKunjungan();
    }//GEN-LAST:event_btnPilihKunjunganActionPerformed

    private void btnHitungActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnHitungActionPerformed
        hitungPembayaran();
    }//GEN-LAST:event_btnHitungActionPerformed

    private void btnSimpanActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSimpanActionPerformed
        simpanData();
    }//GEN-LAST:event_btnSimpanActionPerformed

    private void btnUbahActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnUbahActionPerformed
        ubahData();
    }//GEN-LAST:event_btnUbahActionPerformed

    private void btnHapusActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnHapusActionPerformed
        hapusData();
    }//GEN-LAST:event_btnHapusActionPerformed

    private void btnBatalActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnBatalActionPerformed
        clear();
        dataTable();
    }//GEN-LAST:event_btnBatalActionPerformed

    private void btnKeluarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnKeluarActionPerformed
        dispose();
    }//GEN-LAST:event_btnKeluarActionPerformed

    private void btnCariActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCariActionPerformed
        dataTable();
    }//GEN-LAST:event_btnCariActionPerformed

    private void txtCariActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtCariActionPerformed
        dataTable();
    }//GEN-LAST:event_txtCariActionPerformed

    private void tblPembayaranMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblPembayaranMouseClicked
        tableMouseClicked();
    }//GEN-LAST:event_tblPembayaranMouseClicked

    private void txtBiayaKonsultasiActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtBiayaKonsultasiActionPerformed
        hitungPembayaran();
    }//GEN-LAST:event_txtBiayaKonsultasiActionPerformed

    private void txtBiayaTindakanActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtBiayaTindakanActionPerformed
        hitungPembayaran();
    }//GEN-LAST:event_txtBiayaTindakanActionPerformed

    private void txtJumlahBayarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtJumlahBayarActionPerformed
        hitungPembayaran();
    }//GEN-LAST:event_txtJumlahBayarActionPerformed

    private void clear() {
        txtIdPembayaran.setText(nextIdPembayaran());
        txtNoKunjungan.setText("");
        DateChooserHelper.setDate(txtTanggal, LocalDate.now());
        txtBiayaKonsultasi.setText("0");
        txtBiayaTindakan.setText("0");
        txtBiayaObat.setText("0");
        txtTotalTagihan.setText("0");
        txtJumlahBayar.setText("0");
        txtKembalian.setText("0");
        txtCari.setText("");
        cmbMetode.setSelectedIndex(0);
        cmbStatus.setSelectedIndex(0);
        btnSimpan.setEnabled(true);
    }

    private String nextIdPembayaran() {
        if (conn == null) {
            return "BYR001";
        }
        String sql = "SELECT COALESCE(MAX(CASE WHEN id_pembayaran REGEXP ? "
                + "THEN CAST(SUBSTRING(id_pembayaran, ?) AS UNSIGNED) ELSE 0 END), 0) + 1 FROM pembayaran";
        try (PreparedStatement stat = conn.prepareStatement(sql)) {
            stat.setString(1, "^BYR[0-9]+$");
            stat.setInt(2, 4);
            try (ResultSet rs = stat.executeQuery()) {
                if (rs.next()) {
                    return "BYR" + String.format("%03d", rs.getInt(1));
                }
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "ID pembayaran otomatis gagal dibuat: " + e.getMessage());
        }
        return "BYR001";
    }

    private void pilihKunjungan() {
        LookupDialog dialog = new LookupDialog(this, "Pilih Kunjungan", "kunjungan",
                new String[]{"no_kunjungan", "id_pasien", "id_dokter", "tanggal_kunjungan", "status"},
                new String[]{"no_kunjungan", "id_pasien", "status"});
        dialog.setVisible(true);
        if (dialog.isSelected()) {
            txtNoKunjungan.setText(dialog.getSelectedId());
            txtBiayaObat.setText(getBiayaObat(dialog.getSelectedId()).toPlainString());
            hitungPembayaran();
        }
    }

    private BigDecimal getBiayaObat(String noKunjungan) {
        if (conn == null) {
            return BigDecimal.ZERO;
        }
        String sql = "SELECT COALESCE(SUM(rd.subtotal), 0) AS biaya_obat "
                + "FROM resep r JOIN resep_detail rd ON rd.id_resep = r.id_resep "
                + "WHERE r.no_kunjungan = ? OR r.no_kunjungan = (SELECT no_kunjungan FROM kunjungan WHERE no_kunjungan = ? LIMIT 1)";
        try (PreparedStatement stat = conn.prepareStatement(sql)) {
            stat.setString(1, noKunjungan);
            stat.setString(2, noKunjungan);
            try (ResultSet rs = stat.executeQuery()) {
                if (rs.next()) {
                    return rs.getBigDecimal("biaya_obat");
                }
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Biaya obat gagal dihitung: " + e.getMessage());
        }
        return BigDecimal.ZERO;
    }

    private void hitungPembayaran() {
        BigDecimal konsultasi = parseMoney(txtBiayaKonsultasi.getText(), "Biaya konsultasi");
        BigDecimal tindakan = parseMoney(txtBiayaTindakan.getText(), "Biaya tindakan");
        BigDecimal obat = parseMoney(txtBiayaObat.getText(), "Biaya obat");
        BigDecimal bayar = parseMoney(txtJumlahBayar.getText(), "Jumlah bayar");
        if (konsultasi == null || tindakan == null || obat == null || bayar == null) {
            return;
        }
        BigDecimal total = konsultasi.add(tindakan).add(obat);
        BigDecimal kembalian = bayar.subtract(total);
        txtTotalTagihan.setText(total.toPlainString());
        txtKembalian.setText(kembalian.signum() < 0 ? "0" : kembalian.toPlainString());
        cmbStatus.setSelectedItem(bayar.compareTo(total) >= 0 ? "Lunas" : "Belum Lunas");
    }

    private boolean isInputValid() {
        if (txtIdPembayaran.getText().trim().isEmpty()
                || txtNoKunjungan.getText().trim().isEmpty()
                || DateChooserHelper.getText(txtTanggal).trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "ID pembayaran, kunjungan, dan tanggal wajib diisi.");
            return false;
        }
        if (parseDate(DateChooserHelper.getText(txtTanggal).trim()) == null) {
            return false;
        }
        hitungPembayaran();
        return parseMoney(txtTotalTagihan.getText(), "Total tagihan") != null
                && parseMoney(txtJumlahBayar.getText(), "Jumlah bayar") != null;
    }

    private Date parseDate(String value) {
        if (!value.matches("\\d{4}-\\d{2}-\\d{2}")) {
            JOptionPane.showMessageDialog(this, "Tanggal harus berformat yyyy-MM-dd.");
            return null;
        }
        try {
            LocalDate date = LocalDate.parse(value, DATE_FORMATTER);
            if (date.isBefore(LocalDate.now())) {
                JOptionPane.showMessageDialog(this, "Tanggal tidak boleh sebelum tanggal hari ini.");
                return null;
            }
            return Date.valueOf(date);
        } catch (DateTimeParseException e) {
            JOptionPane.showMessageDialog(this, "Tanggal tidak valid.");
            return null;
        }
    }

    private BigDecimal parseMoney(String value, String fieldName) {
        try {
            BigDecimal number = new BigDecimal(value.trim());
            if (number.signum() < 0) {
                JOptionPane.showMessageDialog(this, fieldName + " tidak boleh negatif.");
                return null;
            }
            return number;
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, fieldName + " harus berupa angka.");
            return null;
        }
    }

    private void dataTable() {
        Object[] columns = {"ID Pembayaran", "No Kunjungan", "Tanggal", "Konsultasi", "Tindakan", "Obat", "Total", "Bayar", "Kembalian", "Metode", "Status"};
        tableModel = new DefaultTableModel(null, columns) {
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        if (conn == null) {
            tblPembayaran.setModel(tableModel);
            return;
        }
        String sql = "SELECT id_pembayaran, no_kunjungan, tanggal_pembayaran, biaya_konsultasi, biaya_tindakan, "
                + "biaya_obat, total_tagihan, jumlah_bayar, kembalian, metode_pembayaran, status_pembayaran "
                + "FROM pembayaran WHERE id_pembayaran LIKE ? OR no_kunjungan LIKE ? OR metode_pembayaran LIKE ? OR status_pembayaran LIKE ? "
                + "ORDER BY tanggal_pembayaran DESC, id_pembayaran DESC";
        try (PreparedStatement stat = conn.prepareStatement(sql)) {
            String keyword = "%" + txtCari.getText().trim() + "%";
            for (int i = 1; i <= 4; i++) {
                stat.setString(i, keyword);
            }
            try (ResultSet rs = stat.executeQuery()) {
                while (rs.next()) {
                    tableModel.addRow(new Object[]{
                        rs.getString("id_pembayaran"),
                        rs.getString("no_kunjungan"),
                        rs.getDate("tanggal_pembayaran"),
                        rs.getBigDecimal("biaya_konsultasi"),
                        rs.getBigDecimal("biaya_tindakan"),
                        rs.getBigDecimal("biaya_obat"),
                        rs.getBigDecimal("total_tagihan"),
                        rs.getBigDecimal("jumlah_bayar"),
                        rs.getBigDecimal("kembalian"),
                        rs.getString("metode_pembayaran"),
                        rs.getString("status_pembayaran")
                    });
                }
            }
            tblPembayaran.setModel(tableModel);
            int[] widths = {120, 120, 110, 110, 110, 110, 120, 120, 120, 120, 120};
            for (int i = 0; i < widths.length; i++) {
                tblPembayaran.getColumnModel().getColumn(i).setPreferredWidth(widths[i]);
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Data pembayaran gagal dipanggil: " + e.getMessage());
        }
    }

    private void simpanData() {
        if (conn == null || !isInputValid()) {
            return;
        }
        String sql = "INSERT INTO pembayaran (id_pembayaran, no_kunjungan, tanggal_pembayaran, biaya_konsultasi, "
                + "biaya_tindakan, biaya_obat, total_tagihan, jumlah_bayar, kembalian, metode_pembayaran, status_pembayaran) "
                + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        executeSaveOrUpdate(sql, false);
    }

    private void ubahData() {
        if (conn == null || !isInputValid()) {
            return;
        }
        String sql = "UPDATE pembayaran SET no_kunjungan = ?, tanggal_pembayaran = ?, biaya_konsultasi = ?, "
                + "biaya_tindakan = ?, biaya_obat = ?, total_tagihan = ?, jumlah_bayar = ?, kembalian = ?, "
                + "metode_pembayaran = ?, status_pembayaran = ? WHERE id_pembayaran = ?";
        executeSaveOrUpdate(sql, true);
    }

    private void executeSaveOrUpdate(String sql, boolean update) {
        try (PreparedStatement stat = conn.prepareStatement(sql)) {
            if (update) {
                bindPayment(stat, 1);
                stat.setString(11, txtIdPembayaran.getText().trim());
            } else {
                stat.setString(1, txtIdPembayaran.getText().trim());
                bindPayment(stat, 2);
            }
            stat.executeUpdate();
            JOptionPane.showMessageDialog(this, update ? "Data pembayaran berhasil diubah." : "Data pembayaran berhasil disimpan.");
            clear();
            dataTable();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Data pembayaran gagal disimpan: " + e.getMessage());
        }
    }

    private void bindPayment(PreparedStatement stat, int start) throws SQLException {
        stat.setString(start, txtNoKunjungan.getText().trim());
        stat.setDate(start + 1, parseDate(DateChooserHelper.getText(txtTanggal).trim()));
        stat.setBigDecimal(start + 2, parseMoney(txtBiayaKonsultasi.getText(), "Biaya konsultasi"));
        stat.setBigDecimal(start + 3, parseMoney(txtBiayaTindakan.getText(), "Biaya tindakan"));
        stat.setBigDecimal(start + 4, parseMoney(txtBiayaObat.getText(), "Biaya obat"));
        stat.setBigDecimal(start + 5, parseMoney(txtTotalTagihan.getText(), "Total tagihan"));
        stat.setBigDecimal(start + 6, parseMoney(txtJumlahBayar.getText(), "Jumlah bayar"));
        stat.setBigDecimal(start + 7, parseMoney(txtKembalian.getText(), "Kembalian"));
        stat.setString(start + 8, cmbMetode.getSelectedItem().toString());
        stat.setString(start + 9, cmbStatus.getSelectedItem().toString());
    }

    private void hapusData() {
        if (conn == null || txtIdPembayaran.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Pilih pembayaran yang ingin dihapus.");
            return;
        }
        int ok = JOptionPane.showConfirmDialog(this, "Hapus pembayaran ini?", "Konfirmasi", JOptionPane.YES_NO_OPTION);
        if (ok != JOptionPane.YES_OPTION) {
            return;
        }
        try (PreparedStatement stat = conn.prepareStatement("DELETE FROM pembayaran WHERE id_pembayaran = ?")) {
            stat.setString(1, txtIdPembayaran.getText().trim());
            stat.executeUpdate();
            JOptionPane.showMessageDialog(this, "Data pembayaran berhasil dihapus.");
            clear();
            dataTable();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Data pembayaran gagal dihapus: " + e.getMessage());
        }
    }

    private void tableMouseClicked() {
        int row = tblPembayaran.getSelectedRow();
        if (row < 0) {
            return;
        }
        int modelRow = tblPembayaran.convertRowIndexToModel(row);
        txtIdPembayaran.setText(tableModel.getValueAt(modelRow, 0).toString());
        txtNoKunjungan.setText(tableModel.getValueAt(modelRow, 1).toString());
        DateChooserHelper.setDateText(txtTanggal, tableModel.getValueAt(modelRow, 2).toString());
        txtBiayaKonsultasi.setText(tableModel.getValueAt(modelRow, 3).toString());
        txtBiayaTindakan.setText(tableModel.getValueAt(modelRow, 4).toString());
        txtBiayaObat.setText(tableModel.getValueAt(modelRow, 5).toString());
        txtTotalTagihan.setText(tableModel.getValueAt(modelRow, 6).toString());
        txtJumlahBayar.setText(tableModel.getValueAt(modelRow, 7).toString());
        txtKembalian.setText(tableModel.getValueAt(modelRow, 8).toString());
        cmbMetode.setSelectedItem(tableModel.getValueAt(modelRow, 9).toString());
        cmbStatus.setSelectedItem(tableModel.getValueAt(modelRow, 10).toString());
        btnSimpan.setEnabled(false);
    }

    public static void main(String args[]) {
        java.awt.EventQueue.invokeLater(() -> new FormPembayaran().setVisible(true));
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnBatal;
    private javax.swing.JButton btnCari;
    private javax.swing.JButton btnHapus;
    private javax.swing.JButton btnHitung;
    private javax.swing.JButton btnKeluar;
    private javax.swing.JButton btnPilihKunjungan;
    private javax.swing.JButton btnSimpan;
    private javax.swing.JButton btnUbah;
    private javax.swing.JComboBox<String> cmbMetode;
    private javax.swing.JComboBox<String> cmbStatus;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JLabel lblBiayaKonsultasi;
    private javax.swing.JLabel lblBiayaObat;
    private javax.swing.JLabel lblBiayaTindakan;
    private javax.swing.JLabel lblIdPembayaran;
    private javax.swing.JLabel lblJumlahBayar;
    private javax.swing.JLabel lblKembalian;
    private javax.swing.JLabel lblMetode;
    private javax.swing.JLabel lblNoKunjungan;
    private javax.swing.JLabel lblStatus;
    private javax.swing.JLabel lblTanggal;
    private javax.swing.JLabel lblTitle;
    private javax.swing.JLabel lblTotalTagihan;
    private javax.swing.JPanel panelData;
    private javax.swing.JTable tblPembayaran;
    private javax.swing.JTextField txtBiayaKonsultasi;
    private javax.swing.JTextField txtBiayaObat;
    private javax.swing.JTextField txtBiayaTindakan;
    private javax.swing.JTextField txtCari;
    private javax.swing.JTextField txtIdPembayaran;
    private javax.swing.JTextField txtJumlahBayar;
    private javax.swing.JTextField txtKembalian;
    private javax.swing.JTextField txtNoKunjungan;
    private com.toedter.calendar.JDateChooser txtTanggal;
    private javax.swing.JTextField txtTotalTagihan;
    // End of variables declaration//GEN-END:variables
}
