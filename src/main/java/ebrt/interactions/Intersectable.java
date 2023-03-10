package ebrt.interactions;

import ebrt.interactions.Ray;
import ebrt.interactions.SurfaceInteraction;

public interface Intersectable {

    SurfaceInteraction intersect(Ray ray, double tmax, boolean testAlpha);

    default boolean intersectP(Ray ray) { return intersect(ray, Double.MAX_VALUE, false) != null; }
}
