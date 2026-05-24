package view;

import connection.DBConnection;
import java.awt.event.KeyEvent;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

public class FormUser extends JFrame {

    private final Connection conn = new DBConnection().connect();
    private DefaultTableModel tableModel;

    public FormUser() {
        initComponents();        clear();
        dataTable();
        setLocationRelativeTo(null);
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {
        lblTitle = new javax.swing.JLabel();
        lblIdUser = new javax.swing.JLabel();
        lblUsername = new javax.swing.JLabel();
        lblPassword = new javax.swing.JLabel();
        lblRole = new javax.swing.JLabel();
        lblStatus = new javax.swing.JLabel();
        txtIdUser = new javax.swing.JTextField();
        txtUsername = new javax.swing.JTextField();
        txtPassword = new javax.swing.JTextField();
        cmbRole = new javax.swing.JComboBox<>();
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
        tblUser = new javax.swing.JTable();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Form User");

        lblTitle.setFont(new java.awt.Font("Segoe UI", 1, 18));
        lblTitle.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblTitle.setText("Data User");

        lblIdUser.setText("ID User");
        lblUsername.setText("Username");
        lblPassword.setText("Password");
        lblRole.setText("Role");
        lblStatus.setText("Status");
        txtIdUser.setEditable(false);
        cmbRole.setModel(new DefaultComboBoxModel<>(new String[]{"admin", "petugas", "dokter", "kasir"}));
        cmbStatus.setModel(new DefaultComboBoxModel<>(new String[]{"aktif", "nonaktif"}));

        btnSimpan.setText("Simpan");
        btnUbah.setText("Ubah");
        btnHapus.setText("Hapus");
        btnBatal.setText("Batal");
        btnKeluar.setText("Keluar");
        btnCari.setText("Cari");

        btnSimpan.addActionListener(evt -> saveData());
        btnUbah.addActionListener(evt -> updateData());
        btnHapus.addActionListener(evt -> deleteData());
        btnBatal.addActionListener(evt -> {
            clear();
            dataTable();
        });
        btnKeluar.addActionListener(evt -> dispose());
        btnCari.addActionListener(evt -> dataTable());
        txtCari.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
                    dataTable();
                }
            }
        });
        tblUser.setAutoCreateRowSorter(true);
        tblUser.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_OFF);
        tblUser.setRowHeight(24);
        tblUser.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tableMouseClicked();
            }
        });

        jScrollPaneTable.setViewportView(tblUser);

        panelData.setBorder(javax.swing.BorderFactory.createTitledBorder("Data User"));

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
                            .addComponent(lblIdUser)
                            .addComponent(lblUsername)
                            .addComponent(lblPassword)
                            .addComponent(lblRole)
                            .addComponent(lblStatus))
                        .addGap(29, 29, 29)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(txtIdUser)
                            .addComponent(txtUsername)
                            .addComponent(txtPassword)
                            .addComponent(cmbRole, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(cmbStatus, 0, 260, Short.MAX_VALUE)))
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
                    .addComponent(lblIdUser)
                    .addComponent(txtIdUser, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblUsername)
                    .addComponent(txtUsername, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblPassword)
                    .addComponent(txtPassword, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblRole)
                    .addComponent(cmbRole, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
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

    private void clear() {
        txtIdUser.setText("");
        txtUsername.setText("");
        txtPassword.setText("");
        txtCari.setText("");
        cmbRole.setSelectedIndex(0);
        cmbStatus.setSelectedIndex(0);
        btnSimpan.setEnabled(true);
        txtUsername.requestFocus();
    }

    private boolean isInputValid() {
        if (txtUsername.getText().trim().isEmpty() || txtPassword.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Username dan password wajib diisi.");
            return false;
        }
        return true;
    }

    private void dataTable() {
        Object[] columns = {"ID User", "Username", "Password", "Role", "Status"};
        tableModel = new DefaultTableModel(null, columns) {
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        if (conn == null) {
            tblUser.setModel(tableModel);
            return;
        }

        String sql = "SELECT id_user, username, password, role, status FROM users "
                + "WHERE username LIKE ? OR role LIKE ? OR status LIKE ? ORDER BY id_user";
        try (PreparedStatement stat = conn.prepareStatement(sql)) {
            String keyword = "%" + txtCari.getText().trim() + "%";
            stat.setString(1, keyword);
            stat.setString(2, keyword);
            stat.setString(3, keyword);
            try (ResultSet rs = stat.executeQuery()) {
                while (rs.next()) {
                    tableModel.addRow(new Object[]{
                        rs.getString("id_user"),
                        rs.getString("username"),
                        rs.getString("password"),
                        rs.getString("role"),
                        rs.getString("status")
                    });
                }
            }
            tblUser.setModel(tableModel);
            int[] widths = {80, 160, 160, 110, 110};
            for (int i = 0; i < widths.length; i++) {
                tblUser.getColumnModel().getColumn(i).setPreferredWidth(widths[i]);
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Data user gagal dipanggil: " + e.getMessage());
        }
    }

    private void saveData() {
        if (conn == null || !isInputValid()) {
            return;
        }
        String sql = "INSERT INTO users (username, password, role, status) VALUES (?, ?, ?, ?)";
        try (PreparedStatement stat = conn.prepareStatement(sql)) {
            bind(stat);
            stat.executeUpdate();
            JOptionPane.showMessageDialog(this, "Data user berhasil disimpan.");
            clear();
            dataTable();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Data user gagal disimpan: " + e.getMessage());
        }
    }

    private void updateData() {
        if (conn == null || !isInputValid()) {
            return;
        }
        if (txtIdUser.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Pilih user yang ingin diubah.");
            return;
        }
        String sql = "UPDATE users SET username = ?, password = ?, role = ?, status = ? WHERE id_user = ?";
        try (PreparedStatement stat = conn.prepareStatement(sql)) {
            bind(stat);
            stat.setInt(5, Integer.parseInt(txtIdUser.getText().trim()));
            stat.executeUpdate();
            JOptionPane.showMessageDialog(this, "Data user berhasil diubah.");
            clear();
            dataTable();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Data user gagal diubah: " + e.getMessage());
        }
    }

    private void deleteData() {
        if (conn == null || txtIdUser.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Pilih user yang ingin dihapus.");
            return;
        }
        int ok = JOptionPane.showConfirmDialog(this, "Hapus user ini?", "Konfirmasi", JOptionPane.YES_NO_OPTION);
        if (ok != JOptionPane.YES_OPTION) {
            return;
        }
        String sql = "DELETE FROM users WHERE id_user = ?";
        try (PreparedStatement stat = conn.prepareStatement(sql)) {
            stat.setInt(1, Integer.parseInt(txtIdUser.getText().trim()));
            stat.executeUpdate();
            JOptionPane.showMessageDialog(this, "Data user berhasil dihapus.");
            clear();
            dataTable();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Data user gagal dihapus: " + e.getMessage());
        }
    }

    private void bind(PreparedStatement stat) throws SQLException {
        stat.setString(1, txtUsername.getText().trim());
        stat.setString(2, txtPassword.getText().trim());
        stat.setString(3, cmbRole.getSelectedItem().toString());
        stat.setString(4, cmbStatus.getSelectedItem().toString());
    }

    private void tableMouseClicked() {
        int row = tblUser.getSelectedRow();
        if (row < 0) {
            return;
        }
        int modelRow = tblUser.convertRowIndexToModel(row);
        txtIdUser.setText(tableModel.getValueAt(modelRow, 0).toString());
        txtUsername.setText(tableModel.getValueAt(modelRow, 1).toString());
        txtPassword.setText(tableModel.getValueAt(modelRow, 2).toString());
        cmbRole.setSelectedItem(tableModel.getValueAt(modelRow, 3).toString());
        cmbStatus.setSelectedItem(tableModel.getValueAt(modelRow, 4).toString());
        btnSimpan.setEnabled(false);
    }

    private void applyHints() {
        txtIdUser.setToolTipText("ID user dibuat otomatis oleh database.");
        txtUsername.setToolTipText("Masukkan username akun.");
        txtPassword.setToolTipText("Masukkan password akun.");
        cmbRole.setToolTipText("Pilih role user.");
        cmbStatus.setToolTipText("Pilih status user.");
        txtCari.setToolTipText("Ketik kata kunci pencarian user lalu tekan Enter atau tombol Cari.");
    }

    public static void main(String args[]) {
        java.awt.EventQueue.invokeLater(() -> new FormUser().setVisible(true));
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnBatal;
    private javax.swing.JButton btnCari;
    private javax.swing.JButton btnHapus;
    private javax.swing.JButton btnKeluar;
    private javax.swing.JButton btnSimpan;
    private javax.swing.JButton btnUbah;
    private javax.swing.JComboBox<String> cmbRole;
    private javax.swing.JComboBox<String> cmbStatus;
    private javax.swing.JScrollPane jScrollPaneTable;
    private javax.swing.JLabel lblIdUser;
    private javax.swing.JLabel lblPassword;
    private javax.swing.JLabel lblRole;
    private javax.swing.JLabel lblStatus;
    private javax.swing.JLabel lblTitle;
    private javax.swing.JLabel lblUsername;
    private javax.swing.JPanel panelData;
    private javax.swing.JTable tblUser;
    private javax.swing.JTextField txtCari;
    private javax.swing.JTextField txtIdUser;
    private javax.swing.JTextField txtPassword;
    private javax.swing.JTextField txtUsername;
    // End of variables declaration//GEN-END:variables
}
