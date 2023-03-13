package ebrt.camera;

import ebrt.interactions.Ray;
import ebrt.interactions.RayDifferential;
import ebrt.math.Bounds2d;
import ebrt.math.Point3d;
import ebrt.math.Projections;
import ebrt.math.Transform;
import ebrt.math.Vector3d;
import ebrt.math.Weighted;
import ebrt.media.Medium;
import ebrt.samplers.CameraSample;

public class PerspectiveCamera extends ProjectiveCamera {

    private final Vector3d cameraX;
    private final Vector3d cameraY;
    private final double a;

    public PerspectiveCamera(
            Transform cameraToWorld,
            Bounds2d screenWindow,
            double lensRadius,
            double focalDistance,
            double fieldOfView,
            Medium medium,
            Film film) {

        super(cameraToWorld, Projections.perspective(fieldOfView), screenWindow, lensRadius, focalDistance, medium, film);

        Vector3d o = Vector3d.ZERO.transform(rasterToCamera());
        Vector3d x = Vector3d.X.transform(rasterToCamera());
        Vector3d y = Vector3d.Y.transform(rasterToCamera());
        this.cameraX = x.minus(o);
        this.cameraY = y.minus(o);

        Point3d min = Point3d.ORIGIN.transform(rasterToCamera());
        Point3d max = film.resolution().toPoint3d().transform(rasterToCamera());
        min = min.div(min.z());
        max = max.div(max.z());
        this.a = Math.abs((max.x() - min.x()) * (max.y()) - min.y());
    }

    @Override
    public Weighted<Ray> makeRay(CameraSample sample) {
        RayParts parts = makeRayParts(sample);
        return new Weighted<>(parts.ray(), 1);
    }

    @Override
    public Weighted<RayDifferential> makeRayDifferential(CameraSample sample) {

        RayParts parts = makeRayParts(sample);
        Vector3d dx = parts.camera().toVector3d().plus(cameraX).normalize();
        Vector3d dy = parts.camera().toVector3d().plus(cameraY).normalize();

        Point3d rox = null;
        Point3d roy = null;
        Vector3d rdx = null;
        Vector3d rdy = null;

        if (lensRadius() > 0) {
            Point3d focusX = Point3d.ORIGIN.plus(dx.mul(parts.ft()));
            Point3d focusY = Point3d.ORIGIN.plus(dy.mul(parts.ft()));
            rox = roy = parts.lens().toPoint3d();
            rdx = focusX.minus(rox).normalize();
            rdy = focusY.minus(roy).normalize();
        } else {
            Ray mainRay = parts.ray();
            rox = mainRay.origin();
            roy = mainRay.origin();
            rdx = dx;
            rdy = dy;
        }

        RayDifferential rd = new RayDifferential(parts.ray(), true, rox, roy, rdx, rdy);
        return new Weighted<>(rd.transform(cameraToWorld()), 1);
    }
}
