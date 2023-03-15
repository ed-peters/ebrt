package ebrt.camera;

import ebrt.interactions.Ray;
import ebrt.interactions.RayPack;
import ebrt.math.Bounds2d;
import ebrt.math.Point3d;
import ebrt.math.Projections;
import ebrt.math.Transform;
import ebrt.math.Vector3d;
import ebrt.math.Weighted;
import ebrt.media.Medium;
import ebrt.samplers.CameraSample;

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
        dx = Vector3d.X.transform(rasterToCamera());
        dy = Vector3d.Y.transform(rasterToCamera());
    }

    @Override
    public Weighted<Ray> makeRay(CameraSample sample) {
        RayParts parts = makeRayParts(sample);
        return new Weighted<>(parts.ray().transform(cameraToWorld()), 1);
    }

    @Override
    public Weighted<RayPack> makeRayPack(CameraSample sample) {

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

        RayPack rd = new RayPack(parts.ray(), new Ray(rox, rdx), new Ray(roy, rdy));
        return new Weighted<>(rd.transform(cameraToWorld()), 1);
    }
}
