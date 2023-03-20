package ebrt.main;

import ebrt.Color;
import ebrt.Scene;
import ebrt.camera.Camera;
import ebrt.camera.Film;
import ebrt.camera.PerspectiveCamera;
import ebrt.integrators.SamplerIntegrator;
import ebrt.integrators.WhittedIntegrator;
import ebrt.interactions.Ray;
import ebrt.interactions.RayPack;
import ebrt.interactions.SurfaceInteraction;
import ebrt.lights.GradientLight;
import ebrt.math.Bounds2d;
import ebrt.math.Normal3d;
import ebrt.math.Point2i;
import ebrt.math.Point3d;
import ebrt.math.Transform;
import ebrt.math.Vector3d;
import ebrt.math.Weighted;
import ebrt.media.Medium;
import ebrt.primitives.GeometricPrimitive;
import ebrt.primitives.Primitive;
import ebrt.primitives.PrimitiveList;
import ebrt.samplers.CameraSample;
import ebrt.samplers.RandomSampler;
import ebrt.samplers.Sampler;
import ebrt.shapes.Sphere;

import java.util.Collections;

public class Tracer {

    private final TracerConfig config;
    private final Primitive world = PrimitiveList.from(
            new GeometricPrimitive(new Sphere(new Point3d(0, 0, -1), 0.5)),
            new GeometricPrimitive(new Sphere(new Point3d(0, -100.5, -1), 100)));
    private final Scene scene;

    public Tracer(TracerConfig config) {
        this.config = config.complete();
        this.scene = new Scene(world, Collections.singletonList(GradientLight.sky()));
    }
//
//    private Color computeColor(Ray ray) {
//
//        SurfaceInteraction si = world.intersect(ray, Double.MAX_VALUE, false);
//        if (si != null) {
//            Normal3d n = si.normal;
//            return new Color(n.x() + 1, n.y() + 1, n.z() + 1).mul(0.5);
//        }
//
//        double t = 0.5 * (ray.direction().normalize().y() + 1.0);
//        return Color.WHITE.mul(1.0 - t).plus(Color.SKY_BLUE.mul(t));
//    }
//
//    private Ray castRay(double u, double v) {
//        Vector3d dir = config.lowerLeft
//                .plus(config.horizontal.mul(u))
//                .plus(config.vertical.mul(v))
//                .minus(config.origin);
//        return new Ray(config.origin, dir);
//    }

    public void run() {

        Sampler sampler = new RandomSampler(config.samplesPerPixel);
        Camera camera = config.makeCamera();
        Film film = camera.film();
        WhittedIntegrator integrator = new WhittedIntegrator(camera, sampler, config.maxDepth);

        for (int j=config.imageHeight-1; j>=0; j--) {
            for (int i=0; i<config.imageWidth; i++) {
                double u = (double) i / ((config.imageWidth) - 1);
                double v = (double) j / ((config.imageHeight) - 1);

                CameraSample sample = sampler.cameraSample(new Point2i(i, j));
                Weighted<RayPack> ray = camera.makeRayPack(sample);
                if (ray.weight() != 0) {
                    Color c = integrator.li(ray.t(), scene, sampler, 0);
                    Point2i p = new Point2i(i, j);
                    film.splat(p, c);
                }
            }
        }

        film.writeToFile(1.0, Computer.desktopFile("ebrt.png"));
    }

    public static void main(String [] args) {
        new Tracer(new TracerConfig()).run();
    }
}
