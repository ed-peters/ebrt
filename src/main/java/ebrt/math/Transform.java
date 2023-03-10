package ebrt.math;

import ebrt.interactions.SurfaceInteraction;
import ebrt.interactions.Ray;
import ebrt.interactions.RayDifferential;

import java.util.List;
import java.util.stream.Collectors;

import static java.lang.Math.cos;
import static java.lang.Math.sin;
import static java.lang.Math.toRadians;

public record Transform(double [][] matrix, double [][] inverse) {

    public static final Transform IDENT = Transform.from(new double [][]{
            { 1, 0, 0, 0 },
            { 0, 1, 0, 0 },
            { 0, 0, 1, 0 },
            { 0, 0, 0, 1 }
    });

    public RayDifferential forward(RayDifferential rd) {
        return execute(rd, matrix);
    }

    public RayDifferential reverse(RayDifferential rd) {
        return execute(rd, inverse);
    }

    private RayDifferential execute(RayDifferential rd, double [][] m) {

        Ray r = execute(rd.ray(), m);
        if (!rd.hasDifferentials()) {
            return new RayDifferential(r);
        }

        Point3d rox = execute(rd.originX(), m);
        Point3d roy = execute(rd.originY(), m);
        Vector3d rdx = execute(rd.directionX(), m);
        Vector3d rdy = execute(rd.directionY(), m);
        return new RayDifferential(r, true, rox, roy, rdx, rdy);
    }

    public SurfaceInteraction forward(SurfaceInteraction si) {
        return execute(si, matrix);
    }

    public SurfaceInteraction reverse(SurfaceInteraction si) {
        return execute(si, inverse);
    }

    private SurfaceInteraction execute(SurfaceInteraction si, double [][] m) {
        return SurfaceInteraction.build(si)
                .withPoint(execute(si.point, m))
                .withNormal(execute(si.normal, m))
                .build();
    }

    public Point3d forward(Point3d p) {
        return execute(p, matrix);
    }

    public Point3d reverse(Point3d p) {
        return execute(p, inverse);
    }

    private static Point3d execute(Point3d p, double [][] m) {
        double x = p.x();
        double y = p.y();
        double z = p.z();
        double xp = m[0][0]*x + m[0][1]*y + m[0][2]*z + m[0][3];
        double yp = m[1][0]*x + m[1][1]*y + m[1][2]*z + m[1][3];
        double zp = m[2][0]*x + m[2][1]*y + m[2][2]*z + m[2][3];
        double wp = m[3][0]*x + m[3][1]*y + m[3][2]*z + m[3][3];
        return new Point3d(xp / wp, yp / wp, zp / wp);
    }

    public Vector3d forward(Vector3d v) {
        return execute(v, matrix);
    }

    public Vector3d reverse(Vector3d v) {
        return execute(v, inverse);
    }

    private static Vector3d execute(Vector3d v, double [][] m) {
        double x = v.x();
        double y = v.y();
        double z = v.z();
        return new Vector3d(
                m[0][0]*x + m[0][1]*y + m[0][2]*z,
                m[1][0]*x + m[1][1]*y + m[1][2]*z,
                m[2][0]*x + m[2][1]*y + m[2][2]*z);    
    }
    
    public Normal3d forward(Normal3d n) {
        return execute(n, inverse);
    }
    
    public Normal3d reverse(Normal3d n) {
        return execute(n, matrix);
    }

    private static Normal3d execute(Normal3d n, double [][] m) {
        double x = n.x();
        double y = n.y();
        double z = n.z();
        return new Normal3d(
                m[0][0]*x + m[0][1]*y + m[0][2]*z,
                m[1][0]*x + m[1][1]*y + m[1][2]*z,
                m[2][0]*x + m[2][1]*y + m[2][2]*z);
    }

    public Bounds3d forward(Bounds3d b) {
        return execute(b, matrix);
    }

    public Bounds3d reverse(Bounds3d b) {
        return execute(b, inverse);
    }

    public static Bounds3d execute(Bounds3d b, double [][] m) {
        List<Point3d> corners = b.corners().stream()
                        .map(c -> execute(c, m))
                        .collect(Collectors.toList());
        return Bounds3d.around(corners);
    }

