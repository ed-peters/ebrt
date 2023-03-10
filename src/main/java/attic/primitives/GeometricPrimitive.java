package attic.primitives;

import ebrt.interactions.SurfaceInteraction;
import attic.lights.AreaLight;
import attic.material.Material;
import attic.math.Bounds3d;
import attic.math.Ray;
import ebrt.media.MediumInterface;
import attic.shapes.Shape;

public class GeometricPrimitive implements Primitive {

    private final Shape shape;
    private final Material material;
    private final AreaLight areaLight;
    private final MediumInterface mediumInterface;

    public GeometricPrimitive(Shape shape) {
        this(shape, null, null, null);
    }

    public GeometricPrimitive(Shape shape, Material material, AreaLight areaLight, MediumInterface mediumInterface) {
        this.shape = shape;
        this.material = material;
        this.areaLight = areaLight;
        this.mediumInterface = mediumInterface;
    }

    public Shape shape() {
        return shape;
    }

    @Override
    public Material material() {
        return material;
    }

    @Override
    public AreaLight areaLight() {
        return areaLight;
    }

    public MediumInterface mediumInterface() {
        return mediumInterface;
    }

    @Override
    public Bounds3d worldBound() {
        return shape.worldBounds();
    }

    @Override
    public SurfaceInteraction intersect(Ray ray, double tmax, boolean testAlpha) {

        SurfaceInteraction si = shape.intersect(ray, tmax, testAlpha);
        if (si == null) {
            return null;
        }

        MediumInterface mi = (mediumInterface != null && mediumInterface.isTransition())
                ? mediumInterface
                : new MediumInterface(ray.medium());

        si.setMediumInterface(mi);
        si.setPrimitive(this);
        return si;
    }

}