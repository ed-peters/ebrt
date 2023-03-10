package ebrt.interactions;

import ebrt.math.Normal3d;
import ebrt.math.Point3d;
import ebrt.math.Vector3d;
import ebrt.media.Medium;
import ebrt.media.MediumInterface;

public abstract class Interaction {

    public final Point3d point;
    public final Normal3d normal;
    public final Vector3d wo;
    public final double t;
    public MediumInterface mediumInterface;

    public Interaction(Point3d point, Normal3d normal, Vector3d wo, double t) {
        this.point = point;
        this.normal = normal;
        this.wo = wo;
        this.t = t;
    }

    public void setMediumInterface(MediumInterface mi) {
        this.mediumInterface = mi;
    }

    public Ray spawnRay(Vector3d direction) {
        // TODO offset origin by epsilon
        return new Ray(point, direction);
    }

    public Ray spawnRayTo(Point3d destination) {
        // TODO offset origin by epsilon
        return new Ray(point, destination.minus(point));
    }

    public Ray spawnRayTo(Interaction other) {
        return spawnRayTo(other.point);
    }

    public Medium computeMedium(Vector3d w) {
        if (mediumInterface == null) {
            return null;
        }
        return w.dot(normal) > 0
                ? mediumInterface.outside()
                : mediumInterface.inside();
    }

    public Medium medium() {
        if (mediumInterface == null) {
            return null;
        }
        if (mediumInterface.isTransition()) {
            throw new UnsupportedOperationException();
        }
        return mediumInterface.inside();
    }
}