    public Ray forward(Ray r) {
        return execute(r, matrix);
    }

    public Ray reverse(Ray r) {
        return execute(r, inverse);
    }

    public static Ray execute(Ray r, double [][] m) {
        Point3d o = execute(r.origin(), m);
        Vector3d d = execute(r.direction(), m);
        return new Ray(o, d);
    }

    public boolean swapsHandedness() {
        double det = matrix[0][0] * (matrix[1][1] * matrix[2][2] - matrix[1][2] * matrix[2][1]) -
                        matrix[0][1] * (matrix[1][0] * matrix[2][2] - matrix[1][2] * matrix[2][0]) +
                        matrix[0][2] * (matrix[1][0] * matrix[2][1] - matrix[1][1] * matrix[2][0]);
        return det < 0;
    }

    public Transform invert() {
        return new Transform(inverse, matrix);
    }

    public Transform compose(Transform other) {
        double [][] mm = Matrix.multiply(matrix, other.matrix);
        double [][] mi = Matrix.multiply(other.inverse, inverse);
        return new Transform(mm, mi);
    }

    public Transform andThen(Transform other) {
        return other.compose(this);
    }

    public static Transform translate(double dx, double dy, double dz) {
        double [][] mm = {
                { 1, 0, 0, dx },
                { 0, 1, 0, dy },
                { 0, 0, 1, dz },
                { 0, 0, 0, 1 }
        };
        double [][] mi = {
                { 1, 0, 0, -dx },
                { 0, 1, 0, -dy },
                { 0, 0, 1, -dz },
                { 0, 0, 0, 1 }
        };
        return new Transform(mm, mi);
    }

    public static Transform scale(double dx, double dy, double dz) {
        double [][] mm = {
                { dx, 0, 0, 0 },
                { 0, dy, 0, 0 },
                { 0, 0, dz, 0 },
                { 0, 0, 0, 1 }
        };
        double [][] mi = {
                { 1 / dx, 0, 0, 0 },
                { 0, 1 / dy, 0, 0 },
                { 0, 0, 1 / dz, 0 },
                { 0, 0, 0, 1 }
        };
        return new Transform(mm, mi);
    }

    public static Transform rotate(Vector3d axis, double degrees) {

        Vector3d a = axis.normalize();
        double s = sin(toRadians(degrees));
        double c = cos(toRadians(degrees));

        double [][] d = new double[4][4];

        d[0][0] = a.x() * a.x() + (1 - a.x() * a.x()) * c;
        d[0][1] = a.x() * a.y() * (1 - c) - a.z() * s;
        d[0][2] = a.x() * a.z() * (1 - c) + a.y() * s;

        d[1][0] = a.x() * a.y() * (1 - c) + a.z() * s;
        d[1][1] = a.y() * a.y() + (1 - a.y() * a.y()) * c;
        d[1][2] = a.y() * a.z() * (1 - c) - a.x() * s;

        d[2][0] = a.x() * a.z() * (1 - c) - a.y() * s;
        d[2][1] = a.y() * a.z() * (1 - c) + a.x() * s;
        d[2][2] = a.z() * a.z() + (1 - a.z() * a.z()) * c;

        d[3][3] = 1;

        return from(d);
    }

    public static Transform lookAt(Point3d position, Point3d target, Vector3d up) {

        double [][] d = new double[4][4];

        d[0][3] = position.x();
        d[1][3] = position.y();
        d[2][3] = position.z();
        d[3][3] = 1;

        Vector3d dir = target.minus(position).normalize();
        Vector3d right = up.normalize().cross(dir).normalize();
        Vector3d newup = dir.cross(right);

        d[0][0] = right.x();
        d[1][0] = right.y();
        d[2][0] = right.z();

        d[0][1] = newup.x();
        d[1][1] = newup.y();
        d[2][1] = newup.z();

        d[0][2] = dir.x();
        d[1][2] = dir.y();
        d[2][2] = dir.z();

        d[0][3] = position.x();
        d[1][3] = position.y();
        d[2][3] = position.z();
        d[3][3] = 1;

        return from(d);
    }

    public static Transform from(double [][] matrix) {
        return new Transform(matrix, Matrix.invert(matrix));
    }
}
