package app;

public final class Session {

    private static String username;
    private static String role;

    private Session() {
    }

    public static void login(String usernameValue, String roleValue) {
        username = usernameValue;
        role = roleValue;
    }

    public static void logout() {
        username = null;
        role = null;
    }

    public static String getUsername() {
        return username == null ? "" : username;
    }

    public static String getRole() {
        return role == null ? "" : role;
    }

    public static boolean isAdmin() {
        return "admin".equalsIgnoreCase(getRole());
    }

    public static boolean isPetugas() {
        return "petugas".equalsIgnoreCase(getRole());
    }

    public static boolean isDokter() {
        return "dokter".equalsIgnoreCase(getRole());
    }

    public static boolean isKasir() {
        return "kasir".equalsIgnoreCase(getRole());
    }
}
