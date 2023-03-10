package ebrt.math;

public record Hits(double t0, double t1) {

    public static final Hits NONE = new Hits(Double.NaN, Double.NaN);

    public Hits(double t0, double t1) {
        this.t0 = Math.min(t0, t1);
        this.t1 = Math.max(t0, t1);
    }

    public double select(double tmax) {

        if (this == NONE) {
            return Double.NaN;
        }

        if (t0 > tmax || t1 < 0) {
            return Double.NaN;
        }

        double t = t0;
        if (t <= 0) {
            t = t1;
            if (t > tmax) {
                return Double.NaN;
            }
        }
        return t;
    }
}
