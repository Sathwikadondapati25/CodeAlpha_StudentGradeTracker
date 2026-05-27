public final class Validator {
    private Validator() {}

    public static String normalizeName(String raw) {
        return raw == null ? "" : raw.trim();
    }

    public static boolean isValidName(String name) {
        if (name == null) return false;
        String n = name.trim();
        if (n.isEmpty()) return false;
        // beginner-friendly: letters + spaces + common separators
        return n.matches("[A-Za-z][A-Za-z .'-]{1,49}");
    }

    public static boolean isValidMarks(int marks) {
        return marks >= 0 && marks <= 100;
    }
}
