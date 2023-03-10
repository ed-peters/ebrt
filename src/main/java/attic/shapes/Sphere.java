package attic.shapes;

import ebrt.interactions.SurfaceInteraction;
import attic.math.Bounds3d;
import attic.math.Hits;
import attic.math.Point3d;
import attic.math.Ray;
import attic.math.Transform;
import attic.math.Utils;

import static attic.math.Utils.TAU;
import static attic.math.Utils.clamp;

public class Sphere extends Shape {

    private final double radius;
    private final double zmin;
    private final double zmax;
    private final double thetaMin;
    private final double thetaMax;
    private final double thetaRange;
    private final double phiMax;

    public Sphere(Point3d center, double radius) {
        this(Transform.translate(center.x(), center.y(), center.z()),
                false, radius, -radius, radius, 360);
    }

    public Sphere(Transform objectToWorld, boolean reverse, double radius) {
        this(objectToWorld, reverse, radius, -radius, radius, 360);
    }

    public Sphere(Transform objectToWorld, boolean reverse, double radius, double zmin, double zmax, double phiMax) {
        super(objectToWorld, bounds(radius, zmin, zmax), reverse);
        this.radius = radius;
        this.zmin = clamp(Math.min(zmin, zmax), -radius, radius);
        this.zmax = clamp(Math.max(zmin, zmax), -radius, radius);
        this.thetaMin = Math.acos(clamp(zmin / radius, -1, 1));
        this.thetaMax = Math.acos(clamp(zmax / radius, -1, 1));
        this.thetaRange = thetaMax - thetaMin;
        this.phiMax = Math.toRadians(clamp(phiMax, 0, 360));
    }

    @Override
    protected SurfaceInteraction intersectObject(Ray objectRay, double tmax, boolean testAlpha) {

        double dx = objectRay.direction().x();
        double dy = objectRay.direction().y();
        double dz = objectRay.direction().z();
        double ox = objectRay.origin().x();
        double oy = objectRay.origin().y();
        double oz = objectRay.origin().z();

        double a = Utils.square(dx, dy, dz);
        double b = 2 * Utils.dot(dx, dy, dz, ox, oy, oz);
        double c = Utils.square(ox, oy, oz) - radius * radius;
        Hits hits = Utils.quadratic(a, b, c);

        double t = hits.select(tmax);
        if (Double.isNaN(t)) {
            return null;
        }

        Point3d p = refinePoint3d(objectRay.at(t));
        double phi = Math.atan2(p.x(), p.y());
        if (phi < 0) {
            phi += TAU;
        }

        if (outOfBounds3d(p, phi)) {

            if (t == hits.t1() || Double.isNaN(hits.t1()) || hits.t1() > tmax) {
                return null;
            }

            t = hits.t1();
            p = refinePoint3d(objectRay.at(t));
            phi = Math.atan2(p.x(), p.y());
            if (phi < 0) {
                phi += TAU;
            }

            if (outOfBounds3d(p, phi)) {
                return null;
            }
        }

        double theta = Math.acos(clamp(p.z() / radius, -1, 1));
        return SurfaceInteraction.build()
                .withPoint(p)
                .withNormal(p.toVector3d().toNormal())
                .withWo(objectRay.direction())
                .withT(t)
                .withUV(phi / phiMax, (theta - thetaMin) / thetaRange)
                .withShape(this)
                .build();
    }

    private Point3d refinePoint3d(Point3d point) {
        double rd = radius / point.minus(Point3d.ORIGIN).length();
        return point.mul(rd);
    }

    private boolean outOfBounds3d(Point3d point, double phi) {
        return (zmin > -radius && point.z() < zmin)
                || (zmax < radius && point.z() > zmax)
                || phi > phiMax;
    }

    @Override
    public double area() {
        return phiMax * radius * (zmax - zmin);
    }

    private static Bounds3d bounds(double radius, double zmin, double zmax) {
        Point3d min = new Point3d(-radius, -radius, zmin);
        Point3d max = new Point3d(radius, radius, zmax);
        return new Bounds3d(min, max);
    }
}
