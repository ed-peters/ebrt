package attic.primitives;

import ebrt.interactions.SurfaceInteraction;
import attic.lights.AreaLight;
import attic.material.Material;
import attic.math.Bounds3d;
import attic.math.Ray;
import attic.math.Transform;

public class TransformedPrimitive implements Primitive {

    private final Primitive target;
    private final Transform worldToPrimitive;

    public TransformedPrimitive(Primitive target, Transform worldToPrimitive) {
        this.target = target;
        this.worldToPrimitive = worldToPrimitive;
    }

    @Override
    public Bounds3d worldBound() {
        return worldToPrimitive.reverse(target.worldBound());
    }

    @Override
    public AreaLight areaLight() {
        return target.areaLight();
    }

    @Override
    public Material material() {
        return target.material();
    }

    @Override
    public SurfaceInteraction intersect(Ray r, double tmax, boolean testAlpha) {
        return null;
    }
}
