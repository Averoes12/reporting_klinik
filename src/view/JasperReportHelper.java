package view;

import java.io.File;
import java.io.InputStream;
import java.lang.reflect.Method;
import java.sql.Connection;
import java.util.Map;
import javax.swing.JOptionPane;

public final class JasperReportHelper {

    private JasperReportHelper() {
    }

    public static void previewReport(java.awt.Component parent, String reportPath, Map<String, Object> parameters, Connection connection) {
        if (connection == null) {
            JOptionPane.showMessageDialog(parent, "Koneksi database belum tersedia.");
            return;
        }

        try (InputStream reportStream = openReport(reportPath)) {
            if (reportStream == null) {
                JOptionPane.showMessageDialog(parent, "Template report tidak ditemukan: " + reportPath);
                return;
            }

            Class<?> compileManager = Class.forName("net.sf.jasperreports.engine.JasperCompileManager");
            Class<?> fillManager = Class.forName("net.sf.jasperreports.engine.JasperFillManager");
            Class<?> loader = Class.forName("net.sf.jasperreports.engine.util.JRLoader");
            Class<?> jasperReportClass = Class.forName("net.sf.jasperreports.engine.JasperReport");
            Class<?> jasperPrintClass = Class.forName("net.sf.jasperreports.engine.JasperPrint");
            Class<?> viewer = Class.forName("net.sf.jasperreports.view.JasperViewer");

            Object jasperReport;
            if (reportPath.endsWith(".jasper")) {
                Method loadObject = loader.getMethod("loadObject", InputStream.class);
                jasperReport = loadObject.invoke(null, reportStream);
            } else {
                Method compileReport = compileManager.getMethod("compileReport", InputStream.class);
                jasperReport = compileReport.invoke(null, reportStream);
            }

            Method fillReport = fillManager.getMethod("fillReport", jasperReportClass, Map.class, Connection.class);
            Object jasperPrint = fillReport.invoke(null, jasperReport, parameters, connection);

            Method viewReport = viewer.getMethod("viewReport", jasperPrintClass, boolean.class);
            viewReport.invoke(null, jasperPrint, false);
        } catch (ClassNotFoundException | NoClassDefFoundError e) {
            JOptionPane.showMessageDialog(parent,
                    "Library JasperReports belum ada di classpath.\n"
                    + "Tambahkan jasperreports beserta dependency-nya dari Jaspersoft Studio/iReport ke folder lib atau Library NetBeans.");
        } catch (Exception e) {
            JOptionPane.showMessageDialog(parent, "Preview Jasper gagal: " + rootMessage(e));
        }
    }

    private static InputStream openReport(String reportPath) {
        InputStream classpathReport = JasperReportHelper.class.getResourceAsStream(reportPath);
        if (classpathReport != null) {
            return classpathReport;
        }

        File sourceReport = new File("src" + reportPath);
        if (sourceReport.isFile()) {
            try {
                return sourceReport.toURI().toURL().openStream();
            } catch (Exception ignored) {
                return null;
            }
        }
        return null;
    }

    private static String rootMessage(Exception e) {
        Throwable cause = e;
        while (cause.getCause() != null) {
            cause = cause.getCause();
        }
        String message = cause.getMessage();
        return message == null || message.isBlank() ? cause.toString() : message;
    }
}
