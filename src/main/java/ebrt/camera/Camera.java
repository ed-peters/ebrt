package ebrt.camera;

import ebrt.math.Bounds2i;
import ebrt.math.Transform;
import ebrt.math.Weighted;
import ebrt.interactions.Ray;
import ebrt.interactions.RayPack;
import ebrt.media.Medium;
import ebrt.samplers.CameraSample;

public abstract class Camera {

    private final Transform cameraToWorld;
    private final Medium medium;
    private final Film film;

    protected Camera(Transform cameraToWorld, Medium medium, Film film) {
        this.cameraToWorld = cameraToWorld;
        this.medium = medium;
        this.film = film;
    }

    public final Transform cameraToWorld() {
        return cameraToWorld;
    }

    public final Medium medium() {
        return medium;
    }

    public final Film film() {
        return film;
    }

    public FilmTile makeFilmTile(Bounds2i bounds) {
        return film.makeTile(bounds);
    }

    public abstract Weighted<Ray> makeRay(CameraSample sample);

    public Weighted<RayPack> makeRayPack(CameraSample sample) {
        Weighted<Ray> ray = makeRay(sample);
        Weighted<Ray> rx = makeRay(sample.shift(1, 0));
        if (rx == null) {
            return null;
        }
        Weighted<Ray> ry = makeRay(sample.shift(0, 1));
        if (ry == null) {
            return null;
        }
        RayPack rd = new RayPack(ray.t(), rx.t(), ry.t());
        return new Weighted<>(rd, ray.weight());
    }

    /*

        virtual Spectrum We(const Ray &primary, Point2f *pRaster2 = nullptr) const;
       virtual void Pdf_We(const Ray &primary, Float *pdfPos, Float *pdfDir) const;
       virtual Spectrum Sample_Wi(const Interaction &ref, const Point2f &u,
                                  Vector3f *wi, Float *pdf, Point2f *pRaster,
                                  VisibilityTester *vis) const;

     */
}
