package ebrt.math;

public record Normal3d(double x, double y, double z) {

    public Normal3d(double x, double y, double z) {
        double l = Math.sqrt(x * x + y * y + z * z);
        this.x = x / l + 0;
        this.y = y / l + 0;
        this.z = z / l + 0;
    }

    public double component(Axis axis) {
        return switch (axis) {
            case X -> x();
            case Y -> y();
            case Z -> z();
        };
    }

    public Vector3d plus(Vector3d other) {
        return toVector3d().plus(other);
    }

    public Vector3d minus(Vector3d other) {
        return toVector3d().minus(other);
    }

    public Vector3d mul(Vector3d other) {
        return toVector3d().mul(other);
    }

    public Vector3d mul(double factor) {
        return toVector3d().mul(factor);
    }

    public Vector3d div(double factor) {
        return toVector3d().div(factor);
    }

    public double dot(Point3d point) {
        return x * point.x() + y * point.y() + z * point.z();
    }

    public double dot(Vector3d vector) {
        return x * vector.x() + y * vector.y() + z * vector.z();
    }

    public Normal3d negate() {
        return new Normal3d(-x, -y, -z);
    }

    public Vector3d toVector3d() {
        return new Vector3d(x, y, z);
    }
}
