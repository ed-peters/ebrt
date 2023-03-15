package ebrt.main;

import ebrt.Scene;
import ebrt.camera.Camera;
import ebrt.integrators.Integrator;
import ebrt.integrators.SimpleNormalColoredIntegrator;
import ebrt.math.Point3d;
import ebrt.primitives.GeometricPrimitive;
import ebrt.primitives.Primitive;
import ebrt.primitives.PrimitiveList;
import ebrt.samplers.RandomSampler;
import ebrt.samplers.Sampler;
import ebrt.shapes.Sphere;

import java.util.Collections;

public class IntegratorTracer {

    private final TracerConfig config;
    private final Camera camera;
    private final Sampler sampler;
    private final Integrator integrator;

    public IntegratorTracer(TracerConfig config) {
        this.config = config;
        this.camera = config.makeCamera();
        this.sampler = new RandomSampler(config.samplesPerPixel);
        this.integrator = new SimpleNormalColoredIntegrator(camera, sampler);
    }

    public void run() {

        Primitive world = PrimitiveList.from(
                new GeometricPrimitive(new Sphere(new Point3d(0, 0, -1), 0.5)),
                new GeometricPrimitive(new Sphere(new Point3d(0, -100.5, -1), 100)));

        Scene scene = new Scene(world, Collections.emptyList());

        integrator.render(scene, camera.film());

        camera.film().writeToFile(1.0, Computer.desktopFile("ebrt.png"));
    }

    public static void main(String [] args) {
        new IntegratorTracer(new TracerConfig()).run();
    }
}
