package view;

import com.toedter.calendar.JDateChooser;
import java.beans.PropertyChangeListener;
import java.sql.Date;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.format.ResolverStyle;

final class DateChooserHelper {

    static final String DISPLAY_FORMAT = "yyyy-MM-dd";

    private static final DateTimeFormatter FORMATTER = DateTimeFormatter
            .ofPattern("uuuu-MM-dd")
            .withResolverStyle(ResolverStyle.STRICT);
    private static final ZoneId ZONE = ZoneId.systemDefault();

    private DateChooserHelper() {
    }

    static JDateChooser createDateChooser() {
        JDateChooser chooser = new JDateChooser();
        chooser.setDateFormatString(DISPLAY_FORMAT);
        return chooser;
    }

    static void preventBackdate(JDateChooser chooser) {
        chooser.setMinSelectableDate(java.util.Date.from(LocalDate.now().atStartOfDay(ZONE).toInstant()));
    }

    static void setDate(JDateChooser chooser, LocalDate date) {
        chooser.setDate(java.util.Date.from(date.atStartOfDay(ZONE).toInstant()));
    }

    static void setDate(JDateChooser chooser, Date date) {
        chooser.setDate(date == null ? null : new java.util.Date(date.getTime()));
    }

    static void setDateText(JDateChooser chooser, Object value) {
        if (value == null || value.toString().trim().isEmpty()) {
            chooser.setDate(null);
            return;
        }

        try {
            setDate(chooser, LocalDate.parse(value.toString().trim(), FORMATTER));
        } catch (DateTimeParseException ex) {
            chooser.setDate(null);
        }
    }

    static void clear(JDateChooser chooser) {
        chooser.setDate(null);
    }

    static String getText(JDateChooser chooser) {
        LocalDate date = getLocalDate(chooser);
        if (date == null) {
            return "";
        }
        return date.format(DateTimeFormatter.ISO_LOCAL_DATE);
    }

    static LocalDate getLocalDate(JDateChooser chooser) {
        java.util.Date date = chooser.getDate();
        return date == null ? null : date.toInstant().atZone(ZONE).toLocalDate();
    }

    static boolean isBackdate(JDateChooser chooser) {
        LocalDate date = getLocalDate(chooser);
        return date != null && date.isBefore(LocalDate.now());
    }

    static Date getSqlDate(JDateChooser chooser) {
        java.util.Date date = chooser.getDate();
        return date == null ? null : new Date(date.getTime());
    }

    static void addDateChangeListener(JDateChooser chooser, PropertyChangeListener listener) {
        chooser.addPropertyChangeListener("date", listener);
    }
}
