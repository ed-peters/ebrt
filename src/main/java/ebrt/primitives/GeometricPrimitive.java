package ebrt.primitives;

import ebrt.interactions.Ray;
import ebrt.interactions.SurfaceInteraction;
import ebrt.lights.AreaLight;
import ebrt.material.Material;
import ebrt.math.Bounds3d;
import ebrt.media.MediumInterface;
import ebrt.shapes.Shape;

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
