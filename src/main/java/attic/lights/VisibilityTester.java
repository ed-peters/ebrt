package attic.lights;

import ebrt.Color;
import attic.Scene;
import ebrt.interactions.Interaction;
import ebrt.interactions.SurfaceInteraction;
import attic.math.Ray;
import attic.samplers.Sampler;

public record VisibilityTester(Interaction p0, Interaction p1) {

    public boolean unoccluded(Scene scene) {
        return !scene.intersectP(p0.spawnRayTo(p1));
    }

    Color tr(Scene scene, Sampler sampler) {

        Ray ray = p0.spawnRayTo(p1);
        Color tr = Color.WHITE;

        while (true) {

            SurfaceInteraction si = scene.intersect(ray, Double.MAX_VALUE, false);
            if (si != null && si.primitive().material() != null) {
                return Color.BLACK;
            }

            if (ray.medium() != null) {
                tr = tr.mul(ray.medium().tr(ray, sampler));
            }

            if (si == null) {
                break;
            }

            ray = si.spawnRayTo(p1);
        }

        return tr;
    }
}
