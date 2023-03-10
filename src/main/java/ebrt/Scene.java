package ebrt;

import ebrt.interactions.Intersectable;
import ebrt.interactions.Ray;
import ebrt.interactions.SurfaceInteraction;
import ebrt.lights.Light;
import ebrt.primitives.Primitive;

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
