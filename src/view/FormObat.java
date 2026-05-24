package view;

import connection.DBConnection;
import java.awt.event.KeyEvent;
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
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;

public class FormObat extends javax.swing.JFrame {

    private static final String ID_OBAT_PREFIX = "OBT";
    private static final int ID_OBAT_DIGITS = 3;
    private static final String DATE_FORMAT_HINT = "yyyy-MM-dd";
    private static final String DATE_FORMAT_EXAMPLE = "2026-12-31";
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter
            .ofPattern("uuuu-MM-dd")
            .withResolverStyle(ResolverStyle.STRICT);

    private final Connection conn = new DBConnection().connect();
    private DefaultTableModel tableModel;

    public FormObat() {
        initComponents();clear();
        focusForm();
        dataTable();
        setLocationRelativeTo(null);
    }

    private void focusForm() {
        txtNamaObat.requestFocus();
    }

    private void clear() {
        generateIdObat();
        txtNamaObat.setText("");
        txtSatuan.setText("");
        txtHarga.setText("");
        txtStokAwal.setText("0");
        txtStokMasuk.setText("0");
        txtStokRetur.setText("0");
        txtStokAkhir.setText("0");
        DateChooserHelper.clear(txtTanggalExpired);
        txtCari.setText("");
        btnSimpan.setEnabled(true);
        updateStokAkhir();
    }

    private void generateIdObat() {
        txtIdObat.setText(nextIdObat());
    }

    private String nextIdObat() {
        if (conn == null) {
            return formatIdObat(1);
        }

        String sql = "SELECT COALESCE(MAX(CASE "
                + "WHEN id_obat REGEXP ? THEN CAST(SUBSTRING(id_obat, ?) AS UNSIGNED) "
                + "WHEN id_obat REGEXP ? THEN CAST(id_obat AS UNSIGNED) "
                + "ELSE 0 END), 0) + 1 AS next_number FROM obat";
        try (PreparedStatement stat = conn.prepareStatement(sql)) {
            stat.setString(1, "^" + ID_OBAT_PREFIX + "[0-9]+$");
            stat.setInt(2, ID_OBAT_PREFIX.length() + 1);
            stat.setString(3, "^[0-9]+$");

            try (ResultSet hasil = stat.executeQuery()) {
                if (hasil.next()) {
                    return formatIdObat(hasil.getInt("next_number"));
                }
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "ID otomatis gagal dibuat: " + e.getMessage());
        }
        return formatIdObat(1);
    }

    private String formatIdObat(int number) {
        return ID_OBAT_PREFIX + String.format("%0" + ID_OBAT_DIGITS + "d", number);
    }

    private boolean isInputValid() {
        if (txtIdObat.getText().trim().isEmpty()
                || txtNamaObat.getText().trim().isEmpty()
                || txtSatuan.getText().trim().isEmpty()
                || txtHarga.getText().trim().isEmpty()
                || txtStokAwal.getText().trim().isEmpty()
                || txtStokMasuk.getText().trim().isEmpty()
                || txtStokRetur.getText().trim().isEmpty()
                || DateChooserHelper.getText(txtTanggalExpired).trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Data obat masih ada yang kosong.");
            return false;
        }

        if (parseBigDecimal(txtHarga.getText(), "Harga") == null) {
            return false;
        }

        if (parseInteger(txtStokAwal.getText(), "Stok awal") == null
                || parseInteger(txtStokMasuk.getText(), "Stok masuk") == null
                || parseInteger(txtStokRetur.getText(), "Stok retur") == null) {
            return false;
        }

        if (parseDate(DateChooserHelper.getText(txtTanggalExpired), "Tanggal expired") == null) {
            return false;
        }

        return true;
    }

