package ebrt.primitives;

import ebrt.interactions.Intersectable;
import ebrt.lights.AreaLight;
import ebrt.material.Material;
import ebrt.math.Bounds3d;

public interface Primitive extends Intersectable {

    Bounds3d worldBound();

    AreaLight areaLight();

    Material material();

    // TODO
//    Object computeScatteringFunctions(SurfaceIntersection interaction);
}
