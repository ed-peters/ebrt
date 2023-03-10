package ebrt.math;

import java.util.concurrent.ThreadLocalRandom;

import static java.lang.Math.abs;
import static java.lang.Math.cos;
import static java.lang.Math.sin;
import static java.lang.Math.sqrt;

public record Vector3d(double x, double y, double z) {

    public static final Vector3d X = new Vector3d(1, 0, 0);
    public static final Vector3d Y = new Vector3d(0, 1, 0);
    public static final Vector3d Z = new Vector3d(0, 0, 1);
    public static final Vector3d ZERO = new Vector3d(0, 0, 0);

    public double component(Axis axis) {
        return switch (axis) {
            case X -> x();
            case Y -> y();
            case Z -> z();
        };
    }

    public Vector3d plus(Vector3d other) {
        return new Vector3d(x + other.x(), y + other.y(), z + other.z());
    }

    public Vector3d minus(Vector3d other) {
        return new Vector3d(x - other.x(), y - other.y(), z - other.z());
    }

    public Vector3d mul(Vector3d other) {
        return new Vector3d(x * other.x(), y * other.y(), z * other.z());
    }

    public Vector3d plus(Normal3d other) {
        return new Vector3d(x + other.x(), y + other.y(), z + other.z());
    }

    public Vector3d minus(Normal3d other) {
        return new Vector3d(x - other.x(), y - other.y(), z - other.z());
    }

    public Vector3d mul(Normal3d other) {
        return new Vector3d(x * other.x(), y * other.y(), z * other.z());
    }

    public Vector3d mul(double factor) {
        return new Vector3d(x * factor, y * factor, z * factor);
    }

    public Vector3d div(double factor) {
        return mul(1.0 / factor);
    }

    public Vector3d normalize() {
        return div(length());
    }

    public Normal3d toNormal() {
        return new Normal3d(x, y, z);
    }

    public double dot(Vector3d other) {
        return x * other.x + y * other.y + z * other.z;
    }

    public double dot(Normal3d other) {
        return x * other.x() + y * other.y() + z * other.z();
    }

    public double absDot(Normal3d other) {
        return Math.abs(dot(other));
    }

    public Vector3d cross(Vector3d other) {
        double x = y() * other.z() - z() * other.y();
        double y = z() * other.x() - x() * other.z();
        double z = x() * other.y() - y() * other.x();
        return new Vector3d(x, y, z);
    }

    public Vector3d cross(Normal3d other) {
        double x = y() * other.z() - z() * other.y();
        double y = z() * other.x() - x() * other.z();
        double z = x() * other.y() - y() * other.x();
        return new Vector3d(x, y, z);
    }

    public Vector3d negate() {
        return new Vector3d(-x, -y, -z);
    }

    public double squared() {
        return dot(this);
    }

    public double length() {
        return Math.sqrt(squared());
    }

    public boolean isOpposite(Vector3d other) {
        return dot(other) < 0.0;
    }

    public boolean isOpposite(Normal3d normal) {
        return dot(normal) < 0.0;
    }

    public Vector3d randomHemisphericReflection() {
        Vector3d next = randomUnitVector3d();
        return dot(next) < 0.0 ? next.negate() : next;
    }

    public boolean equalish(Vector3d other) {
        return abs(x - other.x) < 1e-6
                && abs(y - other.y) < 1e-6
                && abs(z - other.z) < 1e-6;
    }

    // =================================================================================
    // spherical coords
    // =================================================================================

    public double cosTheta() {
        return z;
    }

    public double cos2Theta() {
        return z * z;
    }

    public double absCosTheta() {
        return Math.abs(z);
    }

    public double sinTheta() {
        return Math.sqrt(sin2Theta());
    }

    public double sin2Theta() {
        return Math.max(0, 1 - cos2Theta());
    }

    public double tanTheta() {
        return sinTheta() / cosTheta();
    }

    public double tan2Theta() {
        return sin2Theta() / cos2Theta();
    }

    public double cosPhi() {
        double st = sinTheta();
        return (st == 0) ? 1 : Utils.clamp(x / st, -1, 1);
    }

    public double sinPhi() {
        double st = sinTheta();
        return (st == 0) ? 0 : Utils.clamp(y / st, -1, 1);
    }

    public double cos2Phi() {
        double cp = cosPhi();
        return cp * cp;
    }

    public double sin2Phi() {
        double sp = sinPhi();
        return sp * sp;
    }

    // =================================================================================
    // random generator
    // =================================================================================

    public static Vector3d randomVector3d(double min, double max) {
        return new Vector3d(
                ThreadLocalRandom.current().nextDouble(min, max),
                ThreadLocalRandom.current().nextDouble(min, max),
                ThreadLocalRandom.current().nextDouble(min, max));
    }

    public static Vector3d randomVectorInUnitCube() {
        return randomVector3d(-1.0, 1.0);
    }

    public static Vector3d randomUnitVector3d() {
        while (true) {
            Vector3d next = randomVectorInUnitCube();
            if (next.squared() < 1.0) {
                return next.normalize();
            }
        }
    }

    public static Vector3d randomCosineDirection() {
        double r1 = ThreadLocalRandom.current().nextDouble();
        double r2 = ThreadLocalRandom.current().nextDouble();
        double z = sqrt(1.0 - r2);
        double phi = 2.0 * Math.PI * r1;
        double x = cos(phi) * sqrt(r2);
        double y = sin(phi) * sqrt(r2);
        return new Vector3d(x, y, z);
    }

    public static Vector3d randomVectorInUnitDisc() {
        while (true) {
            Vector3d next = new Vector3d(
                    ThreadLocalRandom.current().nextDouble(-1.0, 1.0),
                    ThreadLocalRandom.current().nextDouble(-1.0, 1.0),
                    0.0);
            if (next.squared() < 1.0) {
                return next;
            }
        }
    }
}