    private BigDecimal parseBigDecimal(String value, String fieldName) {
        try {
            return new BigDecimal(value.trim());
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, fieldName + " harus berupa angka.");
            return null;
        }
    }

    private Integer parseInteger(String value, String fieldName) {
        try {
            return Integer.parseInt(value.trim());
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, fieldName + " harus berupa angka bulat.");
            return null;
        }
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

    private int currentStockAkhir() {
        Integer stokAwal = parseIntegerSilent(txtStokAwal.getText());
        Integer stokMasuk = parseIntegerSilent(txtStokMasuk.getText());
        Integer stokRetur = parseIntegerSilent(txtStokRetur.getText());

        if (stokAwal == null || stokMasuk == null || stokRetur == null) {
            return 0;
        }
        return stokAwal + stokMasuk - stokRetur;
    }

    private Integer parseIntegerSilent(String value) {
        try {
            return Integer.parseInt(value.trim());
        } catch (Exception e) {
            return null;
        }
    }

    private void updateStokAkhir() {
        Integer stokAwal = parseIntegerSilent(txtStokAwal.getText());
        Integer stokMasuk = parseIntegerSilent(txtStokMasuk.getText());
        Integer stokRetur = parseIntegerSilent(txtStokRetur.getText());
        if (stokAwal == null || stokMasuk == null || stokRetur == null) {
            txtStokAkhir.setText("");
            return;
        }
        txtStokAkhir.setText(String.valueOf(stokAwal + stokMasuk - stokRetur));
    }

    private String valueOrEmpty(String value) {
        return value == null ? "" : value;
    }

    private void dataTable() {
        Object[] columns = {"ID Obat", "Nama Obat", "Satuan", "Harga", "Stok Awal", "Stok Masuk", "Stok Retur", "Stok Akhir", "Tanggal Expired"};
        tableModel = new DefaultTableModel(null, columns) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        if (conn == null) {
            tblObat.setModel(tableModel);
            return;
        }

        String cari = txtCari.getText().trim();
        String sql = "SELECT id_obat, nama_obat, satuan, harga, stok_awal, stok_masuk, stok_retur, stok_akhir, tanggal_expired "
                + "FROM obat "
                + "WHERE id_obat LIKE ? OR nama_obat LIKE ? OR satuan LIKE ? "
                + "ORDER BY CASE "
                + "WHEN id_obat REGEXP '^" + ID_OBAT_PREFIX + "[0-9]+$' THEN CAST(SUBSTRING(id_obat, " + (ID_OBAT_PREFIX.length() + 1) + ") AS UNSIGNED) "
                + "WHEN id_obat REGEXP '^[0-9]+$' THEN CAST(id_obat AS UNSIGNED) "
                + "ELSE 0 END ASC";

        try (PreparedStatement stat = conn.prepareStatement(sql)) {
            String keyword = "%" + cari + "%";
            stat.setString(1, keyword);
            stat.setString(2, keyword);
            stat.setString(3, keyword);

            try (ResultSet hasil = stat.executeQuery()) {
                while (hasil.next()) {
                    Date expired = hasil.getDate("tanggal_expired");
                    tableModel.addRow(new Object[]{
                        valueOrEmpty(hasil.getString("id_obat")),
                        valueOrEmpty(hasil.getString("nama_obat")),
                        valueOrEmpty(hasil.getString("satuan")),
                        valueOrEmpty(hasil.getString("harga")),
                        valueOrEmpty(hasil.getString("stok_awal")),
                        valueOrEmpty(hasil.getString("stok_masuk")),
                        valueOrEmpty(hasil.getString("stok_retur")),
                        valueOrEmpty(hasil.getString("stok_akhir")),
                        expired == null ? "" : expired.toString()
                    });
                }
            }

            tblObat.setModel(tableModel);
            setTableColumnWidth();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Data obat gagal dipanggil: " + e.getMessage());
        }
    }

    private void setTableColumnWidth() {
        int[] widths = {80, 190, 90, 100, 90, 90, 90, 90, 120};
        for (int i = 0; i < widths.length && i < tblObat.getColumnModel().getColumnCount(); i++) {
            tblObat.getColumnModel().getColumn(i).setPreferredWidth(widths[i]);
        }
    }

    private void onlyNumber(java.awt.event.KeyEvent evt) {
        char karakter = evt.getKeyChar();
        if (!Character.isDigit(karakter) && !Character.isISOControl(karakter)) {
            evt.consume();
        }
    }

    private void onlyDecimal(java.awt.event.KeyEvent evt) {
        char karakter = evt.getKeyChar();
        JTextField source = (JTextField) evt.getComponent();
        if (Character.isDigit(karakter) || Character.isISOControl(karakter)) {
            return;
        }
        if (karakter == '.' && !source.getText().contains(".")) {
            return;
        }
        evt.consume();
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

        lblTitle = new javax.swing.JLabel();
        lblId = new javax.swing.JLabel();
        lblNama = new javax.swing.JLabel();
        lblSatuan = new javax.swing.JLabel();
        lblHarga = new javax.swing.JLabel();
        lblStokAwal = new javax.swing.JLabel();
        lblStokMasuk = new javax.swing.JLabel();
        lblStokRetur = new javax.swing.JLabel();
        lblStokAkhir = new javax.swing.JLabel();
        lblTanggalExpired = new javax.swing.JLabel();
        txtIdObat = new javax.swing.JTextField();
        txtNamaObat = new javax.swing.JTextField();
        txtSatuan = new javax.swing.JTextField();
        txtHarga = new javax.swing.JTextField();
        txtStokAwal = new javax.swing.JTextField();
        txtStokMasuk = new javax.swing.JTextField();
        txtStokRetur = new javax.swing.JTextField();
        txtStokAkhir = new javax.swing.JTextField();
        txtTanggalExpired = DateChooserHelper.createDateChooser();
        DateChooserHelper.preventBackdate(txtTanggalExpired);
        btnSimpan = new javax.swing.JButton();
        btnUbah = new javax.swing.JButton();
        btnHapus = new javax.swing.JButton();
        btnBatal = new javax.swing.JButton();
        btnKeluar = new javax.swing.JButton();
        panelData = new javax.swing.JPanel();
        txtCari = new javax.swing.JTextField();
        btnCari = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        tblObat = new javax.swing.JTable();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Form Obat");

        lblTitle.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        lblTitle.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblTitle.setText("Data Obat");

        lblId.setText("ID Obat");

        lblNama.setText("Nama Obat");

        lblSatuan.setText("Satuan");

        lblHarga.setText("Harga");

        lblStokAwal.setText("Stok Awal");

        lblStokMasuk.setText("Stok Masuk");

        lblStokRetur.setText("Stok Retur");

        lblStokAkhir.setText("Stok Akhir");

        lblTanggalExpired.setText("Tanggal Expired (yyyy-MM-dd)");

        txtIdObat.setEditable(false);

        txtHarga.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtHargaKeyTyped(evt);
            }
        });

        txtStokAwal.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtStokAwalKeyReleased(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtStokAwalKeyTyped(evt);
            }
        });

        txtStokMasuk.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtStokMasukKeyReleased(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtStokMasukKeyTyped(evt);
            }
        });

        txtStokRetur.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtStokReturKeyReleased(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtStokReturKeyTyped(evt);
            }
        });

        txtStokAkhir.setEditable(false);

        txtTanggalExpired.setToolTipText("Format tanggal: yyyy-MM-dd, contoh: 2026-12-31.");
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

        panelData.setBorder(javax.swing.BorderFactory.createTitledBorder("Data Obat"));

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

        tblObat.setAutoCreateRowSorter(true);
        tblObat.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_OFF);
        tblObat.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "ID Obat", "Nama Obat", "Satuan", "Harga", "Stok Awal", "Stok Masuk", "Stok Retur", "Stok Akhir", "Tanggal Expired"
            }
        ));
        tblObat.setRowHeight(24);
        tblObat.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblObatMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(tblObat);

        javax.swing.GroupLayout panelDataLayout = new javax.swing.GroupLayout(panelData);
        panelData.setLayout(panelDataLayout);
        panelDataLayout.setHorizontalGroup(
            panelDataLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelDataLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(panelDataLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 846, Short.MAX_VALUE)
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
                            .addComponent(lblNama)
                            .addComponent(lblSatuan)
                            .addComponent(lblHarga)
                            .addComponent(lblStokAwal)
                            .addComponent(lblStokMasuk)
                            .addComponent(lblStokRetur)
                            .addComponent(lblStokAkhir)
                            .addComponent(lblTanggalExpired))
                        .addGap(29, 29, 29)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(txtIdObat)
                            .addComponent(txtNamaObat)
                            .addComponent(txtSatuan)
                            .addComponent(txtHarga)
                            .addComponent(txtStokAwal)
                            .addComponent(txtStokMasuk)
                            .addComponent(txtStokRetur)
                            .addComponent(txtStokAkhir)
                            .addComponent(txtTanggalExpired, javax.swing.GroupLayout.DEFAULT_SIZE, 260, Short.MAX_VALUE)))
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
                    .addComponent(txtIdObat, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblNama)
                    .addComponent(txtNamaObat, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblSatuan)
                    .addComponent(txtSatuan, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblHarga)
                    .addComponent(txtHarga, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblStokAwal)
                    .addComponent(txtStokAwal, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblStokMasuk)
                    .addComponent(txtStokMasuk, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblStokRetur)
                    .addComponent(txtStokRetur, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblStokAkhir)
                    .addComponent(txtStokAkhir, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblTanggalExpired)
                    .addComponent(txtTanggalExpired, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
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

        updateStokAkhir();
        if (!isInputValid()) {
            return;
        }

        int stokAkhir = currentStockAkhir();
        if (stokAkhir < 0) {
            JOptionPane.showMessageDialog(this, "Stok akhir tidak boleh minus.");
            return;
        }

        Date tanggalExpired = parseDate(DateChooserHelper.getText(txtTanggalExpired), "Tanggal expired");
        BigDecimal harga = parseBigDecimal(txtHarga.getText(), "Harga");
        Integer stokAwal = parseInteger(txtStokAwal.getText(), "Stok awal");
        Integer stokMasuk = parseInteger(txtStokMasuk.getText(), "Stok masuk");
        Integer stokRetur = parseInteger(txtStokRetur.getText(), "Stok retur");
        if (tanggalExpired == null || harga == null || stokAwal == null || stokMasuk == null || stokRetur == null) {
            return;
        }

        String sql = "INSERT INTO obat (id_obat, nama_obat, satuan, harga, stok_awal, stok_masuk, stok_retur, stok_akhir, tanggal_expired) "
                + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement stat = conn.prepareStatement(sql)) {
            stat.setString(1, txtIdObat.getText().trim());
            stat.setString(2, txtNamaObat.getText().trim());
            stat.setString(3, txtSatuan.getText().trim());
            stat.setBigDecimal(4, harga);
            stat.setInt(5, stokAwal);
            stat.setInt(6, stokMasuk);
            stat.setInt(7, stokRetur);
            stat.setInt(8, stokAkhir);
            stat.setDate(9, tanggalExpired);
            stat.executeUpdate();

            JOptionPane.showMessageDialog(this, "Data obat berhasil disimpan.");
            clear();
            focusForm();
            dataTable();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Data obat gagal disimpan: " + e.getMessage());
        }
    }//GEN-LAST:event_btnSimpanActionPerformed

    private void btnUbahActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnUbahActionPerformed
        if (conn == null) {
            JOptionPane.showMessageDialog(this, "Koneksi database belum berhasil.");
            return;
        }

        updateStokAkhir();
        if (!isInputValid()) {
            return;
        }

        int stokAkhir = currentStockAkhir();
        if (stokAkhir < 0) {
            JOptionPane.showMessageDialog(this, "Stok akhir tidak boleh minus.");
            return;
        }

        Date tanggalExpired = parseDate(DateChooserHelper.getText(txtTanggalExpired), "Tanggal expired");
        BigDecimal harga = parseBigDecimal(txtHarga.getText(), "Harga");
        Integer stokAwal = parseInteger(txtStokAwal.getText(), "Stok awal");
        Integer stokMasuk = parseInteger(txtStokMasuk.getText(), "Stok masuk");
        Integer stokRetur = parseInteger(txtStokRetur.getText(), "Stok retur");
        if (tanggalExpired == null || harga == null || stokAwal == null || stokMasuk == null || stokRetur == null) {
            return;
        }

        String sql = "UPDATE obat SET nama_obat = ?, satuan = ?, harga = ?, stok_awal = ?, stok_masuk = ?, stok_retur = ?, stok_akhir = ?, tanggal_expired = ? "
                + "WHERE id_obat = ?";
        try (PreparedStatement stat = conn.prepareStatement(sql)) {
            stat.setString(1, txtNamaObat.getText().trim());
            stat.setString(2, txtSatuan.getText().trim());
            stat.setBigDecimal(3, harga);
            stat.setInt(4, stokAwal);
            stat.setInt(5, stokMasuk);
            stat.setInt(6, stokRetur);
            stat.setInt(7, stokAkhir);
            stat.setDate(8, tanggalExpired);
            stat.setString(9, txtIdObat.getText().trim());
            stat.executeUpdate();

            JOptionPane.showMessageDialog(this, "Data obat berhasil diubah.");
            clear();
            focusForm();
            dataTable();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Data obat gagal diubah: " + e.getMessage());
        }
    }//GEN-LAST:event_btnUbahActionPerformed

    private void btnHapusActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnHapusActionPerformed
        if (conn == null) {
            JOptionPane.showMessageDialog(this, "Koneksi database belum berhasil.");
            return;
        }

        if (txtIdObat.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Pilih data obat yang ingin dihapus terlebih dahulu.");
            return;
        }

        int ok = JOptionPane.showConfirmDialog(this, "Hapus data ini?", "Konfirmasi", JOptionPane.YES_NO_OPTION);
        if (ok != JOptionPane.YES_OPTION) {
            return;
        }

        String sql = "DELETE FROM obat WHERE id_obat = ?";
        try (PreparedStatement stat = conn.prepareStatement(sql)) {
            stat.setString(1, txtIdObat.getText().trim());
            stat.executeUpdate();

            JOptionPane.showMessageDialog(this, "Data obat berhasil dihapus.");
            clear();
            focusForm();
            dataTable();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Data obat gagal dihapus: " + e.getMessage());
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

    private void tblObatMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblObatMouseClicked
        int row = tblObat.getSelectedRow();
        if (row < 0) {
            return;
        }

        int modelRow = tblObat.convertRowIndexToModel(row);
        txtIdObat.setText(tableModel.getValueAt(modelRow, 0).toString());
        txtNamaObat.setText(tableModel.getValueAt(modelRow, 1).toString());
        txtSatuan.setText(tableModel.getValueAt(modelRow, 2).toString());
        txtHarga.setText(tableModel.getValueAt(modelRow, 3).toString());
        txtStokAwal.setText(tableModel.getValueAt(modelRow, 4).toString());
        txtStokMasuk.setText(tableModel.getValueAt(modelRow, 5).toString());
        txtStokRetur.setText(tableModel.getValueAt(modelRow, 6).toString());
        txtStokAkhir.setText(tableModel.getValueAt(modelRow, 7).toString());
        DateChooserHelper.setDateText(txtTanggalExpired, tableModel.getValueAt(modelRow, 8).toString());
        btnSimpan.setEnabled(false);
    }//GEN-LAST:event_tblObatMouseClicked

    private void txtHargaKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtHargaKeyTyped
        onlyDecimal(evt);
    }//GEN-LAST:event_txtHargaKeyTyped

    private void txtStokAwalKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtStokAwalKeyTyped
        onlyNumber(evt);
    }//GEN-LAST:event_txtStokAwalKeyTyped

    private void txtStokMasukKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtStokMasukKeyTyped
        onlyNumber(evt);
    }//GEN-LAST:event_txtStokMasukKeyTyped

    private void txtStokReturKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtStokReturKeyTyped
        onlyNumber(evt);
    }//GEN-LAST:event_txtStokReturKeyTyped

    private void txtStokAwalKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtStokAwalKeyReleased
        updateStokAkhir();
    }//GEN-LAST:event_txtStokAwalKeyReleased

    private void txtStokMasukKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtStokMasukKeyReleased
        updateStokAkhir();
    }//GEN-LAST:event_txtStokMasukKeyReleased

    private void txtStokReturKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtStokReturKeyReleased
        updateStokAkhir();
    }//GEN-LAST:event_txtStokReturKeyReleased

    private void txtTanggalExpiredKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtTanggalExpiredKeyTyped
        onlyDateCharacter(evt);
    }//GEN-LAST:event_txtTanggalExpiredKeyTyped

    private void applyHints() {
        txtIdObat.setToolTipText("ID obat dibuat otomatis.");
        txtNamaObat.setToolTipText("Masukkan nama obat.");
        txtSatuan.setToolTipText("Masukkan satuan obat, contoh tablet/botol/strip.");
        txtHarga.setToolTipText("Masukkan harga obat dalam angka.");
        txtStokAwal.setToolTipText("Masukkan stok awal.");
        txtStokMasuk.setToolTipText("Masukkan stok masuk.");
        txtStokRetur.setToolTipText("Masukkan stok retur.");
        txtStokAkhir.setToolTipText("Stok akhir dihitung otomatis.");
        txtTanggalExpired.setToolTipText("Masukkan tanggal expired format yyyy-MM-dd.");
        txtCari.setToolTipText("Ketik kata kunci pencarian obat lalu tekan Enter atau tombol Cari.");
    }

    public static void main(String args[]) {
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(FormObat.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }

        java.awt.EventQueue.invokeLater(() -> new FormObat().setVisible(true));
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnBatal;
    private javax.swing.JButton btnCari;
    private javax.swing.JButton btnHapus;
    private javax.swing.JButton btnKeluar;
    private javax.swing.JButton btnSimpan;
    private javax.swing.JButton btnUbah;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JLabel lblHarga;
    private javax.swing.JLabel lblId;
    private javax.swing.JLabel lblNama;
    private javax.swing.JLabel lblSatuan;
    private javax.swing.JLabel lblStokAkhir;
    private javax.swing.JLabel lblStokAwal;
    private javax.swing.JLabel lblStokMasuk;
    private javax.swing.JLabel lblStokRetur;
    private javax.swing.JLabel lblTanggalExpired;
    private javax.swing.JLabel lblTitle;
    private javax.swing.JPanel panelData;
    private javax.swing.JTable tblObat;
    private javax.swing.JTextField txtCari;
    private javax.swing.JTextField txtHarga;
    private javax.swing.JTextField txtIdObat;
    private javax.swing.JTextField txtNamaObat;
    private javax.swing.JTextField txtSatuan;
    private javax.swing.JTextField txtStokAkhir;
    private javax.swing.JTextField txtStokAwal;
    private javax.swing.JTextField txtStokMasuk;
    private javax.swing.JTextField txtStokRetur;
    private com.toedter.calendar.JDateChooser txtTanggalExpired;
    // End of variables declaration//GEN-END:variables
}
