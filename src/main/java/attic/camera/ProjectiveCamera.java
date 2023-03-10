package attic.camera;

import attic.math.Bounds2d;
import attic.math.Point2d;
import attic.math.Point3d;
import attic.math.Projections;
import attic.math.Ray;
import attic.math.Transform;
import ebrt.media.Medium;
import attic.samplers.CameraSample;

/**
 * Three coordinate systems:
 *  - screen space (film plane, ranges based on film resolution and sample bounds - y goes up)
 *  - camera space (aka NDC space, ranges from 0,0 at UL to 1,1 at BR - y goes down)
 *  - raster space (ranges from 0,0 at UL to resx,resy at BR - y goes down)
 */
public abstract class ProjectiveCamera extends Camera {

    private final Transform cameraToScreen;
    private final Transform rasterToCamera;
    private final Transform screenToRaster;
    private final double lensRadius;
    private final double focalDistance;

    public ProjectiveCamera(
            Transform cameraToWorld,
            Transform cameraToScreen,
            Bounds2d screenWindow,
            double lensRadius,
            double focalDistance,
            Medium medium,
            Film film) {
        super(cameraToWorld, medium, film);
        this.cameraToScreen = cameraToScreen;
        this.lensRadius = lensRadius;
        this.focalDistance = focalDistance;
        this.screenToRaster = Projections.screenToRaster(film().resolution(), screenWindow);
        this.rasterToCamera = Projections.rasterToCamera(screenToRaster, cameraToScreen);
    }

    public final Transform cameraToScreen() {
        return cameraToScreen;
    }

    public final Transform rasterToCamera() {
        return rasterToCamera;
    }

    public final Transform screenToRaster() {
        return screenToRaster;
    }

    public final double lensRadius() {
        return lensRadius;
    }

    public final double focalDistance() {
        return focalDistance;
    }

    protected RayParts makeRayParts(CameraSample sample) {

        Point3d camera = rasterToCamera.forward(sample.film().toPoint3d());
        Ray ray = new Ray(Point3d.ORIGIN, camera.toVector3d().normalize());
        if (lensRadius <= 0) {
            return new RayParts(camera, ray, null, null, 0);
        }

        double ft = focalDistance / ray.direction().z();
        Point3d focus = ray.at(ft);
        Point2d lens = sample.lens().concentricSampleDisk();
        ray = new Ray(lens.toPoint3d(), focus.minus(ray.origin()).normalize());
        return new RayParts(camera, ray, lens, focus, ft);
    }

    public record RayParts(Point3d camera, Ray ray, Point2d lens, Point3d focus, double ft) {

    }
}
