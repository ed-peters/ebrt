package ebrt.integrators;

import ebrt.Color;
import ebrt.Scene;
import ebrt.camera.Camera;
import ebrt.interactions.RayPack;
import ebrt.interactions.SurfaceInteraction;
import ebrt.lights.Light;
import ebrt.math.Normal3d;
import ebrt.samplers.Sampler;

public class SimpleNormalColoredIntegrator extends SamplerIntegrator {

    public SimpleNormalColoredIntegrator(Camera camera, Sampler sampler) {
        super(camera, sampler);
    }

    @Override
    public Color li(RayPack rays, Scene scene, Sampler sampler, int depth) {

        SurfaceInteraction si = scene.intersect(rays.primary(), Double.MAX_VALUE, false);
        if (si != null) {
            Normal3d n = si.normal;
            return new Color(n.x() + 1, n.y() + 1, n.z() + 1).mul(0.5);
        }

        return computeLightColor(rays, scene);
    }

    protected Color computeLightColor(RayPack rays, Scene scene) {
        Color l = Color.BLACK;
        for (Light light : scene.lights) {
            l = l.plus(light.le(rays.primary()));
        }
        return l;
    }
}
