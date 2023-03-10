package ebrt.math;

import java.util.Arrays;
import java.util.concurrent.ThreadLocalRandom;

public record Point3d(double x, double y, double z) {

    public static final Point3d ORIGIN = new Point3d(0, 0, 0);

    public double component(Axis axis) {
        return switch (axis) {
            case X -> x();
            case Y -> y();
            case Z -> z();
        };
    }

    public Vector3d toVector3d() {
        return new Vector3d(x, y, z);
    }

    public Vector3d minus(Point3d other) {
        return new Vector3d(x - other.x(), y - other.y(), z - other.z());
    }

    public Point3d minus(Vector3d other) {
        return new Point3d(x - other.x(), y - other.y(), z - other.z());
    }

    public Point3d plus(Vector3d other) {
        return new Point3d(x + other.x(), y + other.y(), z + other.z());
    }

    public Point3d mul(double factor) {
        return new Point3d(x * factor, y * factor, z * factor);
    }

    public Point3d div(double factor) {
        return mul(1.0 / factor);
    }

    public double dot(Point3d other) {
        return x * other.x() + y * other.y() + z * other.z();
    }

    public static Point3d min(Point3d... points) {
        return min(Arrays.asList(points));
    }

    public static Point3d min(Iterable<Point3d> points) {
        double x = Double.POSITIVE_INFINITY;
        double y = Double.POSITIVE_INFINITY;
        double z = Double.POSITIVE_INFINITY;
        for (Point3d p : points) {
            x = Math.min(x, p.x());
            y = Math.min(y, p.y());
            z = Math.min(z, p.z());
        }
        return new Point3d(x, y, z);
    }

    public static Point3d max(Point3d... points) {
        return max(Arrays.asList(points));
    }

    public static Point3d max(Iterable<Point3d> points) {
        double x = Double.NEGATIVE_INFINITY;
        double y = Double.NEGATIVE_INFINITY;
        double z = Double.NEGATIVE_INFINITY;
        for (Point3d p : points) {
            x = Math.max(x, p.x());
            y = Math.max(y, p.y());
            z = Math.max(z, p.z());
        }
        return new Point3d(x, y, z);
    }

    public static Point3d randomPoint(double min, double max) {
        return new Point3d(
                ThreadLocalRandom.current().nextDouble(min, max),
                ThreadLocalRandom.current().nextDouble(min, max),
                ThreadLocalRandom.current().nextDouble(min, max));
    }
}
