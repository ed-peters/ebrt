package ebrt.primitives;

import ebrt.interactions.Ray;
import ebrt.interactions.SurfaceInteraction;
import ebrt.interactions.TransportMode;
import ebrt.lights.AreaLight;
import ebrt.material.Material;
import ebrt.math.Bounds3d;
import ebrt.math.Transform;

public class TransformedPrimitive implements Primitive {

    private final Primitive target;
    private final Transform worldToPrimitive;

    public TransformedPrimitive(Primitive target, Transform worldToPrimitive) {
        this.target = target;
        this.worldToPrimitive = worldToPrimitive;
    }

    @Override
    public Bounds3d worldBound() {
        // TODO is this correct?
        return target.worldBound().transform(worldToPrimitive.invert());
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
    public void computeScatteringFunctions(SurfaceInteraction interaction, boolean allowMultipleLobes, TransportMode mode) {
        target.computeScatteringFunctions(interaction, allowMultipleLobes, mode);
    }

    @Override
    public SurfaceInteraction intersect(Ray r, double tmax, boolean testAlpha) {
        return null;
    }
}
