package ebrt.math;

public record Point2i(int x, int y) {

    public static final Point2i ORIGIN = new Point2i(0, 0);

    public Point2i(double x, double y) {
        this((int)x, (int)y);
    }

    public Point2i minWith(Point2i other) {
        return new Point2i(Math.min(x, other.x), Math.min(y, other.y));
    }

    public Point2i maxWith(Point2i other) {
        return new Point2i(Math.max(x, other.x), Math.max(y, other.y));
    }

    public Point2d plus(Point2d other) {
        return new Point2d(x + other.x(), y + other.y());
    }

    public Point2d minus(Point2d other) {
        return new Point2d(x - other.x(), y - other.y());
    }

    public Vector2i towards(Point2i other) {
        return new Vector2i(other.x() - x, other.y() - y);
    }

    public Point2i min(Point2i other) {
        return new Point2i(Math.min(x, other.x), Math.min(y, other.y));
    }

    public Point2i max(Point2i other) {
        return new Point2i(Math.max(x, other.x), Math.max(y, other.y));
    }

    public Point2i abs() {
        return new Point2i(Math.abs(x), Math.abs(y));
    }

    public Point2d toPoint2d() {
        return new Point2d(x, y);
    }

    public Point3d toPoint3d() {
        return toPoint3d(0);
    }

    public Point3d toPoint3d(double z) {
        return new Point3d(x, y, z);
    }
}
