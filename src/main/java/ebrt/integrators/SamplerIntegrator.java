package ebrt.integrators;

import ebrt.Color;
import ebrt.Scene;
import ebrt.camera.Camera;
import ebrt.camera.Film;
import ebrt.camera.FilmTile;
import ebrt.interactions.Ray;
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

    public abstract Color li(Ray ray, Scene scene, Sampler sampler, int depth);

/*
    public abstract Color specularReflect(Ray ray, SurfaceInteraction surfaceInteraction, Scene scene, Sampler sampler, int depth);

    public abstract Color specularTransmit(Ray ray, SurfaceInteraction surfaceInteraction, Scene scene, Sampler sampler, int depth);
*/

    @Override
    public void render(Scene scene, Film film) {

        preprocess(scene, sampler);

        FilmTile tile = film.makeTile(film.sampleBounds());

        for (Point2i point : tile.bounds()) {

            sampler.startPixel(point);

            CameraSample sample = sampler.cameraSample();
            Weighted<Ray> ray = camera.makeRay(sample);
            // TODO scale ray differentials?

            Color l = Color.BLACK;
            if (ray.weight() > 0) {
                l = li(ray.t(), scene, sampler, 0);
                if (!l.isValid()) {
                    System.err.println("warning: invalid luminance at "+point);
                }
            }
            tile.addSample(sample.film(), l, ray.weight());
        }

        film.mergeTile(tile);
    }
}
