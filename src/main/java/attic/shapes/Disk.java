package attic.shapes;

import ebrt.interactions.SurfaceInteraction;
import attic.math.Bounds3d;
import attic.math.Normal3d;
import attic.math.Point3d;
import attic.math.Ray;
import attic.math.Transform;
import attic.math.Utils;

public class Disk extends Shape {

    public static final Normal3d UP = new Normal3d(0, 0, 1);
    public static final Normal3d DOWN = new Normal3d(0, 0, -1);

    private final double height;
    private final double outerRadius;
    private final double innerRadius;
    private final double phiMax;

    public Disk(Transform objectToWorld, boolean reverse, double height, double outerRadius, double innerRadius, double phiMax) {
        super(objectToWorld, bounds(height, outerRadius), reverse);
        this.height = height;
        this.outerRadius = outerRadius;
        this.innerRadius = innerRadius;
        this.phiMax = Math.toRadians(Utils.clamp(phiMax, 0, 360));
    }

    @Override
    protected SurfaceInteraction intersectObject(Ray objectRay, double tmax, boolean testAlpha) {

        if (objectRay.direction().z() == 0) {
            return null;
        }

        double t = (height - objectRay.origin().z()) / objectRay.direction().z();
        if (t <= 0 || t >= tmax) {
            return null;
        }

        Point3d p = refine(objectRay.at(t));
        double d = p.x() * p.x() + p.y() * p.y();
        if (d > outerRadius * outerRadius || d < innerRadius * innerRadius) {
            return null;
        }

        double phi = Math.atan2(p.y(), p.x());
        if (phi < 0) {
            phi += Utils.TAU;
        }
        if (phi > phiMax) {
            return null;
        }

        double rHit = Math.sqrt(d);
        double oneMinusV = (rHit - innerRadius) / (outerRadius - innerRadius);

        return SurfaceInteraction.build()
                .withPoint(p)
                .withNormal(objectRay.origin().z() < height ? DOWN : UP)
                .withWo(objectRay.direction())
                .withT(t)
                .withUV(phi / phiMax, 1 - oneMinusV)
                .withShape(this)
                .build();
    }

    private Point3d refine(Point3d point) {
        if (point.z() != height) {
            return new Point3d(point.x(), point.y(), height);
        }
        return point;
    }

    @Override
    public double area() {
        return phiMax * 0.5 * (outerRadius * outerRadius - innerRadius * innerRadius);
    }

    private static Bounds3d bounds(double height, double outerRadius) {
        return new Bounds3d(
                new Point3d(-outerRadius, -outerRadius, Math.nextUp(height)),
                new Point3d(outerRadius,  outerRadius, Math.nextDown(height)));
    }
}
