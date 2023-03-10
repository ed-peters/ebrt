package attic.shapes;

import ebrt.interactions.SurfaceInteraction;
import attic.math.Bounds3d;
import attic.math.Hits;
import attic.math.Normal3d;
import attic.math.Point3d;
import attic.math.Ray;
import attic.math.Transform;
import attic.math.Utils;

import static attic.math.Utils.TAU;

public class Cylinder extends Shape {

    private final double radius;
    private final double zmin;
    private final double zmax;
    private final double phiMax;

    public Cylinder(Transform objectToWorld, double radius) {
        this(objectToWorld, false, radius, -radius, radius, 360);
    }

    public Cylinder(Transform objectToWorld, boolean reverse, double radius, double height) {
        this(objectToWorld, reverse, radius, 0, height, 360);
    }

    public Cylinder(Transform objectToWorld, boolean reverse, double radius, double zmin, double zmax) {
        this(objectToWorld, reverse, radius, zmin, zmax, 360);
    }

    public Cylinder(Transform objectToWorld, boolean reverse, double radius, double zmin, double zmax, double phiMax) {
        super(objectToWorld, bounds(radius, zmin, zmax, phiMax), reverse);
        this.radius = radius;
        this.zmin = Math.min(zmin, zmax);
        this.zmax = Math.max(zmin, zmax);
        this.phiMax = Math.toRadians(Utils.clamp(phiMax, 0, 360));
    }

    @Override
    protected SurfaceInteraction intersectObject(Ray objectRay, double tmax, boolean testAlpha) {

        double dx = objectRay.direction().x();
        double dy = objectRay.direction().y();
        double ox = objectRay.origin().x();
        double oy = objectRay.origin().y();

        double a = dx * dx + dy * dy;
        double b = 2 * (dx * ox + dy * oy);
        double c = ox * ox + oy * oy - radius * radius;
        Hits hits = Utils.quadratic(a, b, c);

        double t = hits.select(tmax);
        if (Double.isNaN(t)) {
            return null;
        }

        Point3d p = refinePoint3d(objectRay.at(t));
        double phi = Math.atan2(p.y(), p.x());
        if (phi < 0) {
            phi += TAU;
        }

        if (outOfBounds3d(p, phi)) {

            if (t == hits.t1() || Double.isNaN(hits.t1()) || hits.t1() > tmax) {
                return null;
            }

            t = hits.t1();
            p = refinePoint3d(objectRay.at(t));
            phi = Math.atan2(p.y(), p.x());
            if (phi < 0) {
                phi += TAU;
            }

            if (outOfBounds3d(p, phi)) {
                return null;
            }
        }

        return SurfaceInteraction.build()
                .withPoint(p)
                .withNormal(new Normal3d(p.x(), p.y(), p.z()))
                .withT(t)
                .withUV(phi / phiMax, (p.z() - zmin) / (zmax - zmin))
                .withShape(this)
                .build();
    }

    private Point3d refinePoint3d(Point3d point) {
        double hr = radius / Math.sqrt(point.x() * point.x() + point.y() * point.y());
        return new Point3d(point.x() * hr, point.y() * hr, point.z());
    }

    private boolean outOfBounds3d(Point3d p, double phi) {
        return p.z() < zmin || p.z() > zmax || phi > phiMax;
    }

    @Override
    public double area() {
        return (zmax - zmin) * radius * phiMax;
    }

    private static Bounds3d bounds(double radius, double zmin, double zmax, double phiMax) {
        return new Bounds3d(new Point3d(-radius, -radius, zmin), new Point3d(radius, radius, zmax));
    }
}
