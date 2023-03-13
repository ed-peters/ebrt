package attic.integrators;

import ebrt.Color;
import attic.Scene;
import attic.camera.Camera;
import ebrt.interactions.SurfaceInteraction;
import attic.lights.SampleResult;
import attic.lights.Light;
import attic.math.Normal3d;
import attic.math.Ray;
import attic.math.Vector3d;
import attic.samplers.Sampler;

public class WhittedIntegrator extends SamplerIntegrator {

    public final int maxDepth;

    public WhittedIntegrator(Camera camera, Sampler sampler, int maxDepth) {
        super(camera, sampler);
        this.maxDepth = maxDepth;
    }

    @Override
    public Color li(Ray ray, Scene scene, Sampler sampler, int depth) {

        // Determine the closest intersection in the scene. If there isn't one,
        // we're left with the ambient light from all the light sources.
        SurfaceInteraction si = scene.intersect(ray, Double.MAX_VALUE, false);
        if (si == null) {
            return computeLightColor(ray);
        }

        Normal3d n = si.normal;
        Vector3d wo = si.wo;

        // Compute the scattering functions for the interaction based on its material.
        Color l = si.le(wo);

        // Add the contribution of each visible light source.
        for (Light light : scene.lights) {

            SampleResult sample = light.sampleLi(si, sampler.get2d());
            if (sample.li().isBlack() || sample.pdf() == 0) {
                continue;
            }

            Color f = si.bsdf().f(wo, sample.wi());
            if (!f.isBlack() || sample.tester().unoccluded(scene)) {
                //    L += f * Li * AbsDot(wi, n) / pdf;
            }
        }

        // Trace rays for specular reflection and refraction.
        if (depth + 1 < maxDepth) {
            l = l.plus(specularReflect(ray, si, scene, sampler, 0));
            l = l.plus(specularTransmit(ray, si, scene, sampler, 0));
        }

        return l;
    }
w
    private Color computeLightColor(Ray ray) {
        Color l = Color.BLACK;
        for (Light light : scene.lights) {
            l = l.plus(light.le(ray));
        }
        return l;
    }
}
