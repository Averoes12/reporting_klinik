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

public class FormPasien extends javax.swing.JFrame {

    private static final String ID_PASIEN_PREFIX = "PSN";
    private static final int ID_PASIEN_DIGITS = 3;
    private static final String DATE_FORMAT_HINT = "yyyy-MM-dd";
    private static final String DATE_FORMAT_EXAMPLE = "1990-01-31";
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter
            .ofPattern("uuuu-MM-dd")
            .withResolverStyle(ResolverStyle.STRICT);

    private final Connection conn = new DBConnection().connect();
    private DefaultTableModel tableModel;

    public FormPasien() {
        initComponents();
        clear();
        focusForm();
        dataTable();
        setLocationRelativeTo(null);
    }

    private void focusForm() {
        txtNoRm.requestFocus();
    }

    private void clear() {
        generateIdPasien();
        txtNoRm.setText("");
        txtNik.setText("");
        txtNamaPasien.setText("");
        txtTanggalLahir.setText("");
        txtNoHp.setText("");
        txtAlamat.setText("");
        txtAlergi.setText("");
        txtCari.setText("");
        gender.clearSelection();
        btnSimpan.setEnabled(true);
    }

    private void generateIdPasien() {
        txtIdPasien.setText(nextIdPasien());
    }

    private String nextIdPasien() {
        if (conn == null) {
            return formatIdPasien(1);
        }

        String sql = "SELECT COALESCE(MAX(CASE "
                + "WHEN id_pasien REGEXP '^" + ID_PASIEN_PREFIX + "[0-9]+$' THEN CAST(SUBSTRING(id_pasien, 4) AS UNSIGNED) "
                + "WHEN id_pasien REGEXP '^[0-9]+$' THEN CAST(id_pasien AS UNSIGNED) "
                + "ELSE 0 END), 0) + 1 AS next_number "
                + "FROM pasien";
        try (PreparedStatement stat = conn.prepareStatement(sql);
             ResultSet hasil = stat.executeQuery()) {
            if (hasil.next()) {
                return formatIdPasien(hasil.getInt("next_number"));
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "ID otomatis gagal dibuat: " + e.getMessage());
        }
        return formatIdPasien(1);
    }

    private String formatIdPasien(int number) {
        return ID_PASIEN_PREFIX + String.format("%0" + ID_PASIEN_DIGITS + "d", number);
    }

    private boolean isIdPasienValid() {
        return txtIdPasien.getText().trim().matches(ID_PASIEN_PREFIX + "\\d{" + ID_PASIEN_DIGITS + ",}");
    }

    private String selectedGender() {
        if (rbL.isSelected()) {
            return "L";
        }
        if (rbP.isSelected()) {
            return "P";
        }
        return "";
    }

    private void setSelectedGender(String genderValue) {
        if ("L".equalsIgnoreCase(genderValue) || "Laki-Laki".equalsIgnoreCase(genderValue)) {
            rbL.setSelected(true);
        } else if ("P".equalsIgnoreCase(genderValue) || "Perempuan".equalsIgnoreCase(genderValue)) {
            rbP.setSelected(true);
        } else {
            gender.clearSelection();
        }
    }

    private String displayGender(String genderValue) {
        if (genderValue == null || genderValue.trim().isEmpty()) {
            return "";
        }
        return "L".equalsIgnoreCase(genderValue) ? "Laki-Laki" : "Perempuan";
    }

    private String valueOrEmpty(String value) {
        return value == null ? "" : value;
    }

    private boolean isInputValid() {
        if (txtIdPasien.getText().trim().isEmpty()
                || txtNoRm.getText().trim().isEmpty()
                || txtNik.getText().trim().isEmpty()
                || txtNamaPasien.getText().trim().isEmpty()
                || selectedGender().isEmpty()
                || txtTanggalLahir.getText().trim().isEmpty()
                || txtAlamat.getText().trim().isEmpty()
                || txtNoHp.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Data pasien masih ada yang kosong.");
            return false;
        }

        if (!isIdPasienValid()) {
            JOptionPane.showMessageDialog(this, "ID pasien harus berformat " + formatIdPasien(1) + ".");
            return false;
        }

        if (parseTanggal(txtTanggalLahir.getText()) == null) {
            return false;
        }

        return true;
    }

