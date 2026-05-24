package view;

import connection.DBConnection;
import java.awt.event.KeyEvent;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import javax.swing.JOptionPane;
import javax.swing.JSpinner;
import javax.swing.SpinnerDateModel;
import javax.swing.table.DefaultTableModel;

public class FormJadwalDokter extends javax.swing.JFrame {

    private static final String ID_JADWAL_PREFIX = "JDW";
    private static final int ID_JADWAL_DIGITS = 3;
    private static final String[] HARI_OPTIONS = {"Senin", "Selasa", "Rabu", "Kamis", "Jumat", "Sabtu", "Minggu"};
    private static final String TIME_FORMAT = "HH:mm";

    private final Connection conn = new DBConnection().connect();
    private final SimpleDateFormat timeFormatter = new SimpleDateFormat(TIME_FORMAT);
    private DefaultTableModel tableModel;

    public FormJadwalDokter() {
        initComponents();
        applyHints();
        clear();
        focusForm();
        dataTable();
        setLocationRelativeTo(null);
    }

    private void focusForm() {
        txtIdDokter.requestFocus();
    }

    private void clear() {
        generateIdJadwal();
        txtIdDokter.setText("");
        txtIdPoli.setText("");
        cmbHari.setSelectedIndex(0);
        setTimeValue(spnJamMulai, "08:00");
        setTimeValue(spnJamSelesai, "12:00");
        txtKuota.setText("");
        txtCari.setText("");
        btnSimpan.setEnabled(true);
    }

    private void generateIdJadwal() {
        txtId.setText(nextIdJadwal());
    }

    private String nextIdJadwal() {
        if (conn == null) {
            return formatIdJadwal(1);
        }

        String sql = "SELECT COALESCE(MAX(CASE "
                + "WHEN id_jadwal REGEXP ? THEN CAST(SUBSTRING(id_jadwal, ?) AS UNSIGNED) "
                + "WHEN id_jadwal REGEXP ? THEN CAST(id_jadwal AS UNSIGNED) "
                + "ELSE 0 END), 0) + 1 AS next_number FROM jadwal_dokter";
        try (PreparedStatement stat = conn.prepareStatement(sql)) {
            stat.setString(1, "^" + ID_JADWAL_PREFIX + "[0-9]+$");
            stat.setInt(2, ID_JADWAL_PREFIX.length() + 1);
            stat.setString(3, "^[0-9]+$");

            try (ResultSet hasil = stat.executeQuery()) {
                if (hasil.next()) {
                    return formatIdJadwal(hasil.getInt("next_number"));
                }
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "ID otomatis gagal dibuat: " + e.getMessage());
        }
        return formatIdJadwal(1);
    }

    private String formatIdJadwal(int number) {
        return ID_JADWAL_PREFIX + String.format("%0" + ID_JADWAL_DIGITS + "d", number);
    }

    private boolean isInputValid() {
        if (txtId.getText().trim().isEmpty()
                || txtIdDokter.getText().trim().isEmpty()
                || txtIdPoli.getText().trim().isEmpty()
                || cmbHari.getSelectedItem() == null
                || txtKuota.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Data jadwal dokter masih ada yang kosong.");
            return false;
        }
        if (!commitTimeInput(spnJamMulai, "Jam Mulai") || !commitTimeInput(spnJamSelesai, "Jam Selesai")) {
            return false;
        }
        if (parseInteger(txtKuota.getText(), "Kuota") == null) {
            return false;
        }
        return true;
    }

