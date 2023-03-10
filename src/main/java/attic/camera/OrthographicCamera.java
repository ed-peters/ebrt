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

public class OrthographicCamera extends ProjectiveCamera {

    private final Vector3d dx;
    private final Vector3d dy;

    public OrthographicCamera(
            Transform cameraToWorld,
            Bounds2d screenWindow,
            double lensRadius,
            double focalDistance,
            Medium medium,
            Film film) {
        super(cameraToWorld, Projections.orthographic(0, 1), screenWindow, lensRadius, focalDistance, medium, film);
        dx = rasterToCamera().forward(Vector3d.X);
        dy = rasterToCamera().forward(Vector3d.Y);
    }

    @Override
    public Weighted<Ray> makeRay(CameraSample sample) {
        RayParts parts = makeRayParts(sample);
        return new Weighted<>(cameraToWorld().forward(parts.ray()), 1);
    }

    @Override
    public Weighted<RayDifferential> makeRayDifferential(CameraSample sample) {

        RayParts parts = makeRayParts(sample);

        Point3d rox = null;
        Point3d roy = null;
        Vector3d rdx = null;
        Vector3d rdy = null;

        if (lensRadius() > 0) {
            Point3d focusX = parts.camera().plus(dx).plus(Vector3d.Z.mul(parts.ft()));
            Point3d focusY = parts.camera().plus(dy).plus(Vector3d.Z.mul(parts.ft()));
            rox = roy = parts.lens().toPoint3d();
            rdx = focusX.minus(rox).normalize();
            rdy = focusY.minus(roy).normalize();
        } else {
            Ray mainRay = parts.ray();
            rox = mainRay.origin().plus(dx);
            roy = mainRay.origin().plus(dy);
            rdx = mainRay.direction();
            rdy = mainRay.direction();
        }

        RayDifferential rd = new RayDifferential(parts.ray(), true, rox, roy, rdx, rdy);
        return new Weighted<>(cameraToWorld().forward(rd), 1);
    }
}
