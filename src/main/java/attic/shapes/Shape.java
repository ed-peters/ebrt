package attic.shapes;

import attic.Intersectable;
import ebrt.interactions.SurfaceInteraction;
import attic.math.Bounds3d;
import attic.math.Ray;
import attic.math.Transform;

public abstract class Shape implements Intersectable {

    private final Transform objectToWorld;
    private final Bounds3d objectBounds;
    private final boolean reverse;
    private final boolean transformSwapsHands;

    public Shape(Transform objectToWorld, Bounds3d objectBounds, boolean reverse) {
        this.objectToWorld = objectToWorld;
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
        return objectToWorld.forward(objectBounds);
    }

    public final SurfaceInteraction intersect(Ray worldRay, double tmax, boolean testAlpha) {
        SurfaceInteraction result = intersectObject(objectToWorld.reverse(worldRay), tmax, testAlpha);
        return result == null ? null : objectToWorld.forward(result);
    }

    protected abstract SurfaceInteraction intersectObject(Ray objectRay, double tmax, boolean testAlpha);

    public abstract double area();

}
