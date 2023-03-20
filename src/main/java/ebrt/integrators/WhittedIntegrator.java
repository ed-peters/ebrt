package ebrt.integrators;

import ebrt.Color;
import ebrt.Scene;
import ebrt.camera.Camera;
import ebrt.interactions.LightCastOnObject;
import ebrt.interactions.RayPack;
import ebrt.interactions.SurfaceInteraction;
import ebrt.interactions.TransportMode;
import ebrt.lights.Light;
import ebrt.samplers.Sampler;

public class WhittedIntegrator extends SamplerIntegrator {

    public final int maxDepth;

    public WhittedIntegrator(Camera camera, Sampler sampler, int maxDepth) {
        super(camera, sampler);
        this.maxDepth = maxDepth;
    }

    @Override
    public Color li(RayPack rays, Scene scene, Sampler sampler, int depth) {

        // Determine the closest intersection in the scene. If there isn't one,
        // we're left with the ambient light from all the light sources.
        SurfaceInteraction si = scene.intersect(rays.primary(), Double.MAX_VALUE, false);
        if (si == null) {
            return computeLightColor(rays, scene);
        }

        // TODO what are the right parameters here?
        si.computeScatteringFunctions(rays, true, TransportMode.RADIANCE);

        Color l = si.le(si.wo);
        l = l.plus(computeLightSourceContributions(rays, si, scene, sampler));
        if (depth + 1 < maxDepth) {
            l = l.plus(computeSpecularReflection(rays, si, scene, sampler, 0));
            l = l.plus(computeSpecularTransmission(rays, si, scene, sampler, 0));
        }

        return l;
    }

    protected Color computeLightColor(RayPack rays, Scene scene) {
        Color l = Color.BLACK;
        for (Light light : scene.lights) {
            l = l.plus(light.le(rays.primary()));
        }
        return l;
    }

    // https://pbr-book.org/3ed-2018/Introduction/pbrt_System_Overview#fragment-Addcontributionofeachlightsource-0
    protected Color computeLightSourceContributions(RayPack rays, SurfaceInteraction interaction, Scene scene, Sampler sampler) {
        Color color = Color.BLACK;
        for (Light light : scene.lights) {
            LightCastOnObject cast = light.li(interaction, sampler.get2d());
            if (cast.color().isBlack() || cast.pdf() == 0) {
                continue;
            }
            // WTF is wi?
            // WTF is Li?
            Color f = interaction.bsdf().f(interaction.wo, interaction.normal);
            if (!f.isBlack() && cast.tester().unoccluded(scene)) {

            }
            if (!f.IsBlack() && visibility.Unoccluded(scene)) {
                color = color.plus(f.mul(li).mul(interaction.normal.abs))
            }
        }


        return Color.BLACK;
    }

    // https://pbr-book.org/3ed-2018/Introduction/pbrt_System_Overview#SamplerIntegrator::SpecularReflect
    protected Color computeSpecularReflection(RayPack rays, SurfaceInteraction interaction, Scene scene, Sampler sampler, int depth) {
        return Color.BLACK;
    }

    // https://pbr-book.org/3ed-2018/Introduction/pbrt_System_Overview#SamplerIntegrator::SpecularTransmit
    protected Color computeSpecularTransmission(RayPack rays, SurfaceInteraction interaction, Scene scene, Sampler sampler, int depth) {
        return Color.BLACK;
    }
}
