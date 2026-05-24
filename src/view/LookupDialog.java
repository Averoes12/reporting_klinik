package view;

import connection.DBConnection;
import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;

public class LookupDialog extends JDialog {

    private final Connection conn = new DBConnection().connect();
    private final String title;
    private final String tableName;
    private final String[] columns;
    private final String[] searchColumns;
    private String selectedId = "";
    private String selectedText = "";
    private JTextField txtCari;
    private JTable tblData;
    private DefaultTableModel tableModel;

    public LookupDialog(java.awt.Frame parent, String title, String tableName, String[] columns, String[] searchColumns) {
        super(parent, title, true);
        this.title = title;
        this.tableName = tableName;
        this.columns = columns;
        this.searchColumns = searchColumns;
        initComponents();
        dataTable();
        setLocationRelativeTo(parent);
    }

    private void initComponents() {
        txtCari = new JTextField(24);
        JButton btnCari = new JButton("Cari");
        JButton btnPilih = new JButton("Pilih");
        JButton btnBatal = new JButton("Batal");
        tblData = new JTable();

        btnCari.addActionListener(evt -> dataTable());
        btnPilih.addActionListener(evt -> chooseSelectedRow());
        btnBatal.addActionListener(evt -> dispose());
        txtCari.addActionListener(evt -> dataTable());
        tblData.setAutoCreateRowSorter(true);
        tblData.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        tblData.setRowHeight(24);
        tblData.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                if (evt.getClickCount() == 2) {
                    chooseSelectedRow();
                }
            }
        });

        JPanel panelCari = new JPanel(new FlowLayout(FlowLayout.LEFT, 6, 6));
        panelCari.add(txtCari);
        panelCari.add(btnCari);

        JPanel panelButton = new JPanel(new FlowLayout(FlowLayout.RIGHT, 6, 6));
        panelButton.add(btnPilih);
        panelButton.add(btnBatal);

        setLayout(new BorderLayout(8, 8));
        add(panelCari, BorderLayout.NORTH);
        add(new JScrollPane(tblData), BorderLayout.CENTER);
        add(panelButton, BorderLayout.SOUTH);
        setSize(760, 420);
    }

    private void dataTable() {
        tableModel = new DefaultTableModel(null, displayHeaders()) {
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        if (conn == null) {
            tblData.setModel(tableModel);
            return;
        }

        StringBuilder sql = new StringBuilder("SELECT ");
        sql.append(String.join(", ", columns)).append(" FROM ").append(tableName);
        if (searchColumns.length > 0) {
            sql.append(" WHERE ");
            for (int i = 0; i < searchColumns.length; i++) {
                if (i > 0) {
                    sql.append(" OR ");
                }
                sql.append(searchColumns[i]).append(" LIKE ?");
            }
        }
        sql.append(" ORDER BY ").append(columns[0]);

        try (PreparedStatement stat = conn.prepareStatement(sql.toString())) {
            String keyword = "%" + txtCari.getText().trim() + "%";
            for (int i = 0; i < searchColumns.length; i++) {
                stat.setString(i + 1, keyword);
            }
            try (ResultSet rs = stat.executeQuery()) {
                ResultSetMetaData meta = rs.getMetaData();
                while (rs.next()) {
                    Object[] row = new Object[meta.getColumnCount()];
                    for (int i = 0; i < row.length; i++) {
                        Object value = rs.getObject(i + 1);
                        row[i] = value == null ? "" : value.toString();
                    }
                    tableModel.addRow(row);
                }
            }
            tblData.setModel(tableModel);
            for (int i = 0; i < tblData.getColumnCount(); i++) {
                tblData.getColumnModel().getColumn(i).setPreferredWidth(i == 0 ? 110 : 180);
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, title + " gagal dipanggil: " + e.getMessage());
        }
    }

    private void chooseSelectedRow() {
        int row = tblData.getSelectedRow();
        if (row < 0) {
            JOptionPane.showMessageDialog(this, "Pilih data terlebih dahulu.");
            return;
        }
        int modelRow = tblData.convertRowIndexToModel(row);
        selectedId = tableModel.getValueAt(modelRow, 0).toString();
        StringBuilder text = new StringBuilder(selectedId);
        for (int i = 1; i < tableModel.getColumnCount(); i++) {
            text.append(" - ").append(tableModel.getValueAt(modelRow, i));
        }
        selectedText = text.toString();
        dispose();
    }

    public String getSelectedId() {
        return selectedId;
    }

    public String getSelectedText() {
        return selectedText;
    }

    public boolean isSelected() {
        return !selectedId.isEmpty();
    }

    private String[] displayHeaders() {
        String[] headers = new String[columns.length];
        for (int i = 0; i < columns.length; i++) {
            headers[i] = toHeaderLabel(columns[i]);
        }
        return headers;
    }

    private String toHeaderLabel(String column) {
        String[] words = column.split("_");
        StringBuilder label = new StringBuilder();
        for (String word : words) {
            if (word.isEmpty()) {
                continue;
            }
            if (label.length() > 0) {
                label.append(' ');
            }
            if ("id".equalsIgnoreCase(word)) {
                label.append("ID");
            } else if ("hp".equalsIgnoreCase(word)) {
                label.append("HP");
            } else if ("nik".equalsIgnoreCase(word)) {
                label.append("NIK");
            } else {
                label.append(Character.toUpperCase(word.charAt(0))).append(word.substring(1));
            }
        }
        return label.toString();
    }
}
