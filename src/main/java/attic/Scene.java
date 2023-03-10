package attic;

import ebrt.interactions.SurfaceInteraction;
import attic.lights.Light;
import attic.math.Ray;
import attic.primitives.Primitive;

import java.util.List;

public class Scene implements Intersectable {

    public final Primitive world;
    public final List<Light> lights;

    public Scene(Primitive world, List<Light> lights) {
        this.world = world;
        this.lights = lights;
    }

    @Override
    public SurfaceInteraction intersect(Ray ray, double tmax, boolean testAlpha) {
        return world.intersect(ray, tmax, testAlpha);
    }
}
