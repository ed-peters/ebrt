package ebrt.math;

public record Point2d(double x, double y) {

    public static final Point2d ORIGIN = new Point2d(0, 0);
    public static final Point2d HALVSIES = new Point2d(0.5, 0.5);
    public static final Point2d ONESIES = new Point2d(1.0, 1.0);

    public Point2d invert() {
        return new Point2d(1 / x, 1 / y);
    }

    public Point2i ceil() {
        return new Point2i(Math.ceil(x), Math.ceil(y));
    }

    public Point2i floor() {
        return new Point2i(Math.floor(x), Math.floor(y));
    }

    public Point2d mul(double factor) {
        return new Point2d(x * factor, y * factor);
    }

    public Point2d mul(Point2i other) {
        return new Point2d(x * other.x(), y * other.y());
    }

    public Point2d mul(Point2d other) {
        return new Point2d(x * other.x(), y * other.y());
    }

    public Point2d div(double factor) {
        return new Point2d(x / factor, y / factor);
    }

    public Point2d plus(Point2d other) { return new Point2d(x + other.x, y + other.y); }

    public Point2d minus(Point2d other) { return new Point2d(x - other.x, y - other.y); }

    public Point2d min(Point2d other) {
        return new Point2d(Math.min(x, other.x), Math.min(y, other.y));
    }

    public Point2d max(Point2d other) {
        return new Point2d(Math.max(x, other.x), Math.max(y, other.y));
    }

    public Point3d toPoint3d() {
        return toPoint3d(0);
    }

    public Point3d toPoint3d(double z) {
        return new Point3d(x, y, z);
    }

    public Point2d concentricSampleDisk() {

        Point2d offset = mul(2).minus(ONESIES);
        if (offset.x == 0 && offset.y == 0) {
            return ORIGIN;
        }

        double r = 0;
        double phi = 0;
        if (Math.abs(offset.x) > Math.abs(offset.y)) {
            r = offset.x;
            phi = Utils.PI_OVER_4 * (offset.y / offset.x);
        } else {
            r = offset.y;
            phi = Utils.PI_OVER_2  - Utils.PI_OVER_4 * (offset.x / offset.y);
        }

        return new Point2d(Math.cos(phi), Math.sin(phi)).mul(r);
    }
}
