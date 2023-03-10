package attic.primitives;

import attic.Intersectable;
import attic.lights.AreaLight;
import attic.material.Material;
import attic.math.Bounds3d;

public interface Primitive extends Intersectable {

    Bounds3d worldBound();

    AreaLight areaLight();

    Material material();

    // TODO
//    Object computeScatteringFunctions(SurfaceIntersection interaction);
}
