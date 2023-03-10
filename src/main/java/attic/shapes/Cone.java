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

public class Cone extends Shape {

    private final double radius;
    private final double height;
    private final double phiMax;

    public Cone(Transform objectToWorld, boolean reverse, double radius, double height) {
        this(objectToWorld, reverse, radius, height, 360);
    }

    public Cone(Transform objectToWorld, boolean reverse, double radius, double height, double phiMax) {
        super(objectToWorld, bounds(radius, height), reverse);
        this.radius = radius;
        this.height = height;
        this.phiMax = Math.toRadians(Utils.clamp(phiMax, 0, 360));
    }

    @Override
    protected SurfaceInteraction intersectObject(Ray objectRay, double tmax, boolean testAlpha) {

        double dx = objectRay.direction().x();
        double dy = objectRay.direction().y();
        double dz = objectRay.direction().z();
        double ox = objectRay.origin().x();
        double oy = objectRay.origin().y();
        double oz = objectRay.origin().z();

        double k = (radius / height) * (radius / height);
        double a = dx * dx + dy * dy - k * dz * dz;
        double b = 2 * (dx * ox + dy * oy - k * dz * (oz - height));
        double c = ox * ox + oy * oy - k * (oz - height) * (oz - height);

        Hits hits = Utils.quadratic(a, b, c);

        double t = hits.select(tmax);
        if (Double.isNaN(t)) {
            return null;
        }

        Point3d p = objectRay.at(t);
        double phi = Math.atan2(p.y(), p.x());
        if (phi < 0) {
            phi += Utils.TAU;
        }

        if (outOfBounds3d(p, phi)) {

            if (t == hits.t1() || Double.isNaN(hits.t1()) || hits.t1() > tmax) {
                return null;
            }

            t = hits.t1();
            p = objectRay.at(t);
            phi = Math.atan2(p.x(), p.y());
            if (phi < 0) {
                phi += TAU;
            }

            if (outOfBounds3d(p, phi)) {
                return null;
            }
        }

        double m = p.x() * p.x() + p.y() * p.y();
        double o = height / radius;

        return SurfaceInteraction.build()
                .withPoint(p)
                .withNormal(new Normal3d(p.x() * o / m, radius / height, p.z() * o / m))
                .withWo(objectRay.direction())
                .withT(t)
                .withUV(phi / phiMax, p.z() / height)
                .withShape(this)
                .build();
    }

    private boolean outOfBounds3d(Point3d point, double phi) {
        return point.z() < 0 || point.z() > height || phi > phiMax;
    }

    @Override
    public double area() {
        return radius * Math.sqrt((height * height) + (radius * radius)) * phiMax / 2;
    }

    private static Bounds3d bounds(double radius, double height) {
        return new Bounds3d(new Point3d(-radius, -radius, 0), new Point3d(radius, radius, height));
    }
}
