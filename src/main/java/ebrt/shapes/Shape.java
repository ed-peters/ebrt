package ebrt.shapes;

import ebrt.interactions.Intersectable;
import ebrt.interactions.Ray;
import ebrt.interactions.SurfaceInteraction;
import ebrt.math.Bounds3d;
import ebrt.math.Transform;

public abstract class Shape implements Intersectable {

    private final Transform objectToWorld;
    private final Transform worldToObject;
    private final Bounds3d objectBounds;
    private final boolean reverse;
    private final boolean transformSwapsHands;

    public Shape(Transform objectToWorld, Bounds3d objectBounds, boolean reverse) {
        this.objectToWorld = objectToWorld;
        this.worldToObject = objectToWorld.invert();
        this.objectBounds = objectBounds;
        this.reverse = reverse;
        this.transformSwapsHands = objectToWorld.swapsHandedness();
    }

    public final Transform objectToWorld() {
        return objectToWorld;
    }

    public final boolean reverse() {
        return reverse;
    }

    public final Bounds3d objectBounds() {
        return objectBounds;
    }

    public Bounds3d worldBounds() {
        return objectBounds.transform(objectToWorld);
    }

    public final SurfaceInteraction intersect(Ray worldRay, double tmax, boolean testAlpha) {
        SurfaceInteraction result = intersectObject(worldRay.transform(worldToObject), tmax, testAlpha);
        return result == null ? null : result.transform(objectToWorld);
    }

    protected abstract SurfaceInteraction intersectObject(Ray objectRay, double tmax, boolean testAlpha);

    public abstract double area();

}
