package ebrt.interactions;

import ebrt.math.Transform;
import ebrt.media.Medium;
import ebrt.math.Point3d;
import ebrt.math.Vector3d;

public record Ray(
        Point3d origin, Vector3d direction,
        Point3d originX, Vector3d directionX,
        Point3d originY, Vector3d directionY,
        boolean hasDifferentials,
        Medium medium) {

    public Ray(Point3d origin, Vector3d direction) {
        this(origin, direction, null, null, null, null, false, null);
    }

    public Point3d at(double t) {
        return origin.plus(direction.mul(t));
    }

    public Ray transform(Transform t) {
        return new Ray(origin.transform(t), direction.transform(t));
    }
}