    private Integer parseInteger(String value, String fieldName) {
        try {
            return Integer.parseInt(value.trim());
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, fieldName + " harus berupa angka bulat.");
            return null;
        }
    }

    private String valueOrEmpty(String value) {
        return value == null ? "" : value;
    }

    private boolean commitTimeInput(JSpinner spinner, String fieldName) {
        try {
            spinner.commitEdit();
            return true;
        } catch (ParseException e) {
            JOptionPane.showMessageDialog(this, fieldName + " harus berformat " + TIME_FORMAT + ".");
            return false;
        }
    }

    private String selectedHari() {
        Object selected = cmbHari.getSelectedItem();
        return selected == null ? "" : selected.toString();
    }

    private String selectedTime(JSpinner spinner) {
        Object value = spinner.getValue();
        if (value instanceof Date) {
            return timeFormatter.format((Date) value);
        }
        return "";
    }

    private void setSelectedHari(String value) {
        if (value == null || value.trim().isEmpty()) {
            cmbHari.setSelectedIndex(0);
            return;
        }

        String trimmedValue = value.trim();
        for (String hari : HARI_OPTIONS) {
            if (hari.equalsIgnoreCase(trimmedValue)) {
                cmbHari.setSelectedItem(hari);
                return;
            }
        }
        cmbHari.setSelectedIndex(0);
    }

    private void setTimeValue(JSpinner spinner, String value) {
        try {
            spinner.setValue(timeFormatter.parse(value));
        } catch (ParseException e) {
            spinner.setValue(spinner == spnJamSelesai ? defaultTime(12, 0) : defaultTime(8, 0));
        }
    }

    private Date defaultTime(int hour, int minute) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, hour);
        calendar.set(Calendar.MINUTE, minute);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        return calendar.getTime();
    }

    private void dataTable() {
        Object[] columns = {"ID Jadwal", "ID Dokter", "Nama Dokter", "ID Poli", "Nama Poli", "Hari", "Jam Mulai", "Jam Selesai", "Kuota"};
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

        String sql = "SELECT j.id_jadwal, j.id_dokter, d.nama_dokter, j.id_poli, p.nama_poli, j.hari, j.jam_mulai, j.jam_selesai, j.kuota "
                + "FROM jadwal_dokter j "
                + "JOIN dokter d ON j.id_dokter = d.id_dokter "
                + "JOIN poli p ON j.id_poli = p.id_poli "
                + "WHERE j.id_jadwal LIKE ? OR d.nama_dokter LIKE ? OR p.nama_poli LIKE ? OR j.hari LIKE ? "
                + "ORDER BY CASE "
                + "WHEN j.id_jadwal REGEXP '^" + ID_JADWAL_PREFIX + "[0-9]+$' THEN CAST(SUBSTRING(j.id_jadwal, " + (ID_JADWAL_PREFIX.length() + 1) + ") AS UNSIGNED) "
                + "WHEN j.id_jadwal REGEXP '^[0-9]+$' THEN CAST(j.id_jadwal AS UNSIGNED) "
                + "ELSE 0 END ASC";
        try (PreparedStatement stat = conn.prepareStatement(sql)) {
            String keyword = "%" + txtCari.getText().trim() + "%";
            stat.setString(1, keyword);
            stat.setString(2, keyword);
            stat.setString(3, keyword);
            stat.setString(4, keyword);

            try (ResultSet hasil = stat.executeQuery()) {
                while (hasil.next()) {
                    tableModel.addRow(new Object[]{
                        valueOrEmpty(hasil.getString("id_jadwal")),
                        valueOrEmpty(hasil.getString("id_dokter")),
                        valueOrEmpty(hasil.getString("nama_dokter")),
                        valueOrEmpty(hasil.getString("id_poli")),
                        valueOrEmpty(hasil.getString("nama_poli")),
                        valueOrEmpty(hasil.getString("hari")),
                        valueOrEmpty(hasil.getString("jam_mulai")),
                        valueOrEmpty(hasil.getString("jam_selesai")),
                        valueOrEmpty(hasil.getString("kuota"))
                    });
                }
            }

            tblData.setModel(tableModel);
            setTableColumnWidth();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Data jadwal dokter gagal dipanggil: " + e.getMessage());
        }
    }

    private void setTableColumnWidth() {
        int[] widths = {95, 95, 160, 95, 150, 100, 100, 100, 80};
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
        lblIdDokter = new javax.swing.JLabel();
        txtIdDokter = new javax.swing.JTextField();
        btnPilihIdDokter = new javax.swing.JButton();
        lblIdPoli = new javax.swing.JLabel();
        txtIdPoli = new javax.swing.JTextField();
        btnPilihIdPoli = new javax.swing.JButton();
        lblHari = new javax.swing.JLabel();
        cmbHari = new javax.swing.JComboBox<>();
        lblJamMulai = new javax.swing.JLabel();
        spnJamMulai = new javax.swing.JSpinner();
        lblJamSelesai = new javax.swing.JLabel();
        spnJamSelesai = new javax.swing.JSpinner();
        lblKuota = new javax.swing.JLabel();
        txtKuota = new javax.swing.JTextField();
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
        setTitle("Form Jadwal Dokter");

        lblTitle.setFont(new java.awt.Font("Segoe UI", 1, 18));
        lblTitle.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblTitle.setText("Data Jadwal Dokter");
        lblId.setText("Id Jadwal");
        txtId.setEditable(false);
        lblIdDokter.setText("Dokter");
        lblIdPoli.setText("Poli");
        txtIdDokter.setEditable(false);
        txtIdPoli.setEditable(false);
        btnPilihIdDokter.setText("Pilih");
        btnPilihIdPoli.setText("Pilih");
        btnPilihIdDokter.addActionListener(evt -> pilihDokter());
        btnPilihIdPoli.addActionListener(evt -> pilihPoli());
        lblHari.setText("Hari");
        cmbHari.setModel(new javax.swing.DefaultComboBoxModel<>(HARI_OPTIONS));
        lblJamMulai.setText("Jam Mulai");
        spnJamMulai.setModel(new SpinnerDateModel(defaultTime(8, 0), null, null, Calendar.MINUTE));
        spnJamMulai.setEditor(new JSpinner.DateEditor(spnJamMulai, TIME_FORMAT));
        lblJamSelesai.setText("Jam Selesai");
        spnJamSelesai.setModel(new SpinnerDateModel(defaultTime(12, 0), null, null, Calendar.MINUTE));
        spnJamSelesai.setEditor(new JSpinner.DateEditor(spnJamSelesai, TIME_FORMAT));
        lblKuota.setText("Kuota");
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

        panelData.setBorder(javax.swing.BorderFactory.createTitledBorder("Data Jadwal Dokter"));

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
                            .addComponent(lblIdDokter)
                            .addComponent(lblIdPoli)
                            .addComponent(lblHari)
                            .addComponent(lblJamMulai)
                            .addComponent(lblJamSelesai)
                            .addComponent(lblKuota))
                        .addGap(29, 29, 29)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(txtId)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(txtIdDokter, javax.swing.GroupLayout.PREFERRED_SIZE, 180, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(btnPilihIdDokter))
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(txtIdPoli, javax.swing.GroupLayout.PREFERRED_SIZE, 180, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(btnPilihIdPoli))
                            .addComponent(cmbHari, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(spnJamMulai)
                            .addComponent(spnJamSelesai)
                            .addComponent(txtKuota, javax.swing.GroupLayout.DEFAULT_SIZE, 260, Short.MAX_VALUE)))
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
                    .addComponent(lblIdDokter)
                    .addComponent(txtIdDokter, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnPilihIdDokter))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblIdPoli)
                    .addComponent(txtIdPoli, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnPilihIdPoli))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblHari)
                    .addComponent(cmbHari, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblJamMulai)
                    .addComponent(spnJamMulai, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblJamSelesai)
                    .addComponent(spnJamSelesai, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblKuota)
                    .addComponent(txtKuota, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
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

    private void applyHints() {
        txtId.setToolTipText("ID jadwal dibuat otomatis.");
        txtIdDokter.setToolTipText("Klik tombol Pilih untuk memilih dokter.");
        txtIdPoli.setToolTipText("Klik tombol Pilih untuk memilih poli.");
        cmbHari.setToolTipText("Pilih hari praktik.");
        spnJamMulai.setToolTipText("Pilih jam mulai praktik.");
        spnJamSelesai.setToolTipText("Pilih jam selesai praktik.");
        txtKuota.setToolTipText("Masukkan jumlah kuota pasien.");
        txtCari.setToolTipText("Ketik kata kunci pencarian jadwal lalu tekan Enter atau tombol Cari.");
        btnPilihIdDokter.setToolTipText("Buka popup pencarian dokter.");
        btnPilihIdPoli.setToolTipText("Buka popup pencarian poli.");
    }

    public static void main(String args[]) {
        java.awt.EventQueue.invokeLater(() -> new FormJadwalDokter().setVisible(true));
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

        String sql = "INSERT INTO jadwal_dokter (id_jadwal, id_dokter, id_poli, hari, jam_mulai, jam_selesai, kuota) VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement stat = conn.prepareStatement(sql)) {
            bindInput(stat);
            stat.executeUpdate();

            JOptionPane.showMessageDialog(this, "Data jadwal dokter berhasil disimpan.");
            clear();
            focusForm();
            dataTable();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Data jadwal dokter gagal disimpan: " + e.getMessage());
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

        String sql = "UPDATE jadwal_dokter SET id_dokter = ?, id_poli = ?, hari = ?, jam_mulai = ?, jam_selesai = ?, kuota = ? WHERE id_jadwal = ?";
        try (PreparedStatement stat = conn.prepareStatement(sql)) {
            stat.setString(1, txtIdDokter.getText().trim());
            stat.setString(2, txtIdPoli.getText().trim());
            stat.setString(3, selectedHari());
            stat.setString(4, selectedTime(spnJamMulai));
            stat.setString(5, selectedTime(spnJamSelesai));
            stat.setInt(6, parseInteger(txtKuota.getText(), "Kuota"));
            stat.setString(7, txtId.getText().trim());
            stat.executeUpdate();

            JOptionPane.showMessageDialog(this, "Data jadwal dokter berhasil diubah.");
            clear();
            focusForm();
            dataTable();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Data jadwal dokter gagal diubah: " + e.getMessage());
        }
    }

    private void bindInput(PreparedStatement stat) throws SQLException {
        stat.setString(1, txtId.getText().trim());
        stat.setString(2, txtIdDokter.getText().trim());
        stat.setString(3, txtIdPoli.getText().trim());
        stat.setString(4, selectedHari());
        stat.setString(5, selectedTime(spnJamMulai));
        stat.setString(6, selectedTime(spnJamSelesai));
        stat.setInt(7, parseInteger(txtKuota.getText(), "Kuota"));
    }

    private void hapusData() {
        if (conn == null) {
            JOptionPane.showMessageDialog(this, "Koneksi database belum berhasil.");
            return;
        }
        if (txtId.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Pilih data jadwal dokter yang ingin dihapus terlebih dahulu.");
            return;
        }

        int ok = JOptionPane.showConfirmDialog(this, "Hapus data ini?", "Konfirmasi", JOptionPane.YES_NO_OPTION);
        if (ok != JOptionPane.YES_OPTION) {
            return;
        }

        String sql = "DELETE FROM jadwal_dokter WHERE id_jadwal = ?";
        try (PreparedStatement stat = conn.prepareStatement(sql)) {
            stat.setString(1, txtId.getText().trim());
            stat.executeUpdate();

            JOptionPane.showMessageDialog(this, "Data jadwal dokter berhasil dihapus.");
            clear();
            focusForm();
            dataTable();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Data jadwal dokter gagal dihapus: " + e.getMessage());
        }
    }

    private void tableMouseClicked() {
        int row = tblData.getSelectedRow();
        if (row < 0 || tableModel == null) {
            return;
        }

        int modelRow = tblData.convertRowIndexToModel(row);
        txtId.setText(tableModel.getValueAt(modelRow, 0).toString());
        txtIdDokter.setText(tableModel.getValueAt(modelRow, 1).toString());
        txtIdPoli.setText(tableModel.getValueAt(modelRow, 3).toString());
        setSelectedHari(tableModel.getValueAt(modelRow, 5).toString());
        setTimeValue(spnJamMulai, tableModel.getValueAt(modelRow, 6).toString());
        setTimeValue(spnJamSelesai, tableModel.getValueAt(modelRow, 7).toString());
        txtKuota.setText(tableModel.getValueAt(modelRow, 8).toString());
        btnSimpan.setEnabled(false);
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnBatal;
    private javax.swing.JButton btnCari;
    private javax.swing.JButton btnHapus;
    private javax.swing.JButton btnKeluar;
    private javax.swing.JButton btnPilihIdDokter;
    private javax.swing.JButton btnPilihIdPoli;
    private javax.swing.JButton btnSimpan;
    private javax.swing.JButton btnUbah;
    private javax.swing.JComboBox<String> cmbHari;
    private javax.swing.JLabel lblHari;
    private javax.swing.JLabel lblId;
    private javax.swing.JLabel lblIdDokter;
    private javax.swing.JLabel lblIdPoli;
    private javax.swing.JLabel lblJamMulai;
    private javax.swing.JLabel lblJamSelesai;
    private javax.swing.JLabel lblKuota;
    private javax.swing.JLabel lblTitle;
    private javax.swing.JPanel panelData;
    private javax.swing.JScrollPane jScrollPaneTable;
    private javax.swing.JTable tblData;
    private javax.swing.JTextField txtCari;
    private javax.swing.JTextField txtId;
    private javax.swing.JTextField txtIdDokter;
    private javax.swing.JTextField txtIdPoli;
    private javax.swing.JTextField txtKuota;
    private javax.swing.JSpinner spnJamMulai;
    private javax.swing.JSpinner spnJamSelesai;
    // End of variables declaration//GEN-END:variables
}
