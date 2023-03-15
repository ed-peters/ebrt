package ebrt.math;

public record Bounds2d(Point2d min, Point2d max) {

    public static final Bounds2d UNIT = new Bounds2d(new Point2d(0, 0), new Point2d(1, 1));

    public Bounds2d(Point2d min, Point2d max) {
        this.min = min.min(max);
        this.max = min.max(max);
    }

    public double width() {
        return max.x() - min.x();
    }

    public double height() {
        return max.y() - min.y();
    }

    public double area() {
        return width() * height();
    }

    public double aspectRatio() {
        return width() / height();
    }

    public Bounds2d mul(Point2d point) {
        return new Bounds2d(min.mul(point), max.mul(point));
    }

    public Bounds2d mul(Point2i point) {
        return new Bounds2d(min.mul(point), max.mul(point));
    }

    public Bounds2i ceil() {
        return new Bounds2i(min.ceil(), max.ceil());
    }
}
