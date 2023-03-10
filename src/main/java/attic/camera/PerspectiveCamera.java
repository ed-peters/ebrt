package attic.camera;

import attic.math.Bounds2d;
import attic.math.Point3d;
import attic.math.Projections;
import attic.math.Ray;
import attic.math.RayDifferential;
import attic.math.Transform;
import attic.math.Vector3d;
import attic.math.Weighted;
import ebrt.media.Medium;
import attic.samplers.CameraSample;

public class PerspectiveCamera extends ProjectiveCamera {

    private final Vector3d cameraX;
    private final Vector3d cameraY;
    private final double a;

    public PerspectiveCamera(
            Transform cameraToWorld,
            Transform cameraToScreen,
            Bounds2d screenWindow,
            double lensRadius,
            double focalDistance,
            double fieldOfView,
            Medium medium,
            Film film) {

        super(cameraToWorld, Projections.perspective(fieldOfView), screenWindow, lensRadius, focalDistance, medium, film);

        Vector3d o = rasterToCamera().forward(Vector3d.ZERO);
        Vector3d x = rasterToCamera().forward(new Vector3d(1, 0, 0));
        Vector3d y = rasterToCamera().forward(new Vector3d(0, 1, 0));
        this.cameraX = x.minus(o);
        this.cameraY = y.minus(o);

        Point3d min = rasterToCamera().forward(Point3d.ORIGIN);
        Point3d max = rasterToCamera().forward(film.resolution().toPoint3d());
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
        return new Weighted<>(cameraToWorld().forward(rd), 1);
    }
}