    private Date parseTanggal(String value) {
        String trimmed = value.trim();
        if (!trimmed.matches("\\d{4}-\\d{2}-\\d{2}")) {
            showDateFormatMessage("Tanggal lahir");
            return null;
        }

        try {
            return Date.valueOf(LocalDate.parse(trimmed, DATE_FORMATTER));
        } catch (DateTimeParseException e) {
            showDateFormatMessage("Tanggal lahir");
            return null;
        }
    }

    private void showDateFormatMessage(String fieldName) {
        JOptionPane.showMessageDialog(this, fieldName + " harus berformat "
                + DATE_FORMAT_HINT + ". Contoh: " + DATE_FORMAT_EXAMPLE + ".");
    }

    private void dataTable() {
        Object[] columns = {"ID Pasien", "No. RM", "NIK", "Nama Pasien", "Jenis Kelamin", "Tanggal Lahir", "No. HP", "Alamat", "Alergi"};
        tableModel = new DefaultTableModel(null, columns) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        if (conn == null) {
            tblPasien.setModel(tableModel);
            return;
        }

        String cari = txtCari.getText().trim();
        String sql = "SELECT id_pasien, no_rm, nik, nama_pasien, jenis_kelamin, tanggal_lahir, alamat, no_hp, alergi "
                + "FROM pasien "
                + "WHERE no_rm LIKE ? OR nik LIKE ? OR nama_pasien LIKE ? "
                + "ORDER BY CASE "
                + "WHEN id_pasien REGEXP '^" + ID_PASIEN_PREFIX + "[0-9]+$' THEN CAST(SUBSTRING(id_pasien, 4) AS UNSIGNED) "
                + "WHEN id_pasien REGEXP '^[0-9]+$' THEN CAST(id_pasien AS UNSIGNED) "
                + "ELSE 0 END ASC";

        try (PreparedStatement stat = conn.prepareStatement(sql)) {
            String keyword = "%" + cari + "%";
            stat.setString(1, keyword);
            stat.setString(2, keyword);
            stat.setString(3, keyword);

            try (ResultSet hasil = stat.executeQuery()) {
                while (hasil.next()) {
                    Date tglLahir = hasil.getDate("tanggal_lahir");
                    tableModel.addRow(new Object[]{
                        valueOrEmpty(hasil.getString("id_pasien")),
                        valueOrEmpty(hasil.getString("no_rm")),
                        valueOrEmpty(hasil.getString("nik")),
                        valueOrEmpty(hasil.getString("nama_pasien")),
                        displayGender(hasil.getString("jenis_kelamin")),
                        tglLahir == null ? "" : tglLahir.toString(),
                        valueOrEmpty(hasil.getString("no_hp")),
                        valueOrEmpty(hasil.getString("alamat")),
                        valueOrEmpty(hasil.getString("alergi"))
                    });
                }
            }

            tblPasien.setModel(tableModel);
            setTableColumnWidth();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Data pasien gagal dipanggil: " + e.getMessage());
        }
    }

    private void setTableColumnWidth() {
        int[] widths = {85, 90, 120, 150, 110, 100, 100, 220, 140};
        for (int i = 0; i < widths.length && i < tblPasien.getColumnModel().getColumnCount(); i++) {
            tblPasien.getColumnModel().getColumn(i).setPreferredWidth(widths[i]);
        }
    }

    private void onlyNumber(java.awt.event.KeyEvent evt) {
        char karakter = evt.getKeyChar();
        if (!Character.isDigit(karakter) && !Character.isISOControl(karakter)) {
            evt.consume();
        }
    }

