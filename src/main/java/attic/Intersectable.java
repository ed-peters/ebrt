package attic;

import ebrt.interactions.SurfaceInteraction;
import attic.math.Ray;

public interface Intersectable {

    SurfaceInteraction intersect(Ray ray, double tmax, boolean testAlpha);

    default boolean intersectP(Ray ray) { return intersect(ray, Double.MAX_VALUE, false) != null; }
}
