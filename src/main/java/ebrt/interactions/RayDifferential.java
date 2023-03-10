package ebrt.interactions;

import ebrt.math.Point3d;
import ebrt.math.Transform;
import ebrt.math.Vector3d;

public record RayDifferential(
        Ray ray,
        boolean hasDifferentials,
        Point3d originX,
        Point3d originY,
        Vector3d directionX,
        Vector3d directionY) {

    public RayDifferential(Ray ray) {
        this(ray, false, null, null, null, null);
    }

    public RayDifferential scaleDifferentials(double factor) {
        if (hasDifferentials) {
            Point3d ox = ray.origin().plus(originX.minus(ray.origin()).mul(factor));
            Point3d oy = ray.origin().plus(originY.minus(ray.origin()).mul(factor));
            Vector3d dx = ray.direction().plus(directionX.minus(ray.direction()).mul(factor));
            Vector3d dy = ray.direction().plus(directionY.minus(ray.direction()).mul(factor));
            return new RayDifferential(ray, true, ox, oy, dx, dy);
        }
        return this;
    }

    public RayDifferential transform(Transform t) {

        Ray r = ray.transform(t);
        if (!hasDifferentials) {
            return new RayDifferential(r);
        }

        Point3d rox = originX.transform(t);
        Point3d roy = originY.transform(t);
        Vector3d rdx = directionX.transform(t);
        Vector3d rdy = directionY.transform(t);
        return new RayDifferential(r, true, rox, roy, rdx, rdy);
    }
}
