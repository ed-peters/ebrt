package ebrt.interactions;

import ebrt.math.Point3d;
import ebrt.math.Transform;
import ebrt.math.Vector3d;

public record RayPack(Ray primary, Ray rx, Ray ry) {

    public RayPack(Ray primary) {
        this(primary, null, null);
    }

    public boolean hasDifferentials() {
        return rx != null;
    }

    public RayPack scaleDifferentials(double factor) {
        if (hasDifferentials()) {
            Point3d ox = primary.origin().plus(rx.origin().minus(primary.origin()).mul(factor));
            Point3d oy = primary.origin().plus(ry.origin().minus(primary.origin()).mul(factor));
            Vector3d dx = primary.direction().plus(rx.direction().minus(primary.direction()).mul(factor));
            Vector3d dy = primary.direction().plus(ry.direction().minus(primary.direction()).mul(factor));
            return new RayPack(primary, new Ray(ox, dx), new Ray(oy, dy));
        }
        return this;
    }

    public RayPack transform(Transform t) {

        Ray tp = primary.transform(t);
        if (!hasDifferentials()) {
            return new RayPack(tp);
        }

        Ray tx = rx.transform(t);
        Ray ty = ry.transform(t);
        return new RayPack(tp, tx, ty);
    }
}