    private void onlyDateCharacter(java.awt.event.KeyEvent evt) {
        char karakter = evt.getKeyChar();
        if (!Character.isDigit(karakter) && karakter != '-' && !Character.isISOControl(karakter)) {
            evt.consume();
        }
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        gender = new javax.swing.ButtonGroup();
        lblTitle = new javax.swing.JLabel();
        lblId = new javax.swing.JLabel();
        lblNoRm = new javax.swing.JLabel();
        lblNik = new javax.swing.JLabel();
        lblNama = new javax.swing.JLabel();
        lblJenis = new javax.swing.JLabel();
        lblTanggalLahir = new javax.swing.JLabel();
        lblNoHp = new javax.swing.JLabel();
        lblAlamat = new javax.swing.JLabel();
        lblAlergi = new javax.swing.JLabel();
        txtIdPasien = new javax.swing.JTextField();
        txtNoRm = new javax.swing.JTextField();
        txtNik = new javax.swing.JTextField();
        txtNamaPasien = new javax.swing.JTextField();
        rbL = new javax.swing.JRadioButton();
        rbP = new javax.swing.JRadioButton();
        txtTanggalLahir = new javax.swing.JTextField();
        txtNoHp = new javax.swing.JTextField();
        jScrollPane2 = new javax.swing.JScrollPane();
        txtAlamat = new javax.swing.JTextArea();
        jScrollPane3 = new javax.swing.JScrollPane();
        txtAlergi = new javax.swing.JTextArea();
        btnSimpan = new javax.swing.JButton();
        btnUbah = new javax.swing.JButton();
        btnHapus = new javax.swing.JButton();
        btnBatal = new javax.swing.JButton();
        btnKeluar = new javax.swing.JButton();
        panelData = new javax.swing.JPanel();
        txtCari = new javax.swing.JTextField();
        btnCari = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        tblPasien = new javax.swing.JTable();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Form Pasien");

        lblTitle.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        lblTitle.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblTitle.setText("Data Pasien");

        lblId.setText("ID Pasien");

        lblNoRm.setText("No. RM");

        lblNik.setText("NIK");

        lblNama.setText("Nama Pasien");

        lblJenis.setText("Jenis Kelamin");

        lblTanggalLahir.setText("Tanggal Lahir (yyyy-MM-dd)");

        lblNoHp.setText("No. HP");

        lblAlamat.setText("Alamat");

        lblAlergi.setText("Alergi");

        txtIdPasien.setEditable(false);

        txtNik.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtNikKeyTyped(evt);
            }
        });

        gender.add(rbL);
        rbL.setText("Laki-Laki");

        gender.add(rbP);
        rbP.setText("Perempuan");

        txtTanggalLahir.setToolTipText("Format tanggal: yyyy-MM-dd, contoh: 1990-01-31.");
        txtTanggalLahir.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtTanggalLahirKeyTyped(evt);
            }
        });

        txtNoHp.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtNoHpKeyTyped(evt);
            }
        });

        txtAlamat.setColumns(20);
        txtAlamat.setRows(4);
        jScrollPane2.setViewportView(txtAlamat);

        txtAlergi.setColumns(20);
        txtAlergi.setRows(3);
        jScrollPane3.setViewportView(txtAlergi);

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

        panelData.setBorder(javax.swing.BorderFactory.createTitledBorder("Data Pasien"));

        txtCari.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtCariKeyPressed(evt);
            }
        });

        btnCari.setText("Cari");
        btnCari.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCariActionPerformed(evt);
            }
        });

        tblPasien.setAutoCreateRowSorter(true);
        tblPasien.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_OFF);
        tblPasien.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "ID Pasien", "No. RM", "NIK", "Nama Pasien", "Jenis Kelamin", "Tanggal Lahir", "No. HP", "Alamat", "Alergi"
            }
        ));
        tblPasien.setRowHeight(24);
        tblPasien.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblPasienMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(tblPasien);

        javax.swing.GroupLayout panelDataLayout = new javax.swing.GroupLayout(panelData);
        panelData.setLayout(panelDataLayout);
        panelDataLayout.setHorizontalGroup(
            panelDataLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelDataLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(panelDataLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 816, Short.MAX_VALUE)
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
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 210, javax.swing.GroupLayout.PREFERRED_SIZE)
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
                            .addComponent(lblNoRm)
                            .addComponent(lblNik)
                            .addComponent(lblNama)
                            .addComponent(lblJenis)
                            .addComponent(lblTanggalLahir)
                            .addComponent(lblNoHp)
                            .addComponent(lblAlamat)
                            .addComponent(lblAlergi))
                        .addGap(29, 29, 29)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(txtIdPasien)
                            .addComponent(txtNoRm)
                            .addComponent(txtNik)
                            .addComponent(txtNamaPasien)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(rbL)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(rbP))
                            .addComponent(txtTanggalLahir)
                            .addComponent(txtNoHp)
                            .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 260, Short.MAX_VALUE)
                            .addComponent(jScrollPane3)))
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
                    .addComponent(txtIdPasien, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblNoRm)
                    .addComponent(txtNoRm, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblNik)
                    .addComponent(txtNik, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblNama)
                    .addComponent(txtNamaPasien, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblJenis)
                    .addComponent(rbL)
                    .addComponent(rbP))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblTanggalLahir)
                    .addComponent(txtTanggalLahir, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblNoHp)
                    .addComponent(txtNoHp, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lblAlamat)
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 76, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lblAlergi)
                    .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 62, javax.swing.GroupLayout.PREFERRED_SIZE))
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

    private void btnSimpanActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSimpanActionPerformed
        if (conn == null) {
            JOptionPane.showMessageDialog(this, "Koneksi database belum berhasil.");
            return;
        }

        if (!isInputValid()) {
            return;
        }

        Date tanggalLahir = parseTanggal(txtTanggalLahir.getText());
        if (tanggalLahir == null) {
            return;
        }

        String sql = "INSERT INTO pasien (id_pasien, no_rm, nik, nama_pasien, jenis_kelamin, tanggal_lahir, alamat, no_hp, alergi) "
                + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (PreparedStatement stat = conn.prepareStatement(sql)) {
            stat.setString(1, txtIdPasien.getText().trim());
            stat.setString(2, txtNoRm.getText().trim());
            stat.setString(3, txtNik.getText().trim());
            stat.setString(4, txtNamaPasien.getText().trim());
            stat.setString(5, selectedGender());
            stat.setDate(6, tanggalLahir);
            stat.setString(7, txtAlamat.getText().trim());
            stat.setString(8, txtNoHp.getText().trim());
            stat.setString(9, txtAlergi.getText().trim());
            stat.executeUpdate();

            JOptionPane.showMessageDialog(this, "Data pasien berhasil disimpan.");
            clear();
            focusForm();
            dataTable();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Data pasien gagal disimpan: " + e.getMessage());
        }
    }//GEN-LAST:event_btnSimpanActionPerformed

    private void btnUbahActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnUbahActionPerformed
        if (conn == null) {
            JOptionPane.showMessageDialog(this, "Koneksi database belum berhasil.");
            return;
        }

        if (!isInputValid()) {
            return;
        }

        Date tanggalLahir = parseTanggal(txtTanggalLahir.getText());
        if (tanggalLahir == null) {
            return;
        }

        String sql = "UPDATE pasien SET no_rm = ?, nik = ?, nama_pasien = ?, jenis_kelamin = ?, tanggal_lahir = ?, alamat = ?, no_hp = ?, alergi = ? "
                + "WHERE id_pasien = ?";

        try (PreparedStatement stat = conn.prepareStatement(sql)) {
            stat.setString(1, txtNoRm.getText().trim());
            stat.setString(2, txtNik.getText().trim());
            stat.setString(3, txtNamaPasien.getText().trim());
            stat.setString(4, selectedGender());
            stat.setDate(5, tanggalLahir);
            stat.setString(6, txtAlamat.getText().trim());
            stat.setString(7, txtNoHp.getText().trim());
            stat.setString(8, txtAlergi.getText().trim());
            stat.setString(9, txtIdPasien.getText().trim());
            stat.executeUpdate();

            JOptionPane.showMessageDialog(this, "Data pasien berhasil diubah.");
            clear();
            focusForm();
            dataTable();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Data pasien gagal diubah: " + e.getMessage());
        }
    }//GEN-LAST:event_btnUbahActionPerformed

    private void btnHapusActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnHapusActionPerformed
        if (conn == null) {
            JOptionPane.showMessageDialog(this, "Koneksi database belum berhasil.");
            return;
        }

        if (txtIdPasien.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Pilih data pasien yang ingin dihapus terlebih dahulu.");
            return;
        }

        int ok = JOptionPane.showConfirmDialog(this, "Hapus data ini?", "Konfirmasi", JOptionPane.YES_NO_OPTION);
        if (ok != JOptionPane.YES_OPTION) {
            return;
        }

        String sql = "DELETE FROM pasien WHERE id_pasien = ?";
        try (PreparedStatement stat = conn.prepareStatement(sql)) {
            stat.setString(1, txtIdPasien.getText().trim());
            stat.executeUpdate();

            JOptionPane.showMessageDialog(this, "Data pasien berhasil dihapus.");
            clear();
            focusForm();
            dataTable();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Data pasien gagal dihapus: " + e.getMessage());
        }
    }//GEN-LAST:event_btnHapusActionPerformed

    private void btnBatalActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnBatalActionPerformed
        clear();
        focusForm();
        dataTable();
    }//GEN-LAST:event_btnBatalActionPerformed

    private void btnKeluarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnKeluarActionPerformed
        dispose();
    }//GEN-LAST:event_btnKeluarActionPerformed

    private void btnCariActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCariActionPerformed
        dataTable();
    }//GEN-LAST:event_btnCariActionPerformed

    private void txtCariKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtCariKeyPressed
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            dataTable();
        }
    }//GEN-LAST:event_txtCariKeyPressed

    private void tblPasienMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblPasienMouseClicked
        int row = tblPasien.getSelectedRow();
        if (row < 0) {
            return;
        }

        int modelRow = tblPasien.convertRowIndexToModel(row);
        txtIdPasien.setText(tableModel.getValueAt(modelRow, 0).toString());
        txtNoRm.setText(tableModel.getValueAt(modelRow, 1).toString());
        txtNik.setText(tableModel.getValueAt(modelRow, 2).toString());
        txtNamaPasien.setText(tableModel.getValueAt(modelRow, 3).toString());
        setSelectedGender(tableModel.getValueAt(modelRow, 4).toString());
        txtTanggalLahir.setText(tableModel.getValueAt(modelRow, 5).toString());
        txtNoHp.setText(tableModel.getValueAt(modelRow, 6).toString());
        txtAlamat.setText(tableModel.getValueAt(modelRow, 7).toString());
        txtAlergi.setText(tableModel.getValueAt(modelRow, 8).toString());
        btnSimpan.setEnabled(false);
    }//GEN-LAST:event_tblPasienMouseClicked

    private void txtNikKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtNikKeyTyped
        onlyNumber(evt);
    }//GEN-LAST:event_txtNikKeyTyped

    private void txtNoHpKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtNoHpKeyTyped
        onlyNumber(evt);
    }//GEN-LAST:event_txtNoHpKeyTyped

    private void txtTanggalLahirKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtTanggalLahirKeyTyped
        onlyDateCharacter(evt);
    }//GEN-LAST:event_txtTanggalLahirKeyTyped

    public static void main(String args[]) {
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(FormPasien.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }

        java.awt.EventQueue.invokeLater(() -> new FormPasien().setVisible(true));
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnBatal;
    private javax.swing.JButton btnCari;
    private javax.swing.JButton btnHapus;
    private javax.swing.JButton btnKeluar;
    private javax.swing.JButton btnSimpan;
    private javax.swing.JButton btnUbah;
    private javax.swing.ButtonGroup gender;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JLabel lblAlergi;
    private javax.swing.JLabel lblAlamat;
    private javax.swing.JLabel lblId;
    private javax.swing.JLabel lblJenis;
    private javax.swing.JLabel lblNama;
    private javax.swing.JLabel lblNik;
    private javax.swing.JLabel lblNoHp;
    private javax.swing.JLabel lblNoRm;
    private javax.swing.JLabel lblTanggalLahir;
    private javax.swing.JLabel lblTitle;
    private javax.swing.JPanel panelData;
    private javax.swing.JRadioButton rbL;
    private javax.swing.JRadioButton rbP;
    private javax.swing.JTable tblPasien;
    private javax.swing.JTextArea txtAlergi;
    private javax.swing.JTextArea txtAlamat;
    private javax.swing.JTextField txtCari;
    private javax.swing.JTextField txtIdPasien;
    private javax.swing.JTextField txtNamaPasien;
    private javax.swing.JTextField txtNik;
    private javax.swing.JTextField txtNoHp;
    private javax.swing.JTextField txtNoRm;
    private javax.swing.JTextField txtTanggalLahir;
    // End of variables declaration//GEN-END:variables
}
