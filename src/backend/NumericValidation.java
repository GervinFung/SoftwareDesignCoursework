package backend;

public final class NumericValidation {

    private NumericValidation() { throw new IllegalStateException("Cannot instantiate AlphaNumericValidation"); }

    public static boolean tryParseIntInRange(final String data, final int min, final int max) {
        try {
            final int temp = Integer.parseInt(data);
            if (temp >= min && temp <= max) {
                return true;
            }
        } catch (final NumberFormatException ignored) {}
        return false;
    }

    public static boolean tryParseDoubleInRange(final String data, final double min, final double max) {
        try {
            final double temp = Double.parseDouble(data);
            if (temp >= min && temp <= max) {
                return true;
            }
        } catch (final NumberFormatException ignored) {}
        return false;
    }
}