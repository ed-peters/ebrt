package ebrt.math;

public class Utils {

    public static final double TAU = 2.0 * Math.PI;
    public static final double PI_OVER_2 = Math.PI / 2;
    public static final double PI_OVER_4 = Math.PI / 4;

    public static final double MACHINE_EPSILON;
    static {
        double tmp = 0.5;
        while (1 + tmp > 1) {
            tmp /= 2;
        }
        MACHINE_EPSILON = tmp;
    }

    public static double gamma(int n) {
        return (n * MACHINE_EPSILON) / (1 - n * MACHINE_EPSILON);
    }

    public static double clamp(double val, double lo, double hi) {
        if (val < lo) {
            return lo;
        }
        if (val > hi) {
            return hi;
        }
        return val;
    }

    public static int scaleInt(double val, int factor) {
        return (int)(clamp(val, 0.0, 1.0) * factor);
    }

    public static Hits quadratic(double a, double b, double c) {

        double d = b * b - 4 * a * c;
        if (d < 0) {
            return Hits.NONE;
        }

        double rd = Math.sqrt(d);
        double q = b < 0
                ? -0.5 * (b - rd)
                : -0.5 * (b + rd);

        double t0 = q / a;
        double t1 = c / q;
        return new Hits(t0, t1);
    }

    public static double square(double x, double y, double z) {
        return dot(x, y, z, x, y, z);
    }

    public static double dot(double x0, double y0, double z0, double x1, double y1, double z1) {
        return x0 * x1 + y0 * y1 + z0 * z1;
    }

    public static double lerp(double l, double r, double t) {
        return (1 - t) * l + t * r;
    }
}
