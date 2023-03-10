package ebrt;

import ebrt.math.Utils;

import java.util.Arrays;

public record Color(double r, double g, double b) {

    public static final Color RED = new Color(1.0, 0.0, 0.0);
    public static final Color BLACK = new Color(0.0, 0.0, 0.0);
    public static final Color WHITE = new Color(1.0, 1.0, 1.0);
    public static final Color GREEN = new Color(0.0, 1.0, 0.0);
    public static final Color BLUE = new Color(0.0, 0.0, 1.0);
    public static final Color SKY_BLUE = new Color(0.5, 0.7, 1.0);
    public static final Color MID_GRAY = new Color(0.5, 0.5, 0.5);
    public static final Color DARK_GREEN = new Color(0.0, 0.4, 0.0);

    // =================================================================================
    // arithmetric ops
    // =================================================================================

    public Color plus(Color other) {
        return new Color(r + other.r, g + other.g, b + other.b);
    }

    public Color plus(double f) {
        return new Color(r + f, g + f, b + f);
    }

    public Color minus(Color other) {
        return new Color(r - other.r, g - other.g, b - other.b);
    }

    public Color minus(double f) {
        return new Color(r - f, g - f, b - f);
    }

    public Color mul(Color other) {
        return new Color(r * other.r, g * other.g, b * other.b);
    }

    public Color mul(double f) {
        return new Color(r * f, g * f, b * f);
    }

    public Color div(Color other) {
        return new Color(r / other.r, g / other.g, b / other.b);
    }

    public Color div(double f) {
        return mul(1.0 / f);
    }

    public Color square() {
        return mul(this);
    }

    public Color sqrt() {
        return new Color(Math.sqrt(r), Math.sqrt(g), Math.sqrt(b));
    }

    public Color normalize(double sampleCount) {
        return new Color(Math.sqrt(r / sampleCount), Math.sqrt(g / sampleCount), Math.sqrt(b / sampleCount));
    }

    // =================================================================================
    // testing and conversion
    // =================================================================================

    public int toRgb() {
        int r = Utils.scaleInt(clean(r()), 255);
        int g = Utils.scaleInt(clean(g()), 255);
        int b = Utils.scaleInt(clean(b()), 255);
        r = (r << 16) & 0x00FF0000;
        g = (g << 8) & 0x0000FF00;
        b = b & 0x000000FF;
        return 0xFF000000 | r | g | b;
    }

    private double clean(double d) {
        if (Double.isNaN(d)) {
            System.err.println("oops!");
        }
        return d;
    }

    public boolean isValid() {
        if (Double.isNaN(r) || Double.isNaN(g) || Double.isNaN(b)) {
            return false;
        }
        if (r < -1e-5 || g < -1e-5 || b < -1e-5) {
            return false;
        }
        if (Double.isInfinite(r) || Double.isInfinite(g) || Double.isInfinite(b)) {
            return false;
        }
        return true;
    }

    public boolean isBlack() {
        throw new UnsupportedOperationException();
    }

    public static Color [] arrayOfBlack(int length) {
        Color [] result = new Color[length];
        for (int i=0; i<result.length; i++) {
            result[i] = BLACK;
        }
        return result;
    }

    // =================================================================================
    // constructors
    // =================================================================================

    public static Color sum(Color... colors) {
        return sum(Arrays.asList(colors));
    }

    public static Color sum(Iterable<Color> colors) {
        Color sum = BLACK;
        for (Color next : colors) {
            sum = sum.plus(next);
        }
        return sum;
    }

    public static Color from(double x) {
        return new Color(x, x, x);
    }
}
