package ebrt.integrators;

import ebrt.Color;
import ebrt.Scene;
import ebrt.camera.Camera;
import ebrt.interactions.Ray;
import ebrt.interactions.SurfaceInteraction;
import ebrt.math.Normal3d;
import ebrt.samplers.Sampler;

public class SimpleNormalColoredIntegrator extends SamplerIntegrator {

    public SimpleNormalColoredIntegrator(Camera camera, Sampler sampler) {
        super(camera, sampler);
    }

    @Override
    public Color li(Ray ray, Scene scene, Sampler sampler, int depth) {

        SurfaceInteraction si = scene.intersect(ray, Double.MAX_VALUE, false);
        if (si != null) {
            Normal3d n = si.normal;
            return new Color(n.x() + 1, n.y() + 1, n.z() + 1).mul(0.5);
        }

        double t = 0.5 * (ray.direction().normalize().y() + 1.0);
        return Color.WHITE.mul(1.0 - t).plus(Color.SKY_BLUE.mul(t));
    }
}
