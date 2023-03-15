package ebrt.integrators;

import ebrt.Color;
import ebrt.Scene;
import ebrt.camera.Camera;
import ebrt.camera.Film;
import ebrt.camera.FilmTile;
import ebrt.interactions.Ray;
import ebrt.interactions.RayPack;
import ebrt.math.Bounds2i;
import ebrt.math.Point2i;
import ebrt.math.Weighted;
import ebrt.samplers.CameraSample;
import ebrt.samplers.Sampler;

public abstract class SamplerIntegrator implements Integrator {

    public final Camera camera;
    public final Sampler sampler;

    protected SamplerIntegrator(Camera camera, Sampler sampler) {
        this.camera = camera;
        this.sampler = sampler;
    }

    protected void preprocess(Scene scene, Sampler sampler) {

    }

    public abstract Color li(RayPack rays, Scene scene, Sampler sampler, int depth);

/*
    public abstract Color specularReflect(RayPack rays, SurfaceInteraction surfaceInteraction, Scene scene, Sampler sampler, int depth);

    public abstract Color specularTransmit(RayPack rays, SurfaceInteraction surfaceInteraction, Scene scene, Sampler sampler, int depth);
*/

    @Override
    public void render(Scene scene, Film film) {

        preprocess(scene, sampler);

        FilmTile tile = film.makeTile(film.sampleBounds());
        Bounds2i bounds = tile.bounds();

        for (int x=bounds.min().x(); x<bounds.max().x(); x++) {
            for (int y=bounds.min().y(); y<bounds.max().y(); y++) {
                Point2i point = new Point2i(x, y);
                renderPixel(scene, tile, point);
            }
        }

        film.mergeTile(tile);
    }

    private void renderPixel(Scene scene, FilmTile tile, Point2i point) {

        sampler.startPixel(point);

        for (int p=0; p<sampler.samplesPerPixel(); p++) {

            CameraSample sample = sampler.cameraSample(point);
            Weighted<RayPack> rays = camera.makeRayPack(sample);

            Color l = Color.BLACK;
            if (rays.weight() > 0) {
                l = li(rays.t(), scene, sampler, 0);
                if (!l.isValid()) {
                    System.err.println("warning: invalid luminance at "+point);
                }
            }
            tile.addSample(sample.film(), l, rays.weight());
        }
    }
}
