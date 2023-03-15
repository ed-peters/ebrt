package ebrt.interactions;

import ebrt.math.Transform;
import ebrt.media.Medium;
import ebrt.math.Point3d;
import ebrt.math.Vector3d;

public record Ray(Point3d origin, Vector3d direction, Medium medium) {

    public Ray(Point3d origin, Vector3d direction) {
        this(origin, direction, null);
    }

    public Point3d at(double t) {
        return origin.plus(direction.mul(t));
    }

    public Ray transform(Transform t) {
        return new Ray(origin.transform(t), direction.transform(t));
    }
}
