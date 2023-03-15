package ebrt.primitives;

import ebrt.interactions.Intersectable;
import ebrt.interactions.RayPack;
import ebrt.interactions.SurfaceInteraction;
import ebrt.interactions.TransportMode;
import ebrt.lights.AreaLight;
import ebrt.material.Material;
import ebrt.math.Bounds3d;

public interface Primitive extends Intersectable {

    Bounds3d worldBound();

    AreaLight areaLight();

    Material material();

    void computeScatteringFunctions(SurfaceInteraction interaction, boolean allowMultipleLobes, TransportMode mode);
}
